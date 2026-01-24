package com.gonglin.ai4knowledge.infrastructure.mysql.validator;

import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAdvisorMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientAdvisorFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientModelConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientSystemPromptConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientToolConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiMcpToolMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiModelMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiSystemPromptMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClient;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientAdvisorFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientModelConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientSystemPromptConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientToolConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.RefuseDeleteException;
import com.gonglin.ai4knowledge.types.exception.RefuseInsertException;
import com.gonglin.ai4knowledge.types.exception.RefuseUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiClientValidator implements DatabaseEntityValidator<AiClient> {

    @Autowired
    private AiAgentFlowConfigMapper aiAgentFlowConfigMapper;

    @Autowired
    private AiClientModelConfigMapper aiClientModelConfigMapper;

    @Autowired
    private AiClientSystemPromptConfigMapper aiClientSystemPromptConfigMapper;

    @Autowired
    private AiClientAdvisorFlowConfigMapper aiClientAdvisorFlowConfigMapper;

    @Autowired
    private AiClientToolConfigMapper aiClientToolConfigMapper;

    @Autowired
    private AiModelMapper aiModelMapper;

    @Autowired
    private AiAdvisorMapper aiAdvisorMapper;

    @Autowired
    private AiSystemPromptMapper aiSystemPromptMapper;

    @Autowired
    private AiMcpToolMapper aiMcpToolMapper;

    @Override
    public Boolean validateBeforeInsert(AiClient entity) {
        String clientId = entity.getClientId();

        List<AiClientModelConfig> modelConfigs = aiClientModelConfigMapper.queryByClientId(clientId);
        for (AiClientModelConfig modelConfig : modelConfigs) {
            if (modelConfig.getStatus() == 1 && modelConfig.getIsDeleted() == 0) {
                String modelId = modelConfig.getModelId();
                AiModel queryModel = AiModel.builder()
                        .modelId(modelId)
                        .status(1)
                        .isDeleted(0)
                        .build();
                List<AiModel> models = aiModelMapper.query(queryModel);
                if (models.isEmpty()) {
                    throw new RefuseInsertException(ExceptionCode.REFUSE_INSERT,
                                                    String.format(
                                                            "无法新增Client [%s]，因为依赖的Model [%s] 不存在或未启用",
                                                            clientId,
                                                            modelId));
                }
            }
        }

        List<AiClientSystemPromptConfig> promptConfigs = aiClientSystemPromptConfigMapper.queryByClientId(clientId);
        for (AiClientSystemPromptConfig promptConfig : promptConfigs) {
            if (promptConfig.getStatus() == 1 && promptConfig.getIsDeleted() == 0) {
                String promptId = promptConfig.getSystemPromptId();
                AiSystemPrompt queryPrompt = AiSystemPrompt.builder()
                        .promptId(promptId)
                        .status(1)
                        .isDeleted(0)
                        .build();
                List<AiSystemPrompt> prompts = aiSystemPromptMapper.query(queryPrompt);
                if (prompts.isEmpty()) {
                    throw new RefuseInsertException(ExceptionCode.REFUSE_INSERT,
                                                    String.format(
                                                            "无法新增Client [%s]，因为依赖的SystemPrompt [%s] 不存在或未启用",
                                                            clientId,
                                                            promptId));
                }
            }
        }

        List<AiClientAdvisorFlowConfig> advisorConfigs = aiClientAdvisorFlowConfigMapper.queryByClientId(clientId);
        for (AiClientAdvisorFlowConfig advisorConfig : advisorConfigs) {
            if (advisorConfig.getStatus() == 1 && advisorConfig.getIsDeleted() == 0) {
                String advisorId = advisorConfig.getAdvisorId();
                AiAdvisor queryAdvisor = AiAdvisor.builder()
                        .advisorId(advisorId)
                        .status(1)
                        .isDeleted(0)
                        .build();
                List<AiAdvisor> advisors = aiAdvisorMapper.query(queryAdvisor);
                if (advisors.isEmpty()) {
                    throw new RefuseInsertException(ExceptionCode.REFUSE_INSERT,
                                                    String.format(
                                                            "无法新增Client [%s]，因为依赖的Advisor [%s] 不存在或未启用",
                                                            clientId,
                                                            advisorId));
                }
            }
        }

        List<AiClientToolConfig> toolConfigs = aiClientToolConfigMapper.queryByClientId(clientId);
        for (AiClientToolConfig toolConfig : toolConfigs) {
            if (toolConfig.getStatus() == 1 && toolConfig.getIsDeleted() == 0) {
                String toolId = toolConfig.getToolId();
                AiMcpTool queryMcpTool = AiMcpTool.builder()
                        .mcpId(toolId)
                        .status(1)
                        .isDeleted(0)
                        .build();
                List<AiMcpTool> mcpTools = aiMcpToolMapper.query(queryMcpTool);
                if (mcpTools.isEmpty()) {
                    throw new RefuseInsertException(ExceptionCode.REFUSE_INSERT,
                                                    String.format(
                                                            "无法新增Client [%s]，因为依赖的MCP工具 [%s] 不存在或未启用",
                                                            clientId, toolId));
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeDisable(AiClient entity) {
        String clientId = entity.getClientId();

        List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
        for (AiAgentFlowConfig agentConfig : agentConfigs) {
            if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                String.format(
                                                        "无法禁用Client [%s]，因为存在启用的依赖链路：Client -> 智能体配置",
                                                        clientId));
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeEnable(AiClient entity) {
        String clientId = entity.getClientId();

        List<AiClientModelConfig> modelConfigs = aiClientModelConfigMapper.queryByClientId(clientId);
        for (AiClientModelConfig modelConfig : modelConfigs) {
            if (modelConfig.getStatus() == 1 && modelConfig.getIsDeleted() == 0) {
                String modelId = modelConfig.getModelId();
                AiModel queryModel = AiModel.builder()
                        .modelId(modelId)
                        .status(0)
                        .isDeleted(0)
                        .build();
                List<AiModel> models = aiModelMapper.query(queryModel);
                if (!models.isEmpty()) {
                    throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                    String.format(
                                                            "无法启用Client [%s]，因为依赖的Model [%s] 未启用",
                                                            clientId,
                                                            modelId));
                }
            }
        }

        List<AiClientSystemPromptConfig> promptConfigs = aiClientSystemPromptConfigMapper.queryByClientId(clientId);
        for (AiClientSystemPromptConfig promptConfig : promptConfigs) {
            if (promptConfig.getStatus() == 1 && promptConfig.getIsDeleted() == 0) {
                String promptId = promptConfig.getSystemPromptId();
                AiSystemPrompt queryPrompt = AiSystemPrompt.builder()
                        .promptId(promptId)
                        .status(0)
                        .isDeleted(0)
                        .build();
                List<AiSystemPrompt> prompts = aiSystemPromptMapper.query(queryPrompt);
                if (!prompts.isEmpty()) {
                    throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                    String.format(
                                                            "无法启用Client [%s]，因为依赖的SystemPrompt [%s] 未启用",
                                                            clientId,
                                                            promptId));
                }
            }
        }

        List<AiClientAdvisorFlowConfig> advisorConfigs = aiClientAdvisorFlowConfigMapper.queryByClientId(clientId);
        for (AiClientAdvisorFlowConfig advisorConfig : advisorConfigs) {
            if (advisorConfig.getStatus() == 1 && advisorConfig.getIsDeleted() == 0) {
                String advisorId = advisorConfig.getAdvisorId();
                AiAdvisor queryAdvisor = AiAdvisor.builder()
                        .advisorId(advisorId)
                        .status(0)
                        .isDeleted(0)
                        .build();
                List<AiAdvisor> advisors = aiAdvisorMapper.query(queryAdvisor);
                if (!advisors.isEmpty()) {
                    throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                    String.format(
                                                            "无法启用Client [%s]，因为依赖的Advisor [%s] 未启用",
                                                            clientId,
                                                            advisorId));
                }
            }
        }

        List<AiClientToolConfig> toolConfigs = aiClientToolConfigMapper.queryByClientId(clientId);
        for (AiClientToolConfig toolConfig : toolConfigs) {
            if (toolConfig.getStatus() == 1 && toolConfig.getIsDeleted() == 0) {
                String toolId = toolConfig.getToolId();
                AiMcpTool queryMcpTool = AiMcpTool.builder()
                        .mcpId(toolId)
                        .status(0)
                        .isDeleted(0)
                        .build();
                List<AiMcpTool> mcpTools = aiMcpToolMapper.query(queryMcpTool);
                if (!mcpTools.isEmpty()) {
                    throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                    String.format(
                                                            "无法启用Client [%s]，因为依赖的MCP工具 [%s] 未启用",
                                                            clientId, toolId));
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeDelete(AiClient entity) {
        String clientId = entity.getClientId();

        List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
        for (AiAgentFlowConfig agentConfig : agentConfigs) {
            if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                throw new RefuseDeleteException(ExceptionCode.REFUSE_DELETE,
                                                String.format(
                                                        "无法删除Client [%s]，因为存在启用的依赖链路：Client -> 智能体配置",
                                                        clientId));
            }
        }

        return true;
    }

    @Override
    public Boolean validate(AiClient entity, DatabaseActionType actionType) {
        switch (actionType) {
            case INSERT:
                return validateBeforeInsert(entity);
            case UPDATE:
                if (entity.getStatus() == 0) {
                    return validateBeforeDisable(entity);
                } else if (entity.getStatus() == 1) {
                    return validateBeforeEnable(entity);
                }
                break;
            case SOFT_DELETE, DELETE:
                return validateBeforeDelete(entity);
            default:
                break;
        }
        return true;
    }
}
