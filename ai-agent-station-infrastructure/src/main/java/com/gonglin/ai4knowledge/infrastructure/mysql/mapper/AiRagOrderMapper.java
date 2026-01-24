package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiRagOrder;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AiRagOrderMapper {

    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiRagOrder aiRagOrder);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiRagOrder aiRagOrder);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByRagId(AiRagOrder aiRagOrder);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiRagOrder aiRagOrder);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByRagId(AiRagOrder aiRagOrder);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiRagOrder> query(AiRagOrder aiRagOrder);
}
