package com.gonglin.ai4knowledge.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiClientModelQueryRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模型ID（业务用，可选，模糊查询）
     */
    private String modelId;

    /**
     * API ID（可选，模糊查询）
     */
    private String apiId;

    /**
     * 模型类型（可选，模糊查询）
     */
    private String modelType;

    /**
     * 模型提供商（可选，模糊查询）
     */
    private String modelProvider;

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