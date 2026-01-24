package com.gonglin.ai4knowledge.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientApiResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * API ID（业务用）
     */
    private String apiId;

    /**
     * API名称
     */
    private String apiName;

    /**
     * 基础URL
     */
    private String baseUrl;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 完成路径
     */
    private String completionsPath;

    /**
     * 嵌入路径
     */
    private String embeddingsPath;

    /**
     * API提供商（openai/azure等）
     */
    private String apiProvider;

    /**
     * 扩展参数（JSON格式）
     */
    private String extParam;

    /**
     * 超时时间（秒）
     */
    private Integer timeout;

    /**
     * 状态（0:禁用,1:启用）
     */
    private Integer status;
}
