package com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.fixed;

import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.IAiAgentExecuteStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@Slf4j
@Service("fixed")
public class FixedAiAgentExecuteStrategy implements IAiAgentExecuteStrategy {

    @Override
    public void execute(ExecuteCommand executeCommand, ResponseBodyEmitter emitter) throws Exception {

    }
}
