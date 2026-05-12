package com.ajou.pettown.auth;

// Implementation of AuthService handling registration, login, and JWT issuance.
import com.ajou.pettown.auth.dto.LoginRequest;
import com.ajou.pettown.auth.dto.LoginResponse;
import com.ajou.pettown.auth.dto.RegisterRequest;
import com.ajou.pettown.mail.MailRepository;
import com.ajou.pettown.mail.PetMailService;
import com.ajou.pettown.pet.PetRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PetMailService petMailService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public boolean checkUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public void register(RegisterRequest request) {
        if (checkUserIdDuplicate(request.getUserId())) {
            throw new RuntimeException("ID_ALREADY_EXISTS");
        }

        // Encode password with BCrypt before persisting
        User user = User.builder()
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .shopName(request.getShopName())
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponse getUserStatus(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        List<LoginResponse.OwnedPet> ownedPets = petRepository.findByUser_IdOrderByPetIdAsc(user.getId())
                .stream()
                .map(pet -> new LoginResponse.OwnedPet(pet.getPetId(), pet.getPetTypeId(), pet.getLevel()))
                .collect(Collectors.toList());
        boolean hasUnreadMail = mailRepository.existsByUser_IdAndIsRead(user.getId(), false);
        return new LoginResponse(null, user.getNickname(), user.getShopName(), hasUnreadMail, ownedPets);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password.");
        }

        // Detect first login of the day before updating lastActiveAt
        boolean isFirstLoginToday = user.getLastActiveAt() == null
                || !user.getLastActiveAt().toLocalDate().equals(LocalDate.now());

        // Record the time of this login for admin visibility
        user.updateLastActiveAt();
        userRepository.save(user);

        if (isFirstLoginToday) {
            petMailService.sendRandomMail(user);
        }

        // Issue a signed JWT valid for the configured expiration period
        String token = Jwts.builder()
                .subject(user.getUserId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();

        // Collect all pets owned by this user
        List<LoginResponse.OwnedPet> ownedPets = petRepository.findByUser_IdOrderByPetIdAsc(user.getId())
                .stream()
                .map(pet -> new LoginResponse.OwnedPet(pet.getPetId(), pet.getPetTypeId(), pet.getLevel()))
                .collect(Collectors.toList());

        boolean hasUnreadMail = mailRepository.existsByUser_IdAndIsRead(user.getId(), false);
        return new LoginResponse(token, user.getNickname(), user.getShopName(), hasUnreadMail, ownedPets);
    }
}
