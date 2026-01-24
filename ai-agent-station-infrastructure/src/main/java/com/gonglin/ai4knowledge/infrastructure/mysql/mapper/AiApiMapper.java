package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiApiMapper {
    /**
     * 新增API配置
     *
     * @param aiApi API配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiApi aiApi);

    /**
     * 根据数据库ID删除API配置
     *
     * @param aiApi API配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiApi aiApi);

    /**
     * 根据API业务ID删除API配置
     *
     * @param aiApi API配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByApiId(AiApi aiApi);


    /**
     * 根据数据库ID更新API配置
     *
     * @param aiApi API配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiApi aiApi);

    /**
     * 根据API业务ID更新API配置
     *
     * @param aiApi API配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByApiId(AiApi aiApi);



    /**
     * 分页模糊查询API配置列表
     *
     * @param aiApi 查询条件对象
     * @return AI客户端API配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiApi> query(AiApi aiApi);
}
