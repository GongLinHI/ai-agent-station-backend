package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.data.ILoadDataStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
// RootNode 职责：按照指定策略，并发加载后续步骤所需的数据
public class ArmoryDataNode extends AbstractArmorySupport {

    private final Map<String, ILoadDataStrategy> loadDataStrategyMap;
    private final ApiNode apiNode;

    @Autowired // Spring 自动将所有在容器中的 ILoadDataStrategy 实现类注入到 Map 中
    public ArmoryDataNode(Map<String, ILoadDataStrategy> loadDataStrategyMap,
                          ApiNode apiNode) {
        this.loadDataStrategyMap = loadDataStrategyMap;
        this.apiNode = apiNode;
    }

    // 加载数据的方法
    @Override
    protected void multiThread(ArmoryCommand armoryCommand,
                               ArmoryDynamicContext dynamicContext)
            throws ExecutionException, InterruptedException, TimeoutException {
        String commandType = armoryCommand.getCommandType();
        String loadDataStrategyBeanName = AiAgentEnum.getByCommand(commandType).getLoadDataStrategy();
        ILoadDataStrategy loadDataStrategy = loadDataStrategyMap.get(loadDataStrategyBeanName);
        loadDataStrategy.load(armoryCommand, dynamicContext);

    }

    // 业务处理的方法
    @Override
    protected String doApply(ArmoryCommand armoryCommand,
                             ArmoryDynamicContext dynamicContext) throws Exception {
        return this.router(armoryCommand, dynamicContext);
    }

    // 指定下一个节点(在这里可以实现更复杂的路由逻辑)
    @Override
    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> get(
            ArmoryCommand armoryCommand,
            ArmoryDynamicContext dynamicContext) throws Exception {
        return apiNode;
    }
}
