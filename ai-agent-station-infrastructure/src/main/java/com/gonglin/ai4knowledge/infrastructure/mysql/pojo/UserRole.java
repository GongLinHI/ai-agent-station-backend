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
public class UserRole {
    /**
     * Primary key ID.
     */
    private Long id;

    /**
     * Business user ID.
     */
    private String userId;

    /**
     * Business role ID.
     */
    private String roleId;

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
