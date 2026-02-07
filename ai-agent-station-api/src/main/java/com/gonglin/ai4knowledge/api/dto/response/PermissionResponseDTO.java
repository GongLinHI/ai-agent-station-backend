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
public class PermissionResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID.
     */
    private Long id;

    /**
     * Business permission ID.
     */
    private String permId;

    /**
     * Permission code.
     */
    private String permCode;

    /**
     * Permission name.
     */
    private String permName;

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
