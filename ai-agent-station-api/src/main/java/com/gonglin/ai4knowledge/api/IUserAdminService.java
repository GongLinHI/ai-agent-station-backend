package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.UserAssignRolesRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.UserResponseDTO;

import java.util.List;

public interface IUserAdminService {

    Response<Boolean> createUser(UserRequestDTO request);

    Response<Boolean> updateUserById(UserRequestDTO request);

    Response<Boolean> deleteUserById(Long id);

    Response<UserResponseDTO> queryUserById(Long id);

    Response<List<UserResponseDTO>> queryUserList(UserQueryRequestDTO request);

    Response<Boolean> assignRoles(UserAssignRolesRequestDTO request);
}
