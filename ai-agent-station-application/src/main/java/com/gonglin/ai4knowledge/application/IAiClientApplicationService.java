package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClient;

import java.util.List;

public interface IAiClientApplicationService {
    AiClient createAiClient(AiClient aiClient);

    AiClient updateAiClientById(AiClient aiClient);

    AiClient updateAiClientByClientId(AiClient aiClient);

    void deleteAiClientById(Long id);

    void deleteAiClientByClientId(String clientId);

    AiClient queryAiClientById(Long id);

    AiClient queryAiClientByClientId(String clientId);

    List<AiClient> queryEnabledAiClients();

    List<AiClient> queryAiClientList(AiClient aiClient, Integer pageNum, Integer pageSize);

    List<AiClient> queryAllAiClients();
}
