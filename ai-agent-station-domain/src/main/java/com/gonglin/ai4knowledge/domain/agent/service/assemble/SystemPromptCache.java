package com.gonglin.ai4knowledge.domain.agent.service.assemble;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SystemPromptCache {
    private final Map<ChatClient, Prompt> map = new HashMap<>();

    public void set(ChatClient chatClient, Prompt prompt) {
        map.put(chatClient, prompt);
    }

    public Prompt get(ChatClient chatClient) {
        return map.get(chatClient);
    }
}
