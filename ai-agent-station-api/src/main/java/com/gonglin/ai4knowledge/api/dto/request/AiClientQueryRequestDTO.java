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
public class AiClientQueryRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID（业务用，可选，模糊查询）
     */
    private String clientId;

    /**
     * 客户端名称（可选，模糊查询，最大长度50）
     */
    private String clientName;

    /**
     * 状态（可选，0:禁用,1:启用）
     */
    private Integer status;

    /**
     * 页码（可选，从1开始）
     */
    private Integer pageNum;

    /**
     * 每页大小（可选，默认10）
     */
    private Integer pageSize;
}
