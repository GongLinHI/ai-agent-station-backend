package com.gonglin.ai4knowledge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gonglin.ai4knowledge.config.JacksonObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDate;

@SpringBootTest
@Slf4j
public class AIAgentTest {
    private ChatModel chatModel;
    private EmbeddingModel embeddingModel;
    private ChatClient chatClient;
    private PgVectorStore deepSeekPgVectorStore;
    @Autowired
    @Qualifier("postgresqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void init() {
        // 聊天模型配置
        OpenAiApi deepseekChatApi = OpenAiApi.builder()
                .baseUrl("https://api.deepseek.com/")
                .apiKey("sk-c58a74e3c645467eab377b01b2b29ce9")
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .model("deepseek-chat")
                .temperature(0.6)
                .maxTokens(8192)
                .build();
        chatModel = OpenAiChatModel.builder()
                .openAiApi(deepseekChatApi)
                .defaultOptions(openAiChatOptions)
                .build();
        // 嵌入模型配置
        OpenAiApi deepseekEmbeddingApi = OpenAiApi.builder()
                .baseUrl("https://api.siliconflow.cn/")
                .apiKey("sk-fqxfpupthbtjotojseclmfjwplhtzswufmhoyffjfezlssdh")
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();

        OpenAiEmbeddingOptions deepseekEmbeddingOptions = OpenAiEmbeddingOptions.builder()
                .model("BAAI/bge-m3")
                .dimensions(1024)
                .build();
        embeddingModel = new OpenAiEmbeddingModel(deepseekEmbeddingApi,
                                                  MetadataMode.EMBED,
                                                  deepseekEmbeddingOptions);


        deepSeekPgVectorStore = PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("vector_store_1024")
                .build();
        // MCP工具
        SyncMcpToolCallbackProvider toolCallbackProvider = SyncMcpToolCallbackProvider.builder()
                .mcpClients(stdioSyncMcpClient(), sseSyncMcpClient())
                .build();
        // 配置聊天记忆顾问
        PromptChatMemoryAdvisor promptChatMemoryAdvisor = PromptChatMemoryAdvisor.builder(
                        MessageWindowChatMemory.builder()
                                .maxMessages(100)
                                .build())
                .build();

        // RAG顾问
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(deepSeekPgVectorStore)
                .searchRequest(SearchRequest.builder().similarityThreshold(0.8d).topK(6).build())
                .build();
        //Log顾问
        SimpleLoggerAdvisor simpleLoggerAdvisor = SimpleLoggerAdvisor.builder().build();
        // 构建聊天客户端

        chatClient = ChatClient.builder(chatModel)
                .defaultToolCallbacks(toolCallbackProvider) // 工具回调的注册顺序 通常没有影响
                .defaultAdvisors(promptChatMemoryAdvisor, questionAnswerAdvisor, simpleLoggerAdvisor) // 注册顾问，确定执行顺序
                .defaultSystem("123")
                .build();
    }


    private McpSyncClient stdioSyncMcpClient() {
        ServerParameters serverParams = ServerParameters.builder("D:\\Program Files\\nodejs\\npx.cmd")
                .args("-y",
                      "@modelcontextprotocol/server-filesystem@2025.3.28",
                      "D:\\itheima",
                      "D:\\itheima")
                .build();
        StdioClientTransport stdioClientTransport = new StdioClientTransport(serverParams,
                                                                             new JacksonMcpJsonMapper(
                                                                                     new JacksonObjectMapper()));
        try {
            McpSyncClient mcpSyncClient = McpClient.sync(stdioClientTransport)
                    .requestTimeout(Duration.ofSeconds(30))
                    .build();
            McpSchema.InitializeResult initializeResult = mcpSyncClient.initialize();
            System.out.println("STDIO MCP Server initialized: " + initializeResult);
            return mcpSyncClient;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize STDIO MCP client", e);
        }
    }

//    private McpSyncClient stdioSyncMcpClient2() {
//        ServerParameters serverParams = ServerParameters.builder("D:\\JDKS\\jdk-21.0.5\\bin\\java.exe")
//                .args("-Dfile.encoding=UTF-8",
//                      "-Dspring.ai.mcp.server.stdio=true",
//                      "-jar",
//                      "D:\\Code\\Mcp-Server-1230\\app\\target\\mcp-server.jar")
//                .build();
//        StdioClientTransport stdioClientTransport = new StdioClientTransport(serverParams,
//                                                                             new JacksonMcpJsonMapper(
//                                                                                     new JacksonObjectMapper()));
//        try (McpSyncClient mcpSyncClient = McpClient.sync(stdioClientTransport)
//                .requestTimeout(Duration.ofSeconds(30))
//                .build()) {
//            McpSchema.InitializeResult initializeResult = mcpSyncClient.initialize();
//            System.out.println("STDIO MCP Server initialized: " + initializeResult);
//            return mcpSyncClient;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to initialize STDIO MCP client", e);
//        }
//    }

