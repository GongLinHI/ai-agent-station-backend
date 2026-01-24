package com.gonglin.ai4knowledge.infrastructure.mysql.validator;

import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientModelConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientToolConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiModelToolConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientModelConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientToolConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModelToolConfig;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.RefuseDeleteException;
import com.gonglin.ai4knowledge.types.exception.RefuseUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("AiMcpTool")
public class AiMcpToolValidator implements DatabaseEntityValidator<AiMcpTool> {

    @Autowired
    private AiModelToolConfigMapper aiModelToolConfigMapper;

    @Autowired
    private AiClientToolConfigMapper aiClientToolConfigMapper;

    @Autowired
    private AiClientModelConfigMapper aiClientModelConfigMapper;

    @Autowired
    private AiAgentFlowConfigMapper aiAgentFlowConfigMapper;

    @Override
    public Boolean validateBeforeInsert(AiMcpTool entity) {
        return true;
    }

    @Override
    public Boolean validateBeforeDisable(AiMcpTool entity) {
        String mcpId = entity.getMcpId();

        List<AiModelToolConfig> modelToolConfigs = aiModelToolConfigMapper.queryByToolId(mcpId);
        for (AiModelToolConfig modelToolConfig : modelToolConfigs) {
            if (modelToolConfig.getStatus() == 1 && modelToolConfig.getIsDeleted() == 0) {
                String modelId = modelToolConfig.getModelId();
                List<AiClientModelConfig> clientConfigs = aiClientModelConfigMapper.queryByModelId(modelId);
                for (AiClientModelConfig clientConfig : clientConfigs) {
                    if (clientConfig.getStatus() == 1 && clientConfig.getIsDeleted() == 0) {
                        String clientId = clientConfig.getClientId();
                        List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                        for (AiAgentFlowConfig agentConfig : agentConfigs) {
                            if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                                throw new RefuseUpdateException(
                                        "MCP_TOOL_REFUSE_DISABLE",
                                        String.format(
                                                "无法禁用MCP工具 [%s]，因为存在启用的依赖链路：MCP工具 -> 模型 [%s] -> 客户端 [%s] -> 智能体配置",
                                                mcpId, modelId, clientId));
                            }
                        }
                    }
                }
            }
        }

        List<AiClientToolConfig> clientToolConfigs = aiClientToolConfigMapper.queryByToolId(mcpId);
        for (AiClientToolConfig clientToolConfig : clientToolConfigs) {
            if (clientToolConfig.getStatus() == 1 && clientToolConfig.getIsDeleted() == 0) {
                String clientId = clientToolConfig.getClientId();
                List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                for (AiAgentFlowConfig agentConfig : agentConfigs) {
                    if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                        throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                String.format(
                                        "无法禁用MCP工具 [%s]，因为存在启用的依赖链路：MCP工具 -> 客户端 [%s] -> 智能体配置",
                                        mcpId, clientId));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeEnable(AiMcpTool entity) {
        return true;
    }

    @Override
    public Boolean validateBeforeDelete(AiMcpTool entity) {
        String mcpId = entity.getMcpId();

        List<AiModelToolConfig> modelToolConfigs = aiModelToolConfigMapper.queryByToolId(mcpId);
        for (AiModelToolConfig modelToolConfig : modelToolConfigs) {
            if (modelToolConfig.getStatus() == 1 && modelToolConfig.getIsDeleted() == 0) {
                String modelId = modelToolConfig.getModelId();
                List<AiClientModelConfig> clientConfigs = aiClientModelConfigMapper.queryByModelId(modelId);
                for (AiClientModelConfig clientConfig : clientConfigs) {
                    if (clientConfig.getStatus() == 1 && clientConfig.getIsDeleted() == 0) {
                        String clientId = clientConfig.getClientId();
                        List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                        for (AiAgentFlowConfig agentConfig : agentConfigs) {
                            if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                                throw new RefuseDeleteException(
                                        "MCP_TOOL_REFUSE_DELETE",
                                        String.format(
                                                "无法删除MCP工具 [%s]，因为存在启用的依赖链路：MCP工具 -> 模型 [%s] -> 客户端 [%s] -> 智能体配置",
                                                mcpId, modelId, clientId));
                            }
                        }
                    }
                }
            }
        }

        List<AiClientToolConfig> clientToolConfigs = aiClientToolConfigMapper.queryByToolId(mcpId);
        for (AiClientToolConfig clientToolConfig : clientToolConfigs) {
            if (clientToolConfig.getStatus() == 1 && clientToolConfig.getIsDeleted() == 0) {
                String clientId = clientToolConfig.getClientId();
                List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                for (AiAgentFlowConfig agentConfig : agentConfigs) {
                    if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                        throw new RefuseDeleteException(ExceptionCode.REFUSE_DELETE,
                                String.format(
                                        "无法删除MCP工具 [%s]，因为存在启用的依赖链路：MCP工具 -> 客户端 [%s] -> 智能体配置",
                                        mcpId, clientId));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validate(AiMcpTool entity, DatabaseActionType actionType) {
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
