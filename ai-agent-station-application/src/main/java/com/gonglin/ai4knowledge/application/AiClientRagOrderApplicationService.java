package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiRagOrderMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiRagOrder;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AiRagOrderException;
import com.gonglin.ai4knowledge.types.exception.BaseException;
import com.gonglin.ai4knowledge.types.exception.ResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AiClientRagOrderApplicationService implements IAiClientRagOrderApplicationService {

    @Autowired
    private AiRagOrderMapper aiRagOrderMapper;

    @Autowired
    @Qualifier("pgVectorStore")
    private PgVectorStore pgVectorStore;

    @Autowired
    private TokenTextSplitter tokenTextSplitter;

    private static final String UPLOAD_DIR = "rag/uploadFiles/";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiRagOrder createAiClientRagOrder(AiRagOrder aiRagOrder) {
        log.info("createAiClientRagOrder request: {}", aiRagOrder);
        Integer result = aiRagOrderMapper.insert(aiRagOrder);
        log.info("createAiClientRagOrder success, id: {}, result: {}", aiRagOrder.getId(), result);
        return aiRagOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiRagOrder updateAiClientRagOrderById(AiRagOrder aiRagOrder) {
        log.info("updateAiClientRagOrderById request: {}", aiRagOrder);
        AiRagOrder query = AiRagOrder.builder().id(aiRagOrder.getId()).isDeleted(0).build();
        AiRagOrder existing = aiRagOrderMapper.query(query).stream().findFirst().orElse(null);
        if (existing == null) {
            throw new AiRagOrderException(ExceptionCode.RAG_ORDER_NOT_FOUND,
                                          "AI RAG order not found with id: " + aiRagOrder.getId());
        }
        Integer result = aiRagOrderMapper.updateById(aiRagOrder);
        log.info("updateAiClientRagOrderById success, id: {}, result: {}", aiRagOrder.getId(), result);
        return aiRagOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiRagOrder updateAiClientRagOrderByRagId(AiRagOrder aiRagOrder) {
        log.info("updateAiClientRagOrderByRagId request: {}", aiRagOrder);
        AiRagOrder query = AiRagOrder.builder().ragId(aiRagOrder.getRagId()).isDeleted(0).build();
        AiRagOrder existing = aiRagOrderMapper.query(query).stream().findFirst().orElse(null);
        if (existing == null) {
            throw new AiRagOrderException(ExceptionCode.RAG_ORDER_ID_NOT_FOUND,
                                          "AI RAG order not found with ragId: " + aiRagOrder.getRagId());
        }
        aiRagOrder.setId(existing.getId());
        Integer result = aiRagOrderMapper.updateByRagId(aiRagOrder);
        log.info("updateAiClientRagOrderByRagId success, ragId: {}, result: {}", aiRagOrder.getRagId(), result);
        return aiRagOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientRagOrderById(Long id) {
        log.info("deleteAiClientRagOrderById request: {}", id);
        AiRagOrder query = AiRagOrder.builder().id(id).isDeleted(0).build();
        AiRagOrder existing = aiRagOrderMapper.query(query).stream().findFirst().orElse(null);
        if (existing == null) {
            throw new AiRagOrderException(ExceptionCode.RAG_ORDER_NOT_FOUND,
                                          "AI RAG order not found with id: " + id);
        }
        Integer result = aiRagOrderMapper.deleteById(existing);
        log.info("deleteAiClientRagOrderById success, id: {}, result: {}", id, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientRagOrderByRagId(String ragId) {
        log.info("deleteAiClientRagOrderByRagId request: {}", ragId);
        AiRagOrder query = AiRagOrder.builder().ragId(ragId).isDeleted(0).build();
        AiRagOrder existing = aiRagOrderMapper.query(query).stream().findFirst().orElse(null);
        if (existing == null) {
            throw new AiRagOrderException(ExceptionCode.RAG_ORDER_ID_NOT_FOUND,
                                          "AI RAG order not found with ragId: " + ragId);
        }
        Integer result = aiRagOrderMapper.deleteByRagId(existing);
        log.info("deleteAiClientRagOrderByRagId success, ragId: {}, result: {}", ragId, result);
    }

    @Override
    public AiRagOrder queryAiClientRagOrderById(Long id) {
        log.info("queryAiClientRagOrderById request: {}", id);
        AiRagOrder query = AiRagOrder.builder().id(id).isDeleted(0).build();
        AiRagOrder aiRagOrder = aiRagOrderMapper.query(query).stream().findFirst().orElse(null);
        if (aiRagOrder == null) {
            throw new AiRagOrderException(ExceptionCode.RAG_ORDER_NOT_FOUND,
                                          "AI RAG order not found with id: " + id);
        }
        log.info("queryAiClientRagOrderById success, id: {}", id);
        return aiRagOrder;
    }

    @Override
    public AiRagOrder queryAiClientRagOrderByRagId(String ragId) {
        log.info("queryAiClientRagOrderByRagId request: {}", ragId);
        AiRagOrder query = AiRagOrder.builder().ragId(ragId).isDeleted(0).build();
        AiRagOrder aiRagOrder = aiRagOrderMapper.query(query).stream().findFirst().orElse(null);
        if (aiRagOrder == null) {
            throw new AiRagOrderException(ExceptionCode.RAG_ORDER_ID_NOT_FOUND,
                                          "AI RAG order not found with ragId: " + ragId);
        }
        log.info("queryAiClientRagOrderByRagId success, ragId: {}", ragId);
        return aiRagOrder;
    }

    @Override
    public List<AiRagOrder> queryEnabledAiClientRagOrders() {
        log.info("queryEnabledAiClientRagOrders request");
        AiRagOrder query = AiRagOrder.builder().status(1).isDeleted(0).build();
        List<AiRagOrder> aiRagOrders = aiRagOrderMapper.query(query);
        log.info("queryEnabledAiClientRagOrders success, count: {}", aiRagOrders.size());
        return aiRagOrders;
    }

    @Override
    public List<AiRagOrder> queryAiClientRagOrdersByKnowledgeTag(String knowledgeTag) {
        log.info("queryAiClientRagOrdersByKnowledgeTag request: {}", knowledgeTag);
        AiRagOrder query = AiRagOrder.builder().knowledgeTag(knowledgeTag).isDeleted(0).build();
        List<AiRagOrder> aiRagOrders = aiRagOrderMapper.query(query);
        log.info("queryAiClientRagOrdersByKnowledgeTag success, count: {}", aiRagOrders.size());
        return aiRagOrders;
    }

    @Override
    public List<AiRagOrder> queryAiClientRagOrdersByStatus(Integer status) {
        log.info("queryAiClientRagOrdersByStatus request: {}", status);
        AiRagOrder query = AiRagOrder.builder().status(status).isDeleted(0).build();
        List<AiRagOrder> aiRagOrders = aiRagOrderMapper.query(query);
        log.info("queryAiClientRagOrdersByStatus success, count: {}", aiRagOrders.size());
        return aiRagOrders;
    }

    @Override
    public List<AiRagOrder> queryAiClientRagOrderList(AiRagOrder aiRagOrder, Integer pageNum, Integer pageSize) {
        log.info("queryAiClientRagOrderList request: aiRagOrder={}, pageNum={}, pageSize={}", aiRagOrder, pageNum,
                 pageSize);
        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);
        AiRagOrder query = AiRagOrder.builder()
                .ragId(aiRagOrder.getRagId())
                .ragName(aiRagOrder.getRagName())
                .knowledgeTag(aiRagOrder.getKnowledgeTag())
                .status(aiRagOrder.getStatus())
                .isDeleted(0)
                .build();
        List<AiRagOrder> result = aiRagOrderMapper.query(query);
        log.info("queryAiClientRagOrderList success, returned: {}", result.size());
        return result;
    }

    @Override
    public List<AiRagOrder> queryAllAiClientRagOrders() {
        log.info("queryAllAiClientRagOrders request");
        AiRagOrder query = AiRagOrder.builder().isDeleted(0).build();
        List<AiRagOrder> aiRagOrders = aiRagOrderMapper.query(query);
        log.info("queryAllAiClientRagOrders success, count: {}", aiRagOrders.size());
        return aiRagOrders;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean uploadRagFile(String name, String tag, List<MultipartFile> files) {
        log.info("uploadRagFile request: name={}, tag={}, files={}", name, tag, files != null ? files.size() : 0);
        Path uploadDirPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDirPath)) {
            try {
                Files.createDirectories(uploadDirPath);
            } catch (IOException e) {
                throw new ResourceException(ExceptionCode.RESOURCE_WRITE_FAILED,
                                            "创建上传目录失败: " + e.getMessage(), e);
            }
        }
        if (files == null || files.isEmpty()) {
            throw new AiRagOrderException(ExceptionCode.PARAM_INVALID,
                                          "文件列表不能为空");
        }
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + getFileExtension(originalFilename);
            Path targetPath = uploadDirPath.resolve(uniqueFileName);

            try {
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new BaseException(ExceptionCode.RESOURCE_WRITE_FAILED, "文件保存失败: " + e.getMessage(), e);
            }
            log.info("File saved: {}", targetPath);

            DocumentReader documentReader = new TikaDocumentReader(new PathResource(targetPath));
            Map<String, String> metaData = Map.of(
                    "tag", tag,
                    "filename", originalFilename,
                    "storeName", uniqueFileName
            );

            List<org.springframework.ai.document.Document> documents = documentReader.get();
            for (org.springframework.ai.document.Document document : documents) {
                document.getMetadata().putAll(metaData);
            }

            List<org.springframework.ai.document.Document> splitDocuments = tokenTextSplitter.apply(documents);
            pgVectorStore.add(splitDocuments);

            log.info("Document processed and stored: {}, chunks: {}", originalFilename, splitDocuments.size());
        }

        log.info("uploadRagFile success");
        return true;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }
}
