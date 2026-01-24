package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiClientMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClient;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AiClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AiClientApplicationService implements IAiClientApplicationService {

    @Autowired
    private AiClientMapper aiClientMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiClient createAiClient(AiClient aiClient) {
        log.info("createAiClient request: {}", aiClient);
        Integer result = aiClientMapper.insert(aiClient);
        log.info("createAiClient success, id: {}, result: {}", aiClient.getId(), result);
        return aiClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiClient updateAiClientById(AiClient aiClient) {
        log.info("updateAiClientById request: {}", aiClient);
        AiClient queryParam = AiClient.builder().id(aiClient.getId()).build();
        List<AiClient> resultList = aiClientMapper.query(queryParam);
        if (resultList == null || resultList.isEmpty()) {
            throw new AiClientException(ExceptionCode.AI_CLIENT_NOT_FOUND,
                                    "AI client not found with id: " + aiClient.getId());
        }
        Integer result = aiClientMapper.updateById(aiClient);
        log.info("updateAiClientById success, id: {}, result: {}", aiClient.getId(), result);
        return aiClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiClient updateAiClientByClientId(AiClient aiClient) {
        log.info("updateAiClientByClientId request: {}", aiClient);
        AiClient queryParam = AiClient.builder().clientId(aiClient.getClientId()).build();
        List<AiClient> resultList = aiClientMapper.query(queryParam);
        if (resultList == null || resultList.isEmpty()) {
            throw new AiClientException(ExceptionCode.AI_CLIENT_ID_NOT_FOUND,
                                    "AI client not found with clientId: " + aiClient.getClientId());
        }
        aiClient.setId(resultList.get(0).getId());
        Integer result = aiClientMapper.updateByClientId(aiClient);
        log.info("updateAiClientByClientId success, clientId: {}, result: {}", aiClient.getClientId(), result);
        return aiClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientById(Long id) {
        log.info("deleteAiClientById request: {}", id);
        AiClient queryParam = AiClient.builder().id(id).build();
        List<AiClient> resultList = aiClientMapper.query(queryParam);
        if (resultList == null || resultList.isEmpty()) {
            throw new AiClientException(ExceptionCode.AI_CLIENT_NOT_FOUND,
                                    "AI client not found with id: " + id);
        }
        Integer result = aiClientMapper.deleteById(resultList.get(0));
        log.info("deleteAiClientById success, id: {}, result: {}", id, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientByClientId(String clientId) {
        log.info("deleteAiClientByClientId request: {}", clientId);
        AiClient queryParam = AiClient.builder().clientId(clientId).build();
        List<AiClient> resultList = aiClientMapper.query(queryParam);
        if (resultList == null || resultList.isEmpty()) {
            throw new AiClientException(ExceptionCode.AI_CLIENT_ID_NOT_FOUND,
                                    "AI client not found with clientId: " + clientId);
        }
        Integer result = aiClientMapper.deleteByClientId(resultList.get(0));
        log.info("deleteAiClientByClientId success, clientId: {}, result: {}", clientId, result);
    }

    @Override
    public AiClient queryAiClientById(Long id) {
        log.info("queryAiClientById request: {}", id);
        AiClient queryParam = AiClient.builder().id(id).build();
        List<AiClient> result = aiClientMapper.query(queryParam);
        if (result == null || result.isEmpty()) {
            throw new AiClientException(ExceptionCode.AI_CLIENT_NOT_FOUND,
                                    "AI client not found with id: " + id);
        }
        log.info("queryAiClientById success, id: {}", id);
        return result.get(0);
    }

    @Override
    public AiClient queryAiClientByClientId(String clientId) {
        log.info("queryAiClientByClientId request: {}", clientId);
        AiClient queryParam = AiClient.builder().clientId(clientId).build();
        List<AiClient> result = aiClientMapper.query(queryParam);
        if (result == null || result.isEmpty()) {
            throw new AiClientException(ExceptionCode.AI_CLIENT_ID_NOT_FOUND,
                                    "AI client not found with clientId: " + clientId);
        }
        log.info("queryAiClientByClientId success, clientId: {}", clientId);
        return result.get(0);
    }

    @Override
    public List<AiClient> queryEnabledAiClients() {
        log.info("queryEnabledAiClients request");
        AiClient queryParam = AiClient.builder().status(1).build();
        List<AiClient> aiClients = aiClientMapper.query(queryParam);
        log.info("queryEnabledAiClients success, count: {}", aiClients.size());
        return aiClients;
    }

    @Override
    public List<AiClient> queryAiClientList(AiClient aiClient, Integer pageNum, Integer pageSize) {
        log.info("queryAiClientList request: aiClient={}, pageNum={}, pageSize={}", aiClient, pageNum, pageSize);

        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);
        List<AiClient> result = aiClientMapper.query(aiClient);
        log.info("queryAiClientList success, returned: {}", result.size());
        return result;
    }

    @Override
    public List<AiClient> queryAllAiClients() {
        log.info("queryAllAiClients request");
        AiClient queryParam = AiClient.builder().build();
        List<AiClient> aiClients = aiClientMapper.query(queryParam);
        log.info("queryAllAiClients success, count: {}", aiClients.size());
        return aiClients;
    }
}
