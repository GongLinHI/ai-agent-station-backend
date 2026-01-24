package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgent;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiAgentMapper {

    /**
     * 新增智能体配置
     *
     * @param aiAgent 智能体配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiAgent aiAgent);

    /**
     * 根据数据库ID删除智能体配置
     *
     * @param aiAgent 智能体配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiAgent aiAgent);

    /**
     * 根据智能体业务ID删除智能体配置
     *
     * @param aiAgent 智能体配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByAgentId(AiAgent aiAgent);

    /**
     * 根据数据库ID更新智能体配置
     *
     * @param aiAgent 智能体配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiAgent aiAgent);

    /**
     * 根据智能体业务ID更新智能体配置
     *
     * @param aiAgent 智能体配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByAgentId(AiAgent aiAgent);

    /**
     * 根据数据库ID查询智能体配置
     *
     * @param id 主键ID
     * @return 智能体配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiAgent queryById(@Param("id") Long id);

    /**
     * 根据智能体业务ID查询智能体配置
     *
     * @param agentId 智能体业务ID
     * @return 智能体配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiAgent queryByAgentId(@Param("agentId") String agentId);

    /**
     * 根据状态查询智能体配置列表
     *
     * @param status 状态(0:禁用,1:启用)
     * @return 智能体配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAgent> queryByStatus(@Param("status") Integer status);

    /**
     * 查询所有智能体配置
     *
     * @return 智能体配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAgent> queryAll();
}
