package com.gonglin.ai4knowledge.domain.agent.service.assemble;

import com.gonglin.ai4knowledge.domain.agent.model.aggregate.AiAgentEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.AiAgentEnum;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.repository.IAiAgentRepository;
import com.gonglin.ai4knowledge.domain.agent.service.assemble.factory.DefaultArmoryStrategyFactory;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.ArmoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ArmoryService implements IArmoryService {

    private final IAiAgentRepository repository;
    private final DefaultArmoryStrategyFactory factory;

    @Autowired
    public ArmoryService(IAiAgentRepository repository,
                         DefaultArmoryStrategyFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    /**
     * 查询所有可用的智能体列表（状态为启用）
     *
     * @return 可用的智能体实体列表
     */
    @Override
    public List<AiAgentEntity> queryAvailableAgents() {
        log.info("查询可用的智能体列表");
        List<AiAgentEntity> agents = repository.queryAiAgentEntityByStatus(1);
        log.info("查询到{}个可用智能体", agents.size());
        return agents;
    }

    /**
     * 根据装配命令执行装配操作
     *
     * @param command 装配命令对象，包含commandType和commandIdList（不能为空）
     * @throws NullPointerException 当command、commandType或commandIdList为空时抛出
     * @throws ArmoryException      当装配失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void armoryByCommand(ArmoryCommand command) {
        log.info("开始装配，commandType: {}, commandIdList: {}", command.getCommandType(), command.getCommandIdList());
        Objects.requireNonNull(command, "command 不能为空");
        Objects.requireNonNull(command.getCommandType(), "commandType 不能为空");
        Objects.requireNonNull(command.getCommandIdList(), "commandIdList 不能为空");
        ArmoryDynamicContext context = new ArmoryDynamicContext();
        try {
            factory.getDefaultStrategyHandler().apply(command, context);
            log.info("装配成功，commandType: {}, commandIdList: {}", command.getCommandType(),
                     command.getCommandIdList());
        } catch (Exception e) {
            log.error("AI {}自动装配失败，commandIdList: {}", command.getCommandType(), command.getCommandIdList(), e);
            throw new ArmoryException(ExceptionCode.ARMORY_FAILED,
                                      String.format("AI %s自动装配失败", command.getCommandType()), e);
        }
    }

    /**
     * 根据智能体ID列表执行装配操作
     *
     * @param aiAgentIds 智能体ID列表（不能为空）
     * @throws NullPointerException 当aiAgentIds为空时抛出
     * @throws ArmoryException      当装配失败时抛出
     */
    @Override
    public void armoryByAiAgentIds(List<String> aiAgentIds) {
        log.info("开始装配智能体，aiAgentIds: {}", aiAgentIds);
        Objects.requireNonNull(aiAgentIds, "aiAgentIds 不能为空");
        if (aiAgentIds.isEmpty()) {
            log.info("aiAgentIds 为空，无需进行自动装配");
            return;
        }
        ArmoryCommand command = ArmoryCommand.builder()
                .commandIdList(aiAgentIds)
                .commandType(AiAgentEnum.AI_AGENT.getCommand())
                .build();
        armoryByCommand(command);
    }

    /**
     * 根据客户端ID列表执行装配操作
     *
     * @param aiClientIds 客户端ID列表（不能为空）
     * @throws NullPointerException 当aiClientIds为空时抛出
     * @throws ArmoryException      当装配失败时抛出
     */
    @Override
    public void armoryByAiClientIds(List<String> aiClientIds) {
        log.info("开始装配客户端，aiClientIds: {}", aiClientIds);
        Objects.requireNonNull(aiClientIds, "aiClientIds 不能为空");
        if (aiClientIds.isEmpty()) {
            log.info("aiClientIds 为空，无需进行自动装配");
            return;
        }
        ArmoryCommand command = ArmoryCommand.builder()
                .commandIdList(aiClientIds)
                .commandType(AiAgentEnum.AI_CLIENT.getCommand())
                .build();
        armoryByCommand(command);
    }

    /**
     * 根据模型ID列表执行装配操作
     *
     * @param aiModelIds 模型ID列表（不能为空）
     * @throws NullPointerException 当aiModelIds为空时抛出
     * @throws ArmoryException      当装配失败时抛出
     */
    @Override
    public void armoryByAiModelIds(List<String> aiModelIds) {
        log.info("开始装配模型，aiModelIds: {}", aiModelIds);
        Objects.requireNonNull(aiModelIds, "aiModelIds 不能为空");
        if (aiModelIds.isEmpty()) {
            log.info("aiModelIds 为空，无需进行自动装配");
            return;
        }
        ArmoryCommand command = ArmoryCommand.builder()
                .commandIdList(aiModelIds)
                .commandType(AiAgentEnum.AI_CHAT_MODEL.getCommand())
                .build();
        armoryByCommand(command);
    }

    /**
     * 装配所有可用的智能体
     *
     * @throws ArmoryException 当装配失败时抛出
     */
    @Override
    public void armorAllAvailableAgents() {
        log.info("开始装配所有可用的智能体");
        List<String> agentIds = queryAvailableAgents().stream()
                .map(AiAgentEntity::getAgentId)
                .filter(Objects::nonNull)
                .distinct().toList();

        if (agentIds.isEmpty()) {
            log.info("没有可用的AI Agent进行自动装配");
            return;
        }
        ArmoryCommand command = ArmoryCommand.builder()
                .commandIdList(agentIds)
                .commandType(AiAgentEnum.AI_AGENT.getCommand())
                .build();
        armoryByCommand(command);
    }
}
