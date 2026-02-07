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
public class UserRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID (required for update).
     */
    private Long id;

    /**
     * Business user ID (optional).
     */
    private String userId;

    /**
     * Username (required for create).
     */
    private String username;

    /**
     * Password (required for create, optional for update).
     */
    private String password;

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

    /**
     * Status (0: disabled, 1: enabled).
     */
    private Integer status;
}
