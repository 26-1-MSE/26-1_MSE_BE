package com.ajou.pettown.item.dto;

// Request body for the item acquisition endpoint.
import lombok.Getter;

@Getter
public class ItemAcquireRequest {
    private Integer itemTypeId;
    private Integer count; // quantity to add
}
