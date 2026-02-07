package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.RolePermission;
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
public class RolePermissionMapperTest {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    private RolePermission testRolePermission;

    @BeforeEach
    public void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        testRolePermission = RolePermission.builder()
                .roleId("role-" + suffix)
                .permId("perm-" + suffix)
                .isDeleted(0)
                .build();
    }

    @Test
    public void testInsertAndQuery() {
        Integer result = rolePermissionMapper.insert(testRolePermission);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testRolePermission.getId());

        List<RolePermission> rolePermissions = rolePermissionMapper.queryByRoleId(testRolePermission.getRoleId());
        Assertions.assertFalse(rolePermissions.isEmpty());
        Assertions.assertEquals(testRolePermission.getPermId(), rolePermissions.get(0).getPermId());
    }

    @Test
    public void testSoftDeleteByRoleId() {
        rolePermissionMapper.insert(testRolePermission);
        rolePermissionMapper.deleteByRoleId(RolePermission.builder().roleId(testRolePermission.getRoleId()).build());

        List<RolePermission> rolePermissions = rolePermissionMapper.queryByRoleId(testRolePermission.getRoleId());
        Assertions.assertTrue(rolePermissions.isEmpty());
    }

    @Test
    public void testUniqueConstraint() {
        rolePermissionMapper.insert(testRolePermission);
        RolePermission duplicate = RolePermission.builder()
                .roleId(testRolePermission.getRoleId())
                .permId(testRolePermission.getPermId())
                .isDeleted(0)
                .build();
        Assertions.assertThrows(Exception.class, () -> rolePermissionMapper.insert(duplicate));
    }
}
