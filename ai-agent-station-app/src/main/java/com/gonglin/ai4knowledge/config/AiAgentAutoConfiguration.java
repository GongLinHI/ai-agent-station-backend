package com.gonglin.ai4knowledge.config;

import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.factory.DefaultArmoryStrategyFactory;
import com.gonglin.ai4knowledge.property.AiAgentAutoConfigurationProperties;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.ArmoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * AI Agent 自动配置类
 * <p>
 * 该类用于在应用启动完成后自动预热指定的 AI Agent
 * <p>
 * 当配置属性 <code>ai-agent.auto.enabled</code> 为 <code>true</code> 时 且 配置属性 <code>ai-agent.auto.agent-ids</code> 不为空时，
 * 会在应用启动完成后自动预热指定的 AI Agent
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AiAgentAutoConfigurationProperties.class) // 开启配置属性绑定
@ConditionalOnProperty(prefix = "ai-agent.auto", name = "enabled", havingValue = "true") // 当配置属性 ai-agent.auto.enabled 为 true 时才加载当前配置类
class AiAgentAutoConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    private final AiAgentAutoConfigurationProperties properties;
    private final DefaultArmoryStrategyFactory factory;

    @Autowired
    public AiAgentAutoConfiguration(AiAgentAutoConfigurationProperties properties,
                                    DefaultArmoryStrategyFactory factory) {
        this.properties = properties;
        this.factory = factory;
    }


    @Override // 当应用启动完成后触发该事件
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("AI Agent Auto Configuration is enabled.");
        if (!properties.isEnabled()) {
            log.info("AI Agent Auto Configuration is disabled.");
            return;
        } else if (properties.getAgentIds().isEmpty()) {
            log.info("AI Agent Auto Configuration is empty.");
            return;
        }
        // 在这里添加 AI Agent 自动化预热的逻辑
        ArmoryCommand command = ArmoryCommand.builder()
                .commandIdList(properties.getAgentIds())
                .commandType(AiAgentEnum.AI_AGENT.getCommand())
                .build();
        ArmoryDynamicContext context = new ArmoryDynamicContext();
        try {
            factory.getDefaultStrategyHandler().apply(command, context);
        } catch (Exception e) {
            log.error("AI Agent自动预热失败", e);
            throw new ArmoryException(ExceptionCode.ARMORY_WARMUP_FAILED,
                                      "AI Agent自动预热失败", e);
        }
    }
}
