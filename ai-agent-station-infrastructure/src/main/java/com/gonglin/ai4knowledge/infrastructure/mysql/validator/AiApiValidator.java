package com.gonglin.ai4knowledge.infrastructure.mysql.validator;

import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientModelConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiModelMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientModelConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.RefuseDeleteException;
import com.gonglin.ai4knowledge.types.exception.RefuseUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AiApiValidator implements DatabaseEntityValidator<AiApi> {

    @Autowired
    private AiModelMapper aiModelMapper;

    @Autowired
    private AiClientModelConfigMapper aiClientModelConfigMapper;

    @Autowired
    private AiAgentFlowConfigMapper aiAgentFlowConfigMapper;

    @Override
    public Boolean validateBeforeInsert(AiApi entity) {
        return true;
    }

    @Override
    public Boolean validateBeforeDisable(AiApi entity) {
        String apiId = entity.getApiId();

        AiModel queryModel = AiModel.builder()
                .apiId(apiId)
                .status(1)
                .isDeleted(0)
                .build();
        List<AiModel> models = aiModelMapper.query(queryModel);

        if (!models.isEmpty()) {
            List<String> modelIds = models.stream().map(AiModel::getModelId).collect(Collectors.toList());

            for (String modelId : modelIds) {
                List<AiClientModelConfig> clientConfigs = aiClientModelConfigMapper.queryByModelId(modelId);

                if (!clientConfigs.isEmpty()) {
                    List<String> clientIds = clientConfigs.stream().map(AiClientModelConfig::getClientId).collect(
                            Collectors.toList());

                    for (String clientId : clientIds) {
                        List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                        for (AiAgentFlowConfig agentConfig : agentConfigs) {
                            if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                                throw new RefuseUpdateException(
                                        "API_REFUSE_DISABLE",
                                        String.format(
                                                "无法禁用API [%s]，因为存在启用的依赖链路：API -> 模型 [%s] -> 客户端 [%s] -> 智能体配置",
                                                apiId, modelId, clientId));
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeEnable(AiApi entity) {
        return true;
    }

    @Override
    public Boolean validateBeforeDelete(AiApi entity) {
        String apiId = entity.getApiId();

        AiModel queryModel = AiModel.builder()
                .apiId(apiId)
                .status(1)
                .isDeleted(0)
                .build();
        List<AiModel> models = aiModelMapper.query(queryModel);

        if (!models.isEmpty()) {
            List<String> modelIds = models.stream().map(AiModel::getModelId).collect(Collectors.toList());

            for (String modelId : modelIds) {
                List<AiClientModelConfig> clientConfigs = aiClientModelConfigMapper.queryByModelId(modelId);

                if (!clientConfigs.isEmpty()) {
                    List<String> clientIds = clientConfigs.stream().map(AiClientModelConfig::getClientId).collect(
                            Collectors.toList());

                    for (String clientId : clientIds) {
                        List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                        for (AiAgentFlowConfig agentConfig : agentConfigs) {
                            if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                                throw new RefuseDeleteException(ExceptionCode.REFUSE_DELETE,
                                        String.format(
                                                "无法删除API [%s]，因为存在启用的依赖链路：API -> 模型 [%s] -> 客户端 [%s] -> 智能体配置",
                                                apiId, modelId, clientId));
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validate(AiApi entity, DatabaseActionType actionType) {
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
