package com.gonglin.ai4knowledge.application;

import com.github.pagehelper.PageHelper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserRoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.UserRole;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class UserApplicationService implements IUserApplicationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createUser(User user, String rawPassword) {
        log.info("createUser request: {}", user);
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
        log.info("createUser success, userId: {}", user.getUserId());
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUserById(User user, String rawPassword) {
        log.info("updateUserById request: {}", user);
        Objects.requireNonNull(user, "user cannot be null");
        if (user.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        User existing = queryUserById(user.getId());
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            User dup = userMapper.queryByUsername(user.getUsername());
            if (dup != null && !dup.getId().equals(existing.getId())) {
                throw new UserException(ExceptionCode.USERNAME_EXISTS,
                                        "username already exists: " + user.getUsername());
            }
        }
        if (user.getUserId() != null && !user.getUserId().isBlank()) {
            List<User> dupUsers = userMapper.query(User.builder().userId(user.getUserId()).isDeleted(0).build());
            if (!dupUsers.isEmpty() && !dupUsers.get(0).getId().equals(existing.getId())) {
                throw new UserException(ExceptionCode.USERNAME_EXISTS,
                                        "userId already exists: " + user.getUserId());
            }
        }

        User update = User.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .build();
        if (rawPassword != null && !rawPassword.isBlank()) {
            update.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        userMapper.updateById(update);
        log.info("updateUserById success, id: {}", user.getId());
        return update;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserById(Long id) {
        log.info("deleteUserById request: {}", id);
        User existing = queryUserById(id);
        userRoleMapper.deleteByUserId(UserRole.builder().userId(existing.getUserId()).build());
        userMapper.deleteById(User.builder().id(id).build());
        log.info("deleteUserById success, id: {}", id);
    }

    @Override
    public User queryUserById(Long id) {
        log.info("queryUserById request: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("id is required");
        }
        List<User> result = userMapper.query(User.builder().id(id).isDeleted(0).build());
        if (result == null || result.isEmpty()) {
            throw new UserException(ExceptionCode.USER_NOT_FOUND, "user not found: " + id);
        }
        return result.get(0);
    }

    @Override
    public List<User> queryUserList(User user, Integer pageNum, Integer pageSize) {
        log.info("queryUserList request: user={}, pageNum={}, pageSize={}", user, pageNum, pageSize);
        int actualPageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        int actualPageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
        PageHelper.startPage(actualPageNum, actualPageSize);

        User queryParam = User.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .isDeleted(0)
                .build();
        List<User> result = userMapper.query(queryParam);
        log.info("queryUserList success, count: {}", result.size());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(String userId, List<String> roleIds) {
        log.info("assignRoles request: userId={}, roleIds={}", userId, roleIds);
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("roleIds is required");
        }
        List<User> users = userMapper.query(User.builder().userId(userId).isDeleted(0).build());
        if (users.isEmpty()) {
            throw new UserException(ExceptionCode.USER_NOT_FOUND, "user not found: " + userId);
        }

        Set<String> uniqueRoleIds = new LinkedHashSet<>(roleIds);
        List<Role> roles = roleMapper.queryByRoleIds(uniqueRoleIds.stream().toList());
        if (roles == null || roles.isEmpty() || roles.size() < uniqueRoleIds.size()) {
            throw new UserException(ExceptionCode.ROLE_NOT_FOUND, "some roles not found");
        }

        userRoleMapper.deleteByUserId(UserRole.builder().userId(userId).build());

        for (String roleId : uniqueRoleIds) {
            if (roleId == null || roleId.isBlank()) {
                continue;
            }
            UserRole existing = userRoleMapper.queryByUserIdAndRoleId(userId, roleId);
            if (existing == null) {
                UserRole userRole = UserRole.builder()
                        .userId(userId)
                        .roleId(roleId)
                        .isDeleted(0)
                        .build();
                userRoleMapper.insert(userRole);
            } else if (existing.getIsDeleted() != null && existing.getIsDeleted() == 1) {
                UserRole restore = UserRole.builder()
                        .userId(userId)
                        .roleId(roleId)
                        .isDeleted(0)
                        .deleteTime(null)
                        .build();
                userRoleMapper.updateByUserIdAndRoleId(restore);
            }
        }
        log.info("assignRoles success: userId={}, roleCount={}", userId, uniqueRoleIds.size());
    }
}
