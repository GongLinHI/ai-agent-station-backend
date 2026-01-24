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
public class AiClientApiQueryRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * API ID（业务用，可选，模糊查询）
     */
    private String apiId;

    /**
     * API名称（可选，模糊查询，最大长度50）
     */
    private String apiName;

    /**
     * 基础URL（可选，模糊查询）
     */
    private String baseUrl;

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
