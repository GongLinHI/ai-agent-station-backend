package com.gonglin.ai4knowledge.infrastructure.mysql.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiModel {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模型ID（业务用，model-x）
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
     * 模型类型（chat、embedding等）
     */
    private String modelType;

    /**
     * 模型提供商(openai/azure等)
     */
    private String modelProvider;

    /**
     * 模型版本
     */
    private String modelVersion;

    /**
     * 拓展参数，json格式，如chat模型的temperature、maxTokens；embedding模型的dimensions
     */
    private String extParam;

    /**
     * 超时时间(秒)
     */
    private Integer timeout;

    /**
     * 状态(0:禁用,1:启用)
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

    /**
     * 软删除(0:未删,1:已删)
     */
    @Builder.Default
    private Integer isDeleted = 0;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;
}
