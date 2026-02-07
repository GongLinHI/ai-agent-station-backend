package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.PermissionMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RolePermissionMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Permission;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.RolePermission;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PermissionApplicationService implements IPermissionApplicationService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission createPermission(Permission permission) {
        log.info("createPermission request: {}", permission);
        if (permission == null) {
            throw new IllegalArgumentException("permission cannot be null");
        }
        if (permission.getPermCode() == null || permission.getPermCode().isBlank()) {
            throw new IllegalArgumentException("permCode is required");
        }
        if (permission.getPermName() == null || permission.getPermName().isBlank()) {
            throw new IllegalArgumentException("permName is required");
        }
        if (permission.getPermId() == null || permission.getPermId().isBlank()) {
            permission.setPermId(UUID.randomUUID().toString());
        }
        if (permission.getStatus() == null) {
            permission.setStatus(1);
        }
        permission.setIsDeleted(0);
        permissionMapper.insert(permission);
        log.info("createPermission success, permId: {}", permission.getPermId());
        return permission;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Permission updatePermissionById(Permission permission) {
        log.info("updatePermissionById request: {}", permission);
        if (permission == null || permission.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }
        Permission existing = queryPermissionById(permission.getId());
        Permission update = Permission.builder()
                .id(permission.getId())
                .permId(permission.getPermId())
                .permCode(permission.getPermCode())
                .permName(permission.getPermName())
                .description(permission.getDescription())
                .status(permission.getStatus())
                .build();
        permissionMapper.updateById(update);
        log.info("updatePermissionById success, id: {}", existing.getId());
        return update;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermissionById(Long id) {
        log.info("deletePermissionById request: {}", id);
        Permission existing = queryPermissionById(id);
        rolePermissionMapper.deleteByPermId(RolePermission.builder().permId(existing.getPermId()).build());
        permissionMapper.deleteById(Permission.builder().id(id).build());
        log.info("deletePermissionById success, id: {}", id);
    }

    @Override
    public Permission queryPermissionById(Long id) {
        log.info("queryPermissionById request: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id is required");
        }
        List<Permission> result = permissionMapper.query(Permission.builder().id(id).isDeleted(0).build());
        if (result == null || result.isEmpty()) {
            throw new UserException(ExceptionCode.PERMISSION_NOT_FOUND, "permission not found: " + id);
        }
        return result.get(0);
    }

    @Override
    public List<Permission> queryPermissionList(Permission permission, Integer pageNum, Integer pageSize) {
        log.info("queryPermissionList request: permission={}, pageNum={}, pageSize={}", permission, pageNum, pageSize);
        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);

        Permission queryParam = Permission.builder()
                .permId(permission.getPermId())
                .permCode(permission.getPermCode())
                .permName(permission.getPermName())
                .status(permission.getStatus())
                .isDeleted(0)
                .build();
        List<Permission> result = permissionMapper.query(queryParam);
        log.info("queryPermissionList success, count: {}", result.size());
        return result;
    }
}
