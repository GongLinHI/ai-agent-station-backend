package com.gonglin.ai4knowledge.infrastructure.adapter.repository;

import com.gonglin.ai4knowledge.domain.agent.model.aggregate.AiAgentEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiApiEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiClientEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiModelEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiSystemPromptEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.WorkflowStep;
import com.gonglin.ai4knowledge.domain.agent.repository.IAiAgentRepository;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAdvisorMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentTaskScheduleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiApiMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientAdvisorFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientModelConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientSystemPromptConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientToolConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiMcpToolMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiModelMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiModelToolConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiRagOrderMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiSystemPromptMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgent;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClient;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientAdvisorFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientToolConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModelToolConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;
import com.gonglin.ai4knowledge.types.common.Status;
import com.gonglin.ai4knowledge.types.common.ToolType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class AiAgentRepository implements IAiAgentRepository {
    @Autowired
    private AiAgentFlowConfigMapper aiAgentFlowConfigMapper;
    @Autowired
    private AiAgentMapper aiAgentMapper;
    @Autowired
    private AiAgentTaskScheduleMapper aiAgentTaskScheduleMapper;

    @Autowired
    private AiClientMapper aiClientMapper;

    @Autowired
    private AiClientModelConfigMapper aiClientModelConfigMapper;

    @Autowired
    private AiModelMapper aiModelMapper;
    @Autowired
    private AiModelToolConfigMapper aiModelToolConfigMapper;

    @Autowired
    private AiApiMapper aiApiMapper;

    @Autowired
    private AiClientToolConfigMapper aiClientToolConfigMapper;

    @Autowired
    private AiMcpToolMapper aiMcpToolMapper;

    @Autowired
    private AiClientAdvisorFlowConfigMapper aiClientAdvisorFlowConfigMapper;

    @Autowired
    private AiAdvisorMapper aiAdvisorMapper;

    @Autowired
    private AiClientSystemPromptConfigMapper aiClientSystemPromptConfigMapper;

    @Autowired
    private AiSystemPromptMapper aiSystemPromptMapper;

    @Autowired
    private AiRagOrderMapper aiRagOrderMapper;

    /**
     * 模板
     */
    @Override
    public List<AiClientEntity> queryAiClientEntityByClientIds(List<String> clientIds) {
        if (clientIds == null || clientIds.isEmpty()) {
            return List.of();
        }
        return clientIds.stream()
                .map(clientId -> AiClient.builder().clientId(clientId).build())
                .map(aiClientMapper::query)
                .map(e -> e.get(0))
                .filter(Objects::nonNull)
                .map(this::fittingToAiClientEntity)
                .toList();
    }


    @Override
    public List<AiAdvisorEntity> queryAiClientAdvisorEntityByAdvisorIds(List<String> advisorIds) {
        if (advisorIds == null || advisorIds.isEmpty()) {
            return List.of();
        }
        return advisorIds.stream()
                .map(advisorId -> AiAdvisor.builder().advisorId(advisorId).build())
                .map(aiAdvisorMapper::query)
                .map(e -> e.get(0))
                .filter(Objects::nonNull)
                .map(this::fittingToAiAdvisorEntity)
                .toList();
    }

    @Override
    public List<AiSystemPromptEntity> queryAiSystemPromptEntityByPromptIds(List<String> promptIds) {
        if (promptIds == null || promptIds.isEmpty()) {
            return List.of();
        }
        return promptIds.stream()
                .map(promptId -> AiSystemPrompt.builder().promptId(promptId).build())
                .map(aiSystemPromptMapper::query)
                .map(e -> e.get(0))
                .filter(Objects::nonNull)
                .map(this::fittingToAiSystemPromptEntity)
                .toList();
    }

    @Override
    public List<AiModelEntity> queryAiModelEntityByModelIds(List<String> modelIds) {
        if (modelIds == null || modelIds.isEmpty()) {
            return List.of();
        }
        return modelIds.stream()
                .map(modelId -> AiModel.builder().modelId(modelId).build())
                .map(aiModelMapper::query)
                .map(e -> e.get(0))
                .filter(Objects::nonNull)
                .map(this::fittingToAiModelEntity)
                .toList();
    }


    @Override
    public List<AiApiEntity> queryAiApiEntityByApiIds(List<String> apiIds) {
        if (apiIds == null || apiIds.isEmpty()) {
            return List.of();
        }
        return apiIds.stream()
                .map(apiId -> AiApi.builder().apiId(apiId).build())
                .map(aiApiMapper::query)
                .map(e -> e.get(0))
                .filter(Objects::nonNull)
                .map(this::fittingToAiApiEntity)
                .toList();
    }


    @Override
    public List<AiMcpToolEntity> queryAiMcpToolEntityByMcpToolIds(List<String> mcpIds) {
        if (mcpIds == null || mcpIds.isEmpty()) {
            return List.of();
        }
        return mcpIds.stream()
                .map(mcpId -> AiMcpTool.builder().mcpId(mcpId).build())
                .map(aiMcpToolMapper::query)
                .map(e -> e.get(0))
                .filter(Objects::nonNull)
                .map(this::fittingToAiMcpToolEntity)
                .toList();
    }

    @Override
    public List<AiAgentEntity> queryAiAgentEntityByAgentIds(List<String> agentIds) {
        if (agentIds == null || agentIds.isEmpty()) {
            return List.of();
        }
        return agentIds.stream()
                .map(aiAgentMapper::queryByAgentId)
                .filter(Objects::nonNull)
                .map(this::fittingToAiAgentEntity)
                .toList();
    }

    @Override
    public List<AiAgentEntity> queryAiAgentEntityByStatus(Integer status) {
        if (Status.ENABLE.equals(status) || Status.DISABLE.equals(status)) {
            return aiAgentMapper.queryByStatus(status).stream()
                    .filter(Objects::nonNull)
                    .map(this::fittingToAiAgentEntity)
                    .toList();
        } else {
            return List.of();
        }
    }

    private AiAgentEntity fittingToAiAgentEntity(AiAgent aiAgent) {
        AiAgentEntity aiAgentEntity = AiAgentEntity.builder()
                .agentId(aiAgent.getAgentId())
                .agentName(aiAgent.getAgentName())
                .description(aiAgent.getDescription())
                .channel(aiAgent.getChannel())
                .strategy(aiAgent.getStrategy())
                .status(aiAgent.getStatus())
                .build();
        String agentId = aiAgent.getAgentId();
        List<WorkflowStep> workflowSteps = aiAgentFlowConfigMapper.queryByAgentId(agentId)
                .stream()
                .filter(config -> config.getStatus().equals(Status.ENABLE))
                .sorted(Comparator.comparing(AiAgentFlowConfig::getSequence))
                .map(config -> WorkflowStep.builder()
                        .clientId(config.getClientId())
                        .clientType(config.getClientType())
                        .clientStepPrompt(config.getClientStepPrompt())
                        .sequence(config.getSequence())
                        .build())
                .toList();
        aiAgentEntity.setWorkflowStepList(workflowSteps);
        return aiAgentEntity;
    }


    private AiClientEntity fittingToAiClientEntity(AiClient aiClient) {
        if (aiClient == null) {
            return null;
        }
        // 0. 基础信息
        AiClientEntity aiClientEntity = AiClientEntity.builder()
                .clientId(aiClient.getClientId())
                .clientName(aiClient.getClientName())
                .description(aiClient.getDescription())
                .status(aiClient.getStatus())
                .build();
        String clientId = aiClient.getClientId();

        // 设置Active Model
        aiClientModelConfigMapper.queryByClientId(clientId).stream()
                .filter(config -> config.getStatus().equals(Status.ENABLE))
                .distinct()
                .findFirst()
                .ifPresentOrElse(config -> aiClientEntity.setActiveModelId(config.getModelId()),
                                 () -> log.warn("没有与 {} 匹配的有效模型", clientId));
        // 设置Active Prompt
        aiClientSystemPromptConfigMapper.queryByClientId(clientId).stream()
                .filter(config -> config.getStatus().equals(Status.ENABLE))
                .distinct()
                .findFirst()
                .ifPresentOrElse(config -> aiClientEntity.setActiveSystemPromptId(config.getSystemPromptId()),
                                 () -> log.warn("没有与 {} 匹配的有效系统提示词", clientId));
        // 设置Active MCP工具
        List<String> mcpIds = aiClientToolConfigMapper.queryByClientId(clientId).stream()
                .filter(config -> config.getStatus().equals(Status.ENABLE))
                .filter(config -> config.getToolType().equals(ToolType.MCP))
                .map(AiClientToolConfig::getToolId)
                .distinct()
                .toList();
        aiClientEntity.setActiveMcpIdList(mcpIds);
        // 设置Active Advisor工具
        List<String> advisorIds = aiClientAdvisorFlowConfigMapper.queryByClientId(clientId).stream()
                .filter(config -> config.getStatus().equals(Status.ENABLE))
                .sorted(Comparator.comparing(AiClientAdvisorFlowConfig::getSequence))
                .map(AiClientAdvisorFlowConfig::getAdvisorId)
                .distinct()
                .toList();
        aiClientEntity.setActiveAdvisorIdList(advisorIds);
        return aiClientEntity;
    }

    private AiAdvisorEntity fittingToAiAdvisorEntity(AiAdvisor advisor) {
        if (advisor == null) {
            return null;
        }
        return AiAdvisorEntity.builder()
                .advisorId(advisor.getAdvisorId())
                .advisorName(advisor.getAdvisorName())
                .description(advisor.getDescription())
                .advisorType(advisor.getAdvisorType())
                .extParam(advisor.getExtParam())
                .status(advisor.getStatus())
                .build();
    }

    private AiSystemPromptEntity fittingToAiSystemPromptEntity(AiSystemPrompt aiSystemPrompt) {
        if (aiSystemPrompt == null) {
            return null;
        }
        return AiSystemPromptEntity.builder()
                .promptId(aiSystemPrompt.getPromptId())
                .promptName(aiSystemPrompt.getPromptName())
                .promptContent(aiSystemPrompt.getPromptContent())
                .description(aiSystemPrompt.getDescription())
                .status(aiSystemPrompt.getStatus())
                .build();
    }

    private AiModelEntity fittingToAiModelEntity(AiModel aiModel) {
        if (aiModel == null) {
            return null;
        }
        AiModelEntity aiModelEntity = AiModelEntity.builder()
                .modelId(aiModel.getModelId())
                .modelName(aiModel.getModelName())
                .apiId(aiModel.getApiId())
                .modelType(aiModel.getModelType())
                .modelProvider(aiModel.getModelProvider())
                .modelVersion(aiModel.getModelVersion())
                .extParam(aiModel.getExtParam())
                .timeout(aiModel.getTimeout())
                .status(aiModel.getStatus())
                .build();

        List<String> mcpToolIds = aiModelToolConfigMapper.queryByModelId(aiModel.getModelId()).stream()
                .filter(config -> config.getStatus().equals(Status.ENABLE))
                .filter(config -> config.getToolType().equals(ToolType.MCP))
                .map(AiModelToolConfig::getToolId)
                .distinct()
                .toList();
        aiModelEntity.setActiveMcpIdList(mcpToolIds);
        return aiModelEntity;
    }

    private AiApiEntity fittingToAiApiEntity(AiApi aiApi) {
        if (aiApi == null) {
            return null;
        }
        return AiApiEntity.builder()
                .apiId(aiApi.getApiId())
                .apiName(aiApi.getApiName())
                .baseUrl(aiApi.getBaseUrl())
                .apiKey(aiApi.getApiKey())
                .completionsPath(aiApi.getCompletionsPath())
                .embeddingsPath(aiApi.getEmbeddingsPath())
                .apiProvider(aiApi.getApiProvider())
                .extParam(aiApi.getExtParam())
                .timeout(aiApi.getTimeout())
                .status(aiApi.getStatus())
                .build();
    }

    private AiMcpToolEntity fittingToAiMcpToolEntity(AiMcpTool aiMcpTool) {
        if (aiMcpTool == null) {
            return null;
        }
        return AiMcpToolEntity.builder()
                .mcpId(aiMcpTool.getMcpId())
                .mcpName(aiMcpTool.getMcpName())
                .description(aiMcpTool.getDescription())
                .transportType(aiMcpTool.getTransportType())
                .transportConfig(aiMcpTool.getTransportConfig())
                .timeout(aiMcpTool.getTimeout())
                .status(aiMcpTool.getStatus())
                .build();
    }
}
