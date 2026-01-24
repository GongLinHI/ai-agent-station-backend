package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.data;

import com.gonglin.ai4knowledge.domain.agent.model.aggregate.AiAgentEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiClientEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiModelEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiSystemPromptEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.WorkflowStep;
import com.gonglin.ai4knowledge.domain.agent.repository.IAiAgentRepository;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.ArmoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class AiAgentLoadDataStrategy implements ILoadDataStrategy {
    private final IAiAgentRepository aiAgentRepository;

    private final ThreadPoolExecutor executor;


    @Autowired
    public AiAgentLoadDataStrategy(IAiAgentRepository aiAgentRepository,
                                   ThreadPoolExecutor threadPoolExecutor) {
        this.aiAgentRepository = aiAgentRepository;
        this.executor = threadPoolExecutor;
    }

    // 1.查询Agent的基本信息
    // 2 依据Agent信息，查询Client信息
    // 3.1 MCP（依赖 clients，但返回结果给主线程处理）
    // 3.2 Advisor(依赖 clients，但返回结果给主线程处理)
    // 3.3 Prompt(依赖 clients，但返回结果给主线程处理)
    // 3.4 Model(依赖 clients，但返回结果给主线程处理)
    // 4.1 API（依赖 Model,但返回结果给主线程处理）
    // 4.2 Model MCP TOOL（依赖 Model）（可选，但返回结果
    @Override
    public void load(ArmoryCommand armoryCommand, ArmoryDynamicContext dynamicContext) {
        List<String> agentIds = armoryCommand.getCommandIdList();
        if (agentIds == null || agentIds.isEmpty()) {
            log.warn("load 失败：传入 agentIds 为空，无法加载数据");
            return;
        }
        // 1.查询Agent的基本信息
        CompletableFuture<List<AiAgentEntity>> agentFuture = CompletableFuture.supplyAsync(() -> {
            log.info("基于 {}[{}] 查询[{}]配置", "agentIds", agentIds, "Agent");
            return aiAgentRepository.queryAiAgentEntityByAgentIds(agentIds);
        }, executor);
        // 2. 依据Agent信息，查询Client信息
        CompletableFuture<List<AiClientEntity>> clientFuture = agentFuture.thenCompose(agents -> {
            if (agents == null || agents.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> clientIds = agents.stream()
                    .<String>mapMulti((agent, con) -> {
                        agent.getWorkflowStepList().stream().map(WorkflowStep::getClientId).forEach(con::accept);
                    }).distinct().toList();
            if (clientIds == null || clientIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "clientIds", clientIds, "Client");
                return aiAgentRepository.queryAiClientEntityByClientIds(clientIds);
            }, executor);
        });


        // Step 3.1 MCP（依赖 clients，但返回结果给主线程处理）
        CompletableFuture<List<AiMcpToolEntity>> mcpFuture = clientFuture.thenCompose(clients -> {
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

        // Step 3.2 Advisor
        CompletableFuture<List<AiAdvisorEntity>> advisorFuture = clientFuture.thenCompose(clients -> {
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

        // Step 3.3 Prompt
        CompletableFuture<List<AiSystemPromptEntity>> promptFuture = clientFuture.thenCompose(clients -> {
            if (clients == null || clients.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> promptIds = clients.stream()
                    .map(AiClientEntity::getActiveSystemPromptId)
                    .distinct().toList();
            if (promptIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "promptIds", promptIds, "AiClientPrompt");
                return aiAgentRepository.queryAiSystemPromptEntityByPromptIds(promptIds);
            }, executor);
        });

        // Step 3.4 Model
        CompletableFuture<List<AiModelEntity>> modelFuture = clientFuture.thenCompose(clients -> {
            if (clients == null || clients.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> modelIds = clients.stream()
                    .map(AiClientEntity::getActiveModelId)
                    .distinct().toList();
            if (modelIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "modelIds", modelIds, "AiClientModel");
                return aiAgentRepository.queryAiModelEntityByModelIds(modelIds);
            }, executor);
        });

        // Step 4.1 API（依赖 Model）
        CompletableFuture<List<?>> apiFuture = modelFuture.thenCompose(models -> {
            if (models == null || models.isEmpty()) return CompletableFuture.completedFuture(List.of());
            List<String> apiIds = models.stream()
                    .map(AiModelEntity::getApiId)
                    .distinct().toList();
            if (apiIds.isEmpty()) return CompletableFuture.completedFuture(List.of());
            return CompletableFuture.supplyAsync(() -> {
                log.info("基于 {}[{}] 查询[{}]配置", "apiIds", apiIds, "AiApi");
                return aiAgentRepository.queryAiApiEntityByApiIds(apiIds);
            }, executor);
        });

        // Step 4.2 Model MCP TOOL（依赖 Model）（可选）
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


        CompletableFuture<Void> all = CompletableFuture.allOf(
                agentFuture,
                clientFuture, mcpFuture, advisorFuture, promptFuture,
                modelFuture,
                apiFuture, modelMcpToolFuture
        );
        try {
            all.join();
            // === 主线程统一写入 dynamicContext（保证写入在同一线程完成） ===
            dynamicContext.setValue(AiAgentEnum.AI_AGENT.getDataName(), agentFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_CLIENT.getDataName(), clientFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_CLIENT_MCP_TOOL.getDataName(), mcpFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_ADVISOR.getDataName(), advisorFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_SYSTEM_PROMPT.getDataName(), promptFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_CHAT_MODEL.getDataName(), modelFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_API.getDataName(), apiFuture.join());
            dynamicContext.setValue(AiAgentEnum.AI_MODEL_MCP_TOOL.getDataName(), modelMcpToolFuture.join());
            log.info("load 完成（主线程写入 dynamicContext）");
        } catch (Exception e) {
            log.error("load 失败", e);
            throw new ArmoryException(ExceptionCode.ARMORY_DATA_LOAD_FAILED, 
                "装配数据加载失败", e);
        }
    }
}
