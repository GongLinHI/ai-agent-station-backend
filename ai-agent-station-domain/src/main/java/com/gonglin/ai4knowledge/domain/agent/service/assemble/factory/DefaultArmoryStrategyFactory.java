package com.gonglin.ai4knowledge.domain.agent.service.assemble.factory;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.node.ArmoryDataNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultArmoryStrategyFactory {


    private final ArmoryDataNode dataNode;

    @Autowired
    public DefaultArmoryStrategyFactory(ArmoryDataNode dataNode) {
        this.dataNode = dataNode;
    }

    public StrategyHandler<ArmoryCommand, ArmoryDynamicContext, String> getDefaultStrategyHandler() {
        return dataNode;
    }

}
