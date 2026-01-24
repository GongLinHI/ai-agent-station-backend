package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AiModelMapper {

    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(AiModel aiModel);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(AiModel aiModel);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByModelId(AiModel aiModel);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(AiModel aiModel);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByModelId(AiModel aiModel);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<AiModel> query(AiModel aiModel);
}
