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
public class QuestionAnswerConfig {
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer topK = 4;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Double similarityThreshold = 0.0;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private String filterExpression = null;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private String query = "";
}
