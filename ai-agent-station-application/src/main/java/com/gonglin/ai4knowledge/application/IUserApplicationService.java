package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;

import java.util.List;

public interface IUserApplicationService {

    User createUser(User user, String rawPassword);

    User updateUserById(User user, String rawPassword);

    void deleteUserById(Long id);

    User queryUserById(Long id);

    List<User> queryUserList(User user, Integer pageNum, Integer pageSize);

    void assignRoles(String userId, List<String> roleIds);
}
