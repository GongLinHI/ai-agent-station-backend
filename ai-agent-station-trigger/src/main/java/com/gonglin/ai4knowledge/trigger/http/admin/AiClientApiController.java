package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IAiClientApiAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.AiClientApiQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientApiRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientApiResponseDTO;
import com.gonglin.ai4knowledge.application.IAiClientApiApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;
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
@RequestMapping("/admin/ai-client-api")
public class AiClientApiController implements IAiClientApiAdminService {

    @Autowired
    private IAiClientApiApplicationService aiClientApiApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientApi(@RequestBody AiClientApiRequestDTO request) {
        log.info("createAiClientApi request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getApiId())) {
            throw new IllegalArgumentException("apiId is required");
        }
        if (StringUtils.isBlank(request.getApiName())) {
            throw new IllegalArgumentException("apiName is required");
        }
        if (StringUtils.isBlank(request.getBaseUrl())) {
            throw new IllegalArgumentException("baseUrl is required");
        }
        if (StringUtils.isBlank(request.getApiKey())) {
            throw new IllegalArgumentException("apiKey is required");
        }

        AiApi aiApi = AiApi.builder()
                .apiId(request.getApiId())
                .apiName(request.getApiName())
                .baseUrl(request.getBaseUrl())
                .apiKey(request.getApiKey())
                .completionsPath(request.getCompletionsPath())
                .embeddingsPath(request.getEmbeddingsPath())
                .apiProvider(request.getApiProvider())
                .extParam(request.getExtParam())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiClientApiApplicationService.createAiClientApi(aiApi);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientApiById(@RequestBody AiClientApiRequestDTO request) {
        log.info("updateAiClientApiById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        AiApi aiApi = AiApi.builder()
                .id(request.getId())
                .apiId(request.getApiId())
                .apiName(request.getApiName())
                .baseUrl(request.getBaseUrl())
                .apiKey(request.getApiKey())
                .completionsPath(request.getCompletionsPath())
                .embeddingsPath(request.getEmbeddingsPath())
                .apiProvider(request.getApiProvider())
                .extParam(request.getExtParam())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiClientApiApplicationService.updateAiClientApiById(aiApi);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-api-id")
    public Response<Boolean> updateAiClientApiByApiId(@RequestBody AiClientApiRequestDTO request) {
        log.info("updateAiClientApiByApiId request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getApiId())) {
            throw new IllegalArgumentException("apiId is required");
        }

        AiApi aiApi = AiApi.builder()
                .apiId(request.getApiId())
                .apiName(request.getApiName())
                .baseUrl(request.getBaseUrl())
                .apiKey(request.getApiKey())
                .completionsPath(request.getCompletionsPath())
                .embeddingsPath(request.getEmbeddingsPath())
                .apiProvider(request.getApiProvider())
                .extParam(request.getExtParam())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiClientApiApplicationService.updateAiClientApiByApiId(aiApi);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientApiById(@PathVariable Long id) {
        log.info("deleteAiClientApiById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        aiClientApiApplicationService.deleteAiClientApiById(id);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-api-id/{apiId}")
    public Response<Boolean> deleteAiClientApiByApiId(@PathVariable String apiId) {
        log.info("deleteAiClientApiByApiId request: apiId={}", apiId);
        Objects.requireNonNull(apiId, "apiId cannot be null");
        aiClientApiApplicationService.deleteAiClientApiByApiId(apiId);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientApiResponseDTO> queryAiClientApiById(@PathVariable Long id) {
        log.info("queryAiClientApiById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        AiApi aiApi = aiClientApiApplicationService.queryAiClientApiById(id);
        return Response.success(toAiClientApiResponseDTO(aiApi));
    }

    @Override
    @GetMapping("/query-by-api-id/{apiId}")
    public Response<AiClientApiResponseDTO> queryAiClientApiByApiId(@PathVariable String apiId) {
        log.info("queryAiClientApiByApiId request: apiId={}", apiId);
        Objects.requireNonNull(apiId, "apiId cannot be null");
        AiApi aiApi = aiClientApiApplicationService.queryAiClientApiByApiId(apiId);
        return Response.success(toAiClientApiResponseDTO(aiApi));
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientApiResponseDTO>> queryEnabledAiClientApis() {
        log.info("queryEnabledAiClientApis request");
        List<AiApi> aiApis = aiClientApiApplicationService.queryEnabledAiClientApis();
        List<AiClientApiResponseDTO> responseDTOList = aiApis.stream()
                .map(this::toAiClientApiResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientApiResponseDTO>> queryAiClientApiList(
            @RequestBody AiClientApiQueryRequestDTO request) {
        log.info("queryAiClientApiList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        AiApi queryCondition = AiApi.builder()
                .apiId(request.getApiId())
                .apiName(request.getApiName())
                .baseUrl(request.getBaseUrl())
                .status(request.getStatus())
                .build();

        List<AiApi> aiApis = aiClientApiApplicationService.queryAiClientApiList(
                queryCondition, request.getPageNum(), request.getPageSize());

        List<AiClientApiResponseDTO> responseDTOList = aiApis.stream()
                .map(this::toAiClientApiResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientApiResponseDTO>> queryAllAiClientApis() {
        log.info("queryAllAiClientApis request");
        List<AiApi> aiApis = aiClientApiApplicationService.queryAllAiClientApis();
        List<AiClientApiResponseDTO> responseDTOList = aiApis.stream()
                .map(this::toAiClientApiResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    private AiClientApiResponseDTO toAiClientApiResponseDTO(AiApi aiApi) {
        return AiClientApiResponseDTO.builder()
                .id(aiApi.getId())
                .apiId(aiApi.getApiId())
                .apiName(aiApi.getApiName())
                .baseUrl(aiApi.getBaseUrl())
                .apiKey(aiApi.getApiKey())
                .completionsPath(aiApi.getCompletionsPath())
                .embeddingsPath(aiApi.getEmbeddingsPath())
                .apiProvider(aiApi.getApiProvider())
                .extParam(aiApi.getExtParam())
                .timeout(aiApi.getTimeout())
                .status(aiApi.getStatus())
                .build();
    }
}
