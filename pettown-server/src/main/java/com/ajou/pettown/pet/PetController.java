package com.ajou.pettown.pet;

// REST controller for pet room viewing and pet acquisition.
import com.ajou.pettown.common.dto.ApiResponse;
import com.ajou.pettown.pet.dto.PetAcquireRequest;
import com.ajou.pettown.pet.dto.PetAcquireResponse;
import com.ajou.pettown.pet.dto.PetRoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
