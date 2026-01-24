package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModelToolConfig;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiModelToolConfigMapper {

    /**
     * 新增模型-工具关联配置
     *
     * @param aiModelToolConfig 模型-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiModelToolConfig aiModelToolConfig);

    /**
     * 根据数据库ID删除模型-工具关联配置
     *
     * @param aiModelToolConfig 模型-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiModelToolConfig aiModelToolConfig);

    /**
     * 根据业务组合ID删除模型-工具关联配置
     *
     * @param aiModelToolConfig 模型-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByModelIdAndToolId(AiModelToolConfig aiModelToolConfig);

    /**
     * 根据模型ID删除模型-工具关联配置
     *
     * @param aiModelToolConfig 模型-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByModelId(AiModelToolConfig aiModelToolConfig);

    /**
     * 根据工具ID删除模型-工具关联配置
     *
     * @param aiModelToolConfig 模型-工具关联配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByToolId(AiModelToolConfig aiModelToolConfig);

    /**
     * 根据数据库ID更新模型-工具关联配置
     *
     * @param aiModelToolConfig 模型-工具关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiModelToolConfig aiModelToolConfig);

    /**
     * 根据业务组合ID更新模型-工具关联配置
     *
     * @param aiModelToolConfig 模型-工具关联配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByModelIdAndToolId(AiModelToolConfig aiModelToolConfig);

    /**
     * 根据数据库ID查询模型-工具关联配置
     *
     * @param id 主键ID
     * @return 模型-工具关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiModelToolConfig queryById(@Param("id") Integer id);

    /**
     * 根据业务组合ID查询模型-工具关联配置
     *
     * @param modelId 模型ID
     * @param toolId  工具ID
     * @return 模型-工具关联配置对象
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    AiModelToolConfig queryByModelIdAndToolId(@Param("modelId") String modelId,
                                                   @Param("toolId") String toolId);

    /**
     * 查询所有模型-工具关联配置
     *
     * @return 模型-工具关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiModelToolConfig> queryAll();

    /**
     * 根据模型ID查询模型-工具关联配置列表
     *
     * @param modelId 模型ID
     * @return 模型-工具关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiModelToolConfig> queryByModelId(@Param("modelId") String modelId);

    /**
     * 根据工具ID查询模型-工具关联配置列表
     *
     * @param toolId 工具ID
     * @return 模型-工具关联配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiModelToolConfig> queryByToolId(@Param("toolId") String toolId);
}
