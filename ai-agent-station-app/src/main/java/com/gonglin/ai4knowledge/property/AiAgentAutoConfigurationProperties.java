package com.gonglin.ai4knowledge.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "ai-agent.auto", ignoreInvalidFields = true)
public class AiAgentAutoConfigurationProperties {
    /**
     * 是否启用 AI Agent 自动化预热（加载数据等操作）？默认：false
     */
    private boolean enabled = Boolean.FALSE;

    /**
     * 需要预热的 Agent 列表
     */
    private List<String> agentIds = new ArrayList<>();
}
