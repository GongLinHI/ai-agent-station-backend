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
public class UserCancelRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Password (required).
     */
    private String password;
}
