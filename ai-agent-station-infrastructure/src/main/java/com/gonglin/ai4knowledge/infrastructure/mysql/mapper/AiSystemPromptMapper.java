package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiSystemPromptMapper {

    /**
     * 新增系统提示词配置
     *
     * @param aiSystemPrompt 系统提示词配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiSystemPrompt aiSystemPrompt);

    /**
     * 根据数据库ID删除系统提示词配置
     *
     * @param aiSystemPrompt 系统提示词配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiSystemPrompt aiSystemPrompt);

    /**
     * 根据提示词业务ID删除系统提示词配置
     *
     * @param aiSystemPrompt 系统提示词配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByPromptId(AiSystemPrompt aiSystemPrompt);

    /**
     * 根据数据库ID更新系统提示词配置
     *
     * @param aiSystemPrompt 系统提示词配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiSystemPrompt aiSystemPrompt);

    /**
     * 根据提示词业务ID更新系统提示词配置
     *
     * @param aiSystemPrompt 系统提示词配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByPromptId(AiSystemPrompt aiSystemPrompt);



    /**
     * 通用查询方法
     *
     * @param aiSystemPrompt 查询条件对象
     * @return 系统提示词配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiSystemPrompt> query(AiSystemPrompt aiSystemPrompt);
}
