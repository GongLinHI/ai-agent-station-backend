package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;

import java.util.List;

public interface IAiMcpToolApplicationService {
    AiMcpTool createAiClientToolMcp(AiMcpTool aiMcpTool);

    AiMcpTool updateAiClientToolMcpById(AiMcpTool aiMcpTool);

    AiMcpTool updateAiClientToolMcpByMcpId(AiMcpTool aiMcpTool);

    void deleteAiClientToolMcpById(Long id);

    void deleteAiClientToolMcpByMcpId(String mcpId);

    AiMcpTool queryAiClientToolMcpById(Long id);

    AiMcpTool queryAiClientToolMcpByMcpId(String mcpId);

    List<AiMcpTool> queryAllAiClientToolMcps();

    List<AiMcpTool> queryAiClientToolMcpsByStatus(Integer status);

    List<AiMcpTool> queryAiClientToolMcpsByTransportType(String transportType);

    List<AiMcpTool> queryEnabledAiClientToolMcps();

    List<AiMcpTool> queryAiClientToolMcpList(AiMcpTool aiMcpTool, Integer pageNum, Integer pageSize);
}
