package com.gonglin.ai4knowledge.domain.agent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiMcpToolEntity {
    /**
     * MCP服务ID（业务用）
     */
    private String mcpId;

    /**
     * MCP名称
     */
    private String mcpName;

    /**
     * 描述
     */
    private String description;

    /**
     * 传输类型(sse/stdio)
     */
    private String transportType;

    /**
     * 传输配置(JSON格式)
     */
    private String transportConfig;

    /**
     * 请求超时时间(秒)
     */
    private Integer timeout;

    /**
     * 状态(0:禁用,1:启用)
     */
    private Integer status;


}
