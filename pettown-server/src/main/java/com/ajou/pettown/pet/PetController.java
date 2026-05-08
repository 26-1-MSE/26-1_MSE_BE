package com.ajou.pettown.pet;

// REST controller for pet room viewing, pet list retrieval, and pet acquisition.
import com.ajou.pettown.auth.dto.LoginResponse;
import com.ajou.pettown.common.dto.ApiResponse;
import com.ajou.pettown.pet.dto.PetAcquireRequest;
import com.ajou.pettown.pet.dto.PetAcquireResponse;
import com.ajou.pettown.pet.dto.PetRoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    // Returns the pet's current stats and the user's usable items for the pet room screen
    @GetMapping("/petroom")
    public ResponseEntity<ApiResponse<PetRoomResponse>> getPetRoom(
            @RequestAttribute("userId") String userId,
            @RequestParam Long petId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(petService.getPetRoom(userId, petId)));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }

    // Returns a minimal list of the user's pets (same format as in login response)
    @GetMapping("/list")
    public ResponseEntity<Map<String, List<LoginResponse.OwnedPet>>> getPets(
            @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(Map.of("ownedPets", petService.getPets(userId)));
    }

    // Registers a new pet for the user (max 4 pets per user)
    @PostMapping("/acquire")
    public ResponseEntity<ApiResponse<PetAcquireResponse>> acquirePet(
            @RequestAttribute("userId") String userId,
            @RequestBody PetAcquireRequest request) {
        try {
            PetAcquireResponse response = petService.acquirePet(userId, request.getPetTypeId());
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }
}
