package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArmoryCommand {
    @Builder.Default
    private String commandType = "AGENT";

    @Builder.Default
    private List<String> commandIdList = new ArrayList<>();
}
