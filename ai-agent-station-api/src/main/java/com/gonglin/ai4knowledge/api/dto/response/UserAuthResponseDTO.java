package com.gonglin.ai4knowledge.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Token name.
     */
    private String tokenName;

    /**
     * Token value.
     */
    private String tokenValue;

    /**
     * Token expiration (seconds).
     */
    private Long expiresIn;

    /**
     * User profile (basic info).
     */
    private UserResponseDTO user;
}
