package com.gonglin.ai4knowledge.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Username (required, length 1-64).
     */
    private String username;

    /**
     * Password (required, length 8-64).
     */
    private String password;

    /**
     * Confirm password (must match password).
     */
    private String confirmPassword;

    /**
     * Nickname (optional).
     */
    private String nickname;

    /**
     * Email (optional).
     */
    private String email;

    /**
     * Phone (optional).
     */
    private String phone;
}
