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
public class UserAssignRolesRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Business user ID.
     */
    private String userId;

    /**
     * Role ID list.
     */
    private List<String> roleIds;
}
