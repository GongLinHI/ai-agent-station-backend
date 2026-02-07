package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Permission;

import java.util.List;

public interface IPermissionApplicationService {

    Permission createPermission(Permission permission);

    Permission updatePermissionById(Permission permission);

    void deletePermissionById(Long id);

    Permission queryPermissionById(Long id);

    List<Permission> queryPermissionList(Permission permission, Integer pageNum, Integer pageSize);
}
