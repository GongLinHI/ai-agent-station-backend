package com.gonglin.ai4knowledge.domain.agent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiApiEntity {
    /**
     * API唯一标识(业务ID)
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
     * 生成式路径
     */
    private String completionsPath;
    /**
     * Embeddings路径
     */
    private String embeddingsPath;

    /**
     * API提供商
     */
    private String apiProvider;

    /**
     * 附加参数，JOSN格式存储
     */
    private String extParam;

    /**
     * 连接超时时间，单位秒
     */
    private Integer timeout;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;
}
