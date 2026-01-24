package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.mcp;

import com.gonglin.ai4knowledge.domain.agent.model.entity.AiMcpToolEntity;
import io.modelcontextprotocol.client.McpSyncClient;

public interface IParseMcpToolStrategy {


    McpSyncClient parse(AiMcpToolEntity aiMcpToolEntity);


}
