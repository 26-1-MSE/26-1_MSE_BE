package com.ajou.pettown.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String nickname;
    private String shopName;
    private boolean hasUnreadMail;
    private List<OwnedPet> ownedPets;

    @Getter
    @AllArgsConstructor
    public static class OwnedPet {
        private Long petId;
        private Integer petTypeId;
    }
}
