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
// Step 2: Executor Node
public class ExecutorNode extends AbstractExecuteSupport {
    public static final String RESULT_KEY = "executionResult";
    private final SupervisorNode supervisorNode; // Step 3

    @Autowired
    public ExecutorNode(SupervisorNode supervisorNode) {
        this.supervisorNode = supervisorNode;
    }

    @Override
    protected String doApply(ExecuteCommand command, ExecuteDynamicContext context) throws Exception {
        log.info("\n⚡ 阶段2: 精准任务执行");
        WorkflowStep step = context.getStep(ClientType.EXECUTOR);
        if (step == null) {
            log.error("第 {} 步骤，{} 阶段未配置！", context.getStep(), this.getClass().getSimpleName());
            return null;
        }
        String analysisResult = context.getValue(AnalyzerNode.RESULT_KEY);
        Map<String, Object> variableMap = Map.of(
                "userMessage", command.getUserMessage(),
                "analysisResult", analysisResult
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
        return router(command, context);
    }

//    private void parseResult(ExecuteDynamicContext context) {
//        Integer step = context.getStep();
//        String executionResult = context.getValue(RESULT_KEY);
//        log.info("\n⚡ === 第 {} 步执行结果 ===", step);
//
//        String[] lines = executionResult.split("\n");
//        String currentSection = "";
//
//        for (String line : lines) {
//            line = line.trim();
//            if (line.isEmpty()) continue;
//
//            if (line.contains("执行目标:")) {
//                currentSection = "target";
//                log.info("\n🎯 执行目标:");
//                continue;
//            } else if (line.contains("执行过程:")) {
//                currentSection = "process";
//                log.info("\n🔧 执行过程:");
//                continue;
//            } else if (line.contains("执行结果:")) {
//                currentSection = "result";
//                log.info("\n📈 执行结果:");
//                continue;
//            } else if (line.contains("质量检查:")) {
//                currentSection = "quality";
//                log.info("\n🔍 质量检查:");
//                continue;
//            }
//
//            switch (currentSection) {
//                case "target":
//                    log.info("   🎯 {}", line);
//                    break;
//                case "process":
//                    log.info("   ⚙️ {}", line);
//                    break;
//                case "result":
//                    log.info("   📊 {}", line);
//                    break;
//                case "quality":
//                    log.info("   ✅ {}", line);
//                    break;
//                default:
//                    log.info("   📝 {}", line);
//                    break;
//            }
//        }
//    }

    @Override
    public StrategyHandler<ExecuteCommand, ExecuteDynamicContext, String>
    get(ExecuteCommand command, ExecuteDynamicContext context) throws Exception {
        return supervisorNode;
    }

    private void parseResult(ExecuteDynamicContext context) {
        int step = context.getStep();
        String executionResult = context.getValue(RESULT_KEY);
        log.info("\n⚡ === 第 {} 步执行结果 ===", step);

        String[] lines = executionResult.split("\n");
        String currentSection = "";
        StringBuilder sectionContent = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("执行目标:")) {
                // 发送上一个section的内容
                sendExecutionSubResult(context, currentSection, sectionContent.toString());
                currentSection = "execution_target";
                sectionContent = new StringBuilder();
                log.info("\n🎯 执行目标:");
                continue;
            } else if (line.contains("执行过程:")) {
                // 发送上一个section的内容
                sendExecutionSubResult(context, currentSection, sectionContent.toString());
                currentSection = "execution_process";
                sectionContent = new StringBuilder();
                log.info("\n🔧 执行过程:");
                continue;
            } else if (line.contains("执行结果:")) {
                // 发送上一个section的内容
                sendExecutionSubResult(context, currentSection, sectionContent.toString());
                currentSection = "execution_result";
                sectionContent = new StringBuilder();
                log.info("\n📈 执行结果:");
                continue;
            } else if (line.contains("质量检查:")) {
                // 发送上一个section的内容
                sendExecutionSubResult(context, currentSection, sectionContent.toString());
                currentSection = "execution_quality";
                sectionContent = new StringBuilder();
                log.info("\n🔍 质量检查:");
                continue;
            }

            // 收集当前section的内容
            if (!currentSection.isEmpty()) {
                sectionContent.append(line).append("\n");
                switch (currentSection) {
                    case "execution_target":
                        log.info("   🎯 {}", line);
                        break;
                    case "execution_process":
                        log.info("   ⚙️ {}", line);
                        break;
                    case "execution_result":
                        log.info("   📊 {}", line);
                        break;
                    case "execution_quality":
                        log.info("   ✅ {}", line);
                        break;
                    default:
                        log.info("   📝 {}", line);
                        break;
                }
            }
        }

        // 发送最后一个section的内容
        sendExecutionSubResult(context, currentSection, sectionContent.toString());

    }

    private void sendExecutionSubResult(ExecuteDynamicContext dynamicContext, String subType, String content) {
        // 抽取的通用判断逻辑
        if (!subType.isEmpty() && !content.isEmpty()) {
            AutoAgentExecuteResult result = AutoAgentExecuteResult.createExecutionSubResult(
                    dynamicContext.getStep(), subType, content, dynamicContext.getSessionId());
            sendSseResult(dynamicContext, result);
        }
    }

}
