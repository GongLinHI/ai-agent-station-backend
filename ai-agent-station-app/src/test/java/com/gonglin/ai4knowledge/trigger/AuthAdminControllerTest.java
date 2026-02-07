package com.gonglin.ai4knowledge.trigger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.api.dto.request.UserLoginRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserQueryRequestDTO;
import com.gonglin.ai4knowledge.api.dto.request.UserRegisterRequestDTO;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserRoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.UserRole;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
public class AuthAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        ensureRole("user", "普通用户");
        ensureRole("admin", "管理员");
    }

    private Role ensureRole(String roleCode, String roleName) {
        Role existing = roleMapper.queryByRoleCode(roleCode);
        if (existing != null) {
            return existing;
        }
        Role role = Role.builder()
                .roleId("role-" + UUID.randomUUID())
                .roleCode(roleCode)
                .roleName(roleName)
                .status(1)
                .isDeleted(0)
                .build();
        roleMapper.insert(role);
        return role;
    }

    @Test
    public void testRegisterAndLogin() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        UserRegisterRequestDTO register = UserRegisterRequestDTO.builder()
                .username("reg_" + suffix)
                .password("password123")
                .confirmPassword("password123")
                .nickname("昵称" + suffix)
                .build();
        MvcResult regResult = mockMvc.perform(post("/auth/register")
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(register)))
                .andReturn();
        JsonNode regNode = objectMapper.readTree(regResult.getResponse().getContentAsString());
        Assertions.assertEquals("0000", regNode.get("code").asText());
        Assertions.assertTrue(regNode.get("data").get("tokenValue").asText().length() > 0);

        UserLoginRequestDTO login = UserLoginRequestDTO.builder()
                .username(register.getUsername())
                .password(register.getPassword())
                .build();
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(login)))
                .andReturn();
        JsonNode loginNode = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        Assertions.assertEquals("0000", loginNode.get("code").asText());
        Assertions.assertTrue(loginNode.get("data").get("tokenValue").asText().length() > 0);
    }

    @Test
    public void testMeRequiresLogin() throws Exception {
        MvcResult result = mockMvc.perform(get("/auth/me")).andReturn();
        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        Assertions.assertEquals(ExceptionCode.AUTH_NOT_LOGIN.getCode(), node.get("code").asText());
    }

    @Test
    public void testAdminAccessControl() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Role adminRole = ensureRole("admin", "管理员");

        UserQueryRequestDTO query = UserQueryRequestDTO.builder().build();
        MvcResult noLoginResult = mockMvc.perform(post("/admin/user/query-list")
                                                          .contentType(MediaType.APPLICATION_JSON)
                                                          .content(objectMapper.writeValueAsString(query)))
                .andReturn();
        JsonNode noLoginNode = objectMapper.readTree(noLoginResult.getResponse().getContentAsString());
        Assertions.assertEquals(ExceptionCode.AUTH_NOT_LOGIN.getCode(), noLoginNode.get("code").asText());

        User adminUser = User.builder()
                .userId("admin-user-" + suffix)
                .username("admin_user_" + suffix)
                .passwordHash(passwordEncoder.encode("password123"))
                .status(1)
                .isDeleted(0)
                .build();
        userMapper.insert(adminUser);
        userRoleMapper.insert(UserRole.builder()
                                      .userId(adminUser.getUserId())
                                      .roleId(adminRole.getRoleId())
                                      .isDeleted(0)
                                      .build());

        UserLoginRequestDTO login = UserLoginRequestDTO.builder()
                .username(adminUser.getUsername())
                .password("password123")
                .build();
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(login)))
                .andReturn();
        JsonNode loginNode = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        String adminToken = loginNode.get("data").get("tokenValue").asText();

        MvcResult okResult = mockMvc.perform(post("/admin/user/query-list")
                                                     .contentType(MediaType.APPLICATION_JSON)
                                                     .header("Authorization", "Bearer " + adminToken)
                                                     .content(objectMapper.writeValueAsString(query)))
                .andReturn();
        JsonNode okNode = objectMapper.readTree(okResult.getResponse().getContentAsString());
        Assertions.assertEquals("0000", okNode.get("code").asText());

        String userSuffix = UUID.randomUUID().toString().substring(0, 8);
        UserRegisterRequestDTO register = UserRegisterRequestDTO.builder()
                .username("user_" + userSuffix)
                .password("password123")
                .confirmPassword("password123")
                .build();
        MvcResult regResult = mockMvc.perform(post("/auth/register")
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(register)))
                .andReturn();
        JsonNode regNode = objectMapper.readTree(regResult.getResponse().getContentAsString());
        String userToken = regNode.get("data").get("tokenValue").asText();

        MvcResult forbiddenResult = mockMvc.perform(post("/admin/user/query-list")
                                                            .contentType(MediaType.APPLICATION_JSON)
                                                            .header("Authorization", "Bearer " + userToken)
                                                            .content(objectMapper.writeValueAsString(query)))
                .andReturn();
        JsonNode forbiddenNode = objectMapper.readTree(forbiddenResult.getResponse().getContentAsString());
        Assertions.assertEquals(ExceptionCode.AUTH_NO_PERMISSION.getCode(), forbiddenNode.get("code").asText());
    }
}
