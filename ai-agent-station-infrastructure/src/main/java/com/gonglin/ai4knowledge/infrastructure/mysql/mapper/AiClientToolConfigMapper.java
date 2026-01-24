package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientToolConfig;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiClientToolConfigMapper {

    /**
     * 新增客户端-工具关联配置
     *
     * @param aiClientToolConfig 客户端-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiClientToolConfig aiClientToolConfig);

    /**
     * 根据数据库ID删除客户端-工具关联配置
     *
     * @param aiClientToolConfig 客户端-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiClientToolConfig aiClientToolConfig);

    /**
     * 根据业务组合ID删除客户端-工具关联配置
     *
     * @param aiClientToolConfig 客户端-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientIdAndToolId(AiClientToolConfig aiClientToolConfig);

    /**
     * 根据客户端ID删除客户端-工具关联配置
     *
     * @param aiClientToolConfig 客户端-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientId(AiClientToolConfig aiClientToolConfig);

    /**
     * 根据工具ID删除客户端-工具关联配置
     *
     * @param aiClientToolConfig 客户端-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByToolId(AiClientToolConfig aiClientToolConfig);

    /**
     * 根据数据库ID更新客户端-工具关联配置
     *
     * @param aiClientToolConfig 客户端-工具关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiClientToolConfig aiClientToolConfig);

    /**
     * 根据业务组合ID更新客户端-工具关联配置
     *
     * @param aiClientToolConfig 客户端-工具关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByClientIdAndToolId(AiClientToolConfig aiClientToolConfig);

    /**
     * 根据数据库ID查询客户端-工具关联配置
     *
     * @param id 主键ID
     * @return 客户端-工具关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiClientToolConfig queryById(@Param("id") Long id);

    /**
     * 根据业务组合ID查询客户端-工具关联配置
     *
     * @param clientId 客户端ID
     * @param toolId   工具ID
     * @return 客户端-工具关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiClientToolConfig queryByClientIdAndToolId(@Param("clientId") String clientId,
                                                   @Param("toolId") String toolId);

    /**
     * 查询所有客户端-工具关联配置
     *
     * @return 客户端-工具关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientToolConfig> queryAll();

    /**
     * 根据客户端ID查询客户端-工具关联配置列表
     *
     * @param clientId 客户端ID
     * @return 客户端-工具关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientToolConfig> queryByClientId(@Param("clientId") String clientId);

    /**
     * 根据工具ID查询客户端-工具关联配置列表
     *
     * @param toolId 工具ID
     * @return 客户端-工具关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientToolConfig> queryByToolId(@Param("toolId") String toolId);
}
