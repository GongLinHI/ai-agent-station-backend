package com.gonglin.ai4knowledge.domain.agent.repository;

import com.gonglin.ai4knowledge.domain.agent.model.aggregate.AiAgentEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiApiEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiClientEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiModelEntity;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiSystemPromptEntity;

import java.util.List;

public interface IAiAgentRepository {


    List<AiClientEntity> queryAiClientEntityByClientIds(List<String> clientIds);

    List<AiAgentEntity> queryAiAgentEntityByStatus(Integer status);

    List<AiAdvisorEntity> queryAiClientAdvisorEntityByAdvisorIds(List<String> advisorIds);

    List<AiSystemPromptEntity> queryAiSystemPromptEntityByPromptIds(List<String> promptIds);

    List<AiModelEntity> queryAiModelEntityByModelIds(List<String> modelIds);

    List<AiApiEntity> queryAiApiEntityByApiIds(List<String> apiIds);

    List<AiMcpToolEntity> queryAiMcpToolEntityByMcpToolIds(List<String> mcpIds);

    List<AiAgentEntity> queryAiAgentEntityByAgentIds(List<String> agentIds);


}
