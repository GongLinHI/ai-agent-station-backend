package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Permission;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(Permission permission);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(Permission permission);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByPermId(Permission permission);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(Permission permission);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByPermId(Permission permission);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<Permission> query(Permission permission);

    @DatabaseAction(DatabaseActionType.SELECT)
    Permission queryByPermCode(@Param("permCode") String permCode);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<Permission> queryByPermIds(@Param("permIds") List<String> permIds);
}
