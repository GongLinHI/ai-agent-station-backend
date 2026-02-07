package com.gonglin.ai4knowledge.infrastructure.mysql.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Primary key ID.
     */
    private Long id;

    /**
     * Business user ID (UUID).
     */
    private String userId;

    /**
     * Login username.
     */
    private String username;

    /**
     * BCrypt password hash.
     */
    private String passwordHash;

    /**
     * Nickname.
     */
    private String nickname;

    /**
     * Email.
     */
    private String email;

    /**
     * Phone number.
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

    /**
     * Soft delete flag (0: not deleted, 1: deleted).
     */
    @Builder.Default
    private Integer isDeleted = 0;

    /**
     * Delete time.
     */
    private LocalDateTime deleteTime;
}
