package com.gonglin.ai4knowledge.domain.agent.service.execute;

import com.gonglin.ai4knowledge.domain.agent.model.aggregate.AiAgentEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import com.gonglin.ai4knowledge.domain.agent.repository.IAiAgentRepository;
import com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.IAiAgentExecuteStrategy;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AgentExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class AiAgentExecuteService implements IAiAgentExecuteService {
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private IAiAgentRepository repository;
    @Autowired
    private DefaultAiAgentExecuteFactory factory;

    /**
     * 设置并启动AI Agent执行
     *
     * @param command 执行命令对象，包含aiAgentId、userMessage、sessionId、maxStep等参数（不能为空）
     * @param emitter 响应体发射器，用于流式返回执行结果（不能为空）
     * @throws NullPointerException    当command或emitter为空时抛出
     * @throws AgentExecutionException 当AI Agent执行失败时抛出
     */
    @Override
    public void setup(ExecuteCommand command, ResponseBodyEmitter emitter) {
        log.info("开始设置AI Agent执行，aiAgentId: {}, sessionId: {}", command.getAiAgentId(), command.getSessionId());
        Objects.requireNonNull(command, "ExecuteCommand 不能为空");
        Objects.requireNonNull(emitter, "ResponseBodyEmitter 不能为空");

        AiAgentEntity aiAgentEntity = repository.queryAiAgentEntityByAgentIds(List.of(command.getAiAgentId())).get(0);
        IAiAgentExecuteStrategy executeStrategy = factory.getStrategy(aiAgentEntity.getStrategy());

        threadPoolExecutor.execute(() -> {
            try {
                log.info("开始执行AI Agent，aiAgentId: {}, strategy: {}", command.getAiAgentId(),
                         aiAgentEntity.getStrategy());
                executeStrategy.execute(command, emitter);
                log.info("AI Agent执行完成，aiAgentId: {}", command.getAiAgentId());
            } catch (Exception e) {
                log.error("AutoAgent执行异常，aiAgentId: {}, 错误信息: {}", command.getAiAgentId(), e.getMessage(), e);
                try {
                    emitter.send("执行异常：" + e.getMessage());
                } catch (Exception ex) {
                    log.error("发送异常信息失败：{}", ex.getMessage(), ex);
                }
                throw new AgentExecutionException(ExceptionCode.EXECUTE_FAILED,
                                                  "AI Agent执行失败", e);
            } finally {
                try {
                    emitter.complete();
                    log.info("ResponseBodyEmitter完成，aiAgentId: {}", command.getAiAgentId());
                } catch (Exception e) {
                    log.error("完成流式输出失败：{}", e.getMessage(), e);
                }
            }
        });
    }
}
