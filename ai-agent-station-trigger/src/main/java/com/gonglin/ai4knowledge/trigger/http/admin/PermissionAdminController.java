package com.gonglin.ai4knowledge.trigger.http.admin;

import com.gonglin.ai4knowledge.api.IPermissionAdminService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.PermissionQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.PermissionRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.PermissionResponseDTO;
import com.gonglin.ai4knowledge.application.IPermissionApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Permission;
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
@RequestMapping("/admin/permission")
public class PermissionAdminController implements IPermissionAdminService {

    @Autowired
    private IPermissionApplicationService permissionApplicationService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createPermission(@RequestBody PermissionRequestDTO request) {
        log.info("createPermission request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getPermCode())) {
            throw new IllegalArgumentException("permCode is required");
        }
        if (StringUtils.isBlank(request.getPermName())) {
            throw new IllegalArgumentException("permName is required");
        }

        Permission permission = Permission.builder()
                .permId(request.getPermId())
                .permCode(request.getPermCode())
                .permName(request.getPermName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
        permissionApplicationService.createPermission(permission);
        return Response.success(true);
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updatePermissionById(@RequestBody PermissionRequestDTO request) {
        log.info("updatePermissionById request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (request.getId() == null) {
            throw new IllegalArgumentException("id is required");
        }

        Permission permission = Permission.builder()
                .id(request.getId())
                .permId(request.getPermId())
                .permCode(request.getPermCode())
                .permName(request.getPermName())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
        permissionApplicationService.updatePermissionById(permission);
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deletePermissionById(@PathVariable Long id) {
        log.info("deletePermissionById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        permissionApplicationService.deletePermissionById(id);
        return Response.success(true);
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<PermissionResponseDTO> queryPermissionById(@PathVariable Long id) {
        log.info("queryPermissionById request: id={}", id);
        Objects.requireNonNull(id, "id cannot be null");
        Permission permission = permissionApplicationService.queryPermissionById(id);
        return Response.success(toPermissionResponseDTO(permission));
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<PermissionResponseDTO>> queryPermissionList(@RequestBody PermissionQueryRequestDTO request) {
        log.info("queryPermissionList request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");

        Permission queryCondition = Permission.builder()
                .permCode(request.getPermCode())
                .permName(request.getPermName())
                .status(request.getStatus())
                .build();
        List<Permission> permissions = permissionApplicationService.queryPermissionList(
                queryCondition, request.getPageNum(), request.getPageSize());
        List<PermissionResponseDTO> responseList = permissions.stream()
                .map(this::toPermissionResponseDTO)
                .collect(Collectors.toList());
        return Response.success(responseList);
    }

    private PermissionResponseDTO toPermissionResponseDTO(Permission permission) {
        return PermissionResponseDTO.builder()
                .id(permission.getId())
                .permId(permission.getPermId())
                .permCode(permission.getPermCode())
                .permName(permission.getPermName())
                .description(permission.getDescription())
                .status(permission.getStatus())
                .createTime(permission.getCreateTime())
                .updateTime(permission.getUpdateTime())
                .build();
    }
}
