package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.UserRole;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(UserRole userRole);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(UserRole userRole);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByUserId(UserRole userRole);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByRoleId(UserRole userRole);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByUserIdAndRoleId(UserRole userRole);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(UserRole userRole);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByUserIdAndRoleId(UserRole userRole);

    @DatabaseAction(DatabaseActionType.SELECT)
    UserRole queryById(@Param("id") Long id);

    @DatabaseAction(DatabaseActionType.SELECT)
    UserRole queryByUserIdAndRoleId(@Param("userId") String userId, @Param("roleId") String roleId);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<UserRole> queryAll();

    @DatabaseAction(DatabaseActionType.SELECT)
    List<UserRole> queryByUserId(@Param("userId") String userId);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<UserRole> queryByRoleId(@Param("roleId") String roleId);
}
