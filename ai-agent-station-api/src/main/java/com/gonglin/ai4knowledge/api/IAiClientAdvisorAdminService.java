package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.AiClientAdvisorQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientAdvisorRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientAdvisorResponseDTO;

import java.util.List;

public interface IAiClientAdvisorAdminService {
    Response<Boolean> createAiClientAdvisor(AiClientAdvisorRequestDTO request);

    Response<Boolean> updateAiClientAdvisorById(AiClientAdvisorRequestDTO request);

    Response<Boolean> updateAiClientAdvisorByAdvisorId(AiClientAdvisorRequestDTO request);

    Response<Boolean> deleteAiClientAdvisorById(Long id);

    Response<Boolean> deleteAiClientAdvisorByAdvisorId(String advisorId);

    Response<AiClientAdvisorResponseDTO> queryAiClientAdvisorById(Long id);

    Response<AiClientAdvisorResponseDTO> queryAiClientAdvisorByAdvisorId(String advisorId);

    Response<List<AiClientAdvisorResponseDTO>> queryEnabledAiClientAdvisors();

    Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorsByStatus(Integer status);

    Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorsByType(String advisorType);

    Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorList(AiClientAdvisorQueryRequestDTO request);

    Response<List<AiClientAdvisorResponseDTO>> queryAllAiClientAdvisors();
}
