package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IAiClientModelAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.AiClientModelQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientModelRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientModelResponseDTO;
import com.gonglin.ai4knowledge.application.IAiClientModelApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/ai-client-model")
public class AiClientModelController implements IAiClientModelAdminService {

    @Autowired
    private IAiClientModelApplicationService aiClientModelApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientModel(@RequestBody AiClientModelRequestDTO request) {
        log.info("createAiClientModel request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getModelId())) {
            throw new IllegalArgumentException("modelId is required");
        }
        if (StringUtils.isBlank(request.getModelName())) {
            throw new IllegalArgumentException("modelName is required");
        }
        if (StringUtils.isBlank(request.getApiId())) {
            throw new IllegalArgumentException("apiId is required");
        }

        AiModel aiModel = AiModel.builder()
                .modelId(request.getModelId())
                .modelName(request.getModelName())
                .apiId(request.getApiId())
                .modelType(request.getModelType())
                .modelProvider(request.getModelProvider())
                .modelVersion(request.getModelVersion())
                .extParam(request.getExtParam())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiClientModelApplicationService.createAiClientModel(aiModel);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientModelById(@RequestBody AiClientModelRequestDTO request) {
        log.info("updateAiClientModelById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        AiModel aiModel = AiModel.builder()
                .id(request.getId())
                .modelId(request.getModelId())
                .modelName(request.getModelName())
                .apiId(request.getApiId())
                .modelType(request.getModelType())
                .modelProvider(request.getModelProvider())
                .modelVersion(request.getModelVersion())
                .extParam(request.getExtParam())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiClientModelApplicationService.updateAiClientModelById(aiModel);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-model-id")
    public Response<Boolean> updateAiClientModelByModelId(@RequestBody AiClientModelRequestDTO request) {
        log.info("updateAiClientModelByModelId request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getModelId())) {
            throw new IllegalArgumentException("modelId is required");
        }

        AiModel aiModel = AiModel.builder()
                .modelId(request.getModelId())
                .modelName(request.getModelName())
                .apiId(request.getApiId())
                .modelType(request.getModelType())
                .modelProvider(request.getModelProvider())
                .modelVersion(request.getModelVersion())
                .extParam(request.getExtParam())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiClientModelApplicationService.updateAiClientModelByModelId(aiModel);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientModelById(@PathVariable Long id) {
        log.info("deleteAiClientModelById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        aiClientModelApplicationService.deleteAiClientModelById(id);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-model-id/{modelId}")
    public Response<Boolean> deleteAiClientModelByModelId(@PathVariable String modelId) {
        log.info("deleteAiClientModelByModelId request: modelId={}", modelId);
        Objects.requireNonNull(modelId, "modelId cannot be null");
        aiClientModelApplicationService.deleteAiClientModelByModelId(modelId);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientModelResponseDTO> queryAiClientModelById(@PathVariable Long id) {
        log.info("queryAiClientModelById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        AiModel aiModel = aiClientModelApplicationService.queryAiClientModelById(id);
        return Response.success(toAiClientModelResponseDTO(aiModel));
    }

    @Override
    @GetMapping("/query-by-model-id/{modelId}")
    public Response<AiClientModelResponseDTO> queryAiClientModelByModelId(@PathVariable String modelId) {
        log.info("queryAiClientModelByModelId request: modelId={}", modelId);
        Objects.requireNonNull(modelId, "modelId cannot be null");
        AiModel aiModel = aiClientModelApplicationService.queryAiClientModelByModelId(modelId);
        return Response.success(toAiClientModelResponseDTO(aiModel));
    }

    @Override
    @GetMapping("/query-by-api-id/{apiId}")
    public Response<List<AiClientModelResponseDTO>> queryAiClientModelsByApiId(@PathVariable String apiId) {
        log.info("queryAiClientModelsByApiId request: apiId={}", apiId);
        Objects.requireNonNull(apiId, "apiId cannot be null");
        List<AiModel> aiModels = aiClientModelApplicationService.queryAiClientModelsByApiId(apiId);
        List<AiClientModelResponseDTO> responseDTOList = aiModels.stream()
                .map(this::toAiClientModelResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-by-model-provider/{modelProvider}")
    public Response<List<AiClientModelResponseDTO>> queryAiClientModelsByModelProvider(
            @PathVariable String modelProvider) {
        log.info("queryAiClientModelsByModelProvider request: modelProvider={}", modelProvider);
        Objects.requireNonNull(modelProvider, "modelProvider cannot be null");
        List<AiModel> aiModels = aiClientModelApplicationService.queryAiClientModelsByModelProvider(modelProvider);
        List<AiClientModelResponseDTO> responseDTOList = aiModels.stream()
                .map(this::toAiClientModelResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientModelResponseDTO>> queryEnabledAiClientModels() {
        log.info("queryEnabledAiClientModels request");
        List<AiModel> aiModels = aiClientModelApplicationService.queryEnabledAiClientModels();
        List<AiClientModelResponseDTO> responseDTOList = aiModels.stream()
                .map(this::toAiClientModelResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientModelResponseDTO>> queryAiClientModelList(
            @RequestBody AiClientModelQueryRequestDTO request) {
        log.info("queryAiClientModelList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        AiModel queryCondition = AiModel.builder()
                .modelId(request.getModelId())
                .apiId(request.getApiId())
                .modelType(request.getModelType())
                .modelProvider(request.getModelProvider())
                .status(request.getStatus())
                .build();

        List<AiModel> aiModels = aiClientModelApplicationService.queryAiClientModelList(
                queryCondition, request.getPageNum(), request.getPageSize());

        List<AiClientModelResponseDTO> responseDTOList = aiModels.stream()
                .map(this::toAiClientModelResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientModelResponseDTO>> queryAllAiClientModels() {
        log.info("queryAllAiClientModels request");
        List<AiModel> aiModels = aiClientModelApplicationService.queryAllAiClientModels();
        List<AiClientModelResponseDTO> responseDTOList = aiModels.stream()
                .map(this::toAiClientModelResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    private AiClientModelResponseDTO toAiClientModelResponseDTO(AiModel aiModel) {
        return AiClientModelResponseDTO.builder()
                .id(aiModel.getId())
                .modelId(aiModel.getModelId())
                .modelName(aiModel.getModelName())
                .apiId(aiModel.getApiId())
                .modelType(aiModel.getModelType())
                .modelProvider(aiModel.getModelProvider())
                .modelVersion(aiModel.getModelVersion())
                .extParam(aiModel.getExtParam())
                .timeout(aiModel.getTimeout())
                .status(aiModel.getStatus())
                .createTime(aiModel.getCreateTime())
                .updateTime(aiModel.getUpdateTime())
                .build();
    }
}