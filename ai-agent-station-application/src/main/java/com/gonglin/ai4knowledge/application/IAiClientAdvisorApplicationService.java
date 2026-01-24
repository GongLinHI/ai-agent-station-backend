package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;

import java.util.List;

public interface IAiClientAdvisorApplicationService {
    AiAdvisor createAiClientAdvisor(AiAdvisor aiAdvisor);

    AiAdvisor updateAiClientAdvisorById(AiAdvisor aiAdvisor);

    AiAdvisor updateAiClientAdvisorByAdvisorId(AiAdvisor aiAdvisor);

    void deleteAiClientAdvisorById(Long id);

    void deleteAiClientAdvisorByAdvisorId(String advisorId);

    AiAdvisor queryAiClientAdvisorById(Long id);

    AiAdvisor queryAiClientAdvisorByAdvisorId(String advisorId);

    List<AiAdvisor> queryEnabledAiClientAdvisors();

    List<AiAdvisor> queryAiClientAdvisorsByStatus(Integer status);

    List<AiAdvisor> queryAiClientAdvisorsByType(String advisorType);

    List<AiAdvisor> queryAiClientAdvisorList(AiAdvisor aiAdvisor, Integer pageNum, Integer pageSize);

    List<AiAdvisor> queryAllAiClientAdvisors();
}
