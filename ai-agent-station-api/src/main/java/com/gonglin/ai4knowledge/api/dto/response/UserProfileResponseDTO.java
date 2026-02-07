package com.gonglin.ai4knowledge.api.dto.response;

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
public class UserProfileResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * User info.
     */
    private UserResponseDTO user;

    /**
     * Role code list.
     */
    private List<String> roles;

    /**
     * Permission code list.
     */
    private List<String> permissions;
}
