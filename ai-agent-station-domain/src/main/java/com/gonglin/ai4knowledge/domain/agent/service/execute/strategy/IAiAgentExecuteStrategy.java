package com.gonglin.ai4knowledge.domain.agent.service.execute.strategy;

import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

public interface IAiAgentExecuteStrategy {


    void execute(ExecuteCommand command, ResponseBodyEmitter emitter) throws Exception;
}
