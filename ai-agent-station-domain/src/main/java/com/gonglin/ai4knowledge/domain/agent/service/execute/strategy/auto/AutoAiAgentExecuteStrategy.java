package com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.auto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AutoAgentExecuteResult;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.IAiAgentExecuteStrategy;
import com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.auto.node.DataNode;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AgentExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@Slf4j
@Service("auto")
public class AutoAiAgentExecuteStrategy implements IAiAgentExecuteStrategy {

    private final DataNode dataNode;
    private final ObjectMapper jsonMapper;

    @Autowired
    public AutoAiAgentExecuteStrategy(DataNode dataNode, ObjectMapper jsonMapper) {
        this.dataNode = dataNode;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void execute(ExecuteCommand command, ResponseBodyEmitter emitter) throws Exception {
        ExecuteDynamicContext context = ExecuteDynamicContext.builder()
                .responseBodyEmitter(emitter)
                .currentTask(command.getUserMessage()).build();
        if (command.getMaxStep() != null) {
            context.setMaxStep(command.getMaxStep());
        }

        if (command.getSessionId() != null) {
            context.setSessionId(command.getSessionId());
        } else {
            command.setSessionId(context.getSessionId());
        }
        dataNode.apply(command, context);
        // SSE 响应，发送完成标识
        AutoAgentExecuteResult completeResult = AutoAgentExecuteResult.createCompleteResult(context.getSessionId());
        String json = jsonMapper.writeValueAsString(completeResult);
        String sseData = "data: " + json + "\n\n";
        try {
            emitter.send(sseData);
        } catch (Exception e) {
            log.error("发送[{}]{}失败,原因是{}", completeResult.getType(), json, e.getMessage(), e);
            throw new AgentExecutionException(ExceptionCode.EXECUTE_SEND_FAILED, 
                String.format("发送[%s]%s失败", completeResult.getType(), json), e);
        }
    }
}
