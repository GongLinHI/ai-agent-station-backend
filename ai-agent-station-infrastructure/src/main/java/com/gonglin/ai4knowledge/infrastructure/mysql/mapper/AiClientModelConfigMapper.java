package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientModelConfig;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiClientModelConfigMapper {

    /**
     * 新增客户端-模型关联配置
     *
     * @param aiClientModelConfig 客户端-模型关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiClientModelConfig aiClientModelConfig);

    /**
     * 根据数据库ID删除客户端-模型关联配置
     *
     * @param aiClientModelConfig 客户端-模型关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiClientModelConfig aiClientModelConfig);

    /**
     * 根据业务组合ID删除客户端-模型关联配置
     *
     * @param aiClientModelConfig 客户端-模型关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientIdAndModelId(AiClientModelConfig aiClientModelConfig);

    /**
     * 根据客户端ID删除客户端-模型关联配置
     *
     * @param aiClientModelConfig 客户端-模型关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientId(AiClientModelConfig aiClientModelConfig);

    /**
     * 根据模型ID删除客户端-模型关联配置
     *
     * @param aiClientModelConfig 客户端-模型关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByModelId(AiClientModelConfig aiClientModelConfig);

    /**
     * 根据数据库ID更新客户端-模型关联配置
     *
     * @param aiClientModelConfig 客户端-模型关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiClientModelConfig aiClientModelConfig);

    /**
     * 根据业务组合ID更新客户端-模型关联配置
     *
     * @param aiClientModelConfig 客户端-模型关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByClientIdAndModelId(AiClientModelConfig aiClientModelConfig);

    /**
     * 根据数据库ID查询客户端-模型关联配置
     *
     * @param id 主键ID
     * @return 客户端-模型关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiClientModelConfig queryById(@Param("id") Long id);

    /**
     * 根据业务组合ID查询客户端-模型关联配置
     *
     * @param clientId 客户端ID
     * @param modelId  模型ID
     * @return 客户端-模型关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiClientModelConfig queryByClientIdAndModelId(@Param("clientId") String clientId,
                                                   @Param("modelId") String modelId);

    /**
     * 查询所有客户端-模型关联配置
     *
     * @return 客户端-模型关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientModelConfig> queryAll();

    /**
     * 根据客户端ID查询客户端-模型关联配置列表
     *
     * @param clientId 客户端ID
     * @return 客户端-模型关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientModelConfig> queryByClientId(@Param("clientId") String clientId);

    /**
     * 根据模型ID查询客户端-模型关联配置列表
     *
     * @param modelId 模型ID
     * @return 客户端-模型关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClientModelConfig> queryByModelId(@Param("modelId") String modelId);
}
