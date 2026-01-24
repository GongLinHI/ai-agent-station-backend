package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.data;

import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;

public interface ILoadDataStrategy {
    void load(ArmoryCommand armoryCommand,
              ArmoryDynamicContext dynamicContext);
}
