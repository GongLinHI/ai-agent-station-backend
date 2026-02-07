package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.RoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.mapper.UserRoleMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Slf4j
public class UserAuthApplicationServiceTest {

    @Autowired
    private IUserAuthApplicationService userAuthApplicationService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Role ensureDefaultRole() {
        Role existing = roleMapper.queryByRoleCode("user");
        if (existing != null) {
            return existing;
        }
        Role role = Role.builder()
                .roleId("role-" + UUID.randomUUID())
                .roleCode("user")
                .roleName("默认用户")
                .status(1)
                .isDeleted(0)
                .build();
        roleMapper.insert(role);
        return role;
    }

    @BeforeEach
    public void setUp() {
        ensureDefaultRole();
    }

    @Test
    public void testRegisterAndDuplicate() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        User user = User.builder()
                .username("reg_user_" + suffix)
                .nickname("昵称" + suffix)
                .email("reg" + suffix + "@example.com")
                .phone("1390000" + suffix.substring(0, 4))
                .build();
        User created = userAuthApplicationService.register(user, "password123");
        Assertions.assertNotNull(created.getId());
        Assertions.assertNotNull(created.getUserId());

        User duplicate = User.builder()
                .username(user.getUsername())
                .build();
        UserException exception = Assertions.assertThrows(UserException.class,
                () -> userAuthApplicationService.register(duplicate, "password123"));
        Assertions.assertEquals(ExceptionCode.USERNAME_EXISTS.getCode(), exception.getCode());
    }

    @Test
    public void testLoginSuccessAndPasswordMismatch() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        User user = User.builder()
                .username("login_user_" + suffix)
                .nickname("昵称" + suffix)
                .build();
        userAuthApplicationService.register(user, "password123");

        UserException wrongPwd = Assertions.assertThrows(UserException.class,
                () -> userAuthApplicationService.login(user.getUsername(), "wrongpass", "127.0.0.1"));
        Assertions.assertEquals(ExceptionCode.PASSWORD_ERROR.getCode(), wrongPwd.getCode());

        User loggedIn = userAuthApplicationService.login(user.getUsername(), "password123", "127.0.0.1");
        Assertions.assertNotNull(loggedIn.getLastLoginTime());
    }

    @Test
    public void testLoginDisabledUser() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        User disabled = User.builder()
                .userId("user-" + suffix)
                .username("disabled_user_" + suffix)
                .passwordHash(passwordEncoder.encode("password123"))
                .status(0)
                .isDeleted(0)
                .build();
        userMapper.insert(disabled);

        UserException exception = Assertions.assertThrows(UserException.class,
                () -> userAuthApplicationService.login(disabled.getUsername(), "password123", "127.0.0.1"));
        Assertions.assertEquals(ExceptionCode.USER_DISABLED.getCode(), exception.getCode());
    }

    @Test
    public void testCancelUser() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        User user = User.builder()
                .username("cancel_user_" + suffix)
                .build();
        User created = userAuthApplicationService.register(user, "password123");

        userAuthApplicationService.cancel(created.getUserId(), "password123");

        List<User> users = userMapper.query(User.builder().userId(created.getUserId()).isDeleted(0).build());
        Assertions.assertTrue(users.isEmpty());
        Assertions.assertTrue(userRoleMapper.queryByUserId(created.getUserId()).isEmpty());
    }
}
