package com.ajou.pettown.item;

import com.ajou.pettown.item.dto.ItemAcquireResponse;
import com.ajou.pettown.item.dto.ItemUseResponse;

public interface ItemService {
    ItemAcquireResponse acquireItem(String userId, Integer itemTypeId, Integer count);
    ItemUseResponse useItem(String userId, Long petId, Integer itemTypeId);
}
