package com.gonglin.ai4knowledge.infrastructure.mysql.validator;

import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiApiMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientModelConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiMcpToolMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiModelToolConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientModelConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModelToolConfig;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.RefuseDeleteException;
import com.gonglin.ai4knowledge.types.exception.RefuseInsertException;
import com.gonglin.ai4knowledge.types.exception.RefuseUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiModelValidator implements DatabaseEntityValidator<AiModel> {

    @Autowired
    private AiApiMapper aiApiMapper;

    @Autowired
    private AiModelToolConfigMapper aiModelToolConfigMapper;

    @Autowired
    private AiMcpToolMapper aiMcpToolMapper;

    @Autowired
    private AiClientModelConfigMapper aiClientModelConfigMapper;

    @Autowired
    private AiAgentFlowConfigMapper aiAgentFlowConfigMapper;

    @Override
    public Boolean validateBeforeInsert(AiModel entity) {
        String modelId = entity.getModelId();
        String apiId = entity.getApiId();

        AiApi queryApi = AiApi.builder()
                .apiId(apiId)
                .status(1)
                .isDeleted(0)
                .build();
        List<AiApi> apis = aiApiMapper.query(queryApi);
        if (apis.isEmpty()) {
            throw new RefuseInsertException(ExceptionCode.REFUSE_INSERT,
                                            String.format(
                                                    "无法新增Model [%s]，因为依赖的API [%s] 不存在或未启用",
                                                    modelId, apiId));
        }

        List<AiModelToolConfig> modelToolConfigs = aiModelToolConfigMapper.queryByModelId(modelId);
        for (AiModelToolConfig modelToolConfig : modelToolConfigs) {
            if (modelToolConfig.getStatus() == 1 && modelToolConfig.getIsDeleted() == 0) {
                String toolId = modelToolConfig.getToolId();
                AiMcpTool queryMcpTool = AiMcpTool.builder()
                        .mcpId(toolId)
                        .status(1)
                        .isDeleted(0)
                        .build();
                List<AiMcpTool> mcpTools = aiMcpToolMapper.query(queryMcpTool);
                if (mcpTools.isEmpty()) {
                    throw new RefuseInsertException(ExceptionCode.REFUSE_INSERT,
                                                    String.format(
                                                            "无法新增Model [%s]，因为依赖的MCP工具 [%s] 不存在或未启用",
                                                            modelId, toolId));
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeDisable(AiModel entity) {
        String modelId = entity.getModelId();

        List<AiClientModelConfig> clientConfigs = aiClientModelConfigMapper.queryByModelId(modelId);
        for (AiClientModelConfig clientConfig : clientConfigs) {
            if (clientConfig.getStatus() == 1 && clientConfig.getIsDeleted() == 0) {
                String clientId = clientConfig.getClientId();
                List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                for (AiAgentFlowConfig agentConfig : agentConfigs) {
                    if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                        throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                        String.format(
                                                                "无法禁用Model [%s]，因为存在启用的依赖链路：Model -> 客户端 [%s] -> 智能体配置",
                                                                modelId,
                                                                clientId));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeEnable(AiModel entity) {
        String modelId = entity.getModelId();
        String apiId = entity.getApiId();

        AiApi queryApi = AiApi.builder()
                .apiId(apiId)
                .status(0)
                .isDeleted(0)
                .build();
        List<AiApi> apis = aiApiMapper.query(queryApi);
        if (!apis.isEmpty()) {
            throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                            String.format(
                                                    "无法启用Model [%s]，因为依赖的API [%s] 未启用",
                                                    modelId, apiId));
        }

        List<AiModelToolConfig> modelToolConfigs = aiModelToolConfigMapper.queryByModelId(modelId);
        for (AiModelToolConfig modelToolConfig : modelToolConfigs) {
            if (modelToolConfig.getStatus() == 1 && modelToolConfig.getIsDeleted() == 0) {
                String toolId = modelToolConfig.getToolId();
                AiMcpTool queryMcpTool = AiMcpTool.builder()
                        .mcpId(toolId)
                        .status(0)
                        .isDeleted(0)
                        .build();
                List<AiMcpTool> mcpTools = aiMcpToolMapper.query(queryMcpTool);
                if (!mcpTools.isEmpty()) {
                    throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                    String.format(
                                                            "无法启用Model [%s]，因为依赖的MCP工具 [%s] 未启用",
                                                            modelId, toolId));
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeDelete(AiModel entity) {
        String modelId = entity.getModelId();

        List<AiClientModelConfig> clientConfigs = aiClientModelConfigMapper.queryByModelId(modelId);
        for (AiClientModelConfig clientConfig : clientConfigs) {
            if (clientConfig.getStatus() == 1 && clientConfig.getIsDeleted() == 0) {
                String clientId = clientConfig.getClientId();
                List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                for (AiAgentFlowConfig agentConfig : agentConfigs) {
                    if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                        throw new RefuseDeleteException(ExceptionCode.REFUSE_DELETE,
                                                        String.format(
                                                                "无法删除Model [%s]，因为存在启用的依赖链路：Model -> 客户端 [%s] -> 智能体配置",
                                                                modelId, clientId));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validate(AiModel entity, DatabaseActionType actionType) {
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
