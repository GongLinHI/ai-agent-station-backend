package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.data;

import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiModelEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.repository.IAiAgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class AiChatModelLoadDataStrategy implements ILoadDataStrategy {

    private final IAiAgentRepository aiAgentRepository;

    private final ThreadPoolExecutor executor;


    @Autowired
    public AiChatModelLoadDataStrategy(IAiAgentRepository aiAgentRepository,
                                       ThreadPoolExecutor threadPoolExecutor) {
        this.aiAgentRepository = aiAgentRepository;
        this.executor = threadPoolExecutor;
    }


    //1.查询Model的基本信息
    //2.1.查询Model的API信息
    //2.2.查询Model的Mcp工具信息

    /**
     * Step 3：3.1 / 3.2 两个并发异步
     * ↓
     * load 方法返回前必须全部完成（阻塞）
     */
    @Override
    public void load(ArmoryCommand armoryCommand,
                     ArmoryDynamicContext dynamicContext) {

        List<String> modelIds = armoryCommand.getCommandIdList();
        if (modelIds == null || modelIds.isEmpty()) {
            log.warn("传入 modelIds 为空，无法加载数据");
            return;
        }
        // 1. 根据modelIds 查询Model的基本信息
        CompletableFuture<List<AiModelEntity>> modelFuture = CompletableFuture.supplyAsync(() -> {
            log.info("基于 {}[{}] 查询[{}]配置", "modelIds", modelIds, "AiClientModel");
            return aiAgentRepository.queryAiModelEntityByModelIds(modelIds);
        }, executor);

        //2.1.查询Model的API信息
        CompletableFuture<List<?>> apiFuture = modelFuture.thenCompose(models -> {
            if (models == null || models.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> apiIds = models.stream()
                    .map(AiModelEntity::getApiId)
                    .distinct()
                    .toList();
            if (apiIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "apiIds", apiIds, "AiApi");
                return aiAgentRepository.queryAiApiEntityByApiIds(apiIds);
            }, executor);
        });

        //2.2.查询Model的Mcp工具信息
        CompletableFuture<List<AiMcpToolEntity>> modelMcpToolFuture = modelFuture.thenCompose(models -> {
            if (models == null || models.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> mcpToolIds = models.stream()
                    .<String>mapMulti((model, consumer) -> model.getActiveMcpIdList().forEach(consumer))
                    .distinct().toList();
            if (mcpToolIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "mcpToolIds", mcpToolIds, "AiModelMcpTool");
                return aiAgentRepository.queryAiMcpToolEntityByMcpToolIds(mcpToolIds);
            }, executor);
        });

        // 等待所有任务完成（阻塞）
        CompletableFuture<Void> all = CompletableFuture.allOf(
                modelFuture,
                apiFuture, modelMcpToolFuture);

        try {
            all.join(); // 阻塞直到所有查询完成
            // === 主线程统一写入 dynamicContext（保证写入在同一线程完成） ===
            dynamicContext.setValue(AiAgentEnum.AI_CHAT_MODEL.getDataName(), modelFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_API.getDataName(), apiFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_MODEL_MCP_TOOL.getDataName(), modelMcpToolFuture.join());
            log.info("load 完成（主线程写入 dynamicContext）");
        } catch (Exception e) {
            log.error("load 失败", e);
        }
    }

}
