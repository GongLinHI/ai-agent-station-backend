package com.gonglin.ai4knowledge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.StdioTransportConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@Slf4j
public class JacksonTest {
    @Autowired
    @Qualifier("jsonMapper")
    private ObjectMapper jsonMapper;

    @Test
    void test() throws IOException {
        ClassPathResource resource = new ClassPathResource("1.json");
        String json = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        StdioTransportConfig configStdio = jsonMapper.readValue(json, StdioTransportConfig.class);
        log.info("测试结果：{}", configStdio);
    }


    @Test
    void test2() throws IOException {
        String json = "{\"max_tokens\": 12345}";
        OpenAiChatOptions options = jsonMapper.readValue(json, OpenAiChatOptions.class);
        log.info("测试结果：{}", options);
    }

}
