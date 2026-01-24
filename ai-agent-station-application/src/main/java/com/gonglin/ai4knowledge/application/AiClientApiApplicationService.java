package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiApiMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AiApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AiClientApiApplicationService implements IAiClientApiApplicationService {

    @Autowired
    private AiApiMapper aiApiMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiApi createAiClientApi(AiApi aiApi) {
        log.info("createAiClientApi request: {}", aiApi);
        Integer result = aiApiMapper.insert(aiApi);
        log.info("createAiClientApi success, id: {}, result: {}", aiApi.getId(), result);
        return aiApi;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiApi updateAiClientApiById(AiApi aiApi) {
        log.info("updateAiClientApiById request: {}", aiApi);
        AiApi queryParam = AiApi.builder()
                .id(aiApi.getId())
                .isDeleted(0)
                .build();
        List<AiApi> existingList = aiApiMapper.query(queryParam);
        if (existingList == null || existingList.isEmpty()) {
            throw new AiApiException(ExceptionCode.AI_API_NOT_FOUND,
                                  "AI client API not found with id: " + aiApi.getId());
        }
        Integer result = aiApiMapper.updateById(aiApi);
        log.info("updateAiClientApiById success, id: {}, result: {}", aiApi.getId(), result);
        return aiApi;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiApi updateAiClientApiByApiId(AiApi aiApi) {
        log.info("updateAiClientApiByApiId request: {}", aiApi);
        AiApi queryParam = AiApi.builder()
                .apiId(aiApi.getApiId())
                .isDeleted(0)
                .build();
        List<AiApi> existingList = aiApiMapper.query(queryParam);
        if (existingList == null || existingList.isEmpty()) {
            throw new AiApiException(ExceptionCode.AI_API_ID_NOT_FOUND,
                                  "AI client API not found with apiId: " + aiApi.getApiId());
        }
        Integer result = aiApiMapper.updateByApiId(aiApi);
        log.info("updateAiClientApiByApiId success, apiId: {}, result: {}", aiApi.getApiId(), result);
        return aiApi;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientApiById(Long id) {
        log.info("deleteAiClientApiById request: {}", id);
        AiApi queryParam = AiApi.builder()
                .id(id)
                .isDeleted(0)
                .build();
        List<AiApi> existingList = aiApiMapper.query(queryParam);
        if (existingList == null || existingList.isEmpty()) {
            throw new AiApiException(ExceptionCode.AI_API_NOT_FOUND,
                                  "AI client API not found with id: " + id);
        }
        Integer result = aiApiMapper.deleteById(existingList.get(0));
        log.info("deleteAiClientApiById success, id: {}, result: {}", id, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientApiByApiId(String apiId) {
        log.info("deleteAiClientApiByApiId request: {}", apiId);
        AiApi queryParam = AiApi.builder()
                .apiId(apiId)
                .isDeleted(0)
                .build();
        List<AiApi> existingList = aiApiMapper.query(queryParam);
        if (existingList == null || existingList.isEmpty()) {
            throw new AiApiException(ExceptionCode.AI_API_ID_NOT_FOUND,
                                  "AI client API not found with apiId: " + apiId);
        }
        Integer result = aiApiMapper.deleteByApiId(existingList.get(0));
        log.info("deleteAiClientApiByApiId success, apiId: {}, result: {}", apiId, result);
    }

    @Override
    public AiApi queryAiClientApiById(Long id) {
        log.info("queryAiClientApiById request: {}", id);
        AiApi queryParam = AiApi.builder()
                .id(id)
                .isDeleted(0)
                .build();
        List<AiApi> result = aiApiMapper.query(queryParam);
        if (result == null || result.isEmpty()) {
            throw new AiApiException(ExceptionCode.AI_API_NOT_FOUND,
                                  "AI client API not found with id: " + id);
        }
        log.info("queryAiClientApiById success, id: {}", id);
        return result.get(0);
    }

    @Override
    public AiApi queryAiClientApiByApiId(String apiId) {
        log.info("queryAiClientApiByApiId request: {}", apiId);
        AiApi queryParam = AiApi.builder()
                .apiId(apiId)
                .isDeleted(0)
                .build();
        List<AiApi> result = aiApiMapper.query(queryParam);
        if (result == null || result.isEmpty()) {
            throw new AiApiException(ExceptionCode.AI_API_ID_NOT_FOUND,
                                  "AI client API not found with apiId: " + apiId);
        }
        log.info("queryAiClientApiByApiId success, apiId: {}", apiId);
        return result.get(0);
    }

    @Override
    public List<AiApi> queryEnabledAiClientApis() {
        log.info("queryEnabledAiClientApis request");
        AiApi queryParam = AiApi.builder()
                .status(1)
                .isDeleted(0)
                .build();
        List<AiApi> aiApis = aiApiMapper.query(queryParam);
        log.info("queryEnabledAiClientApis success, count: {}", aiApis.size());
        return aiApis;
    }

    @Override
    public List<AiApi> queryAiClientApiList(AiApi aiApi, Integer pageNum, Integer pageSize) {
        log.info("queryAiClientApiList request: aiApi={}, pageNum={}, pageSize={}", aiApi, pageNum, pageSize);

        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);

        if (aiApi.getIsDeleted() == null) {
            aiApi.setIsDeleted(0);
        }

        List<AiApi> result = aiApiMapper.query(aiApi);
        log.info("queryAiClientApiList success, returned: {}", result.size());
        return result;
    }

    @Override
    public List<AiApi> queryAllAiClientApis() {
        log.info("queryAllAiClientApis request");
        AiApi queryParam = AiApi.builder()
                .isDeleted(0)
                .build();
        List<AiApi> aiApis = aiApiMapper.query(queryParam);
        log.info("queryAllAiClientApis success, count: {}", aiApis.size());
        return aiApis;
    }
}
