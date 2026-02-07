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
public class RolePermission {
    /**
     * Primary key ID.
     */
    private Long id;

    /**
     * Business role ID.
     */
    private String roleId;

    /**
     * Business permission ID.
     */
    private String permId;

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
