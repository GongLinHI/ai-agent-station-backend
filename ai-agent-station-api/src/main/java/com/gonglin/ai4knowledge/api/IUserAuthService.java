package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.api.dto.request.UserCancelRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserLoginRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserRegisterRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.UserAuthResponseDTO;
import com.gonglin.ai4knowledge.api.dto.response.UserProfileResponseDTO;

public interface IUserAuthService {

    Response<UserAuthResponseDTO> register(UserRegisterRequestDTO request);

    Response<UserAuthResponseDTO> login(UserLoginRequestDTO request);

    Response<Boolean> logout();

    Response<Boolean> cancel(UserCancelRequestDTO request);

    Response<UserProfileResponseDTO> me();
}
