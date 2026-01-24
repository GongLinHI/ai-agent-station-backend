package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiAdvisorMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AiAdvisorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AiClientAdvisorApplicationService implements IAiClientAdvisorApplicationService {

    @Autowired
    private AiAdvisorMapper aiAdvisorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiAdvisor createAiClientAdvisor(AiAdvisor aiAdvisor) {
        log.info("createAiClientAdvisor request: {}", aiAdvisor);
        Integer result = aiAdvisorMapper.insert(aiAdvisor);
        log.info("createAiClientAdvisor success, id: {}, result: {}", aiAdvisor.getId(), result);
        return aiAdvisor;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiAdvisor updateAiClientAdvisorById(AiAdvisor aiAdvisor) {
        log.info("updateAiClientAdvisorById request: {}", aiAdvisor);
        AiAdvisor queryParam = AiAdvisor.builder()
                .id(aiAdvisor.getId())
                .isDeleted(0)
                .build();
        List<AiAdvisor> existingList = aiAdvisorMapper.query(queryParam);
        if (existingList.isEmpty()) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_NOT_FOUND,
                                    "AI advisor not found with id: " + aiAdvisor.getId());
        }
        if (existingList.size() > 1) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_MULTIPLE_FOUND,
                                    "Multiple advisors found with id: " + aiAdvisor.getId());
        }
        Integer result = aiAdvisorMapper.updateById(aiAdvisor);
        log.info("updateAiClientAdvisorById success, id: {}, result: {}", aiAdvisor.getId(), result);
        return aiAdvisor;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiAdvisor updateAiClientAdvisorByAdvisorId(AiAdvisor aiAdvisor) {
        log.info("updateAiClientAdvisorByAdvisorId request: {}", aiAdvisor);
        AiAdvisor queryParam = AiAdvisor.builder()
                .advisorId(aiAdvisor.getAdvisorId())
                .isDeleted(0)
                .build();
        List<AiAdvisor> existingList = aiAdvisorMapper.query(queryParam);
        if (existingList.isEmpty()) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_ID_NOT_FOUND,
                                    "AI advisor not found with advisorId: " + aiAdvisor.getAdvisorId());
        }
        if (existingList.size() > 1) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_MULTIPLE_FOUND,
                                    "Multiple advisors found with advisorId: " + aiAdvisor.getAdvisorId());
        }
        aiAdvisor.setId(existingList.get(0).getId());
        Integer result = aiAdvisorMapper.updateByAdvisorId(aiAdvisor);
        log.info("updateAiClientAdvisorByAdvisorId success, advisorId: {}, result: {}", aiAdvisor.getAdvisorId(), result);
        return aiAdvisor;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientAdvisorById(Long id) {
        log.info("deleteAiClientAdvisorById request: {}", id);
        AiAdvisor queryParam = AiAdvisor.builder()
                .id(id)
                .isDeleted(0)
                .build();
        List<AiAdvisor> existingList = aiAdvisorMapper.query(queryParam);
        if (existingList.isEmpty()) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_NOT_FOUND,
                                    "AI advisor not found with id: " + id);
        }
        if (existingList.size() > 1) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_MULTIPLE_FOUND,
                                    "Multiple advisors found with id: " + id);
        }
        Integer result = aiAdvisorMapper.deleteById(existingList.get(0));
        log.info("deleteAiClientAdvisorById success, id: {}, result: {}", id, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientAdvisorByAdvisorId(String advisorId) {
        log.info("deleteAiClientAdvisorByAdvisorId request: {}", advisorId);
        AiAdvisor queryParam = AiAdvisor.builder()
                .advisorId(advisorId)
                .isDeleted(0)
                .build();
        List<AiAdvisor> existingList = aiAdvisorMapper.query(queryParam);
        if (existingList.isEmpty()) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_ID_NOT_FOUND,
                                    "AI advisor not found with advisorId: " + advisorId);
        }
        if (existingList.size() > 1) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_MULTIPLE_FOUND,
                                    "Multiple advisors found with advisorId: " + advisorId);
        }
        Integer result = aiAdvisorMapper.deleteByAdvisorId(existingList.get(0));
        log.info("deleteAiClientAdvisorByAdvisorId success, advisorId: {}, result: {}", advisorId, result);
    }

    @Override
    public AiAdvisor queryAiClientAdvisorById(Long id) {
        log.info("queryAiClientAdvisorById request: {}", id);
        AiAdvisor queryParam = AiAdvisor.builder()
                .id(id)
                .isDeleted(0)
                .build();
        List<AiAdvisor> result = aiAdvisorMapper.query(queryParam);
        if (result.isEmpty()) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_NOT_FOUND,
                                    "AI advisor not found with id: " + id);
        }
        if (result.size() > 1) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_MULTIPLE_FOUND,
                                    "Multiple advisors found with id: " + id);
        }
        log.info("queryAiClientAdvisorById success, id: {}", id);
        return result.get(0);
    }

    @Override
    public AiAdvisor queryAiClientAdvisorByAdvisorId(String advisorId) {
        log.info("queryAiClientAdvisorByAdvisorId request: {}", advisorId);
        AiAdvisor queryParam = AiAdvisor.builder()
                .advisorId(advisorId)
                .isDeleted(0)
                .build();
        List<AiAdvisor> result = aiAdvisorMapper.query(queryParam);
        if (result.isEmpty()) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_ID_NOT_FOUND,
                                    "AI advisor not found with advisorId: " + advisorId);
        }
        if (result.size() > 1) {
            throw new AiAdvisorException(ExceptionCode.AI_ADVISOR_MULTIPLE_FOUND,
                                    "Multiple advisors found with advisorId: " + advisorId);
        }
        log.info("queryAiClientAdvisorByAdvisorId success, advisorId: {}", advisorId);
        return result.get(0);
    }

    @Override
    public List<AiAdvisor> queryEnabledAiClientAdvisors() {
        log.info("queryEnabledAiClientAdvisors request");
        AiAdvisor queryParam = AiAdvisor.builder()
                .status(1)
                .isDeleted(0)
                .build();
        List<AiAdvisor> aiAdvisors = aiAdvisorMapper.query(queryParam);
        log.info("queryEnabledAiClientAdvisors success, count: {}", aiAdvisors.size());
        return aiAdvisors;
    }

    @Override
    public List<AiAdvisor> queryAiClientAdvisorsByStatus(Integer status) {
        log.info("queryAiClientAdvisorsByStatus request: {}", status);
        AiAdvisor queryParam = AiAdvisor.builder()
                .status(status)
                .isDeleted(0)
                .build();
        List<AiAdvisor> aiAdvisors = aiAdvisorMapper.query(queryParam);
        log.info("queryAiClientAdvisorsByStatus success, count: {}", aiAdvisors.size());
        return aiAdvisors;
    }

    @Override
    public List<AiAdvisor> queryAiClientAdvisorsByType(String advisorType) {
        log.info("queryAiClientAdvisorsByType request: {}", advisorType);
        AiAdvisor queryParam = AiAdvisor.builder()
                .advisorType(advisorType)
                .isDeleted(0)
                .build();
        List<AiAdvisor> aiAdvisors = aiAdvisorMapper.query(queryParam);
        log.info("queryAiClientAdvisorsByType success, count: {}", aiAdvisors.size());
        return aiAdvisors;
    }

    @Override
    public List<AiAdvisor> queryAiClientAdvisorList(AiAdvisor aiAdvisor, Integer pageNum, Integer pageSize) {
        log.info("queryAiClientAdvisorList request: aiAdvisor={}, pageNum={}, pageSize={}", aiAdvisor, pageNum, pageSize);

        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);
        
        AiAdvisor queryParam = AiAdvisor.builder()
                .id(aiAdvisor.getId())
                .advisorId(aiAdvisor.getAdvisorId())
                .advisorName(aiAdvisor.getAdvisorName())
                .description(aiAdvisor.getDescription())
                .advisorType(aiAdvisor.getAdvisorType())
                .status(aiAdvisor.getStatus())
                .isDeleted(0)
                .build();
        
        List<AiAdvisor> result = aiAdvisorMapper.query(queryParam);
        log.info("queryAiClientAdvisorList success, returned: {}", result.size());
        return result;
    }

    @Override
    public List<AiAdvisor> queryAllAiClientAdvisors() {
        log.info("queryAllAiClientAdvisors request");
        AiAdvisor queryParam = AiAdvisor.builder()
                .isDeleted(0)
                .build();
        List<AiAdvisor> aiAdvisors = aiAdvisorMapper.query(queryParam);
        log.info("queryAllAiClientAdvisors success, count: {}", aiAdvisors.size());
        return aiAdvisors;
    }
}
