package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClient;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiClientMapper {

    /**
     * 新增客户端配置
     *
     * @param aiClient 客户端配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiClient aiClient);

    /**
     * 根据数据库ID删除客户端配置
     *
     * @param aiClient 客户端配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiClient aiClient);

    /**
     * 根据客户端业务ID删除客户端配置
     *
     * @param aiClient 客户端配置对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByClientId(AiClient aiClient);

    /**
     * 根据数据库ID更新客户端配置
     *
     * @param aiClient 客户端配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiClient aiClient);

    /**
     * 根据客户端业务ID更新客户端配置
     *
     * @param aiClient 客户端配置POJO对象
     * @return 影响行数
     */
    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByClientId(AiClient aiClient);

    /**
     * 模糊查询客户端配置列表
     *
     * @param aiClient 查询条件对象
     * @return 客户端配置列表
     */
    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiClient> query(AiClient aiClient);
}
