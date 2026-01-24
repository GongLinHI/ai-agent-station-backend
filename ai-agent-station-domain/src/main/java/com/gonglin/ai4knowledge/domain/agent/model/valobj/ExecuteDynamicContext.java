package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import cn.bugstack.wrench.design.framework.tree.DynamicContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteDynamicContext extends DynamicContext {

    @Builder.Default
    private Integer step = 0;

    @Builder.Default
    private Integer maxStep = 20;

    @Builder.Default
    private String sessionId = UUID.randomUUID().toString();


    @Builder.Default
    StringJoiner stepSummarySJ = new StringJoiner("");
    @Builder.Default
    private boolean isFinished = false;
    private String currentTask;
    private ResponseBodyEmitter responseBodyEmitter;
    private Map<String, WorkflowStep> workflowStepMap;


    public void putStep(String clientType, WorkflowStep value) {
        Objects.requireNonNull(clientType, "clientType cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");
        this.workflowStepMap.put(clientType, value);
    }

    public WorkflowStep getStep(String clientType) {
        Objects.requireNonNull(clientType, "clientType cannot be null");
        return workflowStepMap.get(clientType);
    }


}
