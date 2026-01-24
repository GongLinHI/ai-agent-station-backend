package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AutoAgentExecuteResult {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * 数据类型：analysis(分析阶段), execution(执行阶段), supervision(监督阶段), summary(总结阶段), error(错误信息), complete(完成标识)
     * 细分类型：analysis_status(任务状态分析), analysis_history(执行历史评估), analysis_strategy(下一步策略), analysis_progress(完成度评估)
     * execution_target(执行目标), execution_process(执行过程), execution_result(执行结果), execution_quality(质量检查)
     * supervision_assessment(质量评估), supervision_issues(问题识别), supervision_suggestions(改进建议), supervision_score(质量评分)
     */
    private String type;

    /**
     * 子类型标识，用于前端细粒度展示
     */
    private String subType;

    /**
     * 当前步骤
     */
    private Integer step;

    /**
     * 数据内容
     */
    private String content;

    /**
     * 是否完成
     */
    private Boolean completed;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 创建分析阶段结果
     */
    public static AutoAgentExecuteResult createAnalysisResult(Integer step, String content, String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("analysis")
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建分析阶段细分结果
     */
    public static AutoAgentExecuteResult createAnalysisSubResult(Integer step, String subType, String content,
                                                                 String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("analysis")
                .subType(subType)
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建执行阶段结果
     */
    public static AutoAgentExecuteResult createExecutionResult(Integer step, String content, String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("execution")
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建执行阶段细分结果
     */
    public static AutoAgentExecuteResult createExecutionSubResult(Integer step, String subType, String content,
                                                                  String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("execution")
                .subType(subType)
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建监督阶段结果
     */
    public static AutoAgentExecuteResult createSupervisionResult(Integer step, String content, String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("supervision")
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建监督阶段细分结果
     */
    public static AutoAgentExecuteResult createSupervisionSubResult(Integer step, String subType, String content,
                                                                    String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("supervision")
                .subType(subType)
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建总结阶段细分的结果
     */
    public static AutoAgentExecuteResult createSummarySubResult(String subType, String content,
                                                                String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("summary")
                .subType(subType)
                .step(4)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建总结阶段结果
     */
    public static AutoAgentExecuteResult createSummaryResult(String content, String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("summary")
                .step(null)
                .content(content)
                .completed(true)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建错误结果
     */
    public static AutoAgentExecuteResult createErrorResult(String content, String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("error")
                .step(null)
                .content(content)
                .completed(true)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建完成标识
     */
    public static AutoAgentExecuteResult createCompleteResult(String sessionId) {
        return AutoAgentExecuteResult.builder()
                .type("complete")
                .step(null)
                .content("执行完成")
                .completed(true)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    @SneakyThrows
    public String toJson() {
        return jsonMapper.writeValueAsString(this);
    }

    public String toSSEData() {
        return "data: " + toJson() + "\n\n";
    }

    public boolean sendTo(ResponseBodyEmitter emitter) {
        try {
            emitter.send(this.toSSEData());
            return true;
        } catch (Exception e) {
            log.warn("发送[{}]{}失败,原因是{}", getSubType(), toJson(), e.getMessage());
        }
        return false;
    }

}