    private McpSyncClient sseSyncMcpClient() {
        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder("http://localhost:8088")
                .sseEndpoint("/sse")
                .jsonMapper(new JacksonMcpJsonMapper(new JacksonObjectMapper()))
                .build();
        try {
            McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport)
                    .requestTimeout(Duration.ofSeconds(60))
                    .build();
            McpSchema.InitializeResult initializeResult = mcpSyncClient.initialize();
            System.out.println("SSE MCP Server initialized: " + initializeResult);
            return mcpSyncClient;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SSE MCP client", e);
        }
    }


    @Test
    public void testChat1() {
        String content = chatClient.prompt(
                        new Prompt("今天长沙的天气怎么样？" +
                                           "总结天气情况并以字符串形式写入" +
                                           "D:\\itheima\\长沙天气.txt中。（若文件不存在则新建）"))
                .system(sp -> sp.param("today", LocalDate.now().toString()))
                .advisors(adv -> adv
                        .param("chat_memory_conversation_id", "chatId-101")
                        .param("chat_memory_response_size", 100))
                .call().content();
        System.out.println(content);

    }

    @Test
    public void testChat2() throws InterruptedException {
        JacksonObjectMapper mapper = new JacksonObjectMapper();

        Prompt prompt = new Prompt(new UserMessage("有哪些工具可以使用?"));
        // 非流式响应
        //ChatResponse chatResponse1 = chatClient.prompt(prompt).call().chatResponse();
        // 流式响应
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        chatResponse.subscribe(response -> {
//                                   AssistantMessage output = response.getResult().getOutput();
//                                   // 用Jackson将output转JSON String
//                                   try {
//                                       String s = mapper.writeValueAsString(output);
//                                       log.info("Received chunk: {}", s);
//                                   } catch (JsonProcessingException e) {
//                                       throw new RuntimeException(e);
//                                   }
//                               },
//                               error -> log.error("Error during chat streaming", error),
//                               () -> {
//                                   countDownLatch.countDown();
//                                   log.info("Chat streaming completed");
//                               }
//        );
//        countDownLatch.await();
        Flux<ChatResponse> chatResponse = chatClient
                .prompt(prompt)
                .advisors(adv -> adv
                        .param("chat_memory_conversation_id", "chatId-101")
                        .param("chat_memory_response_size", 100))
                .stream().chatResponse();
        StringBuilder sb = new StringBuilder();
        chatResponse.map(response -> {
                    // 获取 AssistantMessage
                    AssistantMessage output = response.getResult().getOutput();
                    sb.append(output.getText());
                    try {
                        // 转成 JSON 字符串
                        return mapper.writeValueAsString(output);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to serialize AssistantMessage to JSON", e);
                    }
                    return "";
                })
                .doOnNext(json -> log.info("Received chunk: {}", json))  // 每条消息处理
                .doOnError(error -> log.error("Error during chat streaming", error)) // 出错处理
                .doOnComplete(() -> log.info("Chat streaming completed")) // 流结束处理
                .blockLast(); // 阻塞直到最后一条消息处理完（测试方法里可用）
        System.out.println(sb);
    }
}
