package com.gonglin.ai4knowledge.domain.agent.service.execute;

import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

public interface IAiAgentExecuteService {

    void setup(ExecuteCommand command, ResponseBodyEmitter emitter) throws Exception;

}