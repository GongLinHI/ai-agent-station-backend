package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.SseTransportConfig;
import com.gonglin.ai4knowledge.types.common.McpTransferType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.McpException;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service("sse")
public class SseParseMcpToolStrategy implements IParseMcpToolStrategy {

    private final ObjectMapper jsonMapper;
    private final McpJsonMapper mcpJsonMapper;

    @Autowired
    public SseParseMcpToolStrategy(@Qualifier("jsonMapper") ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        this.mcpJsonMapper = new JacksonMcpJsonMapper(jsonMapper);
    }


    @Override
    public McpSyncClient parse(AiMcpToolEntity aiMcpToolEntity) {
        if (aiMcpToolEntity == null) {
            log.warn("解析 McpTool 失败：传入 aiMcpToolEntity 为空，无法加载数据");
            return null;
        } else if (!aiMcpToolEntity.getTransportType().equalsIgnoreCase(McpTransferType.SSE)) {
            log.warn("解析 McpTool 失败：传入 aiMcpToolEntity 传输类型非 SSE，无法加载数据");
            return null;
        }
        String jsonConfig = aiMcpToolEntity.getTransportConfig();
        if (jsonConfig == null) {
            jsonConfig = "{}";
        }
        // 解析成TransportConfigSse对象
        SseTransportConfig sseTransportConfig = null;
        try {
            sseTransportConfig = jsonMapper.readValue(jsonConfig, SseTransportConfig.class);
        } catch (Exception e) {
            log.error("解析 McpTool 失败：{}", e.getMessage());
            throw new McpException(ExceptionCode.MCP_PARSE_FAILED, 
                "解析MCP配置失败", e);
        }
        // 初始化
        HttpClientSseClientTransport transport = HttpClientSseClientTransport.builder(sseTransportConfig.getBaseUri())
                .sseEndpoint(sseTransportConfig.getSseEndpoint())
                .jsonMapper(this.mcpJsonMapper)
                .build();
        try {
            McpSyncClient mcpSyncClient = McpClient.sync(transport)
                    .requestTimeout(Duration.ofSeconds(aiMcpToolEntity.getTimeout()))
                    .build();
            McpSchema.InitializeResult initializeResult = mcpSyncClient.initialize();
            log.info("SSE MCP 客户端[{}]初始化成功，{}", aiMcpToolEntity.getMcpName(), initializeResult);
            return mcpSyncClient;
        } catch (Exception e) {
            log.error("SSE MCP 客户端初始化失败：{}", e.getMessage());
            throw new McpException(ExceptionCode.MCP_SSE_INIT_FAILED, 
                String.format("SSE MCP 客户端[%s]初始化失败", aiMcpToolEntity.getMcpName()), e);
        }
    }
}
