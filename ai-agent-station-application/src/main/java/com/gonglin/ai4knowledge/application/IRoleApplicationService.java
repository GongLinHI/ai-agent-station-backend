package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;

import java.util.List;

public interface IRoleApplicationService {

    Role createRole(Role role);

    Role updateRoleById(Role role);

    void deleteRoleById(Long id);

    Role queryRoleById(Long id);

    List<Role> queryRoleList(Role role, Integer pageNum, Integer pageSize);

    void assignPermissions(String roleId, List<String> permIds);
}
