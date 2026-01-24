package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.data;

import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiClientEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiModelEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiSystemPromptEntity;
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
public class AiClientLoadDataStrategy implements ILoadDataStrategy {

    private final IAiAgentRepository aiAgentRepository;

    private final ThreadPoolExecutor executor;


    @Autowired
    public AiClientLoadDataStrategy(IAiAgentRepository aiAgentRepository,
                                    ThreadPoolExecutor threadPoolExecutor) {
        this.aiAgentRepository = aiAgentRepository;
        this.executor = threadPoolExecutor;
    }

    //1.查询Client的基本信息
    //2.1.查询Client的Mcp工具信息
    //2.2.查询Client的Advisor信息
    //2.3.查询Client的Prompt信息
    //2.4.查询Client的Model信息
    //3.1.查询Model的API信息
    //3.2.查询Model的Mcp工具信息

    /**
     * Step 1：查询 Client（1 个异步）
     * ↓ 完成后
     * Step 2：2.1 / 2.2 / 2.3 / 2.4 四个并发异步
     * ↓ 其中 2.4 完成后
     * Step 3：3.1 / 3.2 两个并发异步
     * ↓
     * load 方法返回前必须全部完成（阻塞）
     *
     */
    @Override
    public void load(ArmoryCommand armoryCommand,
                     ArmoryDynamicContext dynamicContext) {

        List<String> clientIdList = armoryCommand.getCommandIdList();
        if (clientIdList == null || clientIdList.isEmpty()) {
            log.warn("传入 clientIdList 为空，无法加载数据");
            return;
        }

        // Step 1：Client
        CompletableFuture<List<AiClientEntity>> clientsFuture =
                CompletableFuture.supplyAsync(() -> {
                    log.info("基于 {}[{}] 查询[{}]配置", "clientIdList", clientIdList, "Client");
                    return aiAgentRepository.queryAiClientEntityByClientIds(clientIdList);
                }, executor);

        // Step 2.1 MCP（依赖 clients，但返回结果给主线程处理）
        CompletableFuture<List<AiMcpToolEntity>> mcpFuture = clientsFuture.thenCompose(clients -> {
            if (clients == null || clients.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> mcpIds = clients.stream()
                    .<String>mapMulti((c, consumer) -> c.getActiveMcpIdList().forEach(consumer))
                    .distinct().toList();
            if (mcpIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "mcpIds", mcpIds, "AiClientMcpTool");
                return aiAgentRepository.queryAiMcpToolEntityByMcpToolIds(mcpIds);
            }, executor);
        });

        // Step 2.2 Advisor
        CompletableFuture<List<AiAdvisorEntity>> advisorFuture = clientsFuture.thenCompose(clients -> {
            if (clients == null || clients.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> advisorIds = clients.stream()
                    .<String>mapMulti((c, consumer) -> c.getActiveAdvisorIdList().forEach(consumer))
                    .distinct().toList();
            if (advisorIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "advisorIds", advisorIds, "AiClientAdvisor");
                return aiAgentRepository.queryAiClientAdvisorEntityByAdvisorIds(advisorIds);
            }, executor);
        });

        // Step 2.3 Prompt
        CompletableFuture<List<AiSystemPromptEntity>> promptFuture = clientsFuture.thenCompose(clients -> {
            if (clients == null || clients.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> promptIds = clients.stream()
                    .map(AiClientEntity::getActiveSystemPromptId)
                    .distinct()
                    .toList();
            if (promptIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "promptIds", promptIds, "AiClientPrompt");
                return aiAgentRepository.queryAiSystemPromptEntityByPromptIds(promptIds);
            }, executor);
        });

        // Step 2.4 Model（返回 List<AiModelEntity>）
        CompletableFuture<List<AiModelEntity>> modelFuture = clientsFuture.thenCompose(clients -> {
            if (clients == null || clients.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> modelIds = clients.stream()
                    .map(AiClientEntity::getActiveModelId)
                    .distinct()
                    .toList();
            if (modelIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "modelIds", modelIds, "AiClientModel");
                return aiAgentRepository.queryAiModelEntityByModelIds(modelIds);
            }, executor);
        });

        // Step 3.1 API（依赖 Model）
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

        // Step 3.2 Model MCP TOOL（依赖 Model）（可选）
        CompletableFuture<List<AiMcpToolEntity>> modelMcpToolFuture = modelFuture.thenCompose(models -> {
            if (models == null || models.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> mcpToolIds = models.stream()
                    .<String>mapMulti((model, consumer) -> {
                        model.getActiveMcpIdList().forEach(consumer::accept);
                    })
                    .distinct().toList();
            if (mcpToolIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "mcpToolIds", mcpToolIds, "AiModelMcpTool");
                return aiAgentRepository.queryAiMcpToolEntityByMcpToolIds(mcpToolIds);
            }, executor);
        });

        // 等待所有任务完成（阻塞）
        CompletableFuture<Void> all = CompletableFuture.allOf(
                clientsFuture,
                mcpFuture, advisorFuture, promptFuture, modelFuture,
                apiFuture, modelMcpToolFuture);

        try {
            all.join(); // 阻塞直到所有查询完成
            // === 主线程统一写入 dynamicContext（保证写入在同一线程完成） ===
            dynamicContext.setValue(AiAgentEnum.AI_CLIENT.getDataName(), clientsFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_CLIENT_MCP_TOOL.getDataName(), mcpFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_ADVISOR.getDataName(), advisorFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_SYSTEM_PROMPT.getDataName(), promptFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_CHAT_MODEL.getDataName(), modelFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_API.getDataName(), apiFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_MODEL_MCP_TOOL.getDataName(), modelMcpToolFuture.join());
            log.info("load 完成（主线程写入 dynamicContext）");
        } catch (Exception e) {
            log.error("load 失败", e);
        }
    }
}