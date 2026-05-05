package com.ajou.pettown.item.dto;

import lombok.Getter;

@Getter
public class ItemAcquireRequest {
    private Integer itemTypeId;
    private Integer count;
}
