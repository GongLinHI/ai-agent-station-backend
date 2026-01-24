package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStep {
    private String clientId;
    private String clientType;
    private ChatClient chatClient;
    private String clientStepPrompt;
    private Integer sequence;
}
