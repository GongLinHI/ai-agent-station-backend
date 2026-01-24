package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.aggregate.AiAgentEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.WorkflowStep;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.AiAgentWorkFlowCache;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class CacheNode extends AbstractArmorySupport {

    private final AiAgentWorkFlowCache aiAgentWorkFlowCache;

    @Autowired
    public CacheNode(AiAgentWorkFlowCache aiAgentWorkFlowCache) {
        this.aiAgentWorkFlowCache = aiAgentWorkFlowCache;
    }

    @Override
    protected void multiThread(ArmoryCommand armoryCommand,
                               ArmoryDynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }

    @Override
    protected String doApply(ArmoryCommand armoryCommand, ArmoryDynamicContext dynamicContext) throws Exception {
        log.info("执行{} - {}", this.getClass().getSimpleName(), this.jsonMapper.writeValueAsString(armoryCommand));
        if (!AiAgentEnum.AI_AGENT.getCommand().equals(armoryCommand.getCommandType())) {
            return router(armoryCommand, dynamicContext);
        }
        // 缓存相关逻辑可以在这里实现
        TypeToken<List<AiAgentEntity>> typeToken = new TypeToken<>() {
        };
        List<AiAgentEntity> aiAgentEntities = dynamicContext.getValue(AiAgentEnum.AI_AGENT.getDataName(), typeToken);
        for (AiAgentEntity entity : aiAgentEntities) {
            Map<String, WorkflowStep> map = new HashMap<>();
            for (WorkflowStep step : entity.getWorkflowStepList()) {
                ChatClient chatClient = this.getBean(ClientNode.getBeanName(step.getClientId()), ChatClient.class);
                step.setChatClient(chatClient);
                map.put(StringUtils.capitalize(step.getClientType()), step);
            }
            aiAgentWorkFlowCache.put(entity.getAgentId(), map);
        }
        return router(armoryCommand, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> get(ArmoryCommand armoryCommand,
                                                                            ArmoryDynamicContext dynamicContext) throws Exception {
        return null;
    }
}
