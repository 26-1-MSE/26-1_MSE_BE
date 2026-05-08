package com.ajou.pettown.pet.dto;

// Request body for the pet acquisition endpoint.
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetAcquireRequest {
    private Integer petTypeId;
}
