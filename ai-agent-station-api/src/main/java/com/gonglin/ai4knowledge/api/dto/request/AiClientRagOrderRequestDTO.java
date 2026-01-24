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
public class AiClientRagOrderRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（更新时使用，创建时为空）
     */
    private Long id;

    /**
     * 知识库ID（业务用，非空字符串，最大长度64）
     */
    private String ragId;

    /**
     * 知识库名称（非空字符串，最大长度50）
     */
    private String ragName;

    /**
     * 知识标签（非空字符串，最大长度50）
     */
    private String knowledgeTag;

    /**
     * 状态（0:禁用,1:启用）
     */
    private Integer status;
}
