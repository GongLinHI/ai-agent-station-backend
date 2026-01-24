package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.AiClientRagOrderQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientRagOrderRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientRagOrderUploadRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientRagOrderResponseDTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAiClientRagOrderAdminService {
    Response<Boolean> createAiClientRagOrder(AiClientRagOrderRequestDTO request);

    Response<Boolean> updateAiClientRagOrderById(AiClientRagOrderRequestDTO request);

    Response<Boolean> updateAiClientRagOrderByRagId(AiClientRagOrderRequestDTO request);

    Response<Boolean> deleteAiClientRagOrderById(Long id);

    Response<Boolean> deleteAiClientRagOrderByRagId(String ragId);

    Response<AiClientRagOrderResponseDTO> queryAiClientRagOrderById(Long id);

    Response<AiClientRagOrderResponseDTO> queryAiClientRagOrderByRagId(String ragId);

    Response<List<AiClientRagOrderResponseDTO>> queryEnabledAiClientRagOrders();

    Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrdersByKnowledgeTag(String knowledgeTag);

    Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrdersByStatus(Integer status);

    Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrderList(AiClientRagOrderQueryRequestDTO request);

    Response<List<AiClientRagOrderResponseDTO>> queryAllAiClientRagOrders();

    Response<Boolean> uploadRagFile(String name, String tag, List<MultipartFile> files);
}
