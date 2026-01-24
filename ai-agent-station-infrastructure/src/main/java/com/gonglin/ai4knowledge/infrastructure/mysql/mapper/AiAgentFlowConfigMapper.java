package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiAgentFlowConfigMapper {
    /**
     * 新增智能体-客户端关联配置
     *
     * @param aiAgentFlowConfig 智能体-客户端关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 根据数据库ID删除智能体-客户端关联配置
     *
     * @param aiAgentFlowConfig 智能体-客户端关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 根据业务组合ID删除智能体-客户端关联配置
     *
     * @param aiAgentFlowConfig 智能体-客户端关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByAgentIdAndClientIdAndSequence(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 根据智能体ID删除智能体-客户端关联配置
     *
     * @param aiAgentFlowConfig 智能体-客户端关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByAgentId(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 根据客户端ID删除智能体-客户端关联配置
     *
     * @param aiAgentFlowConfig 智能体-客户端关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientId(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 根据数据库ID更新智能体-客户端关联配置
     *
     * @param aiAgentFlowConfig 智能体-客户端关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 根据业务组合ID更新智能体-客户端关联配置
     *
     * @param aiAgentFlowConfig 智能体-客户端关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByAgentIdAndClientIdAndSequence(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 根据数据库ID查询智能体-客户端关联配置
     *
     * @param id 主键ID
     * @return 智能体-客户端关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiAgentFlowConfig queryById(@Param("id") Long id);

    /**
     * 根据业务组合ID查询智能体-客户端关联配置
     *
     * @param agentId  智能体ID
     * @param clientId 客户端ID
     * @param sequence 序列号
     * @return 智能体-客户端关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiAgentFlowConfig queryByAgentIdAndClientIdAndSequence(@Param("agentId") String agentId,
                                                           @Param("clientId") String clientId,
                                                           @Param("sequence") Integer sequence);

    /**
     * 查询所有智能体-客户端关联配置
     *
     * @return 智能体-客户端关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAgentFlowConfig> queryAll();

    /**
     * 根据智能体ID查询智能体-客户端关联配置列表
     *
     * @param agentId 智能体ID
     * @return 智能体-客户端关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAgentFlowConfig> queryByAgentId(@Param("agentId") String agentId);

    /**
     * 根据客户端ID查询智能体-客户端关联配置列表
     *
     * @param clientId 客户端ID
     * @return 智能体-客户端关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAgentFlowConfig> queryByClientId(@Param("clientId") String clientId);
}
