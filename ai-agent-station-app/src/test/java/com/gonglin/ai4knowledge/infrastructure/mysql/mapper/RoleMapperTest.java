package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Role;
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
public class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    private Role testRole;

    @BeforeEach
    public void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        testRole = Role.builder()
                .roleId("role-" + suffix)
                .roleCode("role_code_" + suffix)
                .roleName("角色-" + suffix)
                .description("desc-" + suffix)
                .status(1)
                .isDeleted(0)
                .build();
    }

    private Role queryById(Long id) {
        List<Role> result = roleMapper.query(Role.builder().id(id).isDeleted(0).build());
        return result.isEmpty() ? null : result.get(0);
    }

    @Test
    public void testInsertAndQuery() {
        Integer result = roleMapper.insert(testRole);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testRole.getId());

        Role queried = queryById(testRole.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testRole.getRoleCode(), queried.getRoleCode());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
    }

    @Test
    public void testUpdateById() {
        roleMapper.insert(testRole);
        Long id = testRole.getId();

        Role update = Role.builder()
                .id(id)
                .roleName("新角色名")
                .status(0)
                .build();
        roleMapper.updateById(update);

        Role queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("新角色名", queried.getRoleName());
        Assertions.assertEquals(0, queried.getStatus());
    }

    @Test
    public void testSoftDelete() {
        roleMapper.insert(testRole);
        Long id = testRole.getId();

        roleMapper.deleteById(Role.builder().id(id).build());
        Role queried = queryById(id);
        Assertions.assertNull(queried);
    }

    @Test
    public void testUniqueRoleCode() {
        roleMapper.insert(testRole);
        Role duplicate = Role.builder()
                .roleId("role-" + UUID.randomUUID())
                .roleCode(testRole.getRoleCode())
                .roleName("duplicate")
                .status(1)
                .isDeleted(0)
                .build();
        Assertions.assertThrows(Exception.class, () -> roleMapper.insert(duplicate));
    }
}
