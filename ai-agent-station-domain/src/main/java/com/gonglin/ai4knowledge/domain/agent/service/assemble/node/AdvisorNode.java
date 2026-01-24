package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.advisor.IParseAdvisorStrategy;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class AdvisorNode extends AbstractArmorySupport {

    private final Map<String, IParseAdvisorStrategy> parseAdvisorStrategyMap;
    private final SystemPromptNode nextNode;
    private final CacheNode cacheNode;

    public AdvisorNode(Map<String, IParseAdvisorStrategy> parseAdvisorStrategyMap,
                       SystemPromptNode systemPromptNode,
                       CacheNode cacheNode) {
        this.parseAdvisorStrategyMap = parseAdvisorStrategyMap;
        this.nextNode = systemPromptNode;
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
        TypeToken<List<AiAdvisorEntity>> typeToken = new TypeToken<>() {
        };
        List<AiAdvisorEntity> aiAdvisorEntities = dynamicContext.getValue(getDataName(), typeToken);
        if (aiAdvisorEntities == null || aiAdvisorEntities.isEmpty()) {
            log.info("没有需要初始化的Advisor");
            return router(armoryCommand, dynamicContext);
        }
        for (AiAdvisorEntity entity : aiAdvisorEntities) {
            String advisorType = entity.getAdvisorType();
            Advisor advisor = this.parseAdvisorStrategyMap.get(advisorType).parse(entity);
            this.registerBean(getBeanName(entity.getAdvisorId()), advisor);
        }

        return router(armoryCommand, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> get(
            ArmoryCommand armoryCommand,
            ArmoryDynamicContext dynamicContext) throws Exception {
        String commandType = armoryCommand.getCommandType();
        if (commandType.equalsIgnoreCase(AiAgentEnum.AI_ADVISOR.getCommand())) {
            return cacheNode;
        }
        // 下一个节点
        return nextNode;
    }


    public static String getBeanName(String beanId) {
        return AiAgentEnum.AI_ADVISOR.getBeanName(beanId);
    }

    public static String getDataName() {
        return AiAgentEnum.AI_ADVISOR.getDataName();
    }
}
