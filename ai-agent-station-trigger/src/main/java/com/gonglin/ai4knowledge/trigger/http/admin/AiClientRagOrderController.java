package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IAiClientRagOrderAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.AiClientRagOrderQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientRagOrderRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientRagOrderResponseDTO;
import com.gonglin.ai4knowledge.application.IAiClientRagOrderApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiRagOrder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/ai-client-rag-order")
public class AiClientRagOrderController implements IAiClientRagOrderAdminService {

    @Autowired
    private IAiClientRagOrderApplicationService aiClientRagOrderApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientRagOrder(@RequestBody AiClientRagOrderRequestDTO request) {
        log.info("createAiClientRagOrder request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getRagId())) {
            throw new IllegalArgumentException("ragId is required");
        }
        if (StringUtils.isBlank(request.getRagName())) {
            throw new IllegalArgumentException("ragName is required");
        }

        AiRagOrder aiRagOrder = AiRagOrder.builder()
                .ragId(request.getRagId())
                .ragName(request.getRagName())
                .knowledgeTag(request.getKnowledgeTag())
                .status(request.getStatus())
                .build();

        aiClientRagOrderApplicationService.createAiClientRagOrder(aiRagOrder);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientRagOrderById(@RequestBody AiClientRagOrderRequestDTO request) {
        log.info("updateAiClientRagOrderById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        AiRagOrder aiRagOrder = AiRagOrder.builder()
                .id(request.getId())
                .ragId(request.getRagId())
                .ragName(request.getRagName())
                .knowledgeTag(request.getKnowledgeTag())
                .status(request.getStatus())
                .build();

        aiClientRagOrderApplicationService.updateAiClientRagOrderById(aiRagOrder);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-rag-id")
    public Response<Boolean> updateAiClientRagOrderByRagId(@RequestBody AiClientRagOrderRequestDTO request) {
        log.info("updateAiClientRagOrderByRagId request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getRagId())) {
            throw new IllegalArgumentException("ragId is required");
        }

        AiRagOrder aiRagOrder = AiRagOrder.builder()
                .ragId(request.getRagId())
                .ragName(request.getRagName())
                .knowledgeTag(request.getKnowledgeTag())
                .status(request.getStatus())
                .build();

        aiClientRagOrderApplicationService.updateAiClientRagOrderByRagId(aiRagOrder);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientRagOrderById(@PathVariable Long id) {
        log.info("deleteAiClientRagOrderById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        aiClientRagOrderApplicationService.deleteAiClientRagOrderById(id);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-rag-id/{ragId}")
    public Response<Boolean> deleteAiClientRagOrderByRagId(@PathVariable String ragId) {
        log.info("deleteAiClientRagOrderByRagId request: ragId={}", ragId);
        Objects.requireNonNull(ragId, "ragId cannot be null");
        aiClientRagOrderApplicationService.deleteAiClientRagOrderByRagId(ragId);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientRagOrderResponseDTO> queryAiClientRagOrderById(@PathVariable Long id) {
        log.info("queryAiClientRagOrderById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        AiRagOrder aiRagOrder = aiClientRagOrderApplicationService.queryAiClientRagOrderById(id);
        return Response.success(toAiClientRagOrderResponseDTO(aiRagOrder));
    }

    @Override
    @GetMapping("/query-by-rag-id/{ragId}")
    public Response<AiClientRagOrderResponseDTO> queryAiClientRagOrderByRagId(@PathVariable String ragId) {
        log.info("queryAiClientRagOrderByRagId request: ragId={}", ragId);
        Objects.requireNonNull(ragId, "ragId cannot be null");
        AiRagOrder aiRagOrder = aiClientRagOrderApplicationService.queryAiClientRagOrderByRagId(ragId);
        return Response.success(toAiClientRagOrderResponseDTO(aiRagOrder));
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientRagOrderResponseDTO>> queryEnabledAiClientRagOrders() {
        log.info("queryEnabledAiClientRagOrders request");
        List<AiRagOrder> aiRagOrders = aiClientRagOrderApplicationService.queryEnabledAiClientRagOrders();
        List<AiClientRagOrderResponseDTO> responseDTOList = aiRagOrders.stream()
                .map(this::toAiClientRagOrderResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-by-knowledge-tag/{knowledgeTag}")
    public Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrdersByKnowledgeTag(
            @PathVariable String knowledgeTag) {
        log.info("queryAiClientRagOrdersByKnowledgeTag request: knowledgeTag={}", knowledgeTag);
        Objects.requireNonNull(knowledgeTag, "knowledgeTag cannot be null");
        List<AiRagOrder> aiRagOrders = aiClientRagOrderApplicationService.queryAiClientRagOrdersByKnowledgeTag(
                knowledgeTag);
        List<AiClientRagOrderResponseDTO> responseDTOList = aiRagOrders.stream()
                .map(this::toAiClientRagOrderResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-by-status/{status}")
    public Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrdersByStatus(@PathVariable Integer status) {
        log.info("queryAiClientRagOrdersByStatus request: status={}", status);
        Objects.requireNonNull(status, "status cannot be null");
        List<AiRagOrder> aiRagOrders = aiClientRagOrderApplicationService.queryAiClientRagOrdersByStatus(status);
        List<AiClientRagOrderResponseDTO> responseDTOList = aiRagOrders.stream()
                .map(this::toAiClientRagOrderResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrderList(
            @RequestBody AiClientRagOrderQueryRequestDTO request) {
        log.info("queryAiClientRagOrderList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        AiRagOrder queryCondition = AiRagOrder.builder()
                .ragId(request.getRagId())
                .ragName(request.getRagName())
                .knowledgeTag(request.getKnowledgeTag())
                .status(request.getStatus())
                .build();

        List<AiRagOrder> aiRagOrders = aiClientRagOrderApplicationService.queryAiClientRagOrderList(
                queryCondition, request.getPageNum(), request.getPageSize());

        List<AiClientRagOrderResponseDTO> responseDTOList = aiRagOrders.stream()
                .map(this::toAiClientRagOrderResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientRagOrderResponseDTO>> queryAllAiClientRagOrders() {
        log.info("queryAllAiClientRagOrders request");
        List<AiRagOrder> aiRagOrders = aiClientRagOrderApplicationService.queryAllAiClientRagOrders();
        List<AiClientRagOrderResponseDTO> responseDTOList = aiRagOrders.stream()
                .map(this::toAiClientRagOrderResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseDTOList);
    }

    @Override
    @PostMapping("/upload-rag-file")
    public Response<Boolean> uploadRagFile(@RequestParam("name") String name,
                                           @RequestParam("tag") String tag,
                                           @RequestParam("files") List<MultipartFile> files) {
        log.info("uploadRagFile request: name={}, tag={}, files={}", name, tag, files.size());
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(tag, "tag cannot be null");
        Objects.requireNonNull(files, "files cannot be null");

        Boolean result = aiClientRagOrderApplicationService.uploadRagFile(name, tag, files);
        return Response.success(result);
    }

    private AiClientRagOrderResponseDTO toAiClientRagOrderResponseDTO(AiRagOrder aiRagOrder) {
        return AiClientRagOrderResponseDTO.builder()
                .id(aiRagOrder.getId())
                .ragId(aiRagOrder.getRagId())
                .ragName(aiRagOrder.getRagName())
                .knowledgeTag(aiRagOrder.getKnowledgeTag())
                .status(aiRagOrder.getStatus())
                .createTime(aiRagOrder.getCreateTime())
                .updateTime(aiRagOrder.getUpdateTime())
                .build();
    }
}
