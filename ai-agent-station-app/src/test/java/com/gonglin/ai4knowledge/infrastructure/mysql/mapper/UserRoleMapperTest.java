package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Slf4j
public class UserRoleMapperTest {

    @Autowired
    private UserRoleMapper userRoleMapper;

    private UserRole testUserRole;

    @BeforeEach
    public void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        testUserRole = UserRole.builder()
                .userId("user-" + suffix)
                .roleId("role-" + suffix)
                .isDeleted(0)
                .build();
    }

    @Test
    public void testInsertAndQuery() {
        Integer result = userRoleMapper.insert(testUserRole);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testUserRole.getId());

        List<UserRole> userRoles = userRoleMapper.queryByUserId(testUserRole.getUserId());
        Assertions.assertFalse(userRoles.isEmpty());
        Assertions.assertEquals(testUserRole.getRoleId(), userRoles.get(0).getRoleId());
    }

    @Test
    public void testSoftDeleteByUserId() {
        userRoleMapper.insert(testUserRole);
        userRoleMapper.deleteByUserId(UserRole.builder().userId(testUserRole.getUserId()).build());

        List<UserRole> userRoles = userRoleMapper.queryByUserId(testUserRole.getUserId());
        Assertions.assertTrue(userRoles.isEmpty());
    }

    @Test
    public void testUniqueConstraint() {
        userRoleMapper.insert(testUserRole);
        UserRole duplicate = UserRole.builder()
                .userId(testUserRole.getUserId())
                .roleId(testUserRole.getRoleId())
                .isDeleted(0)
                .build();
        Assertions.assertThrows(Exception.class, () -> userRoleMapper.insert(duplicate));
    }
}
