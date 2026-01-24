package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiModelEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.types.common.ModelVersion;
import com.google.common.reflect.TypeToken;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ModelNode extends AbstractArmorySupport {

    private final ClientMcpToolNode clientMcpToolNode;
    private final CacheNode cacheNode;

    @Autowired
    public ModelNode(ClientMcpToolNode clientMcpToolNode,
                     CacheNode cacheNode) {
        this.clientMcpToolNode = clientMcpToolNode;
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
        TypeToken<List<AiModelEntity>> typeToken = new TypeToken<>() {
        };
        List<AiModelEntity> aiModelEntities = dynamicContext.getValue(getDataName(), typeToken);
        if (aiModelEntities == null || aiModelEntities.isEmpty()) {
            log.info("没有需要初始化的模型");
            return null;
        }
        for (AiModelEntity entity : aiModelEntities) {
            // API 信息
            String apiBeanName = ApiNode.getBeanName(entity.getApiId());
            OpenAiApi api = this.getBean(apiBeanName, OpenAiApi.class);

            if (entity.getExtParam() == null) {
                entity.setExtParam("{}");
            }
            OpenAiChatOptions chatOptions = jsonMapper.readValue(entity.getExtParam(), OpenAiChatOptions.class);
            chatOptions.setModel(entity.getModelVersion());
            // 保证非流式的情况下不出错
            if (ModelVersion.DeepSeekReasoner.equals(entity.getModelVersion())) {
                chatOptions.setExtraBody(Map.of("thinking", Map.of("type", "enabled")));
            }
            // MCP 工具列表
            List<McpSyncClient> mcpSyncClients = entity.getActiveMcpIdList().stream()
                    .map(mcpId -> this.getBean(ModelMcpToolNode.getBeanName(mcpId), McpSyncClient.class))
                    .toList();
            if (!mcpSyncClients.isEmpty()) {
                SyncMcpToolCallbackProvider mcpToolProvider = SyncMcpToolCallbackProvider.builder()
                        .mcpClients(mcpSyncClients)
                        .build();
                chatOptions.setToolCallbacks(Arrays.asList(mcpToolProvider.getToolCallbacks()));
            }
            // 组装成对话模型
            ChatModel chatModel = OpenAiChatModel.builder()
                    .openAiApi(api)
                    .defaultOptions(chatOptions)
                    .build();
            this.registerBean(getBeanName(entity.getModelId()), chatModel);
        }
        return router(armoryCommand, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> get(
            ArmoryCommand armoryCommand,
            ArmoryDynamicContext dynamicContext) throws Exception {
        String commandType = armoryCommand.getCommandType();

        if (AiAgentEnum.AI_CHAT_MODEL.getCommand().equalsIgnoreCase(commandType)) {
            return cacheNode;
        }
        // 下一个节点
        return clientMcpToolNode;
    }


    public static String getBeanName(String beanId) {
        return AiAgentEnum.AI_CHAT_MODEL.getBeanName(beanId);
    }

    public static String getDataName() {
        return AiAgentEnum.AI_CHAT_MODEL.getDataName();
    }
}
