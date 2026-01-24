package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;

import java.util.List;

public interface IAiClientSystemPromptApplicationService {
    AiSystemPrompt createAiClientSystemPrompt(AiSystemPrompt aiSystemPrompt);

    AiSystemPrompt updateAiClientSystemPromptById(AiSystemPrompt aiSystemPrompt);

    AiSystemPrompt updateAiClientSystemPromptByPromptId(AiSystemPrompt aiSystemPrompt);

    void deleteAiClientSystemPromptById(Long id);

    void deleteAiClientSystemPromptByPromptId(String promptId);

    AiSystemPrompt queryAiClientSystemPromptById(Long id);

    AiSystemPrompt queryAiClientSystemPromptByPromptId(String promptId);

    List<AiSystemPrompt> queryAllAiClientSystemPrompts();

    List<AiSystemPrompt> queryEnabledAiClientSystemPrompts();

    List<AiSystemPrompt> queryAiClientSystemPromptsByPromptName(String promptName);

    List<AiSystemPrompt> queryAiClientSystemPromptList(AiSystemPrompt aiSystemPrompt, Integer pageNum, Integer pageSize);
}
