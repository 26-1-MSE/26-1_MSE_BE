package com.ajou.pettown.admin.dto;

// DTO representing a user's full detail for the admin detail page.
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AdminUserDetailDto {
    private Long id;
    private String userId;
    private String nickname;
    private String shopName;
    private LocalDateTime lastActiveAt;
    private List<PetDetail> pets;
    private List<ItemDetail> items;

    @Getter
    @AllArgsConstructor
    public static class PetDetail {
        private Long petId;
        private String name;       // Scout / Clover / Rusty / Daisy
        private String typeName;   // Judy / Nick / Bambi / Pumba
        private int level;
        private int food;
        private int water;
    }

    @Getter
    @AllArgsConstructor
    public static class ItemDetail {
        private String itemName;   // Pumpkin / Banana / Apple / Carrot / Water
        private int count;
    }
}
