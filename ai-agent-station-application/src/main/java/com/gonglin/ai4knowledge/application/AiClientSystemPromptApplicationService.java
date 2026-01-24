package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiSystemPromptMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AiSystemPromptException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AiClientSystemPromptApplicationService implements IAiClientSystemPromptApplicationService {

    @Autowired
    private AiSystemPromptMapper aiSystemPromptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiSystemPrompt createAiClientSystemPrompt(AiSystemPrompt aiSystemPrompt) {
        log.info("createAiClientSystemPrompt request: {}", aiSystemPrompt);
        Integer result = aiSystemPromptMapper.insert(aiSystemPrompt);
        log.info("createAiClientSystemPrompt success, id: {}, result: {}", aiSystemPrompt.getId(), result);
        return aiSystemPrompt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiSystemPrompt updateAiClientSystemPromptById(AiSystemPrompt aiSystemPrompt) {
        log.info("updateAiClientSystemPromptById request: {}", aiSystemPrompt);
        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .id(aiSystemPrompt.getId())
                .isDeleted(0)
                .build();
        List<AiSystemPrompt> existingList = aiSystemPromptMapper.query(queryCondition);
        if (existingList == null || existingList.isEmpty()) {
            throw new AiSystemPromptException(ExceptionCode.AI_SYSTEM_PROMPT_NOT_FOUND,
                                            "AI system prompt not found with id: " + aiSystemPrompt.getId());
        }
        Integer result = aiSystemPromptMapper.updateById(aiSystemPrompt);
        log.info("updateAiClientSystemPromptById success, id: {}, result: {}", aiSystemPrompt.getId(), result);
        return aiSystemPrompt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiSystemPrompt updateAiClientSystemPromptByPromptId(AiSystemPrompt aiSystemPrompt) {
        log.info("updateAiClientSystemPromptByPromptId request: {}", aiSystemPrompt);
        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .promptId(aiSystemPrompt.getPromptId())
                .isDeleted(0)
                .build();
        List<AiSystemPrompt> existingList = aiSystemPromptMapper.query(queryCondition);
        if (existingList == null || existingList.isEmpty()) {
            throw new AiSystemPromptException(ExceptionCode.AI_SYSTEM_PROMPT_ID_NOT_FOUND,
                                            "AI system prompt not found with promptId: " + aiSystemPrompt.getPromptId());
        }
        aiSystemPrompt.setId(existingList.get(0).getId());
        Integer result = aiSystemPromptMapper.updateByPromptId(aiSystemPrompt);
        log.info("updateAiClientSystemPromptByPromptId success, promptId: {}, result: {}", aiSystemPrompt.getPromptId(),
                 result);
        return aiSystemPrompt;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientSystemPromptById(Long id) {
        log.info("deleteAiClientSystemPromptById request: {}", id);
        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .id(id)
                .isDeleted(0)
                .build();
        List<AiSystemPrompt> existingList = aiSystemPromptMapper.query(queryCondition);
        if (existingList == null || existingList.isEmpty()) {
            throw new AiSystemPromptException(ExceptionCode.AI_SYSTEM_PROMPT_NOT_FOUND,
                                            "AI system prompt not found with id: " + id);
        }
        Integer result = aiSystemPromptMapper.deleteById(existingList.get(0));
        log.info("deleteAiClientSystemPromptById success, id: {}, result: {}", id, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientSystemPromptByPromptId(String promptId) {
        log.info("deleteAiClientSystemPromptByPromptId request: {}", promptId);
        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .promptId(promptId)
                .isDeleted(0)
                .build();
        List<AiSystemPrompt> existingList = aiSystemPromptMapper.query(queryCondition);
        if (existingList == null || existingList.isEmpty()) {
            throw new AiSystemPromptException(ExceptionCode.AI_SYSTEM_PROMPT_ID_NOT_FOUND,
                                            "AI system prompt not found with promptId: " + promptId);
        }
        Integer result = aiSystemPromptMapper.deleteByPromptId(existingList.get(0));
        log.info("deleteAiClientSystemPromptByPromptId success, promptId: {}, result: {}", promptId, result);
    }

    @Override
    public AiSystemPrompt queryAiClientSystemPromptById(Long id) {
        log.info("queryAiClientSystemPromptById request: {}", id);
        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .id(id)
                .isDeleted(0)
                .build();
        List<AiSystemPrompt> aiSystemPrompts = aiSystemPromptMapper.query(queryCondition);
        if (aiSystemPrompts == null || aiSystemPrompts.isEmpty()) {
            throw new AiSystemPromptException(ExceptionCode.AI_SYSTEM_PROMPT_NOT_FOUND,
                                            "AI system prompt not found with id: " + id);
        }
        log.info("queryAiClientSystemPromptById success, id: {}", id);
        return aiSystemPrompts.get(0);
    }

    @Override
    public AiSystemPrompt queryAiClientSystemPromptByPromptId(String promptId) {
        log.info("queryAiClientSystemPromptByPromptId request: {}", promptId);
        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .promptId(promptId)
                .isDeleted(0)
                .build();
        List<AiSystemPrompt> aiSystemPrompts = aiSystemPromptMapper.query(queryCondition);
        if (aiSystemPrompts == null || aiSystemPrompts.isEmpty()) {
            throw new AiSystemPromptException(ExceptionCode.AI_SYSTEM_PROMPT_ID_NOT_FOUND,
                                            "AI system prompt not found with promptId: " + promptId);
        }
        log.info("queryAiClientSystemPromptByPromptId success, promptId: {}", promptId);
        return aiSystemPrompts.get(0);
    }

    @Override
    public List<AiSystemPrompt> queryAllAiClientSystemPrompts() {
        log.info("queryAllAiClientSystemPrompts request");
        AiSystemPrompt systemPrompt = AiSystemPrompt.builder()
                .isDeleted(0)
                .build();
        List<AiSystemPrompt> aiSystemPrompts = aiSystemPromptMapper.query(systemPrompt);
        log.info("queryAllAiClientSystemPrompts success, count: {}", aiSystemPrompts.size());
        return aiSystemPrompts;
    }

    @Override
    public List<AiSystemPrompt> queryEnabledAiClientSystemPrompts() {
        log.info("queryEnabledAiClientSystemPrompts request");
        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .status(1)
                .build();
        List<AiSystemPrompt> aiSystemPrompts = aiSystemPromptMapper.query(queryCondition);
        log.info("queryEnabledAiClientSystemPrompts success, count: {}", aiSystemPrompts.size());
        return aiSystemPrompts;
    }

    @Override
    public List<AiSystemPrompt> queryAiClientSystemPromptsByPromptName(String promptName) {
        log.info("queryAiClientSystemPromptsByPromptName request: {}", promptName);
        AiSystemPrompt queryCondition = AiSystemPrompt.builder()
                .promptName(promptName)
                .build();
        List<AiSystemPrompt> aiSystemPrompts = aiSystemPromptMapper.query(queryCondition);
        log.info("queryAiClientSystemPromptsByPromptName success, count: {}", aiSystemPrompts.size());
        return aiSystemPrompts;
    }

    @Override
    public List<AiSystemPrompt> queryAiClientSystemPromptList(AiSystemPrompt aiSystemPrompt, Integer pageNum,
                                                              Integer pageSize) {
        log.info("queryAiClientSystemPromptList request: aiSystemPrompt={}, pageNum={}, pageSize={}", aiSystemPrompt,
                 pageNum, pageSize);

        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);
        List<AiSystemPrompt> result = aiSystemPromptMapper.query(aiSystemPrompt);
        log.info("queryAiClientSystemPromptList success, returned: {}", result.size());
        return result;
    }
}
