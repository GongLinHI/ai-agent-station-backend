package com.gonglin.ai4knowledge.infrastructure.mysql.validator;

import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAgentFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientAdvisorFlowConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientAdvisorFlowConfig;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.RefuseDeleteException;
import com.gonglin.ai4knowledge.types.exception.RefuseUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiAdvisorValidator implements DatabaseEntityValidator<AiAdvisor> {

    @Autowired
    private AiAgentFlowConfigMapper aiAgentFlowConfigMapper;

    @Autowired
    private AiClientAdvisorFlowConfigMapper aiClientAdvisorFlowConfigMapper;

    @Override
    public Boolean validateBeforeInsert(AiAdvisor entity) {
        return true;
    }

    @Override
    public Boolean validateBeforeDisable(AiAdvisor entity) {
        String advisorId = entity.getAdvisorId();

        List<AiClientAdvisorFlowConfig> advisorConfigs = aiClientAdvisorFlowConfigMapper.queryByAdvisorId(advisorId);

        for (AiClientAdvisorFlowConfig advisorConfig : advisorConfigs) {
            if (advisorConfig.getStatus() == 1 && advisorConfig.getIsDeleted() == 0) {
                String clientId = advisorConfig.getClientId();
                List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                for (AiAgentFlowConfig agentConfig : agentConfigs) {
                    if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                        throw new RefuseUpdateException(ExceptionCode.REFUSE_UPDATE,
                                                        String.format(
                                                                "无法禁用Advisor [%s]，因为存在启用的依赖链路：Advisor -> 客户端 [%s] -> 智能体配置",
                                                                advisorId, clientId));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validateBeforeEnable(AiAdvisor entity) {
        return true;
    }

    @Override
    public Boolean validateBeforeDelete(AiAdvisor entity) {
        String advisorId = entity.getAdvisorId();

        List<AiClientAdvisorFlowConfig> advisorConfigs = aiClientAdvisorFlowConfigMapper.queryByAdvisorId(advisorId);

        for (AiClientAdvisorFlowConfig advisorConfig : advisorConfigs) {
            if (advisorConfig.getStatus() == 1 && advisorConfig.getIsDeleted() == 0) {
                String clientId = advisorConfig.getClientId();
                List<AiAgentFlowConfig> agentConfigs = aiAgentFlowConfigMapper.queryByClientId(clientId);
                for (AiAgentFlowConfig agentConfig : agentConfigs) {
                    if (agentConfig.getStatus() == 1 && agentConfig.getIsDeleted() == 0) {
                        throw new RefuseDeleteException(ExceptionCode.REFUSE_DELETE,
                                String.format(
                                        "无法删除Advisor [%s]，因为存在启用的依赖链路：Advisor -> 客户端 [%s] -> 智能体配置",
                                        advisorId, clientId));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean validate(AiAdvisor entity, DatabaseActionType actionType) {
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
