package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiClientEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.google.common.reflect.TypeToken;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ClientNode extends AbstractArmorySupport {

    private final CacheNode cacheNode;

    @Autowired
    public ClientNode(CacheNode cacheNode) {
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
        TypeToken<List<AiClientEntity>> typeToken = new TypeToken<>() {
        };
        List<AiClientEntity> aiClientEntities = dynamicContext.getValue(getDataName(), typeToken);
        if (aiClientEntities == null || aiClientEntities.isEmpty()) {
            log.info("没有需要初始化的 AI Client");
            return router(armoryCommand, dynamicContext);
        }
        for (AiClientEntity entity : aiClientEntities) {
            ChatModel chatModel = this.getBean(ModelNode.getBeanName(entity.getActiveModelId()), ChatModel.class);
            // MCP
            List<McpSyncClient> mcpSyncClients = entity.getActiveMcpIdList().stream()
                    .map(mcpId -> this.getBean(ClientMcpToolNode.getBeanName(mcpId), McpSyncClient.class))
                    .toList();
            SyncMcpToolCallbackProvider toolCallbackProvider = SyncMcpToolCallbackProvider.builder()
                    .mcpClients(mcpSyncClients).build();
            // Advisor
            List<Advisor> advisors = entity.getActiveAdvisorIdList().stream()
                    .map(advisorId -> this.getBean(AdvisorNode.getBeanName(advisorId), Advisor.class))
                    .toList();
            // SystemPrompt
            Prompt systemPrompt = this.getBean(SystemPromptNode.getBeanName(entity.getActiveSystemPromptId()),
                                               Prompt.class);
            ChatClient chatClient = ChatClient.builder(chatModel)
                    .defaultToolCallbacks(toolCallbackProvider) // 工具回调的注册顺序 通常没有影响
                    .defaultAdvisors(advisors) // 注册顾问，确定执行顺序
                    .defaultSystem(systemPrompt.getSystemMessage().getText()) // 预设话术
                    .build();
            this.registerBean(getBeanName(entity.getClientId()), chatClient);
        }
        return router(armoryCommand, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> get(
            ArmoryCommand armoryCommand,
            ArmoryDynamicContext dynamicContext) throws Exception {
        return cacheNode;
    }


    public static String getBeanName(String beanId) {
        return AiAgentEnum.AI_CLIENT.getBeanName(beanId);
    }

    public static String getDataName() {
        return AiAgentEnum.AI_CLIENT.getDataName();
    }
}
