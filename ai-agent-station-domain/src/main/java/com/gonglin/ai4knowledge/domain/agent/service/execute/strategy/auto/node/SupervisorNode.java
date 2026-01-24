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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@Service
// Step:3 Supervisor Node
public class SupervisorNode extends AbstractExecuteSupport {
    public static final String RESULT_KEY = "supervisorResult";

    private final AnalyzerNode analyzerNode; // Step 1
    private final ReactorNode reactorNode;   // Step 4

    @Autowired
    public SupervisorNode(@Lazy AnalyzerNode analyzerNode,
                          ReactorNode reactorNode) {
        this.analyzerNode = analyzerNode;
        this.reactorNode = reactorNode;
    }

    @Override
    protected String doApply(ExecuteCommand command, ExecuteDynamicContext context) throws Exception {
        log.info("\n🔍 阶段3: 质量监督检查");
        WorkflowStep step = context.getStep(ClientType.SUPERVISOR);
        if (step == null) {
            log.error("第 {} 步骤，{} 阶段未配置！", context.getStep(), this.getClass().getSimpleName());
            return null;
        }
        String executionResult = context.getValue(ExecutorNode.RESULT_KEY);
        if (executionResult == null || executionResult.trim().isEmpty()) {
            log.warn("⚠️ 执行结果为空，跳过质量监督");
            return null;
        }
        Map<String, Object> variableMap = Map.of(
                "userMessage", command.getUserMessage(),
                "executionResult", executionResult
        );
//        String userPrompt = PromptTemplate.builder()
//                .template(step.getClientStepPrompt())
//                .variables(variableMap)
//                .build().render();
        String userPrompt = templateFormat(step.getClientStepPrompt(), variableMap);
        ChatClient chatClient = step.getChatClient();
        Flux<ChatResponse> chatResponse = chatClient
                .prompt().user(userPrompt)
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
        // 更新执行历史
        updateStepSummary(context);
        // 根据监督结果决定是否需要重新执行
        if (result.contains("是否通过: FAIL")) {
            log.info("❌ 质量检查未通过，需要重新执行");
            context.setCurrentTask("根据质量监督的建议重新执行任务");
        } else if (result.contains("是否通过: OPTIMIZE")) {
            log.info("🔧 质量检查建议优化，继续改进");
            context.setCurrentTask("根据质量监督的建议优化执行结果");
        } else {
            log.info("✅ 质量检查通过");
            context.setFinished(true);
        }


        return router(command, context);
    }

    @Override
    public StrategyHandler<ExecuteCommand, ExecuteDynamicContext, String>
    get(ExecuteCommand command, ExecuteDynamicContext context) throws Exception {
        if (context.getStep() >= context.getMaxStep() || context.isFinished()) {
            return reactorNode;
        } else {
            return analyzerNode;
        }
    }


//    private void parseResult(ExecuteDynamicContext context) {
//        Integer step = context.getStep();
//        String supervisionResult = context.getValue(RESULT_KEY);
//        log.info("\n🔍 === 第 {} 步质量监督结果 ===", step);
//
//        String[] lines = supervisionResult.split("\n");
//        String currentSection = "";
//
//        for (String line : lines) {
//            line = line.trim();
//            if (line.isEmpty()) continue;
//
//            if (line.contains("质量评估:")) {
//                currentSection = "assessment";
//                log.info("\n📊 质量评估:");
//                continue;
//            } else if (line.contains("问题识别:")) {
//                currentSection = "issues";
//                log.info("\n⚠️ 问题识别:");
//                continue;
//            } else if (line.contains("改进建议:")) {
//                currentSection = "suggestions";
//                log.info("\n💡 改进建议:");
//                continue;
//            } else if (line.contains("质量评分:")) {
//                currentSection = "score";
//                String score = line.substring(line.indexOf(":") + 1).trim();
//                log.info("\n📊 质量评分: {}", score);
//                continue;
//            } else if (line.contains("是否通过:")) {
//                currentSection = "pass";
//                String status = line.substring(line.indexOf(":") + 1).trim();
//                if (status.equals("PASS")) {
//                    log.info("\n✅ 检查结果: 通过");
//                } else if (status.equals("FAIL")) {
//                    log.info("\n❌ 检查结果: 未通过");
//                } else {
//                    log.info("\n🔧 检查结果: 需要优化");
//                }
//                continue;
//            }
//
//            switch (currentSection) {
//                case "assessment":
//                    log.info("   📋 {}", line);
//                    break;
//                case "issues":
//                    log.info("   ⚠️ {}", line);
//                    break;
//                case "suggestions":
//                    log.info("   💡 {}", line);
//                    break;
//                default:
//                    log.info("   📝 {}", line);
//                    break;
//            }
//        }
//    }


