package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    @DatabaseAction(DatabaseActionType.INSERT)
    Integer insert(User user);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteById(User user);

    @DatabaseAction(DatabaseActionType.SOFT_DELETE)
    Integer deleteByUserId(User user);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateById(User user);

    @DatabaseAction(DatabaseActionType.UPDATE)
    Integer updateByUserId(User user);

    @DatabaseAction(DatabaseActionType.SELECT)
    List<User> query(User user);

    @DatabaseAction(DatabaseActionType.SELECT)
    User queryByUsername(@Param("username") String username);
}
