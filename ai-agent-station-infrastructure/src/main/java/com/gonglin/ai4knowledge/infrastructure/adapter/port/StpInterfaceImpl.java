package com.gonglin.ai4knowledge.infrastructure.adapter.port;

import cn.dev33.satoken.stp.StpInterface;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.PermissionMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RolePermissionMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserRoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Permission;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.RolePermission;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String userId = String.valueOf(loginId);
        log.debug("getPermissionList loginId={}", userId);
        List<UserRole> userRoles = userRoleMapper.queryByUserId(userId);
        if (userRoles == null || userRoles.isEmpty()) {
            return List.of();
        }
        Set<String> roleIds = new LinkedHashSet<>();
        for (UserRole userRole : userRoles) {
            if (userRole.getRoleId() != null && !userRole.getRoleId().isBlank()) {
                roleIds.add(userRole.getRoleId());
            }
        }
        if (roleIds.isEmpty()) {
            return List.of();
        }
        Set<String> permIds = new LinkedHashSet<>();
        for (String roleId : roleIds) {
            List<RolePermission> rolePermissions = rolePermissionMapper.queryByRoleId(roleId);
            for (RolePermission rolePermission : rolePermissions) {
                if (rolePermission.getPermId() != null && !rolePermission.getPermId().isBlank()) {
                    permIds.add(rolePermission.getPermId());
                }
            }
        }
        if (permIds.isEmpty()) {
            return List.of();
        }
        List<Permission> permissions = permissionMapper.queryByPermIds(permIds.stream().toList());
        if (permissions == null || permissions.isEmpty()) {
            return List.of();
        }
        return permissions.stream()
                .map(Permission::getPermCode)
                .filter(code -> code != null && !code.isBlank())
                .distinct()
                .toList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String userId = String.valueOf(loginId);
        log.debug("getRoleList loginId={}", userId);
        List<UserRole> userRoles = userRoleMapper.queryByUserId(userId);
        if (userRoles == null || userRoles.isEmpty()) {
            return List.of();
        }
        Set<String> roleIds = new LinkedHashSet<>();
        for (UserRole userRole : userRoles) {
            if (userRole.getRoleId() != null && !userRole.getRoleId().isBlank()) {
                roleIds.add(userRole.getRoleId());
            }
        }
        if (roleIds.isEmpty()) {
            return List.of();
        }
        List<Role> roles = roleMapper.queryByRoleIds(roleIds.stream().toList());
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream()
                .map(Role::getRoleCode)
                .filter(code -> code != null && !code.isBlank())
                .distinct()
                .toList();
    }
}
