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
public class AutoAiAgentRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * AI智能体ID（必填，非空字符串）
     */
    private String aiAgentId;

    /**
     * 用户消息（必填，非空字符串）
     */
    private String userMessage;

    /**
     * 会话ID（必填，非空字符串，用于关联整个执行过程）
     */
    private String sessionId;

    /**
     * 最大执行步数（可选，正整数，默认值为10）
     */
    private Integer maxStep;

}