    private void updateStepSummary(ExecuteDynamicContext context) {
        String stepSummary = String.format("""
                                                   === 第 %d 步完整记录 ===
                                                   【分析阶段】%s
                                                   【执行阶段】%s
                                                   【监督阶段】%s
                                                   """,
                                           context.getStep(),
                                           context.getValue(AnalyzerNode.RESULT_KEY),
                                           context.getValue(ExecutorNode.RESULT_KEY),
                                           context.getValue(SupervisorNode.RESULT_KEY));
        context.getStepSummarySJ().add(stepSummary);

    }

    /**
     * 解析监督结果
     */
    private void parseResult(ExecuteDynamicContext dynamicContext) {
        int step = dynamicContext.getStep();
        String sessionId = dynamicContext.getSessionId();
        String supervisionResult = dynamicContext.getValue(RESULT_KEY);
        log.info("\n🔍 === 第 {} 步监督结果 ===", step);

        String[] lines = supervisionResult.split("\n");
        String currentSection = "";
        StringBuilder sectionContent = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("质量评估:")) {
                // 发送前一个部分的内容
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString());
                currentSection = "assessment";
                sectionContent.setLength(0);
                log.info("\n📊 质量评估:");
                continue;
            } else if (line.contains("问题识别:")) {
                // 发送前一个部分的内容
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString());
                currentSection = "issues";
                sectionContent.setLength(0);
                log.info("\n⚠️ 问题识别:");
                continue;
            } else if (line.contains("改进建议:")) {
                // 发送前一个部分的内容
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString());
                currentSection = "suggestions";
                sectionContent.setLength(0);
                log.info("\n💡 改进建议:");
                continue;
            } else if (line.contains("质量评分:")) {
                // 发送前一个部分的内容
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString());
                currentSection = "score";
                sectionContent.setLength(0);
                String score = line.substring(line.indexOf(":") + 1).trim();
                log.info("\n📊 质量评分: {}", score);
                sectionContent.append(score);
                continue;
            } else if (line.contains("是否通过:")) {
                // 发送前一个部分的内容
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString());
                currentSection = "pass";
                sectionContent.setLength(0);
                String status = line.substring(line.indexOf(":") + 1).trim();
                if (status.equals("PASS")) {
                    log.info("\n✅ 检查结果: 通过");
                } else if (status.equals("FAIL")) {
                    log.info("\n❌ 检查结果: 未通过");
                } else {
                    log.info("\n🔧 检查结果: 需要优化");
                }
                sectionContent.append(status);
                continue;
            }

            // 收集当前部分的内容
            if (!currentSection.isEmpty()) {
                if (!sectionContent.isEmpty()) {
                    sectionContent.append("\n");
                }
                sectionContent.append(line);
            }

            switch (currentSection) {
                case "assessment":
                    log.info("   📋 {}", line);
                    break;
                case "issues":
                    log.info("   ⚠️ {}", line);
                    break;
                case "suggestions":
                    log.info("   💡 {}", line);
                    break;
                default:
                    log.info("   📝 {}", line);
                    break;
            }
        }

        // 发送最后一个部分的内容
        sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString());

        // 发送完整的监督结果
        sendSupervisionResult(dynamicContext);
    }

    /**
     * 发送监督结果到流式输出
     */
    private void sendSupervisionResult(ExecuteDynamicContext dynamicContext) {
        String supervisionResult = dynamicContext.getValue(RESULT_KEY);
        String sessionId = dynamicContext.getSessionId();
        AutoAgentExecuteResult result = AutoAgentExecuteResult.createSupervisionResult(
                dynamicContext.getStep(), supervisionResult, sessionId);
        sendSseResult(dynamicContext, result);
    }

    /**
     * 发送监督子结果到流式输出（细粒度标识）
     */
    private void sendSupervisionSubResult(ExecuteDynamicContext dynamicContext, String section, String content) {
        // 抽取的通用判断逻辑
        if (!content.isEmpty() && !section.isEmpty()) {
            AutoAgentExecuteResult result = AutoAgentExecuteResult.createSupervisionSubResult(
                    dynamicContext.getStep(), section, content, dynamicContext.getSessionId());
            sendSseResult(dynamicContext, result);
        }
    }
}
