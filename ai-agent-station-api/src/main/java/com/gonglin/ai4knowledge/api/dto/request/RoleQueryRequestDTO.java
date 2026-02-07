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
public class RoleQueryRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Role code (optional, fuzzy match).
     */
    private String roleCode;

    /**
     * Role name (optional, fuzzy match).
     */
    private String roleName;

    /**
     * Status (0: disabled, 1: enabled).
     */
    private Integer status;

    /**
     * Page number (default 1).
     */
    private Integer pageNum;

    /**
     * Page size (default 10).
     */
    private Integer pageSize;
}
