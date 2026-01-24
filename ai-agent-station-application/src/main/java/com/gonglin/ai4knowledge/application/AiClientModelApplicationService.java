package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientModelConfigMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiModelMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientModelConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AiModelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AiClientModelApplicationService implements IAiClientModelApplicationService {

    @Autowired
    private AiModelMapper aiModelMapper;

    @Autowired
    private AiClientModelConfigMapper aiClientModelConfigMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiModel createAiClientModel(AiModel aiModel) {
        log.info("createAiClientModel request: {}", aiModel);
        Integer result = aiModelMapper.insert(aiModel);
        log.info("createAiClientModel success, id: {}, result: {}", aiModel.getId(), result);
        return aiModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiModel updateAiClientModelById(AiModel aiModel) {
        log.info("updateAiClientModelById request: {}", aiModel);
        AiModel existing = aiModelMapper.query(
                AiModel.builder().id(aiModel.getId()).build()).stream().findFirst().orElse(null);
        if (existing == null) {
            throw new AiModelException(ExceptionCode.AI_MODEL_NOT_FOUND,
                                    "AI model not found with id: " + aiModel.getId());
        }
        Integer result = aiModelMapper.updateById(aiModel);
        log.info("updateAiClientModelById success, id: {}, result: {}", aiModel.getId(), result);
        return aiModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiModel updateAiClientModelByModelId(AiModel aiModel) {
        log.info("updateAiClientModelByModelId request: {}", aiModel);
        AiModel existing = aiModelMapper.query(
                AiModel.builder().modelId(aiModel.getModelId()).build()).stream().findFirst().orElse(null);
        if (existing == null) {
            throw new AiModelException(ExceptionCode.AI_MODEL_ID_NOT_FOUND,
                                    "AI model not found with modelId: " + aiModel.getModelId());
        }
        aiModel.setId(existing.getId());
        Integer result = aiModelMapper.updateByModelId(aiModel);
        log.info("updateAiClientModelByModelId success, modelId: {}, result: {}", aiModel.getModelId(), result);
        return aiModel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientModelById(Long id) {
        log.info("deleteAiClientModelById request: {}", id);
        AiModel existing = aiModelMapper.query(AiModel.builder().id(id).build()).stream().findFirst().orElse(null);
        if (existing == null) {
            throw new AiModelException(ExceptionCode.AI_MODEL_NOT_FOUND,
                                    "AI model not found with id: " + id);
        }
        List<AiClientModelConfig> configs = aiClientModelConfigMapper.queryByModelId(existing.getModelId());
        if (!configs.isEmpty()) {
            throw new AiModelException(ExceptionCode.AI_MODEL_HAS_ASSOCIATIONS,
                                    "Cannot delete model with existing client model config associations");
        }
        Integer result = aiModelMapper.deleteById(existing);
        log.info("deleteAiClientModelById success, id: {}, result: {}", id, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientModelByModelId(String modelId) {
        log.info("deleteAiClientModelByModelId request: {}", modelId);
        AiModel existing = aiModelMapper.query(AiModel.builder().modelId(modelId).build()).stream().findFirst().orElse(
                null);
        if (existing == null) {
            throw new AiModelException(ExceptionCode.AI_MODEL_ID_NOT_FOUND,
                                    "AI model not found with modelId: " + modelId);
        }
        List<AiClientModelConfig> configs = aiClientModelConfigMapper.queryByModelId(modelId);
        if (!configs.isEmpty()) {
            throw new AiModelException(ExceptionCode.AI_MODEL_HAS_ASSOCIATIONS,
                                    "Cannot delete model with existing client model config associations");
        }
        Integer result = aiModelMapper.deleteByModelId(existing);
        log.info("deleteAiClientModelByModelId success, modelId: {}, result: {}", modelId, result);
    }

    @Override
    public AiModel queryAiClientModelById(Long id) {
        log.info("queryAiClientModelById request: {}", id);
        List<AiModel> results = aiModelMapper.query(AiModel.builder().id(id).build());
        if (results.isEmpty()) {
            throw new AiModelException(ExceptionCode.AI_MODEL_NOT_FOUND,
                                    "AI model not found with id: " + id);
        }
        log.info("queryAiClientModelById success, id: {}", id);
        return results.get(0);
    }

    @Override
    public AiModel queryAiClientModelByModelId(String modelId) {
        log.info("queryAiClientModelByModelId request: {}", modelId);
        List<AiModel> results = aiModelMapper.query(AiModel.builder().modelId(modelId).build());
        if (results.isEmpty()) {
            throw new AiModelException(ExceptionCode.AI_MODEL_ID_NOT_FOUND,
                                    "AI model not found with modelId: " + modelId);
        }
        log.info("queryAiClientModelByModelId success, modelId: {}", modelId);
        return results.get(0);
    }

    @Override
    public List<AiModel> queryAiClientModelsByApiId(String apiId) {
        log.info("queryAiClientModelsByApiId request: {}", apiId);
        List<AiModel> aiModels = aiModelMapper.query(AiModel.builder().apiId(apiId).build());
        log.info("queryAiClientModelsByApiId success, count: {}", aiModels.size());
        return aiModels;
    }

    @Override
    public List<AiModel> queryAiClientModelsByModelProvider(String modelProvider) {
        log.info("queryAiClientModelsByModelProvider request: {}", modelProvider);
        List<AiModel> aiModels = aiModelMapper.query(AiModel.builder().modelProvider(modelProvider).build());
        log.info("queryAiClientModelsByModelProvider success, count: {}", aiModels.size());
        return aiModels;
    }

    @Override
    public List<AiModel> queryEnabledAiClientModels() {
        log.info("queryEnabledAiClientModels request");
        List<AiModel> aiModels = aiModelMapper.query(AiModel.builder().status(1).build());
        log.info("queryEnabledAiClientModels success, count: {}", aiModels.size());
        return aiModels;
    }

    @Override
    public List<AiModel> queryAiClientModelList(AiModel aiModel, Integer pageNum, Integer pageSize) {
        log.info("queryAiClientModelList request: aiModel={}, pageNum={}, pageSize={}", aiModel, pageNum, pageSize);

        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);
        List<AiModel> result = aiModelMapper.query(aiModel);
        log.info("queryAiClientModelList success, returned: {}", result.size());
        return result;
    }

    @Override
    public List<AiModel> queryAllAiClientModels() {
        log.info("queryAllAiClientModels request");
        List<AiModel> aiModels = aiModelMapper.query(new AiModel());
        log.info("queryAllAiClientModels success, count: {}", aiModels.size());
        return aiModels;
    }
}
