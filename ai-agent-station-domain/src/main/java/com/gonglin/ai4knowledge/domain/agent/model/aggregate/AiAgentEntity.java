package com.gonglin.ai4knowledge.domain.agent.model.aggregate;

import com.gonglin.ai4knowledge.domain.agent.model.valobj.WorkflowStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAgentEntity {
    /**
     * 智能体ID（业务用）
     */
    private String agentId;

    /**
     * 智能体名称
     */
    private String agentName;

    /**
     * 描述
     */
    private String description;

    /**
     * 渠道类型(agent，chat_stream)
     */
    private String channel;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;

    /**
     * 执行策略(auto、flow)
     */
    private String strategy;

    private List<WorkflowStep> workflowStepList;
}
