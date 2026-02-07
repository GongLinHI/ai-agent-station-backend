package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;

import java.util.List;

public interface IUserAuthApplicationService {

    User register(User user, String rawPassword);

    User login(String username, String rawPassword, String loginIp);

    void cancel(String userId, String rawPassword);

    User queryUserByUserId(String userId);

    List<String> queryRoleCodesByUserId(String userId);

    List<String> queryPermissionCodesByUserId(String userId);
}
