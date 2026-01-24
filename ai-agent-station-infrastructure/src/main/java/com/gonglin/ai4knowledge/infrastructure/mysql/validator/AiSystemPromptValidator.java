package com.gonglin.ai4knowledge.infrastructure.mysql.validator;

import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientSystemPromptConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientSystemPromptConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.RefuseDeleteException;
import com.gonglin.ai4knowledge.types.exception.RefuseUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiSystemPromptValidator implements DatabaseEntityValidator<AiSystemPrompt> {

    @Autowired
    private AiAgentFlowConfigMapper aiAgentFlowConfigMapper;

    @Autowired
    private AiClientSystemPromptConfigMapper aiClientSystemPromptConfigMapper;

    @Override
    public Boolean validateBeforeInsert(AiSystemPrompt entity) {
        return true;
    }

    @Override
    public Boolean validateBeforeDisable(AiSystemPrompt entity) {
        String promptId = entity.getPromptId();

        List<AiClientSystemPromptConfig> promptConfigs = aiClientSystemPromptConfigMapper.queryBySystemPromptId(
                promptId);

        for (AiClientSystemPromptConfig promptConfig : promptConfigs) {
            if (promptConfig.getStatus() == 1 && promptConfig.getIsDeleted() == 0) {
                String clientId = promptConfig.getClientId();
                List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                for (AiAgentFlowConfig agentConfig : agentConfigs) {
                    if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                        throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                        String.format(
                                                                "无法禁用SystemPrompt [%s]，因为存在启用的依赖链路：SystemPrompt -> 客户端 [%s] -> 智能体配置",
                                                                promptId, clientId));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeEnable(AiSystemPrompt entity) {
        return true;
    }

    @Override
    public Boolean validateBeforeDelete(AiSystemPrompt entity) {
        String promptId = entity.getPromptId();

        List<AiClientSystemPromptConfig> promptConfigs = aiClientSystemPromptConfigMapper.queryBySystemPromptId(
                promptId);

        for (AiClientSystemPromptConfig promptConfig : promptConfigs) {
            if (promptConfig.getStatus() == 1 && promptConfig.getIsDeleted() == 0) {
                String clientId = promptConfig.getClientId();
                List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                for (AiAgentFlowConfig agentConfig : agentConfigs) {
                    if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                        throw new RefuseDeleteException(ExceptionCode.REFUSE_DELETE,
                                String.format(
                                        "无法删除SystemPrompt [%s]，因为存在启用的依赖链路：SystemPrompt -> 客户端 [%s] -> 智能体配置",
                                        promptId, clientId));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validate(AiSystemPrompt entity, DatabaseActionType actionType) {
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
