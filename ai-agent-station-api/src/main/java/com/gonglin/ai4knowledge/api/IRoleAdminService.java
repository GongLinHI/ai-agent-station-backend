package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.RoleAssignPermissionsRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.RoleQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.RoleRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.RoleResponseDTO;

import java.util.List;

public interface IRoleAdminService {

    Response<Boolean> createRole(RoleRequestDTO request);

    Response<Boolean> updateRoleById(RoleRequestDTO request);

    Response<Boolean> deleteRoleById(Long id);

    Response<RoleResponseDTO> queryRoleById(Long id);

    Response<List<RoleResponseDTO>> queryRoleList(RoleQueryRequestDTO request);

    Response<Boolean> assignPermissions(RoleAssignPermissionsRequestDTO request);
}
