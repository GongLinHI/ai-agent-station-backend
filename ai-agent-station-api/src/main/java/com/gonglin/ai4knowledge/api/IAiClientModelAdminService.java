package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.AiClientModelQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientModelRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientModelResponseDTO;

import java.util.List;

public interface IAiClientModelAdminService {
    Response<Boolean> createAiClientModel(AiClientModelRequestDTO request);

    Response<Boolean> updateAiClientModelById(AiClientModelRequestDTO request);

    Response<Boolean> updateAiClientModelByModelId(AiClientModelRequestDTO request);

    Response<Boolean> deleteAiClientModelById(Long id);

    Response<Boolean> deleteAiClientModelByModelId(String modelId);

    Response<AiClientModelResponseDTO> queryAiClientModelById(Long id);

    Response<AiClientModelResponseDTO> queryAiClientModelByModelId(String modelId);

    Response<List<AiClientModelResponseDTO>> queryAiClientModelsByApiId(String apiId);

    Response<List<AiClientModelResponseDTO>> queryAiClientModelsByModelProvider(String modelProvider);

    Response<List<AiClientModelResponseDTO>> queryEnabledAiClientModels();

    Response<List<AiClientModelResponseDTO>> queryAiClientModelList(AiClientModelQueryRequestDTO request);

    Response<List<AiClientModelResponseDTO>> queryAllAiClientModels();
}