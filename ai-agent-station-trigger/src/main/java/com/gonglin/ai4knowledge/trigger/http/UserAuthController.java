package com.gonglin.ai4knowledge.trigger.http;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.gonglin.ai4knowledge.api.IUserAuthService;
import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.api.dto.request.UserCancelRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserLoginRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserRegisterRequestDTO;
import com.gonglin.ai4knowledge.api.dto.response.UserAuthResponseDTO;
import com.gonglin.ai4knowledge.api.dto.response.UserProfileResponseDTO;
import com.gonglin.ai4knowledge.api.dto.response.UserResponseDTO;
import com.gonglin.ai4knowledge.application.IUserAuthApplicationService;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserAuthController implements IUserAuthService {

    @Autowired
    private IUserAuthApplicationService userAuthApplicationService;

    @Override
    @PostMapping("/register")
    public Response<UserAuthResponseDTO> register(@RequestBody UserRegisterRequestDTO request) {
        log.info("register request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getUsername())) {
            throw new IllegalArgumentException("username is required");
        }
        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("password is required");
        }
        if (request.getPassword().length() < 8 || request.getPassword().length() > 64) {
            throw new IllegalArgumentException("password length must be between 8 and 64");
        }
        if (StringUtils.isBlank(request.getConfirmPassword())) {
            throw new IllegalArgumentException("confirmPassword is required");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("confirmPassword does not match password");
        }

        User user = User.builder()
                .username(request.getUsername())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .status(1)
                .build();

        User created = userAuthApplicationService.register(user, request.getPassword());
        StpUtil.login(created.getUserId());

        UserAuthResponseDTO responseDTO = buildAuthResponse(created);
        return Response.success(responseDTO);
    }

    @Override
    @PostMapping("/login")
    public Response<UserAuthResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
        log.info("login request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getUsername())) {
            throw new IllegalArgumentException("username is required");
        }
        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("password is required");
        }
        if (request.getPassword().length() < 8 || request.getPassword().length() > 64) {
            throw new IllegalArgumentException("password length must be between 8 and 64");
        }

        String loginIp = resolveClientIp();
        User user = userAuthApplicationService.login(request.getUsername(), request.getPassword(), loginIp);
        StpUtil.login(user.getUserId());

        UserAuthResponseDTO responseDTO = buildAuthResponse(user);
        return Response.success(responseDTO);
    }

    @Override
    @PostMapping("/logout")
    @SaCheckLogin
    public Response<Boolean> logout() {
        log.info("logout request");
        StpUtil.logout();
        return Response.success(true);
    }

    @Override
    @DeleteMapping("/cancel")
    @SaCheckLogin
    public Response<Boolean> cancel(@RequestBody UserCancelRequestDTO request) {
        log.info("cancel request: {}", request);
        Objects.requireNonNull(request, "request cannot be null");
        if (StringUtils.isBlank(request.getPassword())) {
            throw new IllegalArgumentException("password is required");
        }
        if (request.getPassword().length() < 8 || request.getPassword().length() > 64) {
            throw new IllegalArgumentException("password length must be between 8 and 64");
        }

        String userId = StpUtil.getLoginIdAsString();
        userAuthApplicationService.cancel(userId, request.getPassword());
        StpUtil.logout();
        return Response.success(true);
    }

    @Override
    @GetMapping("/me")
    @SaCheckLogin
    public Response<UserProfileResponseDTO> me() {
        log.info("me request");
        String userId = StpUtil.getLoginIdAsString();
        User user = userAuthApplicationService.queryUserByUserId(userId);
        List<String> roles = userAuthApplicationService.queryRoleCodesByUserId(userId);
        List<String> permissions = userAuthApplicationService.queryPermissionCodesByUserId(userId);

        UserProfileResponseDTO responseDTO = UserProfileResponseDTO.builder()
                .user(toUserResponseDTO(user))
                .roles(roles)
                .permissions(permissions)
                .build();
        return Response.success(responseDTO);
    }

    private UserAuthResponseDTO buildAuthResponse(User user) {
        return UserAuthResponseDTO.builder()
                .tokenName(StpUtil.getTokenName())
                .tokenValue(StpUtil.getTokenValue())
                .expiresIn(StpUtil.getTokenTimeout())
                .user(toUserResponseDTO(user))
                .build();
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

    @Autowired
    private HttpServletRequest httpServletRequest;

    private String resolveClientIp() {
        if (httpServletRequest == null) {
            return null;
        }
        String ip = httpServletRequest.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
