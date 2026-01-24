package com.gonglin.ai4knowledge.domain.agent.service.execute.strategy;

import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.repository.IAiAgentRepository;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AgentExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

@Slf4j
public abstract class AbstractExecuteSupport extends AbstractMultiThreadStrategyRouter<ExecuteCommand, ExecuteDynamicContext, String> {
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    protected IAiAgentRepository iAiAgentRepository;
    @Autowired
    @Qualifier("jsonMapper")
    protected ObjectMapper jsonMapper;


    public static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";
    public static final String CHAT_MEMORY_RETRIEVE_SIZE_KEY = "chat_memory_response_size";
    public static final Integer DEFAULT_CHAT_MEMORY_RETRIEVE_SIZE = 200;

    protected <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }


    @Override
    protected void multiThread(ExecuteCommand command, ExecuteDynamicContext context)
            throws ExecutionException, InterruptedException, TimeoutException {

    }

    protected Map<String, String> getContentMap(ExecuteDynamicContext context,
                                                Flux<ChatResponse> chatResponse) {


        StringJoiner thinkingJoiner = new StringJoiner("");
        StringJoiner answererJoiner = new StringJoiner("");

        chatResponse.map(ChatResponse::getResult)
                .filter(Objects::nonNull)
                .map(Generation::getOutput)
                .filter(Objects::nonNull)
                .doOnNext(message -> {
                    String text = message.getText();
                    if (!StringUtils.isEmpty(text)) {
                        answererJoiner.add(text);
                    }
                    Map<String, Object> metadata = message.getMetadata();
                    String thinking = (String) metadata.get("reasoningContent");
                    if (!StringUtils.isEmpty(thinking)) {
                        thinkingJoiner.add(thinking);
                    }
                    sendSseResult(context, message);
                }).blockLast();

        return Map.of("reasoning", thinkingJoiner.toString(),
                      "answer", answererJoiner.toString());
    }

    public String getStepSummaryList(ExecuteDynamicContext context) {
        String txt = context.getStepSummarySJ().toString();
        if (StringUtils.isEmpty(txt)) {
            return "[首次执行]";
        }
        return txt;
    }


    public static String templateFormat(String template, Map<String, Object> variableMap) {
        for (Map.Entry<String, Object> entry : variableMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            template = template.replace("{{" + key + "}}", value != null ? value.toString() : "");
        }
        return template;
    }


    protected void sendSseResult(ExecuteDynamicContext context, Object data) {
        String json;
        try {
            json = jsonMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("序列化SSE结果失败：{}", e.getMessage(), e);
            throw new AgentExecutionException(ExceptionCode.EXECUTE_SEND_FAILED, 
                "序列化SSE结果失败", e);
        }
        try {
            context.getResponseBodyEmitter().send("data: " + json + "\n\n");
        } catch (IOException e) {
            log.error("发送SSE结果失败：{}", e.getMessage(), e);
            throw new AgentExecutionException(ExceptionCode.EXECUTE_SEND_FAILED, 
                "发送SSE结果失败", e);
        }
    }
}
