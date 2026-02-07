package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.RolePermission;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RolePermissionMapper {

    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(RolePermission rolePermission);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(RolePermission rolePermission);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByRoleId(RolePermission rolePermission);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByPermId(RolePermission rolePermission);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByRoleIdAndPermId(RolePermission rolePermission);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(RolePermission rolePermission);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByRoleIdAndPermId(RolePermission rolePermission);

    @DatabaseAction(DatabaseActionType.SELECT)
    RolePermission queryById(@Param("id") Long id);

    @DatabaseAction(DatabaseActionType.SELECT)
    RolePermission queryByRoleIdAndPermId(@Param("roleId") String roleId, @Param("permId") String permId);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<RolePermission> queryAll();

    @DatabaseAction(DatabaseActionType.SELECT)
    List<RolePermission> queryByRoleId(@Param("roleId") String roleId);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<RolePermission> queryByPermId(@Param("permId") String permId);
}
