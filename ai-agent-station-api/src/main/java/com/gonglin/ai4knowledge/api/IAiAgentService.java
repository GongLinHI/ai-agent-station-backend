package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.ArmoryAiAgentRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AutoAiAgentRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiAgentResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

public interface IAiAgentService {
    ResponseBodyEmitter autoAgent(AutoAiAgentRequestDTO request, HttpServletResponse response);

    Response<Boolean> armoryAgent(ArmoryAiAgentRequestDTO request);

    /**
     * 查询可用的智能体列表
     */
    Response<List<AiAgentResponseDTO>> queryAvailableAgents();
}
