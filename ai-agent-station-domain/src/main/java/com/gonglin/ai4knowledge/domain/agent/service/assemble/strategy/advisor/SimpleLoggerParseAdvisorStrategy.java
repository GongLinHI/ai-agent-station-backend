package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.advisor;

import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import com.gonglin.ai4knowledge.types.common.AdvisorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.stereotype.Service;

@Slf4j
@Service("simpleLogger")
public class SimpleLoggerParseAdvisorStrategy implements IParseAdvisorStrategy {
    @Override
    public Advisor parse(AiAdvisorEntity aiAdvisorEntity) {
        if (aiAdvisorEntity == null) {
            log.warn("解析 Advisor 失败：传入 aiAdvisorEntity 为空，无法加载数据");
            return null;
        } else if (!AdvisorType.SIMPLE_LOGGER.equalsIgnoreCase(aiAdvisorEntity.getAdvisorType())) {
            log.warn("解析 Advisor 失败：传入 aiAdvisorEntity 类型不匹配，无法加载数据，期待类型：{}，实际类型：{}",
                     AdvisorType.SIMPLE_LOGGER, aiAdvisorEntity.getAdvisorType());
            return null;
        }
        return SimpleLoggerAdvisor.builder().build();
    }
}
