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
public class AiApi {
    /**
     * 主键ID
     */
    private Long id;

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
