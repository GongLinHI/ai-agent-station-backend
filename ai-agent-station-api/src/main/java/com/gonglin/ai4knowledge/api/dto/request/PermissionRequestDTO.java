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
public class PermissionRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID (required for update).
     */
    private Long id;

    /**
     * Business permission ID (optional).
     */
    private String permId;

    /**
     * Permission code (required for create).
     */
    private String permCode;

    /**
     * Permission name (required for create).
     */
    private String permName;

    /**
     * Description (optional).
     */
    private String description;

    /**
     * Status (0: disabled, 1: enabled).
     */
    private Integer status;
}
