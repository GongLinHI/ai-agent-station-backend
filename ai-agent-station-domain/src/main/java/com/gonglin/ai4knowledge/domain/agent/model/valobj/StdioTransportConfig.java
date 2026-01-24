package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StdioTransportConfig {

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private String command = "npx";

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private List<String> args = new ArrayList<>();

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Map<String, String> env = new HashMap<>();

    @JsonAnySetter
    public void ignoreUnknownProperty(String key, Object value) {
    }
}