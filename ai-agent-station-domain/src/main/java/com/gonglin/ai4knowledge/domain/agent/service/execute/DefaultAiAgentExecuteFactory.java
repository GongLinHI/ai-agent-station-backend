package com.gonglin.ai4knowledge.domain.agent.service.execute;

import com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.IAiAgentExecuteStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class DefaultAiAgentExecuteFactory {
    private final Map<String, IAiAgentExecuteStrategy> strategyMap;

    @Autowired
    public DefaultAiAgentExecuteFactory(Map<String, IAiAgentExecuteStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public IAiAgentExecuteStrategy getStrategy(String strategyKey) {
        String key = StringUtils.lowerCase(strategyKey);
        IAiAgentExecuteStrategy strategy = strategyMap.get(key);
        if (strategy == null) {
            log.warn("No strategy found for key: [{}]", key);
        }
        return strategy;
    }

    public IAiAgentExecuteStrategy getDefaultStrategy() {
        return strategyMap.get("auto");
    }

}
