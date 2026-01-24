package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.AiMcpToolMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AiMcpToolException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AiMcpToolApplicationService implements IAiMcpToolApplicationService {

    @Autowired
    private AiMcpToolMapper aiMcpToolMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiMcpTool createAiClientToolMcp(AiMcpTool aiMcpTool) {
        log.info("createAiClientToolMcp request: {}", aiMcpTool);
        if (aiMcpTool.getMcpId() == null || aiMcpTool.getMcpId().isEmpty()) {
            throw new IllegalArgumentException("mcpId is required");
        }
        if (aiMcpTool.getMcpName() == null || aiMcpTool.getMcpName().isEmpty()) {
            throw new IllegalArgumentException("mcpName is required");
        }
        if (aiMcpTool.getTransportType() == null || aiMcpTool.getTransportType().isEmpty()) {
            throw new IllegalArgumentException("transportType is required");
        }
        if (aiMcpTool.getTransportConfig() == null || aiMcpTool.getTransportConfig().isEmpty()) {
            throw new IllegalArgumentException("transportConfig is required");
        }
        Integer result = aiMcpToolMapper.insert(aiMcpTool);
        log.info("createAiClientToolMcp success, id: {}, result: {}", aiMcpTool.getId(), result);
        return aiMcpTool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiMcpTool updateAiClientToolMcpById(AiMcpTool aiMcpTool) {
        log.info("updateAiClientToolMcpById request: {}", aiMcpTool);
        if (aiMcpTool.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        AiMcpTool existing = aiMcpToolMapper.queryById(aiMcpTool.getId());
        if (existing == null) {
            throw new AiMcpToolException(ExceptionCode.MCP_TOOL_NOT_FOUND,
                                      "MCP tool not found with id: " + aiMcpTool.getId());
        }
        Integer result = aiMcpToolMapper.updateById(aiMcpTool);
        log.info("updateAiClientToolMcpById success, id: {}, result: {}", aiMcpTool.getId(), result);
        return aiMcpTool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiMcpTool updateAiClientToolMcpByMcpId(AiMcpTool aiMcpTool) {
        log.info("updateAiClientToolMcpByMcpId request: {}", aiMcpTool);
        if (aiMcpTool.getMcpId() == null || aiMcpTool.getMcpId().isEmpty()) {
            throw new IllegalArgumentException("mcpId is required");
        }
        AiMcpTool existing = aiMcpToolMapper.queryByMcpId(aiMcpTool.getMcpId());
        if (existing == null) {
            throw new AiMcpToolException(ExceptionCode.MCP_TOOL_ID_NOT_FOUND,
                                      "MCP tool not found with mcpId: " + aiMcpTool.getMcpId());
        }
        aiMcpTool.setId(existing.getId());
        Integer result = aiMcpToolMapper.updateByMcpId(aiMcpTool);
        log.info("updateAiClientToolMcpByMcpId success, mcpId: {}, result: {}", aiMcpTool.getMcpId(), result);
        return aiMcpTool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientToolMcpById(Long id) {
        log.info("deleteAiClientToolMcpById request: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        AiMcpTool existing = aiMcpToolMapper.queryById(id);
        if (existing == null) {
            throw new AiMcpToolException(ExceptionCode.MCP_TOOL_NOT_FOUND,
                                      "MCP tool not found with id: " + id);
        }
        Integer result = aiMcpToolMapper.deleteById(existing);
        log.info("deleteAiClientToolMcpById success, id: {}, result: {}", id, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAiClientToolMcpByMcpId(String mcpId) {
        log.info("deleteAiClientToolMcpByMcpId request: {}", mcpId);
        if (mcpId == null || mcpId.isEmpty()) {
            throw new IllegalArgumentException("mcpId cannot be null");
        }
        AiMcpTool existing = aiMcpToolMapper.queryByMcpId(mcpId);
        if (existing == null) {
            throw new AiMcpToolException(ExceptionCode.MCP_TOOL_ID_NOT_FOUND,
                                      "MCP tool not found with mcpId: " + mcpId);
        }
        Integer result = aiMcpToolMapper.deleteByMcpId(existing);
        log.info("deleteAiClientToolMcpByMcpId success, mcpId: {}, result: {}", mcpId, result);
    }

    @Override
    public AiMcpTool queryAiClientToolMcpById(Long id) {
        log.info("queryAiClientToolMcpById request: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        AiMcpTool aiMcpTool = aiMcpToolMapper.queryById(id);
        if (aiMcpTool == null) {
            throw new AiMcpToolException(ExceptionCode.MCP_TOOL_NOT_FOUND,
                                      "MCP tool not found with id: " + id);
        }
        log.info("queryAiClientToolMcpById success, id: {}", id);
        return aiMcpTool;
    }

    @Override
    public AiMcpTool queryAiClientToolMcpByMcpId(String mcpId) {
        log.info("queryAiClientToolMcpByMcpId request: {}", mcpId);
        if (mcpId == null || mcpId.isEmpty()) {
            throw new IllegalArgumentException("mcpId cannot be null");
        }
        AiMcpTool aiMcpTool = aiMcpToolMapper.queryByMcpId(mcpId);
        if (aiMcpTool == null) {
            throw new AiMcpToolException(ExceptionCode.MCP_TOOL_ID_NOT_FOUND,
                                      "MCP tool not found with mcpId: " + mcpId);
        }
        log.info("queryAiClientToolMcpByMcpId success, mcpId: {}", mcpId);
        return aiMcpTool;
    }

    @Override
    public List<AiMcpTool> queryAllAiClientToolMcps() {
        log.info("queryAllAiClientToolMcps request");
        List<AiMcpTool> aiMcpTools = aiMcpToolMapper.query(new AiMcpTool());
        log.info("queryAllAiClientToolMcps success, count: {}", aiMcpTools.size());
        return aiMcpTools;
    }

    @Override
    public List<AiMcpTool> queryAiClientToolMcpsByStatus(Integer status) {
        log.info("queryAiClientToolMcpsByStatus request: {}", status);
        List<AiMcpTool> aiMcpTools = aiMcpToolMapper.query(AiMcpTool.builder().status(status).build());
        log.info("queryAiClientToolMcpsByStatus success, count: {}", aiMcpTools.size());
        return aiMcpTools;
    }

    @Override
    public List<AiMcpTool> queryAiClientToolMcpsByTransportType(String transportType) {
        log.info("queryAiClientToolMcpsByTransportType request: {}", transportType);
        if (transportType == null || transportType.isEmpty()) {
            throw new IllegalArgumentException("transportType cannot be null");
        }
        List<AiMcpTool> aiMcpTools = aiMcpToolMapper.query(AiMcpTool.builder().transportType(transportType).build());
        log.info("queryAiClientToolMcpsByTransportType success, count: {}", aiMcpTools.size());
        return aiMcpTools;
    }

    @Override
    public List<AiMcpTool> queryEnabledAiClientToolMcps() {
        log.info("queryEnabledAiClientToolMcps request");
        List<AiMcpTool> aiMcpTools = aiMcpToolMapper.query(AiMcpTool.builder().status(1).build());
        log.info("queryEnabledAiClientToolMcps success, count: {}", aiMcpTools.size());
        return aiMcpTools;
    }

    @Override
    public List<AiMcpTool> queryAiClientToolMcpList(AiMcpTool aiMcpTool, Integer pageNum, Integer pageSize) {
        log.info("queryAiClientToolMcpList request: aiMcpTool={}, pageNum={}, pageSize={}", aiMcpTool, pageNum,
                 pageSize);

        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);
        List<AiMcpTool> result = aiMcpToolMapper.query(aiMcpTool);
        log.info("queryAiClientToolMcpList success, returned: {}", result.size());
        return result;
    }
}
