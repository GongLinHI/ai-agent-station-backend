package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.advisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.PromptChatMemoryConfig;
import com.gonglin.ai4knowledge.types.common.AdvisorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("promptChatMemory")
public class PromptChatMemoryParseAdvisorStrategy implements IParseAdvisorStrategy {

    private final ObjectMapper jsonMapper;

    @Autowired
    public PromptChatMemoryParseAdvisorStrategy(@Qualifier("jsonMapper") ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Advisor parse(AiAdvisorEntity aiAdvisorEntity) {
        if (aiAdvisorEntity == null) {
            log.warn("解析 Advisor 失败：传入 aiAdvisorEntity 为空，无法加载数据");
            return null;
        } else if (!AdvisorType.PROMPT_CHAT_MEMORY.equalsIgnoreCase(aiAdvisorEntity.getAdvisorType())) {
            log.warn("解析 Advisor 失败：传入 aiAdvisorEntity 类型不匹配，无法加载数据，期待类型：{}，实际类型：{}",
                     AdvisorType.PROMPT_CHAT_MEMORY, aiAdvisorEntity.getAdvisorType());
            return null;
        }

        String extParam = aiAdvisorEntity.getExtParam();
        if (extParam == null || extParam.isBlank()) {
            extParam = "{}";
        }
        PromptChatMemoryConfig config = new PromptChatMemoryConfig();
        try {
            config = jsonMapper.readValue(extParam, PromptChatMemoryConfig.class);
        } catch (Exception e) {
            log.error("解析 Advisor 失败：{}", e.getMessage());
            return null;
        }


        return PromptChatMemoryAdvisor.builder(
                MessageWindowChatMemory.builder()
                        .maxMessages(config.getMaxMessages())
                        .build()
        ).build();
    }
}
