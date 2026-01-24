package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiSystemPromptEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class SystemPromptNode extends AbstractArmorySupport {
    private final ClientNode nextNode;
    private final CacheNode cacheNode;

    @Autowired
    public SystemPromptNode(ClientNode nextNode,
                            CacheNode cacheNode) {
        this.nextNode = nextNode;
        this.cacheNode = cacheNode;
    }

    @Override
    protected void multiThread(ArmoryCommand armoryCommand,
                               ArmoryDynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
    }

    @Override
    protected String doApply(ArmoryCommand armoryCommand,
                             ArmoryDynamicContext dynamicContext) throws Exception {
        log.info("执行{} - {}", this.getClass().getSimpleName(), this.jsonMapper.writeValueAsString(armoryCommand));
        TypeToken<List<AiSystemPromptEntity>> typeToken = new TypeToken<>() {
        };
        List<AiSystemPromptEntity> aiSystemPromptEntities = dynamicContext.getValue(getDataName(), typeToken);
        if (aiSystemPromptEntities == null || aiSystemPromptEntities.isEmpty()) {
            log.info("没有需要初始化的System Prompt");
            return router(armoryCommand, dynamicContext);
        }
        for (AiSystemPromptEntity entity : aiSystemPromptEntities) {
            String text = entity.getPromptContent();
            Prompt prompt = Prompt.builder().messages(new SystemMessage(text)).build();
            this.registerBean(getBeanName(entity.getPromptId()), prompt);
        }

        return router(armoryCommand, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> get(
            ArmoryCommand armoryCommand,
            ArmoryDynamicContext dynamicContext) throws Exception {
        String commandType = armoryCommand.getCommandType();

        if (commandType.equals(AiAgentEnum.AI_SYSTEM_PROMPT.getCommand())) {
            return cacheNode;
        }
        // 下一个节点
        return nextNode;
    }

    public static String getBeanName(String beanId) {
        return AiAgentEnum.AI_SYSTEM_PROMPT.getBeanName(beanId);
    }

    public static String getDataName() {
        return AiAgentEnum.AI_SYSTEM_PROMPT.getDataName();
    }
}
