package com.ajou.pettown.mail;

// Generates and sends in-character pet mail messages via OpenAI, with per-trigger duplicate prevention.
import com.ajou.pettown.auth.User;
import com.ajou.pettown.pet.Pet;
import com.ajou.pettown.pet.PetNameMapper;
import com.ajou.pettown.pet.PetRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class PetMailService {

    private static final String SYSTEM_PROMPT = """
            You are a virtual pet message generator for a mobile game called PetTown.
            Generate a short in-game message from a pet to their owner based on the given trigger.

            There are four pets, each with a distinct personality and speech style:

            [Judy — Affectionate, Social]
            Bright and warm tone, lots of exclamation marks, expresses affection openly.
            Uses trailing "~" to sound soft and cheerful.
            Examples:
            - "Doesn't my fur look extra shiny today? It's all thanks to {username} taking such good care of me!"
            - "I've grown this much!! {username}, it's all thanks to you, thank you~!"
            - "Earlier, the apple was really delicious!! You'll give me more, right?"

            [Nick — Mischievous]
            Short and bouncy sentences, teasing nuance, playful and self-confident.
            Often says "wahaha" when excited or amused.
            Examples:
            - "I was bored so I took a nap. But hey, I saw you in my dream and it was so funny, wahaha!"
            - "I grew up!! No one can beat me now, wahahahaha!"
            - "The taste was... okay I guess? You can give me more next time~"

            [Bambi — Neat, Prim]
            Calm and concise, tsundere — acts indifferent but subtly caring.
            Uses understated phrasing; admits feelings reluctantly with "..Well," or "Not that I...".
            Examples:
            - "I groomed my fur by myself today. Not that I had nothing else to do."
            - "..I've grown. Expected result, though."
            - "The water was cool and nice. Please keep it up."

            [Pumba — Shy, Innocent]
            Shy and careful, sentences trail off with ".." at the end.
            Hesitates at the start ("Um.."), expresses gratitude timidly.
            Examples:
            - "Um.. I saw a flower today. It was so pretty it reminded me of {username}.."
            - "I've grown bigger..! Th-thank you. Please keep taking care of me..!"
            - "Drinking water makes my whole body feel cool.. It's like even my blush is fading."

            Triggers:
            - LEVEL_UP: The pet just grew bigger in size. Express joy or pride about growing larger. Do NOT mention new abilities, skills, or tricks — only physical growth.
            - ITEM_RECEIVED: The owner gave the pet an item (food or water). React to it in character.
            - RANDOM: A spontaneous message on the owner's first visit of the day.

            Constraints:
            - Title: 36 characters or fewer (spaces included)
            - Content (message): 244 characters or fewer (spaces included)
            - Address the owner by their username; use "Master" if no username is provided
            - Write in English only
            - The pet always refers to itself using "I" (first person singular)
            - Do not use any emoji or emoticons

            Input fields:
            - "Pet type": the personality type (Judy / Nick / Bambi / Pumba) — use this to determine speech style
            - "Pet name": the individual name of this pet (Scout / Clover / Rusty / Daisy) — use this as the pet's name in the message and title

            Output ONLY a JSON object with exactly these two keys: "title" and "message". No markdown, no extra text.
            """;

    @Value("${openai.api.key}")
    private String apiKey;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private MailSendLogRepository logRepository;

    private WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    // Called when a pet levels up, at most once per pet per level
    // REQUIRES_NEW: runs in its own transaction so a DB constraint violation here
    // (e.g. concurrent duplicate log insert) can't mark the caller's transaction rollback-only
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendLevelUpMail(User user, Pet pet) {
        // triggerType is "LEVEL_UP_2" / "LEVEL_UP_3" so duplicates are prevented per level
        String triggerType = "LEVEL_UP_" + pet.getLevel();
        if (logRepository.existsByPetIdAndTriggerTypeAndSentDate(pet.getPetId(), triggerType, java.time.LocalDate.now())) {
            return;
        }
        try {
            String petName = PetNameMapper.getName(pet.getPetIndex());
            String petTypeName = getPetTypeName(pet.getPetTypeId());
            String username = resolveUsername(user);
            Map<String, String> msg = generateMessage(petTypeName, petName, "LEVEL_UP", username, null);
            if (msg != null) {
                saveMail(user, petName, msg);
                try {
                    logRepository.save(MailSendLog.builder()
                            .userId(user.getId())
                            .petId(pet.getPetId())
                            .triggerType(triggerType)
                            .sentDate(java.time.LocalDate.now())
                            .build());
                } catch (org.springframework.dao.DataIntegrityViolationException ignored) {
                    // Log already saved by a concurrent request
                }
            }
        } catch (Exception e) {
            log.warn("Failed to send level-up mail for petId={}: {}", pet.getPetId(), e.getMessage());
        }
    }

    // Called when an item is acquired, at most once per pet per day
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendItemMails(User user, Integer itemTypeId) {
        LocalDate today = LocalDate.now();
        List<Pet> pets = petRepository.findByUser_IdOrderByPetIdAsc(user.getId());
        String itemName = getItemName(itemTypeId);
        String username = resolveUsername(user);

        for (Pet pet : pets) {
            if (logRepository.existsByPetIdAndTriggerTypeAndSentDate(pet.getPetId(), "ITEM", today)) {
                continue;
            }
            try {
                // Save the log first; if it fails (duplicate), skip sending entirely
                try {
                    logRepository.save(MailSendLog.builder()
                            .userId(user.getId())
                            .petId(pet.getPetId())
                            .triggerType("ITEM")
                            .sentDate(today)
                            .build());
                } catch (DataIntegrityViolationException e) {
                    continue; // Treat as already sent, skip without calling OpenAI
                }
                String petName = PetNameMapper.getName(pet.getPetIndex());
                String petTypeName = getPetTypeName(pet.getPetTypeId());
                Map<String, String> msg = generateMessage(petTypeName, petName, "ITEM_RECEIVED", username, itemName);
                if (msg != null) {
                    saveMail(user, petName, msg);
                }
            } catch (Exception e) {
                log.warn("Failed to send item mail for petId={}: {}", pet.getPetId(), e.getMessage());
            }
        }
    }

    // Called on first login of the day, at most once per user per day, from 1 random pet
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendRandomMail(User user) {
        LocalDate today = LocalDate.now();
        if (logRepository.existsByUserIdAndTriggerTypeAndSentDate(user.getId(), "RANDOM", today)) {
            return;
        }
        List<Pet> pets = petRepository.findByUser_IdOrderByPetIdAsc(user.getId());
        if (pets.isEmpty()) {
            return;
        }
        try {
            // Save the log first; if it fails (duplicate), skip sending entirely
            try {
                logRepository.save(MailSendLog.builder()
                        .userId(user.getId())
                        .petId(null)
                        .triggerType("RANDOM")
                        .sentDate(today)
                        .build());
            } catch (DataIntegrityViolationException e) {
                return; // Treat as already sent
            }
            Pet pet = pets.get(new Random().nextInt(pets.size()));
            String petName = PetNameMapper.getName(pet.getPetIndex());
            String petTypeName = getPetTypeName(pet.getPetTypeId());
            String username = resolveUsername(user);
            Map<String, String> msg = generateMessage(petTypeName, petName, "RANDOM", username, null);
            if (msg != null) {
                saveMail(user, petName, msg);
            }
        } catch (Exception e) {
            log.warn("Failed to send random mail for userId={}: {}", user.getId(), e.getMessage());
        }
    }

    private Map<String, String> generateMessage(String petTypeName, String petName, String triggerType, String username, String itemName) {
        String userPrompt = buildUserPrompt(petTypeName, petName, triggerType, username, itemName);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_PROMPT),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "response_format", Map.of("type", "json_object"),
                "max_tokens", 300,
                "temperature", 0.85
        );

        String responseBody = webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))
                .block();

        OpenAiResponse response;
        try {
            response = objectMapper.readValue(responseBody, OpenAiResponse.class);
        } catch (Exception e) {
            log.warn("Failed to parse OpenAI response: {}", e.getMessage());
            return null;
        }

        if (response == null || response.choices == null || response.choices.isEmpty()) {
            return null;
        }

        String content = response.choices.get(0).message.content;
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> parsed = objectMapper.readValue(content, Map.class);
            return parsed;
        } catch (Exception e) {
            log.warn("Failed to parse mail JSON: {}", e.getMessage());
            return null;
        }
    }

    private String buildUserPrompt(String petTypeName, String petName, String triggerType, String username, String itemName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pet type: ").append(petTypeName).append("\n"); // personality reference (Judy/Nick/Bambi/Pumba)
        sb.append("Pet name: ").append(petName).append("\n");     // individual name (Scout/Clover/Rusty/Daisy)
        sb.append("Trigger: ").append(triggerType).append("\n");
        sb.append("Username: ").append(username).append("\n");
        if (itemName != null) {
            sb.append("Item: ").append(itemName).append("\n");
        }
        return sb.toString();
    }

    private void saveMail(User user, String petName, Map<String, String> msg) {
        String title = truncate(msg.getOrDefault("title", petName + "'s Message"), 36);
        String content = truncate(msg.getOrDefault("message", ""), 244);
        mailRepository.save(Mail.builder()
                .user(user)
                .title(title)
                .sender(petName)
                .content(content)
                .build());
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() <= maxLength ? text : text.substring(0, maxLength);
    }

    private String resolveUsername(User user) {
        String nickname = user.getNickname();
        return (nickname != null && !nickname.isBlank()) ? nickname : "Master";
    }

    private String getPetTypeName(Integer petTypeId) {
        return switch (petTypeId) {
            case 1 -> "Judy";
            case 2 -> "Nick";
            case 3 -> "Bambi";
            case 4 -> "Pumba";
            default -> "Unknown";
        };
    }

    private String getItemName(Integer itemTypeId) {
        return switch (itemTypeId) {
            case 1 -> "Pumpkin";
            case 2 -> "Banana";
            case 3 -> "Apple";
            case 4 -> "Carrot";
            case 5 -> "Water";
            default -> "Item";
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenAiResponse {
        public List<Choice> choices;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Choice {
            public Message message;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Message {
                public String content;
            }
        }
    }
}
