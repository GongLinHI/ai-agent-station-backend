package com.gonglin.ai4knowledge.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientModelResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模型ID（业务用）
     */
    private String modelId;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * API ID
     */
    private String apiId;

    /**
     * 模型类型（chat/embedding等）
     */
    private String modelType;

    /**
     * 模型提供商（openai/azure等）
     */
    private String modelProvider;

    /**
     * 模型版本
     */
    private String modelVersion;

    /**
     * 扩展参数（JSON格式，如chat模型的temperature、maxTokens；embedding模型的dimensions）
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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}