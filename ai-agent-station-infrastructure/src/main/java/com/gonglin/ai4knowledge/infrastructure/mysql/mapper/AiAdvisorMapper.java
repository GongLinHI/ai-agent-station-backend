package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiAdvisorMapper {

    /**
     * 新增顾问配置
     *
     * @param aiAdvisor 顾问配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiAdvisor aiAdvisor);

    /**
     * 根据数据库ID删除顾问配置
     *
     * @param aiAdvisor 顾问配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiAdvisor aiAdvisor);

    /**
     * 根据顾问业务ID删除顾问配置
     *
     * @param aiAdvisor 顾问配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByAdvisorId(AiAdvisor aiAdvisor);

    /**
     * 根据数据库ID更新顾问配置
     *
     * @param aiAdvisor 顾问配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiAdvisor aiAdvisor);

    /**
     * 根据顾问业务ID更新顾问配置
     *
     * @param aiAdvisor 顾问配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByAdvisorId(AiAdvisor aiAdvisor);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiAdvisor> query(AiAdvisor aiAdvisor);
}
