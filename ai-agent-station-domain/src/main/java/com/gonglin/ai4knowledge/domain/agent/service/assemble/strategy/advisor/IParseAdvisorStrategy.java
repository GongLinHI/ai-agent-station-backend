package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.advisor;

import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import org.springframework.ai.chat.client.advisor.api.Advisor;

public interface IParseAdvisorStrategy {
    Advisor parse(AiAdvisorEntity aiAdvisorEntity);
}
