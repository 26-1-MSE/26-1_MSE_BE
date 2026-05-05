package com.ajou.pettown.item.dto;

import lombok.Getter;

@Getter
public class ItemUseRequest {
    private Long petId;
    private Integer itemTypeId;
}
