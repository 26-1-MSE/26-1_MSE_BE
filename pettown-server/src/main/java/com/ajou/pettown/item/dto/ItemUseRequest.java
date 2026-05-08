package com.ajou.pettown.item.dto;

// Request body for using an item on a specific pet.
import lombok.Getter;

@Getter
public class ItemUseRequest {
    private Long petId;
    private Integer itemTypeId;
}
