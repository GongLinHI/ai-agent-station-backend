package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.PermissionMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RolePermissionMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserRoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Permission;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.RolePermission;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.UserRole;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class RoleApplicationService implements IRoleApplicationService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role createRole(Role role) {
        log.info("createRole request: {}", role);
        if (role == null) {
            throw new IllegalArgumentException("role cannot be null");
        }
        if (role.getRoleCode() == null || role.getRoleCode().isBlank()) {
            throw new IllegalArgumentException("roleCode is required");
        }
        if (role.getRoleName() == null || role.getRoleName().isBlank()) {
            throw new IllegalArgumentException("roleName is required");
        }
        if (role.getRoleId() == null || role.getRoleId().isBlank()) {
            role.setRoleId(UUID.randomUUID().toString());
        }
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        role.setIsDeleted(0);
        roleMapper.insert(role);
        log.info("createRole success, roleId: {}", role.getRoleId());
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role updateRoleById(Role role) {
        log.info("updateRoleById request: {}", role);
        if (role == null || role.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        Role existing = queryRoleById(role.getId());
        Role update = Role.builder()
                .id(role.getId())
                .roleId(role.getRoleId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .status(role.getStatus())
                .build();
        roleMapper.updateById(update);
        log.info("updateRoleById success, id: {}", existing.getId());
        return update;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleById(Long id) {
        log.info("deleteRoleById request: {}", id);
        Role existing = queryRoleById(id);
        String roleId = existing.getRoleId();
        rolePermissionMapper.deleteByRoleId(RolePermission.builder().roleId(roleId).build());
        userRoleMapper.deleteByRoleId(UserRole.builder().roleId(roleId).build());
        roleMapper.deleteById(Role.builder().id(id).build());
        log.info("deleteRoleById success, id: {}", id);
    }

    @Override
    public Role queryRoleById(Long id) {
        log.info("queryRoleById request: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id is required");
        }
        List<Role> result = roleMapper.query(Role.builder().id(id).isDeleted(0).build());
        if (result == null || result.isEmpty()) {
            throw new UserException(ExceptionCode.ROLE_NOT_FOUND, "role not found: " + id);
        }
        return result.get(0);
    }

    @Override
    public List<Role> queryRoleList(Role role, Integer pageNum, Integer pageSize) {
        log.info("queryRoleList request: role={}, pageNum={}, pageSize={}", role, pageNum, pageSize);
        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);

        Role queryParam = Role.builder()
                .roleId(role.getRoleId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .status(role.getStatus())
                .isDeleted(0)
                .build();
        List<Role> result = roleMapper.query(queryParam);
        log.info("queryRoleList success, count: {}", result.size());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(String roleId, List<String> permIds) {
        log.info("assignPermissions request: roleId={}, permIds={}", roleId, permIds);
        if (roleId == null || roleId.isBlank()) {
            throw new IllegalArgumentException("roleId is required");
        }
        if (permIds == null || permIds.isEmpty()) {
            throw new IllegalArgumentException("permIds is required");
        }
        List<Role> roles = roleMapper.query(Role.builder().roleId(roleId).isDeleted(0).build());
        if (roles.isEmpty()) {
            throw new UserException(ExceptionCode.ROLE_NOT_FOUND, "role not found: " + roleId);
        }

        Set<String> uniquePermIds = new LinkedHashSet<>(permIds);
        List<Permission> permissions = permissionMapper.queryByPermIds(uniquePermIds.stream().toList());
        if (permissions == null || permissions.isEmpty() || permissions.size() < uniquePermIds.size()) {
            throw new UserException(ExceptionCode.PERMISSION_NOT_FOUND, "some permissions not found");
        }

        rolePermissionMapper.deleteByRoleId(RolePermission.builder().roleId(roleId).build());

        for (String permId : uniquePermIds) {
            if (permId == null || permId.isBlank()) {
                continue;
            }
            RolePermission existing = rolePermissionMapper.queryByRoleIdAndPermId(roleId, permId);
            if (existing == null) {
                RolePermission rolePermission = RolePermission.builder()
                        .roleId(roleId)
                        .permId(permId)
                        .isDeleted(0)
                        .build();
                rolePermissionMapper.insert(rolePermission);
            } else if (existing.getIsDeleted() != null && existing.getIsDeleted() == 1) {
                RolePermission restore = RolePermission.builder()
                        .roleId(roleId)
                        .permId(permId)
                        .isDeleted(0)
                        .deleteTime(null)
                        .build();
                rolePermissionMapper.updateByRoleIdAndPermId(restore);
            }
        }
        log.info("assignPermissions success, roleId={}, permCount={}", roleId, uniquePermIds.size());
    }
}
