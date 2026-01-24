package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromptChatMemoryConfig {
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer maxMessages = 200;
}
