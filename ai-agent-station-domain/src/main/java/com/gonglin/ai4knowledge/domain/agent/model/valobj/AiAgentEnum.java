package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AiAgentEnum {
    AI_AGENT("智能体", "AGENT", "aiAgent", "AiAgentEntityList", "aiAgentLoadDataStrategy"),
    AI_CLIENT("客户端", "CLIENT", "aiClient", "AiClientEntityList", "aiClientLoadDataStrategy"),
    AI_CHAT_MODEL("对话模型", "CHAT_MODEL", "aiChatModel", "AiModelEntityList", "aiChatModelLoadDataStrategy"),
    AI_EMBEDDING_MODEL("向量模型", "EMBEDDING_MODEL", "aiEmbeddingModel", "AiModelEntityList",
                       "aiEmbeddingModelLoadDataStrategy"),

    AI_SYSTEM_PROMPT("提示词", "SYSTEM_PROMPT", "aiSystemPrompt", "AiSystemPromptEntityList",
                     "aiSystemPromptLoadDataStrategy"),

    AI_ADVISOR("顾问角色", "ADVISOR", "aiAdvisor", "AiAdvisorEntityList", "aiAdvisorLoadDataStrategy"),
    AI_CLIENT_MCP_TOOL("客户端MCP工具", "CLIENT_MCP_TOOL", "aiClientMcpTool",
                       "AiClientMcpToolEntityList", "aiClientMcpToolLoadDataStrategy"),
    AI_MODEL_MCP_TOOL("模型MCP工具", "MODEL_MCP_TOOL", "aiModelMcpTool",
                      "AiModelMcpToolEntityList", "aiModelMcpToolLoadDataStrategy"),
    AI_API("对话API", "API", "aiApi", "AiApiEntityList", "aiApiLoadDataStrategy");


    /**
     * 名称
     */
    private String name;

    /**
     * code
     */
    private String command;

    /**
     * Bean 对象名称标签
     */
    private String beanNamePrefix;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 装配数据策略
     */
    private String loadDataStrategy;


    public String getBeanName(String name) {
        return this.beanNamePrefix + "@" + name;
    }

    public static AiAgentEnum getByCommand(String command) {
        for (AiAgentEnum enumVO : AiAgentEnum.values()) {
            if (enumVO.getCommand().equals(command)) {
                return enumVO;
            }
        }
        throw new IllegalArgumentException("No enum constant with command: " + command);
    }
}
