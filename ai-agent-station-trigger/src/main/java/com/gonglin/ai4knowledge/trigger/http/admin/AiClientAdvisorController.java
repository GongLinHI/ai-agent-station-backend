package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IAiClientAdvisorAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.AiClientAdvisorQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientAdvisorRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientAdvisorResponseDTO;
import com.gonglin.ai4knowledge.application.IAiClientAdvisorApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;
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
@RequestMapping("/admin/ai-client-advisor")
public class AiClientAdvisorController implements IAiClientAdvisorAdminService {

    @Autowired
    private IAiClientAdvisorApplicationService aiClientAdvisorApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientAdvisor(@RequestBody AiClientAdvisorRequestDTO request) {
        log.info("createAiClientAdvisor request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getAdvisorId())) {
            throw new IllegalArgumentException("advisorId is required");
        }
        if (StringUtils.isBlank(request.getAdvisorName())) {
            throw new IllegalArgumentException("advisorName is required");
        }
        if (StringUtils.isBlank(request.getAdvisorType())) {
            throw new IllegalArgumentException("advisorType is required");
        }

        AiAdvisor aiAdvisor = AiAdvisor.builder()
                .advisorId(request.getAdvisorId())
                .advisorName(request.getAdvisorName())
                .description(request.getDescription())
                .advisorType(request.getAdvisorType())
                .extParam(request.getExtParam())
                .status(request.getStatus())
                .build();

        aiClientAdvisorApplicationService.createAiClientAdvisor(aiAdvisor);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientAdvisorById(@RequestBody AiClientAdvisorRequestDTO request) {
        log.info("updateAiClientAdvisorById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        AiAdvisor aiAdvisor = AiAdvisor.builder()
                .id(request.getId())
                .advisorId(request.getAdvisorId())
                .advisorName(request.getAdvisorName())
                .description(request.getDescription())
                .advisorType(request.getAdvisorType())
                .extParam(request.getExtParam())
                .status(request.getStatus())
                .build();

        aiClientAdvisorApplicationService.updateAiClientAdvisorById(aiAdvisor);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-advisor-id")
    public Response<Boolean> updateAiClientAdvisorByAdvisorId(@RequestBody AiClientAdvisorRequestDTO request) {
        log.info("updateAiClientAdvisorByAdvisorId request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getAdvisorId())) {
            throw new IllegalArgumentException("advisorId is required");
        }

        AiAdvisor aiAdvisor = AiAdvisor.builder()
                .advisorId(request.getAdvisorId())
                .advisorName(request.getAdvisorName())
                .description(request.getDescription())
                .advisorType(request.getAdvisorType())
                .extParam(request.getExtParam())
                .status(request.getStatus())
                .build();

        aiClientAdvisorApplicationService.updateAiClientAdvisorByAdvisorId(aiAdvisor);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientAdvisorById(@PathVariable Long id) {
        log.info("deleteAiClientAdvisorById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        aiClientAdvisorApplicationService.deleteAiClientAdvisorById(id);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-advisor-id/{advisorId}")
    public Response<Boolean> deleteAiClientAdvisorByAdvisorId(@PathVariable String advisorId) {
        log.info("deleteAiClientAdvisorByAdvisorId request: advisorId={}", advisorId);
        Objects.requireNonNull(advisorId, "advisorId cannot be null");
        aiClientAdvisorApplicationService.deleteAiClientAdvisorByAdvisorId(advisorId);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientAdvisorResponseDTO> queryAiClientAdvisorById(@PathVariable Long id) {
        log.info("queryAiClientAdvisorById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        AiAdvisor aiAdvisor = aiClientAdvisorApplicationService.queryAiClientAdvisorById(id);
        return Response.success(toAiClientAdvisorResponseDTO(aiAdvisor));
    }

    @Override
    @GetMapping("/query-by-advisor-id/{advisorId}")
    public Response<AiClientAdvisorResponseDTO> queryAiClientAdvisorByAdvisorId(@PathVariable String advisorId) {
        log.info("queryAiClientAdvisorByAdvisorId request: advisorId={}", advisorId);
        Objects.requireNonNull(advisorId, "advisorId cannot be null");
        AiAdvisor aiAdvisor = aiClientAdvisorApplicationService.queryAiClientAdvisorByAdvisorId(advisorId);
        return Response.success(toAiClientAdvisorResponseDTO(aiAdvisor));
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientAdvisorResponseDTO>> queryEnabledAiClientAdvisors() {
        log.info("queryEnabledAiClientAdvisors request");
        List<AiAdvisor> aiAdvisors = aiClientAdvisorApplicationService.queryEnabledAiClientAdvisors();
        List<AiClientAdvisorResponseDTO> responseDTOList = aiAdvisors.stream()
                .map(this::toAiClientAdvisorResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-by-status/{status}")
    public Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorsByStatus(@PathVariable Integer status) {
        log.info("queryAiClientAdvisorsByStatus request: status={}", status);
        Objects.requireNonNull(status, "status cannot be null");
        List<AiAdvisor> aiAdvisors = aiClientAdvisorApplicationService.queryAiClientAdvisorsByStatus(status);
        List<AiClientAdvisorResponseDTO> responseDTOList = aiAdvisors.stream()
                .map(this::toAiClientAdvisorResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-by-type/{advisorType}")
    public Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorsByType(@PathVariable String advisorType) {
        log.info("queryAiClientAdvisorsByType request: advisorType={}", advisorType);
        Objects.requireNonNull(advisorType, "advisorType cannot be null");
        List<AiAdvisor> aiAdvisors = aiClientAdvisorApplicationService.queryAiClientAdvisorsByType(advisorType);
        List<AiClientAdvisorResponseDTO> responseDTOList = aiAdvisors.stream()
                .map(this::toAiClientAdvisorResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorList(
            @RequestBody AiClientAdvisorQueryRequestDTO request) {
        log.info("queryAiClientAdvisorList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        AiAdvisor queryCondition = AiAdvisor.builder()
                .advisorId(request.getAdvisorId())
                .advisorName(request.getAdvisorName())
                .advisorType(request.getAdvisorType())
                .status(request.getStatus())
                .build();

        List<AiAdvisor> aiAdvisors = aiClientAdvisorApplicationService.queryAiClientAdvisorList(
                queryCondition, request.getPageNum(), request.getPageSize());

        List<AiClientAdvisorResponseDTO> responseDTOList = aiAdvisors.stream()
                .map(this::toAiClientAdvisorResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientAdvisorResponseDTO>> queryAllAiClientAdvisors() {
        log.info("queryAllAiClientAdvisors request");
        List<AiAdvisor> aiAdvisors = aiClientAdvisorApplicationService.queryAllAiClientAdvisors();
        List<AiClientAdvisorResponseDTO> responseDTOList = aiAdvisors.stream()
                .map(this::toAiClientAdvisorResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    private AiClientAdvisorResponseDTO toAiClientAdvisorResponseDTO(AiAdvisor aiAdvisor) {
        return AiClientAdvisorResponseDTO.builder()
                .id(aiAdvisor.getId())
                .advisorId(aiAdvisor.getAdvisorId())
                .advisorName(aiAdvisor.getAdvisorName())
                .description(aiAdvisor.getDescription())
                .advisorType(aiAdvisor.getAdvisorType())
                .extParam(aiAdvisor.getExtParam())
                .status(aiAdvisor.getStatus())
                .createTime(aiAdvisor.getCreateTime())
                .updateTime(aiAdvisor.getUpdateTime())
                .build();
    }
}
