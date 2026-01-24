package com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.auto.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AutoAgentExecuteResult;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.WorkflowStep;
import com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.AbstractExecuteSupport;
import com.gonglin.ai4knowledge.types.common.ClientType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service
// Step:4 Reactor Node
public class ReactorNode extends AbstractExecuteSupport {
    public static final String RESULT_KEY = "reactorResult";

    @Override
    protected String doApply(ExecuteCommand command, ExecuteDynamicContext context) throws Exception {
        log.info("\n📊 === 执行第 {} 步 ===", context.getStep());
        log.info("\n📊 阶段4: 执行总结分析");

        WorkflowStep step = context.getStep(ClientType.REACTOR);
        if (step == null) {
            log.error("第 {} 步骤，{} 阶段未配置！", context.getStep(), this.getClass().getSimpleName());
            return null;
        }
        Map<String, Object> variableMap = Map.of(
                "userMessage", command.getUserMessage(),
                "stepSummaryList", getStepSummaryList(context)
        );
//        String userPrompt = PromptTemplate.builder()
//                .template(step.getClientStepPrompt())
//                .variables(variableMap)
//                .build().render();
        String userPrompt = templateFormat(step.getClientStepPrompt(), variableMap);
        ChatClient chatClient = step.getChatClient();
        Flux<ChatResponse> chatResponse = chatClient
                .prompt().user(userPrompt)
                .system(s -> s.param("today", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, context.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, DEFAULT_CHAT_MEMORY_RETRIEVE_SIZE))
                .stream().chatResponse();
        String result = this.getContentMap(context, chatResponse).get("answer");
        if (StringUtils.isEmpty(result)) {
            log.error("{} 步骤未返回结果！", this.getClass().getSimpleName());
            return null;
        }
        context.setValue(RESULT_KEY, result);
        parseResult(context);
        return router(command, context);
    }

    @Override
    public StrategyHandler<ExecuteCommand, ExecuteDynamicContext, String> get(ExecuteCommand command,
                                                                              ExecuteDynamicContext context) throws Exception {
        return null;
    }

    //    private void parseResult(ExecuteDynamicContext context) {
//        String reactorResult = context.getValue(RESULT_KEY);
//        log.info("🧪 反应器结果解析: \n{}", reactorResult);
//        if (context.isFinished()) {
//            log.info("✅ 任务完成状态: 已完成");
//        } else {
//            log.info("⏸️ 任务完成状态: 未完成（达到最大步数限制）");
//        }
//        log.info("\n📋 === 最终总结报告 ===");
//
//        String[] lines = reactorResult.split("\n");
//        for (String line : lines) {
//            line = line.trim();
//            if (line.isEmpty()) continue;
//
//            // 根据内容类型添加不同图标
//            if (line.contains("已完成") || line.contains("完成的工作")) {
//                log.info("✅ {}", line);
//            } else if (line.contains("未完成") || line.contains("原因")) {
//                log.info("❌ {}", line);
//            } else if (line.contains("建议") || line.contains("推荐")) {
//                log.info("💡 {}", line);
//            } else if (line.contains("评估") || line.contains("效果")) {
//                log.info("📊 {}", line);
//            } else {
//                log.info("📝 {}", line);
//            }
//        }
//    }
    //parseResult
    private void parseResult(ExecuteDynamicContext context) {
        boolean isCompleted = context.isFinished();
        String summaryResult = context.getValue(RESULT_KEY);
        String sessionId = context.getSessionId();
        log.info("\n📋 === {}任务最终总结报告 ===", isCompleted ? "已完成" : "未完成");

        String[] lines = summaryResult.split("\n");
        String currentSection = "summary_overview";
        StringBuilder sectionContent = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // 检测是否开始新的总结部分
            String newSection = detectSummarySection(line);
            if (newSection != null && !newSection.equals(currentSection)) {
                // 发送前一个部分的内容
                if (!sectionContent.isEmpty()) {
                    sendSummarySubResult(context, currentSection, sectionContent.toString());
                }
                currentSection = newSection;
                sectionContent.setLength(0);
            }

            // 收集当前部分的内容
            if (!sectionContent.isEmpty()) {
                sectionContent.append("\n");
            }
            sectionContent.append(line);

            // 根据内容类型添加不同图标
            if (line.contains("已完成") || line.contains("完成的工作")) {
                log.info("✅ {}", line);
            } else if (line.contains("未完成") || line.contains("原因")) {
                log.info("❌ {}", line);
            } else if (line.contains("建议") || line.contains("推荐")) {
                log.info("💡 {}", line);
            } else if (line.contains("评估") || line.contains("效果")) {
                log.info("📊 {}", line);
            } else {
                log.info("📝 {}", line);
            }
        }

        // 发送最后一个部分的内容
        if (!sectionContent.isEmpty()) {
            sendSummarySubResult(context, currentSection, sectionContent.toString());
        }

        // 发送完整的总结结果
        sendSummaryResult(context, summaryResult);

        // 发送完成标识
        sendCompleteResult(context);
    }

    /**
     * 检测总结部分标识
     */
    private String detectSummarySection(String content) {
        if (content.contains("已完成的工作") || content.contains("完成的工作") || content.contains("工作内容和成果")) {
            return "completed_work";
        } else if (content.contains("未完成的原因") || content.contains("未完成原因")) {
            return "incomplete_reasons";
        } else if (content.contains("关键因素") || content.contains("完成的关键因素")) {
            return "key_factors";
        } else if (content.contains("执行效率") || content.contains("执行效率和质量")) {
            return "efficiency_quality";
        } else if (content.contains("完成剩余任务的建议") || content.contains("建议") || content.contains(
                "优化建议") || content.contains("经验总结")) {
            return "suggestions";
        } else if (content.contains("整体执行效果") || content.contains("评估")) {
            return "evaluation";
        }
        return null;
    }

    /**
     * 发送总结结果到流式输出
     */
    private void sendSummaryResult(ExecuteDynamicContext dynamicContext, String summaryResult) {
        AutoAgentExecuteResult result = AutoAgentExecuteResult.createSummaryResult(
                summaryResult, dynamicContext.getSessionId());
        sendSseResult(dynamicContext, result);
    }

    /**
     * 发送总结阶段细分结果到流式输出
     */
    private void sendSummarySubResult(ExecuteDynamicContext dynamicContext, String subType, String content) {
        AutoAgentExecuteResult result = AutoAgentExecuteResult.createSummarySubResult(
                subType, content, dynamicContext.getSessionId());
        sendSseResult(dynamicContext, result);
    }

    /**
     * 发送完成标识到流式输出
     */
    private void sendCompleteResult(ExecuteDynamicContext dynamicContext) {
        AutoAgentExecuteResult result = AutoAgentExecuteResult.createCompleteResult(
                dynamicContext.getSessionId());
        sendSseResult(dynamicContext, result);
        log.info("✅ 已发送完成标识");
    }
}