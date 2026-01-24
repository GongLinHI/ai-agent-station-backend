package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.AiClientQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientResponseDTO;

import java.util.List;

// 提供AI客户端的配置管理功能，用于管理接入系统的客户端(Chat Client)信息。
public interface IAiClientAdminService {
    Response<Boolean> createAiClient(AiClientRequestDTO request);

    Response<Boolean> updateAiClientById(AiClientRequestDTO request);

    Response<Boolean> updateAiClientByClientId(AiClientRequestDTO request);

    Response<Boolean> deleteAiClientById(Long id);

    Response<Boolean> deleteAiClientByClientId(String clientId);

    Response<AiClientResponseDTO> queryAiClientById(Long id);

    Response<AiClientResponseDTO> queryAiClientByClientId(String clientId);

    Response<List<AiClientResponseDTO>> queryEnabledAiClients();

    Response<List<AiClientResponseDTO>> queryAiClientList(AiClientQueryRequestDTO request);

    Response<List<AiClientResponseDTO>> queryAllAiClients();
}
