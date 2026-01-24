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
public class AiClientApiRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（更新时使用，创建时为空）
     */
    private Long id;

    /**
     * API ID（业务用，非空字符串，最大长度64）
     */
    private String apiId;

    /**
     * API名称（非空字符串，最大长度50）
     */
    private String apiName;

    /**
     * 基础URL（非空字符串，最大长度255）
     */
    private String baseUrl;

    /**
     * API密钥（非空字符串，最大长度255）
     */
    private String apiKey;

    /**
     * 完成路径（可选，默认v1/chat/completions，最大长度100）
     */
    private String completionsPath;

    /**
     * 嵌入路径（可选，默认v1/embeddings，最大长度100）
     */
    private String embeddingsPath;

    /**
     * API提供商（非空字符串，如openai/azure等，最大长度50）
     */
    private String apiProvider;

    /**
     * 扩展参数（可选，JSON格式字符串）
     */
    private String extParam;

    /**
     * 超时时间（可选，单位秒，默认180）
     */
    private Integer timeout;

    /**
     * 状态（0:禁用,1:启用）
     */
    private Integer status;
}
