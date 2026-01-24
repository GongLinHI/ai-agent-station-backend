package com.gonglin.ai4knowledge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.node.ApiNode;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.node.ArmoryDataNode;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.node.ClientNode;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.node.ModelNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@SpringBootTest
@Slf4j
public class NodeTest {


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ArmoryDataNode dataNode;

    @Autowired
    @Qualifier("jsonMapper")
    private ObjectMapper jsonMapper;

//    @Autowired
//    private SystemPromptCache systemPromptCache;


    @Test
    public void testNode() throws Exception {
        ArmoryCommand command = ArmoryCommand.builder().commandType("CLIENT").commandIdList(
                List.of("client-2")).build();
        ArmoryDynamicContext dynamicContext = new ArmoryDynamicContext();
        dataNode.apply(command, dynamicContext);
        OpenAiApi api = applicationContext.getBean(ApiNode.getBeanName("api-1"), OpenAiApi.class);
        OpenAiChatModel chatModel = applicationContext.getBean(ModelNode.getBeanName("model-1"), OpenAiChatModel.class);
        ChatClient chatClient = applicationContext.getBean(ClientNode.getBeanName("client-2"), ChatClient.class);
        log.info("测试结果：{}", api);
        log.info("测试结果：{}", chatModel);
        log.info("测试结果：{}", chatClient);
    }


    @Test
    public void testNode2() throws Exception {
        ArmoryCommand command = ArmoryCommand.builder().commandType("CLIENT").commandIdList(
                List.of("client-4")).build();
        ArmoryDynamicContext dynamicContext = new ArmoryDynamicContext();
        dataNode.apply(command, dynamicContext);
        OpenAiApi api = applicationContext.getBean(ApiNode.getBeanName("api-1"), OpenAiApi.class);
        OpenAiChatModel chatModel = applicationContext.getBean(ModelNode.getBeanName("model-2"), OpenAiChatModel.class);
        ChatClient chatClient = applicationContext.getBean(ClientNode.getBeanName("client-4"), ChatClient.class);
        log.info("测试结果：{}", api);
        log.info("测试结果：{}", chatModel);
        log.info("测试结果：{}", chatClient);
        Flux<ChatResponse> chatResponse = chatClient.prompt(
                        new Prompt(new UserMessage("你是什么模型？有哪些可用的MCP工具？"))
                )
                .options(OpenAiChatOptions.builder().model("deepseek-chat").build())
                .stream().chatResponse();
        StringJoiner thinking = new StringJoiner("");
        StringJoiner answer = new StringJoiner("");
        chatResponse.doOnNext(resp -> {
            String chatId = resp.getMetadata().getId(); // metadata
            Map<String, Object> metadata = resp.getResult().getOutput().getMetadata(); // result.output.metadata
            String text = resp.getResult().getOutput().getText(); // 正文
            if (text == null) {
                String reasoningContent = (String) metadata.get("reasoningContent");
                thinking.add(reasoningContent);
            } else {
                answer.add(text);
            }
        }).blockLast();
        System.out.println("思考过程：\n" + thinking.toString());
        System.out.println("回答：\n" + answer.toString());
    }


    @Test
    public void testNode3() throws Exception {
        ArmoryCommand command = ArmoryCommand.builder().commandType("CLIENT").commandIdList(
                List.of("client-1")).build();
        ArmoryDynamicContext dynamicContext = new ArmoryDynamicContext();
        dataNode.apply(command, dynamicContext);
        OpenAiApi api = applicationContext.getBean(ApiNode.getBeanName("api-1"), OpenAiApi.class);
        OpenAiChatModel chatModel = applicationContext.getBean(ModelNode.getBeanName("model-1"), OpenAiChatModel.class);
        ChatClient chatClient = applicationContext.getBean(ClientNode.getBeanName("client-1"), ChatClient.class);
        log.info("测试结果：{}", api);
        log.info("测试结果：{}", chatModel);
        log.info("测试结果：{}", chatClient);
        System.out.println();
        Flux<ChatResponse> chatResponse = chatClient.prompt("我希望以《上海市量子产业发展报告》为题写一篇研究报告.")
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param("chat_memory_conversation_id", 1)
                        .param("chat_memory_response_size", 50))
                .stream().chatResponse();
        StringJoiner thinking = new StringJoiner("");
        StringJoiner answer = new StringJoiner("");
        chatResponse.doOnNext(resp -> {

            String chatId = resp.getMetadata().getId(); // metadata
            Map<String, Object> metadata = resp.getResult().getOutput().getMetadata(); // result.output.metadata
            String text = resp.getResult().getOutput().getText(); // 正文
            if (text == null) {
                String reasoningContent = (String) metadata.get("reasoningContent");
                thinking.add(reasoningContent);
            } else {
                answer.add(text);
            }
        }).blockLast();
        System.out.println("思考过程：\n" + thinking.toString());
        System.out.println("回答：\n" + answer.toString());
    }

    @Test
    public void testNode4() throws Exception {
        ArmoryCommand command = ArmoryCommand.builder().commandType("AGENT").commandIdList(
                List.of("agent-3")).build();
        ArmoryDynamicContext dynamicContext = new ArmoryDynamicContext();
        dataNode.apply(command, dynamicContext);
        OpenAiApi api = applicationContext.getBean(ApiNode.getBeanName("api-1"), OpenAiApi.class);
        OpenAiChatModel chatModel = applicationContext.getBean(ModelNode.getBeanName("model-4"), OpenAiChatModel.class);
        ChatClient chatClient = applicationContext.getBean(ClientNode.getBeanName("client-5"), ChatClient.class);
        log.info("测试结果：{}", api);
        log.info("测试结果：{}", chatModel);
        log.info("测试结果：{}", chatClient);

    }

}
