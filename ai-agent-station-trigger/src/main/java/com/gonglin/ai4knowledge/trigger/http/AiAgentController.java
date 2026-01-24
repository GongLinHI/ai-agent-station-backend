package com.gonglin.ai4knowledge.trigger.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.api.IAiAgentService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.ArmoryAiAgentRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.AutoAiAgentRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.AiAgentResponseDTO;
import com.gonglin.ai4knowledge.domain.agent.model.aggregate.AiAgentEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.WorkflowStep;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.IArmoryService;
import com.gonglin.ai4knowledge.domain.agent.service.execute.AiAgentExecuteService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/agent")
public class AiAgentController implements IAiAgentService {
    @Autowired
    private AiAgentExecuteService aiAgentExecuteService;
    @Autowired
    private IArmoryService armoryService;
    @Autowired
    @Qualifier("jsonMapper")
    private ObjectMapper jsonMapper;

    @Override
    @PostMapping("/auto_agent")
    public ResponseBodyEmitter autoAgent(@RequestBody AutoAiAgentRequestDTO request, HttpServletResponse response) {
        Objects.requireNonNull(request, "AutoAiAgentRequestDTO 不能为空");
        ExecuteCommand command = ExecuteCommand.builder()
                .aiAgentId(request.getAiAgentId())
                .userMessage(request.getUserMessage())
                .maxStep(request.getMaxStep())
                .sessionId(request.getSessionId())
                .build();
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(Long.MAX_VALUE);
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        aiAgentExecuteService.setup(command, emitter);
        return emitter;
    }

    @Override
    @PostMapping("/armory_agent")
    public Response<Boolean> armoryAgent(@RequestBody ArmoryAiAgentRequestDTO request) {
        Objects.requireNonNull(request, "request 不能为空");
        Objects.requireNonNull(request.getAiAgentId(), "aiAgentId 不能为空");
        armoryService.armoryByAiAgentIds(List.of(request.getAiAgentId()));
        return Response.success(true, request.getAiAgentId() + "装配成功");
    }

    @Override
    @GetMapping("/query_available_agents")
    public Response<List<AiAgentResponseDTO>> queryAvailableAgents() {
        List<AiAgentEntity> aiAgentEntities = armoryService.queryAvailableAgents();
        List<AiAgentResponseDTO> dtoList = aiAgentEntities.stream()
                .map(this::fittingToAiAgentResponseDTO)
                .toList();
        return Response.success(dtoList);
    }

    private AiAgentResponseDTO fittingToAiAgentResponseDTO(AiAgentEntity entity) {
        List<String> clientIds = entity.getWorkflowStepList().stream().map(WorkflowStep::getClientId).toList();
        return AiAgentResponseDTO.builder()
                .agentId(entity.getAgentId())
                .agentName(entity.getAgentName())
                .description(entity.getDescription())
                .channel(entity.getChannel())
                .strategy(entity.getStrategy())
                .status(entity.getStatus())
                .workflowStepClientIdList(clientIds)
                .build();
    }
}
