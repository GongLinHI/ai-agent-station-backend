package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(Role role);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(Role role);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByRoleId(Role role);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(Role role);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByRoleId(Role role);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<Role> query(Role role);

    @DatabaseAction(DatabaseActionType.SELECT)
    Role queryByRoleCode(@Param("roleCode") String roleCode);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<Role> queryByRoleIds(@Param("roleIds") List<String> roleIds);
}
