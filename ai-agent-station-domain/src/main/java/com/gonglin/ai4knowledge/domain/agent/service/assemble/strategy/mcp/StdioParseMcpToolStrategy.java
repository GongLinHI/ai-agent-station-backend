package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.StdioTransportConfig;
import com.gonglin.ai4knowledge.types.common.McpTransferType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.McpException;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service("stdio")
public class StdioParseMcpToolStrategy implements IParseMcpToolStrategy {

    private final ObjectMapper jsonMapper;
    private final McpJsonMapper mcpJsonMapper;

    @Autowired
    public StdioParseMcpToolStrategy(@Qualifier("jsonMapper") ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        this.mcpJsonMapper = new JacksonMcpJsonMapper(jsonMapper);
    }

    @Override
    public McpSyncClient parse(AiMcpToolEntity aiMcpToolEntity) {
        if (aiMcpToolEntity == null) {
            log.warn("解析 McpTool 失败：传入 aiMcpToolEntity 为空，无法加载数据");
            return null;
        } else if (!aiMcpToolEntity.getTransportType().equalsIgnoreCase(McpTransferType.STDIO)) {
            log.warn("解析 McpTool 失败：传入 aiMcpToolEntity 传输类型非 STDIO，无法加载数据");
            return null;
        }
        String jsonConfig = aiMcpToolEntity.getTransportConfig();
        if (jsonConfig == null || jsonConfig.isBlank()) {
            jsonConfig = "{}";
        }
        StdioTransportConfig config = null;
        // 解析成TransportConfigStdio对象
        try {
            config = jsonMapper.readValue(jsonConfig, StdioTransportConfig.class);
        } catch (Exception e) {
            log.error("解析 McpTool 失败：{}", e.getMessage());
            throw new McpException(ExceptionCode.MCP_PARSE_FAILED, 
                "解析MCP配置失败", e);
        }

        ServerParameters parameters = ServerParameters.builder(config.getCommand())
                .args(config.getArgs())
                .env(config.getEnv())
                .build();
        StdioClientTransport transport = new StdioClientTransport(parameters, mcpJsonMapper);
        try {
            McpSyncClient mcpSyncClient = McpClient.sync(transport)
                    .requestTimeout(Duration.ofSeconds(aiMcpToolEntity.getTimeout()))
                    .build();
            McpSchema.InitializeResult initializeResult = mcpSyncClient.initialize();
            log.info("STDIO MCP 客户端[{}]初始化成功，{}", aiMcpToolEntity.getMcpName(), initializeResult);
            return mcpSyncClient;
        } catch (Exception e) {
            log.error("STDIO MCP 客户端[{}]初始化失败：{}", aiMcpToolEntity.getMcpName(), e.getMessage());
            throw new McpException(ExceptionCode.MCP_STDIO_INIT_FAILED, 
                String.format("STDIO MCP 客户端[%s]初始化失败", aiMcpToolEntity.getMcpName()), e);
        }
    }
}
