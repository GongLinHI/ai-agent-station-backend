package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IUserAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.UserAssignRolesRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.UserResponseDTO;
import com.gonglin.ai4knowledge.application.IUserApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/user")
public class UserAdminController implements IUserAdminService {

    @Autowired
    private IUserApplicationService userApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createUser(@RequestBody UserRequestDTO request) {
        log.info("createUser request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getUsername())) {
            throw new IllegalArgumentException("username is required");
        }
        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("password is required");
        }

        User user = User.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status(request.getStatus())
                .build();
        userApplicationService.createUser(user, request.getPassword());
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateUserById(@RequestBody UserRequestDTO request) {
        log.info("updateUserById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        User user = User.builder()
                .id(request.getId())
                .userId(request.getUserId())
                .username(request.getUsername())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status(request.getStatus())
                .build();
        userApplicationService.updateUserById(user, request.getPassword());
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteUserById(@PathVariable Long id) {
        log.info("deleteUserById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        userApplicationService.deleteUserById(id);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<UserResponseDTO> queryUserById(@PathVariable Long id) {
        log.info("queryUserById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        User user = userApplicationService.queryUserById(id);
        return Response.success(toUserResponseDTO(user));
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<UserResponseDTO>> queryUserList(@RequestBody UserQueryRequestDTO request) {
        log.info("queryUserList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        User queryCondition = User.builder()
                .username(request.getUsername())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status(request.getStatus())
                .build();
        List<User> users = userApplicationService.queryUserList(
                queryCondition, request.getPageNum(), request.getPageSize());
        List<UserResponseDTO> responseList = users.stream()
                .map(this::toUserResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseList);
    }

    @Override
    @PostMapping("/assign-roles")
    public Response<Boolean> assignRoles(@RequestBody UserAssignRolesRequestDTO request) {
        log.info("assignRoles request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getUserId())) {
            throw new IllegalArgumentException("userId is required");
        }
        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("roleIds is required");
        }
        userApplicationService.assignRoles(request.getUserId(), request.getRoleIds());
        return Response.success(true);
    }

    private UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .lastLoginTime(user.getLastLoginTime())
                .lastLoginIp(user.getLastLoginIp())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .build();
    }
}
