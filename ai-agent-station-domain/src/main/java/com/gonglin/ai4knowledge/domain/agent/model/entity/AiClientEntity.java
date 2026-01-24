package com.gonglin.ai4knowledge.domain.agent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiClientEntity {
    /**
     * 客户端ID（业务用）
     */
    private String clientId;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;

    /**
     * 当前激活的模型ID（业务用）
     */
    private String activeModelId;

    /**
     * 当前激活的系统提示词ID（业务用）
     */
    private String activeSystemPromptId;

    /**
     * 当前激活的MCP服务ID列表（业务用）
     */
    private List<String> activeMcpIdList;

    /**
     * 当前激活的顾问ID列表（业务用）
     */
    private List<String> activeAdvisorIdList;
}
