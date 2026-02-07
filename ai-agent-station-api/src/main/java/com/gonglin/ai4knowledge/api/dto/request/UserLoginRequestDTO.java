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
public class UserLoginRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Username (required).
     */
    private String username;

    /**
     * Password (required).
     */
    private String password;
}
