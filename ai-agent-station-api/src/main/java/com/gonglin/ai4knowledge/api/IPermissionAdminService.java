package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.PermissionQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.PermissionRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.PermissionResponseDTO;

import java.util.List;

public interface IPermissionAdminService {

    Response<Boolean> createPermission(PermissionRequestDTO request);

    Response<Boolean> updatePermissionById(PermissionRequestDTO request);

    Response<Boolean> deletePermissionById(Long id);

    Response<PermissionResponseDTO> queryPermissionById(Long id);

    Response<List<PermissionResponseDTO>> queryPermissionList(PermissionQueryRequestDTO request);
}
