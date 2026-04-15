package com.ajou.pettown.pet;

import com.ajou.pettown.auth.User;
import com.ajou.pettown.auth.UserRepository;
import com.ajou.pettown.pet.dto.PetAcquireResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Pet> getPets(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        return petRepository.findByUser_Id(user.getId());
    }

    @Override
    public PetAcquireResponse acquirePet(String userId, Integer petTypeId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        int count = (int) petRepository.countByUser_Id(user.getId());
        if (count >= 4) {
            throw new RuntimeException("펫은 최대 4마리까지만 보유할 수 있습니다.");
        }

        Pet pet = Pet.builder()
                .user(user)
                .petTypeId(petTypeId)
                .build();
        petRepository.save(pet);

        return new PetAcquireResponse(
                true,
                new PetAcquireResponse.PetInfo(pet.getPetId(), pet.getPetTypeId(), pet.getLevel()),
                user.getUserId(), // User에 getUserId() 있어야 함
                count + 1);
    }
}