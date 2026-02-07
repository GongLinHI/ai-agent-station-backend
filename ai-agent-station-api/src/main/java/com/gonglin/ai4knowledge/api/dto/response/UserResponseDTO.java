package com.gonglin.ai4knowledge.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID.
     */
    private Long id;

    /**
     * Business user ID.
     */
    private String userId;

    /**
     * Username.
     */
    private String username;

    /**
     * Nickname.
     */
    private String nickname;

    /**
     * Email.
     */
    private String email;

    /**
     * Phone.
     */
    private String phone;

    /**
     * Status (0: disabled, 1: enabled).
     */
    private Integer status;

    /**
     * Last login time.
     */
    private LocalDateTime lastLoginTime;

    /**
     * Last login IP.
     */
    private String lastLoginIp;

    /**
     * Create time.
     */
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    private LocalDateTime updateTime;
}
