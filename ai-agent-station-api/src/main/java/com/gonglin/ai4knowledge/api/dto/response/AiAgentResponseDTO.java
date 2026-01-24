package com.gonglin.ai4knowledge.api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 智能体ID（非空字符串，对应ai_agent表的agent_id字段）
     */
    private String agentId;

    /**
     * 智能体名称（非空字符串，最大长度50）
     */
    private String agentName;

    /**
     * 描述（可选，最大长度255）
     */
    private String description;

    /**
     * 渠道类型（可选，值为agent或chat_stream）
     */
    private String channel;

    /**
     * 执行策略（可选，值为auto或flow）
     */
    private String strategy;

    /**
     * 状态（0:禁用,1:启用）
     */
    private Integer status;

    /**
     * 工作流步骤客户端ID列表（可选，按sequence排序）
     */
    private List<String> workflowStepClientIdList;

}