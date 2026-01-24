package com.gonglin.ai4knowledge;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ExecuteCommand;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.AiAgentWorkFlowCache;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.factory.DefaultArmoryStrategyFactory;
import com.gonglin.ai4knowledge.domain.agent.service.execute.DefaultAiAgentExecuteFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
@SpringBootTest
public class AutoAgentTest {

    @Autowired
    private DefaultArmoryStrategyFactory armoryFactory;
    @Autowired
    private DefaultAiAgentExecuteFactory executeFactory;

    @Autowired
    AiAgentWorkFlowCache cache;

    @BeforeEach
    void beforeEach() throws Exception {
        StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> handler = armoryFactory.getDefaultStrategyHandler();
        ArmoryCommand cmd = ArmoryCommand.builder().commandType("AGENT").commandIdList(List.of("agent-3")).build();
        String apply = handler.apply(cmd, new ArmoryDynamicContext());
    }

    @Test
    void valid() {
        log.info("valid{}", cache.size());
    }

    @Test
    void test() throws Exception {
        ExecuteCommand cmd = new ExecuteCommand();
        cmd.setAiAgentId("agent-3");
        cmd.setMaxStep(10);
        cmd.setUserMessage(
                "Google搜索 springboot 相关知识，生成2个章节的内容，你需要确保内容合理，难度循序渐进。" +
                        "每个章节要包括课程内容和配套示例代码，方便小白伙伴学习。你需要将各个章节整理成为一个独立的md文档，" +
                        "并写入D:\\itheima目录下，自己拟定合适的章节与文档名称。" +
                        "在任务中，你需要调用MCP工具完成Google搜索、文件的读取+写入操作。");

        executeFactory.getDefaultStrategy().execute(cmd, new SseEmitter(100L));
    }
}
