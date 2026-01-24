package com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.auto.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.AiAgentWorkFlowCache;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.factory.DefaultArmoryStrategyFactory;
import com.gonglin.ai4knowledge.domain.agent.service.execute.strategy.AbstractExecuteSupport;
import com.gonglin.ai4knowledge.types.common.ClientType;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.ArmoryException;
import com.gonglin.ai4knowledge.types.exception.ResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class DataNode extends AbstractExecuteSupport {

    private final AiAgentWorkFlowCache aiAgentWorkFlowCache;
    private final AnalyzerNode analyzerNode;
    private final DefaultArmoryStrategyFactory armoryStrategyFactory;

    @Autowired
    public DataNode(AiAgentWorkFlowCache aiAgentWorkFlowCache,
                    AnalyzerNode analyzerNode,
                    DefaultArmoryStrategyFactory armoryStrategyFactory) {
        this.aiAgentWorkFlowCache = aiAgentWorkFlowCache;
        this.analyzerNode = analyzerNode;
        this.armoryStrategyFactory = armoryStrategyFactory;
    }

    @Override
    public String proceed(ExecuteCommand command, ExecuteDynamicContext context) {
        String aiAgentId = command.getAiAgentId();
        if (!aiAgentWorkFlowCache.containsKey(aiAgentId)) {
            reload(aiAgentId);
        }
        log.info("proceed");
        ClassPathResource resource = new ClassPathResource("MCP服务器工具调用指南（优化版）.md");
        List<String> roles = List.of(ClientType.ANALYZER, ClientType.EXECUTOR, ClientType.SUPERVISOR,
                                     ClientType.REACTOR);
        for (String role : roles) {
            String stepPrompt = aiAgentWorkFlowCache.get(aiAgentId).get(role).getClientStepPrompt();
            String append = null;
            try {
                append = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ResourceException(ExceptionCode.RESOURCE_READ_FAILED, 
                    "读取MCP服务器工具调用指南失败", e);
            }
            aiAgentWorkFlowCache.get(aiAgentId).get(role).setClientStepPrompt(stepPrompt + "\n\n" + append);
        }

        return super.proceed(command, context);
    }

    private void reload(String aiAgentId) {
        ArmoryDynamicContext armoryDynamicContext = new ArmoryDynamicContext();
        ArmoryCommand armoryCommand = ArmoryCommand.builder()
                .commandType(AiAgentEnum.AI_AGENT.getCommand())
                .commandIdList(List.of(aiAgentId))
                .build();
        try {
            armoryStrategyFactory.getDefaultStrategyHandler().apply(armoryCommand, armoryDynamicContext);
        } catch (Exception e) {
            log.error("AI Agent自动预热失败", e);
            throw new ArmoryException(ExceptionCode.ARMORY_WARMUP_FAILED, 
                "AI Agent自动预热失败", e);
        }
    }

    @Override
    protected String doApply(ExecuteCommand executeCommand,
                             ExecuteDynamicContext executeDynamicContext) throws Exception {
        log.info("执行{} - {}", this.getClass().getSimpleName(), this.jsonMapper.writeValueAsString(executeCommand));
        String aiAgentId = executeCommand.getAiAgentId();

        executeDynamicContext.setWorkflowStepMap(aiAgentWorkFlowCache.get(aiAgentId));
        return router(executeCommand, executeDynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommand, ExecuteDynamicContext, String> get(ExecuteCommand executeCommand,
                                                                              ExecuteDynamicContext executeDynamicContext) throws Exception {
        return analyzerNode;
    }
}
