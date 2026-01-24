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
public class AiAgentFlowConfig {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 智能体ID(业务用)
     */
    private String agentId;

    /**
     * 客户端ID(业务用)
     */
    private String clientId;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 当前步骤的task prompt
     */
    private String clientStepPrompt;

    /**
     * 序列号(执行顺序)
     */
    private Integer sequence;

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
