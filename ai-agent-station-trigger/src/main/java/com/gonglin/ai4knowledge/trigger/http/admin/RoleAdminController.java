package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IRoleAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.RoleAssignPermissionsRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.RoleQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.RoleRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.RoleResponseDTO;
import com.gonglin.ai4knowledge.application.IRoleApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
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
@RequestMapping("/admin/role")
public class RoleAdminController implements IRoleAdminService {

    @Autowired
    private IRoleApplicationService roleApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createRole(@RequestBody RoleRequestDTO request) {
        log.info("createRole request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getRoleCode())) {
            throw new IllegalArgumentException("roleCode is required");
        }
        if (StringUtils.isBlank(request.getRoleName())) {
            throw new IllegalArgumentException("roleName is required");
        }

        Role role = Role.builder()
                .roleId(request.getRoleId())
                .roleCode(request.getRoleCode())
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
        roleApplicationService.createRole(role);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateRoleById(@RequestBody RoleRequestDTO request) {
        log.info("updateRoleById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        Role role = Role.builder()
                .id(request.getId())
                .roleId(request.getRoleId())
                .roleCode(request.getRoleCode())
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
        roleApplicationService.updateRoleById(role);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteRoleById(@PathVariable Long id) {
        log.info("deleteRoleById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        roleApplicationService.deleteRoleById(id);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<RoleResponseDTO> queryRoleById(@PathVariable Long id) {
        log.info("queryRoleById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        Role role = roleApplicationService.queryRoleById(id);
        return Response.success(toRoleResponseDTO(role));
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<RoleResponseDTO>> queryRoleList(@RequestBody RoleQueryRequestDTO request) {
        log.info("queryRoleList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        Role queryCondition = Role.builder()
                .roleCode(request.getRoleCode())
                .roleName(request.getRoleName())
                .status(request.getStatus())
                .build();
        List<Role> roles = roleApplicationService.queryRoleList(
                queryCondition, request.getPageNum(), request.getPageSize());
        List<RoleResponseDTO> responseList = roles.stream()
                .map(this::toRoleResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseList);
    }

    @Override
    @PostMapping("/assign-permissions")
    public Response<Boolean> assignPermissions(@RequestBody RoleAssignPermissionsRequestDTO request) {
        log.info("assignPermissions request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getRoleId())) {
            throw new IllegalArgumentException("roleId is required");
        }
        if (request.getPermIds() == null || request.getPermIds().isEmpty()) {
            throw new IllegalArgumentException("permIds is required");
        }
        roleApplicationService.assignPermissions(request.getRoleId(), request.getPermIds());
        return Response.success(true);
    }

    private RoleResponseDTO toRoleResponseDTO(Role role) {
        return RoleResponseDTO.builder()
                .id(role.getId())
                .roleId(role.getRoleId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .status(role.getStatus())
                .createTime(role.getCreateTime())
                .updateTime(role.getUpdateTime())
                .build();
    }
}
