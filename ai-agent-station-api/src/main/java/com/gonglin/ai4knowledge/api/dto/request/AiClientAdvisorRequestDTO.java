package com.gonglin.ai4knowledge.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientAdvisorRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（更新时使用，创建时为空）
     */
    private Long id;

    /**
     * 顾问ID（业务用，非空字符串，最大长度64）
     */
    private String advisorId;

    /**
     * 顾问名称（非空字符串，最大长度50）
     */
    private String advisorName;

    /**
     * 描述（可选，最大长度255）
     */
    private String description;

    /**
     * 顾问类型（非空字符串，可选值：promptChatMemory/questionAnswer/simpleLogger等，最大长度50）
     */
    private String advisorType;

    /**
     * 扩展参数配置（可选，JSON格式字符串）
     */
    private String extParam;

    /**
     * 状态（0:禁用,1:启用）
     */
    private Integer status;
}
