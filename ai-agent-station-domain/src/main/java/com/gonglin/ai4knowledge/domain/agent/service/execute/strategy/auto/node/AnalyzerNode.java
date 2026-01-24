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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@Service
// Step 1: Analyzer Node
public class AnalyzerNode extends AbstractExecuteSupport {

    public static final String RESULT_KEY = "analysisResult";
    private final ReactorNode reactorNode; // Step 4
    private final ExecutorNode executorNode; // Step 2

    @Autowired
    public AnalyzerNode(ReactorNode reactorNode,
                        ExecutorNode executorNode) {
        this.reactorNode = reactorNode;
        this.executorNode = executorNode;
    }

    @Override
    public String proceed(ExecuteCommand command, ExecuteDynamicContext context) {
        context.setStep(context.getStep() + 1);
        return super.proceed(command, context);
    }

    @Override
    protected String doApply(ExecuteCommand command,
                             ExecuteDynamicContext context) throws Exception {
        log.info("\n🎯 === 执行第 {} 步 ===", context.getStep());
        WorkflowStep step = context.getStep(ClientType.ANALYZER);
        if (step == null) {
            log.error("第 {} 步骤，{} 阶段未配置！", context.getStep(), this.getClass().getSimpleName());
            return null;
        }
        log.info("\n📊 阶段1: 任务状态分析");
        Map<String, Object> variableMap = Map.of(
                "userMessage", command.getUserMessage(),
                "step", context.getStep(),
                "maxStep", context.getMaxStep(),
                "stepSummaryList", getStepSummaryList(context),
                "currentTask", context.getCurrentTask()
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
        String analysisResult = this.getContentMap(context, chatResponse).get("answer");
        if (StringUtils.isEmpty(analysisResult)) {
            log.error("{} 步骤未返回结果！", this.getClass().getSimpleName());
            return null;
        }
        context.setValue(RESULT_KEY, analysisResult);
        parseResult(context);
        // 检查是否已完成
        if (analysisResult.contains("任务状态: COMPLETED") ||
                analysisResult.contains("完成度评估: 100%")) {
            context.setFinished(true);
            log.info("✅ 任务分析显示已完成！");
        }

        return router(command, context);
    }

    @Override
    public StrategyHandler<ExecuteCommand, ExecuteDynamicContext, String>
    get(ExecuteCommand command, ExecuteDynamicContext context) throws Exception {
        if (context.isFinished() || context.getStep() >= context.getMaxStep()) {
            return reactorNode;
        }
        return executorNode;
    }


    //    private void parseResult(ExecuteDynamicContext context) {
//        int step = context.getStep();
//        String analysisResult = context.getValue(RESULT_KEY);
//        log.info("\n📊 === 第 {} 步分析结果 ===", step);
//
//        String[] lines = analysisResult.split("\n");
//        String currentSection = "";
//
//        for (String line : lines) {
//            line = line.trim();
//            if (line.isEmpty()) continue;
//
//            if (line.contains("任务状态分析:")) {
//                currentSection = "status";
//                log.info("\n🎯 任务状态分析:");
//                continue;
//            } else if (line.contains("执行历史评估:")) {
//                currentSection = "history";
//                log.info("\n📈 执行历史评估:");
//                continue;
//            } else if (line.contains("下一步策略:")) {
//                currentSection = "strategy";
//                log.info("\n🚀 下一步策略:");
//                continue;
//            } else if (line.contains("完成度评估:")) {
//                currentSection = "progress";
//                String progress = line.substring(line.indexOf(":") + 1).trim();
//                log.info("\n📊 完成度评估: {}", progress);
//                continue;
//            } else if (line.contains("任务状态:")) {
//                currentSection = "task_status";
//                String status = line.substring(line.indexOf(":") + 1).trim();
//                if (status.equals("COMPLETED")) {
//                    log.info("\n✅ 任务状态: 已完成");
//                } else {
//                    log.info("\n🔄 任务状态: 继续执行");
//                }
//                continue;
//            }
//
//            switch (currentSection) {
//                case "status":
//                    log.info("   📋 {}", line);
//                    break;
//                case "history":
//                    log.info("   📊 {}", line);
//                    break;
//                case "strategy":
//                    log.info("   🎯 {}", line);
//                    break;
//                default:
//                    log.info("   📝 {}", line);
//                    break;
//            }
//        }
//    }
    private void parseResult(ExecuteDynamicContext context) {
        int step = context.getStep();
        String analysisResult = context.getValue(RESULT_KEY);
        String sessionId = context.getSessionId();

        log.info("\n📊 === 第 {} 步分析结果 ===", step);

        String[] lines = analysisResult.split("\n");
        String currentSection = "";
        StringBuilder sectionContent = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("任务状态分析:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(context, currentSection, sectionContent.toString());
                currentSection = "analysis_status";
                sectionContent = new StringBuilder();
                log.info("\n🎯 任务状态分析:");
                continue;
            } else if (line.contains("执行历史评估:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(context, currentSection, sectionContent.toString());
                currentSection = "analysis_history";
                sectionContent = new StringBuilder();
                log.info("\n📈 执行历史评估:");
                continue;
            } else if (line.contains("下一步策略:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(context, currentSection, sectionContent.toString());
                currentSection = "analysis_strategy";
                sectionContent = new StringBuilder();
                log.info("\n🚀 下一步策略:");
                continue;
            } else if (line.contains("完成度评估:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(context, currentSection, sectionContent.toString());
                currentSection = "analysis_progress";
                sectionContent = new StringBuilder();
                String progress = line.substring(line.indexOf(":") + 1).trim();
                log.info("\n📊 完成度评估: {}", progress);
                sectionContent.append(line).append("\n");
                continue;
            } else if (line.contains("任务状态:")) {
                // 发送上一个section的内容
                sendAnalysisSubResult(context, currentSection, sectionContent.toString());
                currentSection = "analysis_task_status";
                sectionContent = new StringBuilder();
                String status = line.substring(line.indexOf(":") + 1).trim();
                if (status.equals("COMPLETED")) {
                    log.info("\n✅ 任务状态: 已完成");
                } else {
                    log.info("\n🔄 任务状态: 继续执行");
                }
                sectionContent.append(line).append("\n");
                continue;
            }

            // 收集当前section的内容
            if (!currentSection.isEmpty()) {
                sectionContent.append(line).append("\n");
                switch (currentSection) {
                    case "analysis_status":
                        log.info("   📋 {}", line);
                        break;
                    case "analysis_history":
                        log.info("   📊 {}", line);
                        break;
                    case "analysis_strategy":
                        log.info("   🎯 {}", line);
                        break;
                    default:
                        log.info("   📝 {}", line);
                        break;
                }
            }
        }

        // 发送最后一个section的内容
        sendAnalysisSubResult(context, currentSection, sectionContent.toString());
    }

    /**
     * 发送分析阶段细分结果到流式输出
     */
    private void sendAnalysisSubResult(ExecuteDynamicContext dynamicContext,
                                       String subType, String content) {
        if (!subType.isEmpty() && !content.isEmpty()) {
            AutoAgentExecuteResult result = AutoAgentExecuteResult.createAnalysisSubResult(
                    dynamicContext.getStep(), subType, content, dynamicContext.getSessionId());
            sendSseResult(dynamicContext, result);
        }
    }
}
