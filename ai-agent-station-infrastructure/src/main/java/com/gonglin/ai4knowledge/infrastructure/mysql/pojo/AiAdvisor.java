package com.gonglin.ai4knowledge.infrastructure.mysql.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAdvisor {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 顾问ID（业务用）
     */
    private String advisorId;

    /**
     * 顾问名称
     */
    private String advisorName;

    /**
     * 描述
     */
    private String description;

    /**
     * 顾问类型(promptChatMemory/questionAnswer/simpleLogger等)
     */
    private String advisorType;

    /**
     * 扩展参数配置(json)
     */
    private String extParam;

    /**
     * 状态(0:禁用,1:启用)
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

    /**
     * 软删除(0:未删,1:已删)
     */
    @Builder.Default
    private Integer isDeleted = 0;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;
}
