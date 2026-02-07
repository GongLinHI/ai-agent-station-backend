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
public class RoleResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID.
     */
    private Long id;

    /**
     * Business role ID.
     */
    private String roleId;

    /**
     * Role code.
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
}
