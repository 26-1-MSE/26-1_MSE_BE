package com.ajou.pettown.pet;

import com.ajou.pettown.common.dto.ApiResponse;
import com.ajou.pettown.pet.dto.PetAcquireRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Pet>>> getPets(
            @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(ApiResponse.ok(petService.getPets(userId)));
    }

    @PostMapping("/acquire")
    public ResponseEntity<ApiResponse<Pet>> acquirePet(
            @RequestAttribute("userId") String userId,
            @RequestBody PetAcquireRequest request) {
        try {
            Pet pet = petService.acquirePet(userId, request.getPetTypeId());
            return ResponseEntity.ok(ApiResponse.ok(pet));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.fail(e.getMessage()));
        }
    }
}