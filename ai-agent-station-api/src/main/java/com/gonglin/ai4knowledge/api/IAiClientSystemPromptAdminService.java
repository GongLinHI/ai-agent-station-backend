package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.AiClientSystemPromptQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AiClientSystemPromptRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiClientSystemPromptResponseDTO;

import java.util.List;

public interface IAiClientSystemPromptAdminService {
    Response<Boolean> createAiClientSystemPrompt(AiClientSystemPromptRequestDTO request);

    Response<Boolean> updateAiClientSystemPromptById(AiClientSystemPromptRequestDTO request);

    Response<Boolean> updateAiClientSystemPromptByPromptId(AiClientSystemPromptRequestDTO request);

    Response<Boolean> deleteAiClientSystemPromptById(Long id);

    Response<Boolean> deleteAiClientSystemPromptByPromptId(String promptId);

    Response<AiClientSystemPromptResponseDTO> queryAiClientSystemPromptById(Long id);

    Response<AiClientSystemPromptResponseDTO> queryAiClientSystemPromptByPromptId(String promptId);

    Response<List<AiClientSystemPromptResponseDTO>> queryAllAiClientSystemPrompts();

    Response<List<AiClientSystemPromptResponseDTO>> queryEnabledAiClientSystemPrompts();

    Response<List<AiClientSystemPromptResponseDTO>> queryAiClientSystemPromptsByPromptName(String promptName);

    Response<List<AiClientSystemPromptResponseDTO>> queryAiClientSystemPromptList(AiClientSystemPromptQueryRequestDTO request);
}
