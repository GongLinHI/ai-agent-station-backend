package com.gonglin.ai4knowledge.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiToolMcpRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（更新时使用，创建时为空）
     */
    private Long id;

    /**
     * MCP服务ID（业务用，非空字符串，最大长度64）
     */
    private String mcpId;

    /**
     * MCP名称（非空字符串，最大长度50）
     */
    private String mcpName;

    /**
     * 描述（可选，最大长度255）
     */
    private String description;

    /**
     * 传输类型（非空字符串，可选值：sse/stdio，最大长度20）
     */
    private String transportType;

    /**
     * 传输配置（可选，JSON格式字符串）
     */
    private String transportConfig;

    /**
     * 请求超时时间（可选，单位秒，默认180）
     */
    private Integer timeout;

    /**
     * 状态（0:禁用,1:启用）
     */
    private Integer status;
}
