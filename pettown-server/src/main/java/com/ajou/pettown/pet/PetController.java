package com.ajou.pettown.pet;

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
    public ResponseEntity<List<Pet>> getPets(@RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(petService.getPets(userId));
    }

    @PostMapping("/acquire")
    public ResponseEntity<Pet> acquirePet(@RequestAttribute("userId") String userId,
                                           @RequestParam Integer petTypeId) {
        return ResponseEntity.ok(petService.acquirePet(userId, petTypeId));
    }
}