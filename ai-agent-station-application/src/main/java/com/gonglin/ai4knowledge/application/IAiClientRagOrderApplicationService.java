package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiRagOrder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAiClientRagOrderApplicationService {
    AiRagOrder createAiClientRagOrder(AiRagOrder aiRagOrder);

    AiRagOrder updateAiClientRagOrderById(AiRagOrder aiRagOrder);

    AiRagOrder updateAiClientRagOrderByRagId(AiRagOrder aiRagOrder);

    void deleteAiClientRagOrderById(Long id);

    void deleteAiClientRagOrderByRagId(String ragId);

    AiRagOrder queryAiClientRagOrderById(Long id);

    AiRagOrder queryAiClientRagOrderByRagId(String ragId);

    List<AiRagOrder> queryEnabledAiClientRagOrders();

    List<AiRagOrder> queryAiClientRagOrdersByKnowledgeTag(String knowledgeTag);

    List<AiRagOrder> queryAiClientRagOrdersByStatus(Integer status);

    List<AiRagOrder> queryAiClientRagOrderList(AiRagOrder aiRagOrder, Integer pageNum, Integer pageSize);

    List<AiRagOrder> queryAllAiClientRagOrders();

    Boolean uploadRagFile(String name, String tag, List<MultipartFile> files);
}
