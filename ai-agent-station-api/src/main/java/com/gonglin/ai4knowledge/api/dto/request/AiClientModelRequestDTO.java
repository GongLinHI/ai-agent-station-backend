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
public class AiClientModelRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（更新时使用，创建时为空）
     */
    private Long id;

    /**
     * 模型ID（业务用，非空字符串，最大长度64）
     */
    private String modelId;

    /**
     * 模型名称（非空字符串，最大长度50）
     */
    private String modelName;

    /**
     * API ID（非空字符串，最大长度64）
     */
    private String apiId;

    /**
     * 模型类型（非空字符串，如chat/embedding等，最大长度50）
     */
    private String modelType;

    /**
     * 模型提供商（非空字符串，如openai/azure等，最大长度50）
     */
    private String modelProvider;

    /**
     * 模型版本（可选，最大长度50）
     */
    private String modelVersion;

    /**
     * 扩展参数（可选，JSON格式字符串，如chat模型的temperature、maxTokens；embedding模型的dimensions）
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