package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;

import java.util.List;

public interface IAiClientModelApplicationService {
    AiModel createAiClientModel(AiModel aiModel);

    AiModel updateAiClientModelById(AiModel aiModel);

    AiModel updateAiClientModelByModelId(AiModel aiModel);

    void deleteAiClientModelById(Long id);

    void deleteAiClientModelByModelId(String modelId);

    AiModel queryAiClientModelById(Long id);

    AiModel queryAiClientModelByModelId(String modelId);

    List<AiModel> queryAiClientModelsByApiId(String apiId);

    List<AiModel> queryAiClientModelsByModelProvider(String modelProvider);

    List<AiModel> queryEnabledAiClientModels();

    List<AiModel> queryAiClientModelList(AiModel aiModel, Integer pageNum, Integer pageSize);

    List<AiModel> queryAllAiClientModels();
}