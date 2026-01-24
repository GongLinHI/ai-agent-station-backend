package com.gonglin.ai4knowledge.domain.agent.service.assemble;

import com.gonglin.ai4knowledge.domain.agent.model.valobj.WorkflowStep;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AiAgentWorkFlowCache {
    private final Map<String, Map<String, WorkflowStep>> cache = new HashMap<>();


    public boolean containsKey(String agentId) {
        return cache.containsKey(agentId);
    }

    public Map<String, WorkflowStep> get(String agentId) {
        return cache.computeIfAbsent(agentId, k -> new HashMap<>());
    }

    public void put(String agentId, Map<String, WorkflowStep> chatClientMap) {
        cache.put(agentId, chatClientMap);
    }

    public Map<String, WorkflowStep> remove(String agentId) {
        return cache.remove(agentId);
    }

    public int size() {
        return cache.size();
    }

    public void clear() {
        cache.clear();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }


}
