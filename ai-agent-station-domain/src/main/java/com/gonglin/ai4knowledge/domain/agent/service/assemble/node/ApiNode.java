package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiApiEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ApiNode extends AbstractArmorySupport {

    private final ModelMcpToolNode modelMcpToolNode;
    private final CacheNode cacheNode;

    @Autowired
    public ApiNode(ModelMcpToolNode modelMcpToolNode,
                   CacheNode cacheNode) {
        this.modelMcpToolNode = modelMcpToolNode;
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
        List<AiApiEntity> aiApiEntities = dynamicContext.getValue(getDataName(),
                                                                  new TypeToken<List<AiApiEntity>>() {
                                                                  });
        if (aiApiEntities == null || aiApiEntities.isEmpty()) {
            log.info("没有需要初始化的API");
            return "";
        }
        for (AiApiEntity entity : aiApiEntities) {
            OpenAiApi api = OpenAiApi.builder().
                    baseUrl(entity.getBaseUrl())
                    .apiKey(entity.getApiKey())
                    .completionsPath(entity.getCompletionsPath())
                    .embeddingsPath(entity.getEmbeddingsPath())
                    .build();
            this.registerBean(getBeanName(entity.getApiId()), api);
        }
        return this.router(armoryCommand, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String>
    get(ArmoryCommand armoryCommand, ArmoryDynamicContext dynamicContext) throws Exception {
        String commandType = armoryCommand.getCommandType();
        if (commandType.equalsIgnoreCase(AiAgentEnum.AI_API.getCommand())) {
            return cacheNode;
        }
        return modelMcpToolNode;
    }

    public static String getBeanName(String beanId) {
        return AiAgentEnum.AI_API.getBeanName(beanId);
    }

    public static String getDataName() {
        return AiAgentEnum.AI_API.getDataName();
    }
}
