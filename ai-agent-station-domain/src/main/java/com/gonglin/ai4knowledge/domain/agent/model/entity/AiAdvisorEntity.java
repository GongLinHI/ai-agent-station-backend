package com.gonglin.ai4knowledge.domain.agent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAdvisorEntity {

    /**
     * 顾问ID（业务用）
     */
    private String advisorId;

    /**
     * 顾问名称
     */
    private String advisorName;

    /**
     * 描述
     */
    private String description;

    /**
     * 顾问类型(PromptChatMemory/RagAnswer/SimpleLoggerAdvisor等)
     */
    private String advisorType;

    /**
     * 扩展参数配置(json)
     */
    private String extParam;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;
}
