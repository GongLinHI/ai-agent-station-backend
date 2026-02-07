package com.gonglin.ai4knowledge.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleAssignPermissionsRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Business role ID.
     */
    private String roleId;

    /**
     * Permission ID list.
     */
    private List<String> permIds;
}
