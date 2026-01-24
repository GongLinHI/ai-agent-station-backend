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
public class AiClientSystemPromptRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（更新时使用，创建时为空）
     */
    private Long id;

    /**
     * 提示词ID（业务用，非空字符串，最大长度64）
     */
    private String promptId;

    /**
     * 提示词名称（非空字符串，最大长度50）
     */
    private String promptName;

    /**
     * 提示词内容（非空字符串，文本类型）
     */
    private String promptContent;

    /**
     * 描述（可选，最大长度1024）
     */
    private String description;

    /**
     * 状态（0:禁用,1:启用）
     */
    private Integer status;
}
