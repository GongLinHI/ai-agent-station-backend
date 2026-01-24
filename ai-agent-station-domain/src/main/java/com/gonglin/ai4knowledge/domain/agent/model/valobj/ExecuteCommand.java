package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCommand {
    // 执行哪一个AI Agent
    private String aiAgentId;
    // 用户原始输入
    private String userMessage;
    // 任务最大步骤
    private Integer maxStep;
    //Session ID
    private String sessionId;
}
