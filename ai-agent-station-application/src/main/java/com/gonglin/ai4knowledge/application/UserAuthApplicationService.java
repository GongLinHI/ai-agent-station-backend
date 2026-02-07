package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.PermissionMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RolePermissionMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserRoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Permission;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.RolePermission;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.UserRole;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class UserAuthApplicationService implements IUserAuthApplicationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(User user, String rawPassword) {
        log.info("register user request: {}", user);
        Objects.requireNonNull(user, "user cannot be null");
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("password is required");
        }

        User existing = userMapper.queryByUsername(user.getUsername());
        if (existing != null) {
            throw new UserException(ExceptionCode.USERNAME_EXISTS,
                                    "username already exists: " + user.getUsername());
        }

        if (user.getUserId() == null || user.getUserId().isBlank()) {
            user.setUserId(UUID.randomUUID().toString());
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setIsDeleted(0);
        userMapper.insert(user);

        Role defaultRole = roleMapper.queryByRoleCode("user");
        if (defaultRole == null
                || (defaultRole.getIsDeleted() != null && defaultRole.getIsDeleted() == 1)
                || (defaultRole.getStatus() != null && defaultRole.getStatus() == 0)) {
            throw new UserException(ExceptionCode.ROLE_NOT_FOUND, "default role 'user' not found");
        }

        UserRole userRole = UserRole.builder()
                .userId(user.getUserId())
                .roleId(defaultRole.getRoleId())
                .isDeleted(0)
                .build();
        userRoleMapper.insert(userRole);
        log.info("register user success, userId: {}", user.getUserId());
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User login(String username, String rawPassword, String loginIp) {
        log.info("login request: username={}", username);
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("password is required");
        }

        User user = userMapper.queryByUsername(username);
        if (user == null) {
            throw new UserException(ExceptionCode.USER_NOT_FOUND, "user not found: " + username);
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new UserException(ExceptionCode.USER_DISABLED, "user is disabled: " + username);
        }
        System.out.println("rawPassword = " + rawPassword);
        System.out.println(user);
        System.out.println("passwordEncoder.encode(rawPassword) = " + passwordEncoder.encode(rawPassword));
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new UserException(ExceptionCode.PASSWORD_ERROR, "password mismatch");
        }

        User update = User.builder()
                .id(user.getId())
                .lastLoginTime(LocalDateTime.now())
                .lastLoginIp(loginIp)
                .build();
        userMapper.updateById(update);

        user.setLastLoginTime(update.getLastLoginTime());
        user.setLastLoginIp(update.getLastLoginIp());
        log.info("login success: userId={}", user.getUserId());
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String userId, String rawPassword) {
        log.info("cancel user request: userId={}", userId);
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("password is required");
        }

        User user = queryUserByUserId(userId);
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new UserException(ExceptionCode.PASSWORD_ERROR, "password mismatch");
        }

        userMapper.deleteByUserId(User.builder().userId(userId).build());
        userRoleMapper.deleteByUserId(UserRole.builder().userId(userId).build());
        log.info("cancel user success: userId={}", userId);
    }

    @Override
    public User queryUserByUserId(String userId) {
        log.info("queryUserByUserId request: userId={}", userId);
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
        List<User> result = userMapper.query(User.builder().userId(userId).isDeleted(0).build());
        if (result == null || result.isEmpty()) {
            throw new UserException(ExceptionCode.USER_NOT_FOUND, "user not found: " + userId);
        }
        return result.get(0);
    }

    @Override
    public List<String> queryRoleCodesByUserId(String userId) {
        log.info("queryRoleCodesByUserId request: userId={}", userId);
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
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
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Override
    public List<String> queryPermissionCodesByUserId(String userId) {
        log.info("queryPermissionCodesByUserId request: userId={}", userId);
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
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
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
