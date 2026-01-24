package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientAdvisorFlowConfig;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiClientAdvisorFlowConfigMapper {

    /**
     * 新增客户端-顾问关联配置
     *
     * @param aiClientAdvisorFlowConfig 客户端-顾问关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiClientAdvisorFlowConfig aiClientAdvisorFlowConfig);

    /**
     * 根据数据库ID删除客户端-顾问关联配置
     *
     * @param aiClientAdvisorFlowConfig 客户端-顾问关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiClientAdvisorFlowConfig aiClientAdvisorFlowConfig);

    /**
     * 根据业务组合ID删除客户端-顾问关联配置
     *
     * @param aiClientAdvisorFlowConfig 客户端-顾问关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientIdAndAdvisorIdAndSequence(AiClientAdvisorFlowConfig aiClientAdvisorFlowConfig);

    /**
     * 根据客户端ID删除客户端-顾问关联配置
     *
     * @param aiClientAdvisorFlowConfig 客户端-顾问关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientId(AiClientAdvisorFlowConfig aiClientAdvisorFlowConfig);

    /**
     * 根据顾问ID删除客户端-顾问关联配置
     *
     * @param aiClientAdvisorFlowConfig 客户端-顾问关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByAdvisorId(AiClientAdvisorFlowConfig aiClientAdvisorFlowConfig);

    /**
     * 根据数据库ID更新客户端-顾问关联配置
     *
     * @param aiClientAdvisorFlowConfig 客户端-顾问关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiClientAdvisorFlowConfig aiClientAdvisorFlowConfig);

    /**
     * 根据业务组合ID更新客户端-顾问关联配置
     *
     * @param aiClientAdvisorFlowConfig 客户端-顾问关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByClientIdAndAdvisorIdAndSequence(AiClientAdvisorFlowConfig aiClientAdvisorFlowConfig);

    /**
     * 根据数据库ID查询客户端-顾问关联配置
     *
     * @param id 主键ID
     * @return 客户端-顾问关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiClientAdvisorFlowConfig queryById(@Param("id") Long id);

    /**
     * 根据业务组合ID查询客户端-顾问关联配置
     *
     * @param clientId  客户端ID
     * @param advisorId 顾问ID
     * @param sequence  序列号
     * @return 客户端-顾问关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiClientAdvisorFlowConfig queryByClientIdAndAdvisorIdAndSequence(@Param("clientId") String clientId,
                                                                   @Param("advisorId") String advisorId,
                                                                   @Param("sequence") Integer sequence);

    /**
     * 查询所有客户端-顾问关联配置
     *
     * @return 客户端-顾问关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientAdvisorFlowConfig> queryAll();

    /**
     * 根据客户端ID查询客户端-顾问关联配置列表
     *
     * @param clientId 客户端ID
     * @return 客户端-顾问关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientAdvisorFlowConfig> queryByClientId(@Param("clientId") String clientId);

    /**
     * 根据顾问ID查询客户端-顾问关联配置列表
     *
     * @param advisorId 顾问ID
     * @return 客户端-顾问关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientAdvisorFlowConfig> queryByAdvisorId(@Param("advisorId") String advisorId);
}
