package com.gonglin.ai4knowledge.domain.agent.service.assemble;

import com.gonglin.ai4knowledge.domain.agent.model.aggregate.AiAgentEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;

import java.util.List;

public interface IArmoryService {
    List<AiAgentEntity> queryAvailableAgents();


    void armorAllAvailableAgents();


    void armoryByCommand(ArmoryCommand command);

    void armoryByAiAgentIds(List<String> aiAgentIds);

    void armoryByAiClientIds(List<String> aiClientIds);

    void armoryByAiModelIds(List<String> aiModelIds);
}
