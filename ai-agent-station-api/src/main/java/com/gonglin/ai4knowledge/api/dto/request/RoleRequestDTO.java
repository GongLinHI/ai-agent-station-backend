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
public class RoleRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID (required for update).
     */
    private Long id;

    /**
     * Business role ID (optional).
     */
    private String roleId;

    /**
     * Role code (required for create).
     */
    private String roleCode;

    /**
     * Role name (required for create).
     */
    private String roleName;

    /**
     * Description (optional).
     */
    private String description;

    /**
     * Status (0: disabled, 1: enabled).
     */
    private Integer status;
}
