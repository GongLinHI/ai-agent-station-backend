package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.AiClientApiQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientApiRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientApiResponseDTO;

import java.util.List;

public interface IAiClientApiAdminService {
    Response<Boolean> createAiClientApi(AiClientApiRequestDTO request);

    Response<Boolean> updateAiClientApiById(AiClientApiRequestDTO request);

    Response<Boolean> updateAiClientApiByApiId(AiClientApiRequestDTO request);

    Response<Boolean> deleteAiClientApiById(Long id);

    Response<Boolean> deleteAiClientApiByApiId(String apiId);

    Response<AiClientApiResponseDTO> queryAiClientApiById(Long id);

    Response<AiClientApiResponseDTO> queryAiClientApiByApiId(String apiId);

    Response<List<AiClientApiResponseDTO>> queryEnabledAiClientApis();

    Response<List<AiClientApiResponseDTO>> queryAiClientApiList(AiClientApiQueryRequestDTO request);

    Response<List<AiClientApiResponseDTO>> queryAllAiClientApis();
}
