package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.User;
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
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    public void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        testUser = User.builder()
                .userId("user-" + suffix)
                .username("test_user_" + suffix)
                .passwordHash("hash-" + suffix)
                .nickname("昵称" + suffix)
                .email("test" + suffix + "@example.com")
                .phone("1380000" + suffix.substring(0, 4))
                .status(1)
                .isDeleted(0)
                .build();
    }

    private User queryById(Long id) {
        List<User> result = userMapper.query(User.builder().id(id).isDeleted(0).build());
        return result.isEmpty() ? null : result.get(0);
    }

    @Test
    public void testInsertAndQuery() {
        Integer result = userMapper.insert(testUser);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testUser.getId());

        User queried = queryById(testUser.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testUser.getUsername(), queried.getUsername());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
    }

    @Test
    public void testUpdateById() {
        userMapper.insert(testUser);
        Long id = testUser.getId();

        User update = User.builder()
                .id(id)
                .nickname("新昵称")
                .status(0)
                .build();
        userMapper.updateById(update);

        User queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("新昵称", queried.getNickname());
        Assertions.assertEquals(0, queried.getStatus());
    }

    @Test
    public void testSoftDelete() {
        userMapper.insert(testUser);
        Long id = testUser.getId();

        userMapper.deleteById(User.builder().id(id).build());
        User queried = queryById(id);
        Assertions.assertNull(queried);
    }

    @Test
    public void testUniqueUsername() {
        userMapper.insert(testUser);
        User duplicate = User.builder()
                .userId("user-" + UUID.randomUUID())
                .username(testUser.getUsername())
                .passwordHash("hash-dup")
                .status(1)
                .isDeleted(0)
                .build();
        Assertions.assertThrows(Exception.class, () -> userMapper.insert(duplicate));
    }
}
