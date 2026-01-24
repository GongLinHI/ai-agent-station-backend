package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.mcp.IParseMcpToolStrategy;
import com.gonglin.ai4knowledge.types.common.McpTransferType;
import com.google.common.reflect.TypeToken;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ModelMcpToolNode extends AbstractArmorySupport {

    private final Map<String, IParseMcpToolStrategy> parseMcpToolStrategyMap;
    private final ModelNode modelNode;
    private final CacheNode cacheNode;

    @Autowired
    public ModelMcpToolNode(Map<String, IParseMcpToolStrategy> parseMcpToolStrategyMap,
                            ModelNode modelNode,
                            CacheNode cacheNode) {
        this.parseMcpToolStrategyMap = parseMcpToolStrategyMap;
        this.modelNode = modelNode;
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
        List<AiMcpToolEntity> aiMcpToolEntities = dynamicContext.getValue(getDataName(),
                                                                          new TypeToken<List<AiMcpToolEntity>>() {
                                                                          });
        if (aiMcpToolEntities == null || aiMcpToolEntities.isEmpty()) {
            log.info("没有需要初始化的模型中的MCP工具");
            return router(armoryCommand, dynamicContext);
        }

        for (AiMcpToolEntity entity : aiMcpToolEntities) {
            McpSyncClient client = null;
            if (McpTransferType.SSE.equalsIgnoreCase(entity.getTransportType())) {
                IParseMcpToolStrategy sseStrategy = this.parseMcpToolStrategyMap.get(McpTransferType.SSE);
                client = sseStrategy.parse(entity);
            } else if (McpTransferType.STDIO.equalsIgnoreCase(entity.getTransportType())) {
                IParseMcpToolStrategy stdioStrategy = this.parseMcpToolStrategyMap.get(McpTransferType.STDIO);
                client = stdioStrategy.parse(entity);
            } else {
                log.warn("不支持的MCP传输类型：{}", entity.getTransportType());
            }
            this.registerBean(getBeanName(entity.getMcpId()), client);
        }

        return router(armoryCommand, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> get(
            ArmoryCommand armoryCommand,
            ArmoryDynamicContext dynamicContext) throws Exception {
        String commandType = armoryCommand.getCommandType();

        if (commandType.equalsIgnoreCase(AiAgentEnum.AI_MODEL_MCP_TOOL.getCommand())) {
            return cacheNode;
        }
        // 下一个节点
        return modelNode;
    }


    public static String getBeanName(String beanId) {
        return AiAgentEnum.AI_MODEL_MCP_TOOL.getBeanName(beanId);
    }

    public static String getDataName() {
        return AiAgentEnum.AI_MODEL_MCP_TOOL.getDataName();
    }
}
