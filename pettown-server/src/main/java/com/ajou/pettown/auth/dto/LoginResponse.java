package com.ajou.pettown.auth.dto;

// Response returned after a successful login, including JWT token and initial user state.
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;   // JWT Bearer token
    private String nickname;
    private String shopName;
    private boolean hasUnreadMail; // whether the user has at least one unread mail
    private List<OwnedPet> ownedPets;

    // Minimal pet info embedded in the login response for quick client-side initialization
    @Getter
    @AllArgsConstructor
    public static class OwnedPet {
        private Long petId;
        private Integer petTypeId;
        private Integer level;
    }
}
