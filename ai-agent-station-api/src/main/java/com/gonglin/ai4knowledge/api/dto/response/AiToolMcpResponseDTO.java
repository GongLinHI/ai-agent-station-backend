package com.gonglin.ai4knowledge.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiToolMcpResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

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
     * 传输类型（sse/stdio）
     */
    private String transportType;

    /**
     * 传输配置（JSON格式）
     */
    private String transportConfig;

    /**
     * 请求超时时间（秒）
     */
    private Integer timeout;

    /**
     * 状态（0:禁用,1:启用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
