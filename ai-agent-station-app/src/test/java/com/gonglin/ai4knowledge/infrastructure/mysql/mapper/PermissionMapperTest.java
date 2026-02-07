package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.Permission;
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
public class PermissionMapperTest {

    @Autowired
    private PermissionMapper permissionMapper;

    private Permission testPermission;

    @BeforeEach
    public void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        testPermission = Permission.builder()
                .permId("perm-" + suffix)
                .permCode("perm_code_" + suffix)
                .permName("权限-" + suffix)
                .description("desc-" + suffix)
                .status(1)
                .isDeleted(0)
                .build();
    }

    private Permission queryById(Long id) {
        List<Permission> result = permissionMapper.query(Permission.builder().id(id).isDeleted(0).build());
        return result.isEmpty() ? null : result.get(0);
    }

    @Test
    public void testInsertAndQuery() {
        Integer result = permissionMapper.insert(testPermission);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testPermission.getId());

        Permission queried = queryById(testPermission.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testPermission.getPermCode(), queried.getPermCode());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
    }

    @Test
    public void testUpdateById() {
        permissionMapper.insert(testPermission);
        Long id = testPermission.getId();

        Permission update = Permission.builder()
                .id(id)
                .permName("新权限名")
                .status(0)
                .build();
        permissionMapper.updateById(update);

        Permission queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("新权限名", queried.getPermName());
        Assertions.assertEquals(0, queried.getStatus());
    }

    @Test
    public void testSoftDelete() {
        permissionMapper.insert(testPermission);
        Long id = testPermission.getId();

        permissionMapper.deleteById(Permission.builder().id(id).build());
        Permission queried = queryById(id);
        Assertions.assertNull(queried);
    }

    @Test
    public void testUniquePermCode() {
        permissionMapper.insert(testPermission);
        Permission duplicate = Permission.builder()
                .permId("perm-" + UUID.randomUUID())
                .permCode(testPermission.getPermCode())
                .permName("duplicate")
                .status(1)
                .isDeleted(0)
                .build();
        Assertions.assertThrows(Exception.class, () -> permissionMapper.insert(duplicate));
    }
}
