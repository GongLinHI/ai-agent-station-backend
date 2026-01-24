package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.AiToolMcpQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiToolMcpRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiToolMcpResponseDTO;

import java.util.List;

public interface IAiToolMcpAdminService {
    Response<Boolean> createAiClientToolMcp(AiToolMcpRequestDTO request);

    Response<Boolean> updateAiClientToolMcpById(AiToolMcpRequestDTO request);

    Response<Boolean> updateAiClientToolMcpByMcpId(AiToolMcpRequestDTO request);

    Response<Boolean> deleteAiClientToolMcpById(Long id);

    Response<Boolean> deleteAiClientToolMcpByMcpId(String mcpId);

    Response<AiToolMcpResponseDTO> queryAiClientToolMcpById(Long id);

    Response<AiToolMcpResponseDTO> queryAiClientToolMcpByMcpId(String mcpId);

    Response<List<AiToolMcpResponseDTO>> queryAllAiClientToolMcps();

    Response<List<AiToolMcpResponseDTO>> queryAiClientToolMcpsByStatus(Integer status);

    Response<List<AiToolMcpResponseDTO>> queryAiClientToolMcpsByTransportType(String transportType);

    Response<List<AiToolMcpResponseDTO>> queryEnabledAiClientToolMcps();

    Response<List<AiToolMcpResponseDTO>> queryAiClientToolMcpList(AiToolMcpQueryRequestDTO request);
}
