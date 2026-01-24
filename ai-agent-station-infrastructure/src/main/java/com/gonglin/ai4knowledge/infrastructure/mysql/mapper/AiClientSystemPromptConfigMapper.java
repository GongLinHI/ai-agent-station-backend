package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientSystemPromptConfig;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiClientSystemPromptConfigMapper {

    /**
     * 新增客户端-系统提示词关联配置
     *
     * @param aiClientSystemPromptConfig 客户端-系统提示词关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiClientSystemPromptConfig aiClientSystemPromptConfig);

    /**
     * 根据数据库ID删除客户端-系统提示词关联配置
     *
     * @param aiClientSystemPromptConfig 客户端-系统提示词关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiClientSystemPromptConfig aiClientSystemPromptConfig);

    /**
     * 根据业务组合ID删除客户端-系统提示词关联配置
     *
     * @param aiClientSystemPromptConfig 客户端-系统提示词关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientIdAndSystemPromptId(AiClientSystemPromptConfig aiClientSystemPromptConfig);

    /**
     * 根据客户端ID删除客户端-系统提示词关联配置
     *
     * @param aiClientSystemPromptConfig 客户端-系统提示词关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientId(AiClientSystemPromptConfig aiClientSystemPromptConfig);

    /**
     * 根据系统提示词ID删除客户端-系统提示词关联配置
     *
     * @param aiClientSystemPromptConfig 客户端-系统提示词关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteBySystemPromptId(AiClientSystemPromptConfig aiClientSystemPromptConfig);

    /**
     * 根据数据库ID更新客户端-系统提示词关联配置
     *
     * @param aiClientSystemPromptConfig 客户端-系统提示词关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiClientSystemPromptConfig aiClientSystemPromptConfig);

    /**
     * 根据业务组合ID更新客户端-系统提示词关联配置
     *
     * @param aiClientSystemPromptConfig 客户端-系统提示词关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByClientIdAndSystemPromptId(AiClientSystemPromptConfig aiClientSystemPromptConfig);

    /**
     * 根据数据库ID查询客户端-系统提示词关联配置
     *
     * @param id 主键ID
     * @return 客户端-系统提示词关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiClientSystemPromptConfig queryById(@Param("id") Long id);

    /**
     * 根据业务组合ID查询客户端-系统提示词关联配置
     *
     * @param clientId       客户端ID
     * @param systemPromptId 系统提示词ID
     * @return 客户端-系统提示词关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiClientSystemPromptConfig queryByClientIdAndSystemPromptId(@Param("clientId") String clientId,
                                                                @Param("systemPromptId") String systemPromptId);

    /**
     * 查询所有客户端-系统提示词关联配置
     *
     * @return 客户端-系统提示词关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientSystemPromptConfig> queryAll();

    /**
     * 根据客户端ID查询客户端-系统提示词关联配置列表
     *
     * @param clientId 客户端ID
     * @return 客户端-系统提示词关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientSystemPromptConfig> queryByClientId(@Param("clientId") String clientId);

    /**
     * 根据系统提示词ID查询客户端-系统提示词关联配置列表
     *
     * @param systemPromptId 系统提示词ID
     * @return 客户端-系统提示词关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientSystemPromptConfig> queryBySystemPromptId(@Param("systemPromptId") String systemPromptId);
}
