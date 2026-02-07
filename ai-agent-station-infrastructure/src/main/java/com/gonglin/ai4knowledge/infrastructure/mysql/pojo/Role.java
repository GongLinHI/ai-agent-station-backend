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
public class Role {
    /**
     * Primary key ID.
     */
    private Long id;

    /**
     * Business role ID (UUID).
     */
    private String roleId;

    /**
     * Role code (e.g., admin).
     */
    private String roleCode;

    /**
     * Role name.
     */
    private String roleName;

    /**
     * Description.
     */
    private String description;

    /**
     * Status (0: disabled, 1: enabled).
     */
    private Integer status;

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
