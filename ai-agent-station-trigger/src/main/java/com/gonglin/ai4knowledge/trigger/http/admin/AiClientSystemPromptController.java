package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IAiClientSystemPromptAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.AiClientSystemPromptQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientSystemPromptRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientSystemPromptResponseDTO;
import com.gonglin.ai4knowledge.application.IAiClientSystemPromptApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;
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
@RequestMapping("/admin/ai-client-system-prompt")
public class AiClientSystemPromptController implements IAiClientSystemPromptAdminService {

    @Autowired
    private IAiClientSystemPromptApplicationService aiClientSystemPromptApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientSystemPrompt(@RequestBody AiClientSystemPromptRequestDTO request) {
        log.info("createAiClientSystemPrompt request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getPromptId())) {
            throw new IllegalArgumentException("promptId is required");
        }
        if (StringUtils.isBlank(request.getPromptName())) {
            throw new IllegalArgumentException("promptName is required");
        }
        if (StringUtils.isBlank(request.getPromptContent())) {
            throw new IllegalArgumentException("promptContent is required");
        }

        AiSystemPrompt aiSystemPrompt = AiSystemPrompt.builder()
                .promptId(request.getPromptId())
                .promptName(request.getPromptName())
                .promptContent(request.getPromptContent())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        aiClientSystemPromptApplicationService.createAiClientSystemPrompt(aiSystemPrompt);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientSystemPromptById(@RequestBody AiClientSystemPromptRequestDTO request) {
        log.info("updateAiClientSystemPromptById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        AiSystemPrompt aiSystemPrompt = AiSystemPrompt.builder()
                .id(request.getId())
                .promptId(request.getPromptId())
                .promptName(request.getPromptName())
                .promptContent(request.getPromptContent())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        aiClientSystemPromptApplicationService.updateAiClientSystemPromptById(aiSystemPrompt);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-prompt-id")
    public Response<Boolean> updateAiClientSystemPromptByPromptId(@RequestBody AiClientSystemPromptRequestDTO request) {
        log.info("updateAiClientSystemPromptByPromptId request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getPromptId())) {
            throw new IllegalArgumentException("promptId is required");
        }

        AiSystemPrompt aiSystemPrompt = AiSystemPrompt.builder()
                .promptId(request.getPromptId())
                .promptName(request.getPromptName())
                .promptContent(request.getPromptContent())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        aiClientSystemPromptApplicationService.updateAiClientSystemPromptByPromptId(aiSystemPrompt);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientSystemPromptById(@PathVariable Long id) {
        log.info("deleteAiClientSystemPromptById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        aiClientSystemPromptApplicationService.deleteAiClientSystemPromptById(id);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-prompt-id/{promptId}")
    public Response<Boolean> deleteAiClientSystemPromptByPromptId(@PathVariable String promptId) {
        log.info("deleteAiClientSystemPromptByPromptId request: promptId={}", promptId);
        Objects.requireNonNull(promptId, "promptId cannot be null");
        aiClientSystemPromptApplicationService.deleteAiClientSystemPromptByPromptId(promptId);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientSystemPromptResponseDTO> queryAiClientSystemPromptById(@PathVariable Long id) {
        log.info("queryAiClientSystemPromptById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        AiSystemPrompt aiSystemPrompt = aiClientSystemPromptApplicationService.queryAiClientSystemPromptById(id);
        return Response.success(toAiClientSystemPromptResponseDTO(aiSystemPrompt));
    }

    @Override
    @GetMapping("/query-by-prompt-id/{promptId}")
    public Response<AiClientSystemPromptResponseDTO> queryAiClientSystemPromptByPromptId(
            @PathVariable String promptId) {
        log.info("queryAiClientSystemPromptByPromptId request: promptId={}", promptId);
        Objects.requireNonNull(promptId, "promptId cannot be null");
        AiSystemPrompt aiSystemPrompt = aiClientSystemPromptApplicationService.queryAiClientSystemPromptByPromptId(
                promptId);
        return Response.success(toAiClientSystemPromptResponseDTO(aiSystemPrompt));
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientSystemPromptResponseDTO>> queryAllAiClientSystemPrompts() {
        log.info("queryAllAiClientSystemPrompts request");
        List<AiSystemPrompt> aiSystemPrompts = aiClientSystemPromptApplicationService.queryAllAiClientSystemPrompts();
        List<AiClientSystemPromptResponseDTO> responseDTOList = aiSystemPrompts.stream()
                .map(this::toAiClientSystemPromptResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientSystemPromptResponseDTO>> queryEnabledAiClientSystemPrompts() {
        log.info("queryEnabledAiClientSystemPrompts request");
        List<AiSystemPrompt> aiSystemPrompts = aiClientSystemPromptApplicationService.queryEnabledAiClientSystemPrompts();
        List<AiClientSystemPromptResponseDTO> responseDTOList = aiSystemPrompts.stream()
                .map(this::toAiClientSystemPromptResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-by-prompt-name/{promptName}")
    public Response<List<AiClientSystemPromptResponseDTO>> queryAiClientSystemPromptsByPromptName(
            @PathVariable String promptName) {
        log.info("queryAiClientSystemPromptsByPromptName request: promptName={}", promptName);
        Objects.requireNonNull(promptName, "promptName cannot be null");
        List<AiSystemPrompt> aiSystemPrompts = aiClientSystemPromptApplicationService.queryAiClientSystemPromptsByPromptName(
                promptName);
        List<AiClientSystemPromptResponseDTO> responseDTOList = aiSystemPrompts.stream()
                .map(this::toAiClientSystemPromptResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientSystemPromptResponseDTO>> queryAiClientSystemPromptList(
            @RequestBody AiClientSystemPromptQueryRequestDTO request) {
        log.info("queryAiClientSystemPromptList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .promptId(request.getPromptId())
                .promptName(request.getPromptName())
                .status(request.getStatus())
                .build();

        List<AiSystemPrompt> aiSystemPrompts = aiClientSystemPromptApplicationService.queryAiClientSystemPromptList(
                queryCondition, request.getPageNum(), request.getPageSize());

        List<AiClientSystemPromptResponseDTO> responseDTOList = aiSystemPrompts.stream()
                .map(this::toAiClientSystemPromptResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    private AiClientSystemPromptResponseDTO toAiClientSystemPromptResponseDTO(AiSystemPrompt aiSystemPrompt) {
        return AiClientSystemPromptResponseDTO.builder()
                .id(aiSystemPrompt.getId())
                .promptId(aiSystemPrompt.getPromptId())
                .promptName(aiSystemPrompt.getPromptName())
                .promptContent(aiSystemPrompt.getPromptContent())
                .description(aiSystemPrompt.getDescription())
                .status(aiSystemPrompt.getStatus())
                .createTime(aiSystemPrompt.getCreateTime())
                .updateTime(aiSystemPrompt.getUpdateTime())
                .build();
    }
}
