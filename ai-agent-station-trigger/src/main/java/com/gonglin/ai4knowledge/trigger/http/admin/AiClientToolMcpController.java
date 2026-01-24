package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IAiToolMcpAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.AiToolMcpQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiToolMcpRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiToolMcpResponseDTO;
import com.gonglin.ai4knowledge.application.IAiMcpToolApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
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
@RequestMapping("admin/ai-client-tool-mcp")
public class AiClientToolMcpController implements IAiToolMcpAdminService {

    @Autowired
    private IAiMcpToolApplicationService aiMcpToolApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientToolMcp(@RequestBody AiToolMcpRequestDTO request) {
        log.info("createAiClientToolMcp request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getMcpId())) {
            throw new IllegalArgumentException("mcpId is required");
        }
        if (StringUtils.isBlank(request.getMcpName())) {
            throw new IllegalArgumentException("mcpName is required");
        }
        if (StringUtils.isBlank(request.getTransportType())) {
            throw new IllegalArgumentException("transportType is required");
        }
        if (StringUtils.isBlank(request.getTransportConfig())) {
            throw new IllegalArgumentException("transportConfig is required");
        }

        AiMcpTool aiMcpTool = AiMcpTool.builder()
                .mcpId(request.getMcpId())
                .mcpName(request.getMcpName())
                .description(request.getDescription())
                .transportType(request.getTransportType())
                .transportConfig(request.getTransportConfig())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiMcpToolApplicationService.createAiClientToolMcp(aiMcpTool);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientToolMcpById(@RequestBody AiToolMcpRequestDTO request) {
        log.info("updateAiClientToolMcpById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        AiMcpTool aiMcpTool = AiMcpTool.builder()
                .id(request.getId())
                .mcpId(request.getMcpId())
                .mcpName(request.getMcpName())
                .description(request.getDescription())
                .transportType(request.getTransportType())
                .transportConfig(request.getTransportConfig())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiMcpToolApplicationService.updateAiClientToolMcpById(aiMcpTool);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-mcp-id")
    public Response<Boolean> updateAiClientToolMcpByMcpId(@RequestBody AiToolMcpRequestDTO request) {
        log.info("updateAiClientToolMcpByMcpId request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getMcpId())) {
            throw new IllegalArgumentException("mcpId is required");
        }

        AiMcpTool aiMcpTool = AiMcpTool.builder()
                .mcpId(request.getMcpId())
                .mcpName(request.getMcpName())
                .description(request.getDescription())
                .transportType(request.getTransportType())
                .transportConfig(request.getTransportConfig())
                .timeout(request.getTimeout())
                .status(request.getStatus())
                .build();

        aiMcpToolApplicationService.updateAiClientToolMcpByMcpId(aiMcpTool);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientToolMcpById(@PathVariable Long id) {
        log.info("deleteAiClientToolMcpById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        aiMcpToolApplicationService.deleteAiClientToolMcpById(id);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-mcp-id/{mcpId}")
    public Response<Boolean> deleteAiClientToolMcpByMcpId(@PathVariable String mcpId) {
        log.info("deleteAiClientToolMcpByMcpId request: mcpId={}", mcpId);
        Objects.requireNonNull(mcpId, "mcpId cannot be null");
        aiMcpToolApplicationService.deleteAiClientToolMcpByMcpId(mcpId);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiToolMcpResponseDTO> queryAiClientToolMcpById(@PathVariable Long id) {
        log.info("queryAiClientToolMcpById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        AiMcpTool aiMcpTool = aiMcpToolApplicationService.queryAiClientToolMcpById(id);
        return Response.success(toAiToolMcpResponseDTO(aiMcpTool));
    }

    @Override
    @GetMapping("/query-by-mcp-id/{mcpId}")
    public Response<AiToolMcpResponseDTO> queryAiClientToolMcpByMcpId(@PathVariable String mcpId) {
        log.info("queryAiClientToolMcpByMcpId request: mcpId={}", mcpId);
        Objects.requireNonNull(mcpId, "mcpId cannot be null");
        AiMcpTool aiMcpTool = aiMcpToolApplicationService.queryAiClientToolMcpByMcpId(mcpId);
        return Response.success(toAiToolMcpResponseDTO(aiMcpTool));
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiToolMcpResponseDTO>> queryAllAiClientToolMcps() {
        log.info("queryAllAiClientToolMcps request");
        List<AiMcpTool> aiMcpTools = aiMcpToolApplicationService.queryAllAiClientToolMcps();
        List<AiToolMcpResponseDTO> responseDTOList = aiMcpTools.stream()
                .map(this::toAiToolMcpResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-by-status/{status}")
    public Response<List<AiToolMcpResponseDTO>> queryAiClientToolMcpsByStatus(@PathVariable Integer status) {
        log.info("queryAiClientToolMcpsByStatus request: status={}", status);
        Objects.requireNonNull(status, "status cannot be null");
        List<AiMcpTool> aiMcpTools = aiMcpToolApplicationService.queryAiClientToolMcpsByStatus(status);
        List<AiToolMcpResponseDTO> responseDTOList = aiMcpTools.stream()
                .map(this::toAiToolMcpResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-by-transport-type/{transportType}")
    public Response<List<AiToolMcpResponseDTO>> queryAiClientToolMcpsByTransportType(
            @PathVariable String transportType) {
        log.info("queryAiClientToolMcpsByTransportType request: transportType={}", transportType);
        Objects.requireNonNull(transportType, "transportType cannot be null");
        List<AiMcpTool> aiMcpTools = aiMcpToolApplicationService.queryAiClientToolMcpsByTransportType(transportType);
        List<AiToolMcpResponseDTO> responseDTOList = aiMcpTools.stream()
                .map(this::toAiToolMcpResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiToolMcpResponseDTO>> queryEnabledAiClientToolMcps() {
        log.info("queryEnabledAiClientToolMcps request");
        List<AiMcpTool> aiMcpTools = aiMcpToolApplicationService.queryEnabledAiClientToolMcps();
        List<AiToolMcpResponseDTO> responseDTOList = aiMcpTools.stream()
                .map(this::toAiToolMcpResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiToolMcpResponseDTO>> queryAiClientToolMcpList(
            @RequestBody AiToolMcpQueryRequestDTO request) {
        log.info("queryAiClientToolMcpList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        AiMcpTool queryCondition = AiMcpTool.builder()
                .mcpId(request.getMcpId())
                .mcpName(request.getMcpName())
                .transportType(request.getTransportType())
                .status(request.getStatus())
                .build();

        List<AiMcpTool> aiMcpTools = aiMcpToolApplicationService.queryAiClientToolMcpList(
                queryCondition, request.getPageNum(), request.getPageSize());

        List<AiToolMcpResponseDTO> responseDTOList = aiMcpTools.stream()
                .map(this::toAiToolMcpResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    private AiToolMcpResponseDTO toAiToolMcpResponseDTO(AiMcpTool aiMcpTool) {
        return AiToolMcpResponseDTO.builder()
                .id(aiMcpTool.getId())
                .mcpId(aiMcpTool.getMcpId())
                .mcpName(aiMcpTool.getMcpName())
                .description(aiMcpTool.getDescription())
                .transportType(aiMcpTool.getTransportType())
                .transportConfig(aiMcpTool.getTransportConfig())
                .timeout(aiMcpTool.getTimeout())
                .status(aiMcpTool.getStatus())
                .createTime(aiMcpTool.getCreateTime())
                .updateTime(aiMcpTool.getUpdateTime())
                .build();
    }
}
