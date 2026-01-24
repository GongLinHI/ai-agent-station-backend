package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentTaskSchedule;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiAgentTaskScheduleMapper {

    /**
     * 新增智能体任务调度配置
     *
     * @param aiAgentTaskSchedule 智能体任务调度配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiAgentTaskSchedule aiAgentTaskSchedule);

    /**
     * 根据数据库ID删除智能体任务调度配置
     *
     * @param aiAgentTaskSchedule 智能体任务调度配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiAgentTaskSchedule aiAgentTaskSchedule);

    /**
     * 根据智能体ID删除智能体任务调度配置
     *
     * @param aiAgentTaskSchedule 智能体任务调度配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByAgentId(AiAgentTaskSchedule aiAgentTaskSchedule);

    /**
     * 根据数据库ID更新智能体任务调度配置
     *
     * @param aiAgentTaskSchedule 智能体任务调度配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiAgentTaskSchedule aiAgentTaskSchedule);

    /**
     * 根据数据库ID查询智能体任务调度配置
     *
     * @param id 主键ID
     * @return 智能体任务调度配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiAgentTaskSchedule queryById(@Param("id") Long id);

    /**
     * 根据智能体ID查询智能体任务调度配置列表
     *
     * @param agentId 智能体ID
     * @return 智能体任务调度配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAgentTaskSchedule> queryByAgentId(@Param("agentId") String agentId);

    /**
     * 根据状态查询智能体任务调度配置列表
     *
     * @param status 状态(0:无效,1:有效)
     * @return 智能体任务调度配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAgentTaskSchedule> queryByStatus(@Param("status") Integer status);

    /**
     * 查询所有智能体任务调度配置
     *
     * @return 智能体任务调度配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAgentTaskSchedule> queryAll();
}
