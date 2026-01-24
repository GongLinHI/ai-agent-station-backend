package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IAiClientAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.AiClientQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientResponseDTO;
import com.gonglin.ai4knowledge.application.IAiClientApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClient;
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
@RequestMapping("/admin/ai-client")
public class AiClientController implements IAiClientAdminService {

    @Autowired
    private IAiClientApplicationService aiClientApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClient(@RequestBody AiClientRequestDTO request) {
        log.info("createAiClient request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getClientId())) {
            throw new IllegalArgumentException("clientId is required");
        }
        if (StringUtils.isBlank(request.getClientName())) {
            throw new IllegalArgumentException("clientName is required");
        }

        AiClient aiClient = AiClient.builder()
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        aiClientApplicationService.createAiClient(aiClient);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientById(@RequestBody AiClientRequestDTO request) {
        log.info("updateAiClientById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        AiClient aiClient = AiClient.builder()
                .id(request.getId())
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        aiClientApplicationService.updateAiClientById(aiClient);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-client-id")
    public Response<Boolean> updateAiClientByClientId(@RequestBody AiClientRequestDTO request) {
        log.info("updateAiClientByClientId request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getClientId())) {
            throw new IllegalArgumentException("clientId is required");
        }

        AiClient aiClient = AiClient.builder()
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        aiClientApplicationService.updateAiClientByClientId(aiClient);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientById(@PathVariable Long id) {
        log.info("deleteAiClientById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        aiClientApplicationService.deleteAiClientById(id);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-client-id/{clientId}")
    public Response<Boolean> deleteAiClientByClientId(@PathVariable String clientId) {
        log.info("deleteAiClientByClientId request: clientId={}", clientId);
        Objects.requireNonNull(clientId, "clientId cannot be null");
        aiClientApplicationService.deleteAiClientByClientId(clientId);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientResponseDTO> queryAiClientById(@PathVariable Long id) {
        log.info("queryAiClientById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        AiClient aiClient = aiClientApplicationService.queryAiClientById(id);
        return Response.success(toAiClientResponseDTO(aiClient));
    }

    @Override
    @GetMapping("/query-by-client-id/{clientId}")
    public Response<AiClientResponseDTO> queryAiClientByClientId(@PathVariable String clientId) {
        log.info("queryAiClientByClientId request: clientId={}", clientId);
        Objects.requireNonNull(clientId, "clientId cannot be null");
        AiClient aiClient = aiClientApplicationService.queryAiClientByClientId(clientId);
        return Response.success(toAiClientResponseDTO(aiClient));
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientResponseDTO>> queryEnabledAiClients() {
        log.info("queryEnabledAiClients request");
        List<AiClient> aiClients = aiClientApplicationService.queryEnabledAiClients();
        List<AiClientResponseDTO> responseDTOList = aiClients.stream()
                .map(this::toAiClientResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientResponseDTO>> queryAiClientList(@RequestBody AiClientQueryRequestDTO request) {
        log.info("queryAiClientList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        AiClient queryCondition = AiClient.builder()
                .clientId(request.getClientId())
                .clientName(request.getClientName())
                .status(request.getStatus())
                .build();

        List<AiClient> aiClients = aiClientApplicationService.queryAiClientList(
                queryCondition, request.getPageNum(), request.getPageSize());

        List<AiClientResponseDTO> responseDTOList = aiClients.stream()
                .map(this::toAiClientResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientResponseDTO>> queryAllAiClients() {
        log.info("queryAllAiClients request");
        List<AiClient> aiClients = aiClientApplicationService.queryAllAiClients();
        List<AiClientResponseDTO> responseDTOList = aiClients.stream()
                .map(this::toAiClientResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    private AiClientResponseDTO toAiClientResponseDTO(AiClient aiClient) {
        return AiClientResponseDTO.builder()
                .id(aiClient.getId())
                .clientId(aiClient.getClientId())
                .clientName(aiClient.getClientName())
                .description(aiClient.getDescription())
                .status(aiClient.getStatus())
                .createTime(aiClient.getCreateTime())
                .updateTime(aiClient.getUpdateTime())
                .build();
    }
}
