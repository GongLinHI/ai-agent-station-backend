package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientSystemPromptConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@Slf4j
@Transactional
public class AiClientSystemPromptConfigMapperTest {

    @Autowired
    private AiClientSystemPromptConfigMapper aiClientSystemPromptConfigMapper;

    private AiClientSystemPromptConfig testConfig;
    private java.util.List<AiClientSystemPromptConfig> allTestConfigs = new java.util.ArrayList<>();

    @BeforeEach
    public void setUp() {
        testConfig = AiClientSystemPromptConfig.builder()
                .clientId("client-100")
                .systemPromptId("prompt-100")
                .isDeleted(0)
                .build();
        allTestConfigs.clear();
    }

    @AfterEach
    public void tearDown() {
        for (AiClientSystemPromptConfig config : allTestConfigs) {
            if (config != null && config.getId() != null) {
                try {
                    aiClientSystemPromptConfigMapper.deleteById(config);
                } catch (Exception e) {
                }
            }
        }
        allTestConfigs.clear();
    }

    @Test
    public void testInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        Integer result = aiClientSystemPromptConfigMapper.insert(testConfig);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testConfig.getId());
        Assertions.assertNotNull(testConfig.getCreateTime());
        Assertions.assertNotNull(testConfig.getUpdateTime());

        Assertions.assertTrue(
                testConfig.getCreateTime().isAfter(beforeInsert.minusSeconds(1)) ||
                        testConfig.getCreateTime().isEqual(beforeInsert.minusSeconds(1)));
        Assertions.assertTrue(
                testConfig.getCreateTime().isBefore(afterInsert.plusSeconds(1)) ||
                        testConfig.getCreateTime().isEqual(afterInsert.plusSeconds(1)));

        Assertions.assertEquals(testConfig.getCreateTime(), testConfig.getUpdateTime());

        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testConfig.getId(), testConfig.getCreateTime(), testConfig.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiClientSystemPromptConfig queried = aiClientSystemPromptConfigMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(testConfig.getClientId(), queried.getClientId());
        Assertions.assertEquals(testConfig.getSystemPromptId(), queried.getSystemPromptId());
        log.info("QueryById test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByClientIdAndSystemPromptId() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String clientId = testConfig.getClientId();
        String systemPromptId = testConfig.getSystemPromptId();

        AiClientSystemPromptConfig queried = aiClientSystemPromptConfigMapper.queryByClientIdAndSystemPromptId(
                clientId, systemPromptId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(clientId, queried.getClientId());
        Assertions.assertEquals(systemPromptId, queried.getSystemPromptId());
        log.info("QueryByClientIdAndSystemPromptId test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByClientId() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();

        List<AiClientSystemPromptConfig> configs = aiClientSystemPromptConfigMapper.queryByClientId(clientId);
        Assertions.assertNotNull(configs);
        Assertions.assertEquals(1, configs.size());
        Assertions.assertEquals(testConfig.getId(), configs.get(0).getId());
        Assertions.assertEquals(clientId, configs.get(0).getClientId());
        log.info("QueryByClientId test passed. Found {} config for clientId {}", configs.size(), clientId);
    }

    @Test
    public void testQueryBySystemPromptId() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        String systemPromptId = testConfig.getSystemPromptId();

        AiClientSystemPromptConfig config2 = AiClientSystemPromptConfig.builder()
                .clientId("client-101")
                .systemPromptId(systemPromptId)
                .isDeleted(0)
                .build();
        aiClientSystemPromptConfigMapper.insert(config2);

        AiClientSystemPromptConfig config3 = AiClientSystemPromptConfig.builder()
                .clientId("client-102")
                .systemPromptId(systemPromptId)
                .isDeleted(0)
                .build();
        aiClientSystemPromptConfigMapper.insert(config3);

        List<AiClientSystemPromptConfig> configs = aiClientSystemPromptConfigMapper.queryBySystemPromptId(
                systemPromptId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryBySystemPromptId test passed. Found {} configs for systemPromptId {}", configs.size(),
                 systemPromptId);
    }

    @Test
    public void testQueryAll() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);

        AiClientSystemPromptConfig config2 = AiClientSystemPromptConfig.builder()
                .clientId("client-400")
                .systemPromptId("prompt-400")
                .isDeleted(0)
                .build();
        aiClientSystemPromptConfigMapper.insert(config2);
        allTestConfigs.add(config2);

        AiClientSystemPromptConfig config3 = AiClientSystemPromptConfig.builder()
                .clientId("client-500")
                .systemPromptId("prompt-500")
                .isDeleted(0)
                .build();
        aiClientSystemPromptConfigMapper.insert(config3);
        allTestConfigs.add(config3);

        List<AiClientSystemPromptConfig> allConfigs = aiClientSystemPromptConfigMapper.queryAll();
        Assertions.assertNotNull(allConfigs);
        Assertions.assertTrue(allConfigs.size() >= 3);
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryAll test passed. Found {} configs", allConfigs.size());
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setSystemPromptId("prompt-2");

        Integer result = aiClientSystemPromptConfigMapper.updateById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientSystemPromptConfig updated = aiClientSystemPromptConfigMapper.queryById(id);
        Assertions.assertEquals("prompt-2", updated.getSystemPromptId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        Assertions.assertTrue(
                ChronoUnit.SECONDS.between(originalUpdateTime, updated.getUpdateTime()) >= 0);
        log.info("UpdateById test passed. Updated config: {}", updated);
    }

    @Test
    public void testUpdateByClientIdAndSystemPromptId() throws InterruptedException {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String systemPromptId = testConfig.getSystemPromptId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setIsDeleted(1);

        Integer result = aiClientSystemPromptConfigMapper.updateByClientIdAndSystemPromptId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientSystemPromptConfig updated = aiClientSystemPromptConfigMapper.queryByClientIdAndSystemPromptId(
                clientId, systemPromptId);
        Assertions.assertNull(updated);

        log.info("UpdateByClientIdAndSystemPromptId test passed. Updated config: {}", testConfig);
    }

    @Test
    public void testDeleteById() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        Long id = testConfig.getId();

        AiClientSystemPromptConfig beforeDelete = aiClientSystemPromptConfigMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientSystemPromptConfigMapper.deleteById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientSystemPromptConfig afterDelete = aiClientSystemPromptConfigMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Config with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByClientIdAndSystemPromptId() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String systemPromptId = testConfig.getSystemPromptId();

        AiClientSystemPromptConfig beforeDelete = aiClientSystemPromptConfigMapper.queryByClientIdAndSystemPromptId(
                clientId, systemPromptId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientSystemPromptConfigMapper.deleteByClientIdAndSystemPromptId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientSystemPromptConfig afterDelete = aiClientSystemPromptConfigMapper.queryByClientIdAndSystemPromptId(
                clientId, systemPromptId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByClientIdAndSystemPromptId test passed. Config has been soft deleted");
    }

    @Test
    public void testDeleteByClientId() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();

        AiClientSystemPromptConfig beforeDelete = aiClientSystemPromptConfigMapper.queryByClientId(clientId).get(0);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientSystemPromptConfigMapper.deleteByClientId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        List<AiClientSystemPromptConfig> configs = aiClientSystemPromptConfigMapper.queryByClientId(clientId);
        Assertions.assertTrue(configs.isEmpty());
        log.info("DeleteByClientId test passed. Config for clientId {} has been soft deleted", clientId);
    }

    @Test
    public void testDeleteBySystemPromptId() {
        testConfig = AiClientSystemPromptConfig.builder()
                .clientId("client-100")
                .systemPromptId("prompt-100")
                .isDeleted(0)
                .build();
        aiClientSystemPromptConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String systemPromptId = testConfig.getSystemPromptId();

        AiClientSystemPromptConfig config2 = AiClientSystemPromptConfig.builder()
                .clientId("client-300")
                .systemPromptId(systemPromptId)
                .isDeleted(0)
                .build();
        aiClientSystemPromptConfigMapper.insert(config2);
        allTestConfigs.add(config2);

        Integer result = aiClientSystemPromptConfigMapper.deleteBySystemPromptId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.intValue());

        List<AiClientSystemPromptConfig> configs = aiClientSystemPromptConfigMapper.queryBySystemPromptId(
                systemPromptId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteBySystemPromptId test passed. Configs for systemPromptId {} have been soft deleted",
                 systemPromptId);
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiClientSystemPromptConfigMapper.insert(testConfig);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testConfig.getCreateTime());
        Assertions.assertNotNull(testConfig.getUpdateTime());

        Assertions.assertTrue(
                testConfig.getCreateTime().isAfter(beforeInsert.minusSeconds(1)) ||
                        testConfig.getCreateTime().isEqual(beforeInsert.minusSeconds(1)));
        Assertions.assertTrue(
                testConfig.getCreateTime().isBefore(afterInsert.plusSeconds(1)) ||
                        testConfig.getCreateTime().isEqual(afterInsert.plusSeconds(1)));

        Assertions.assertEquals(testConfig.getCreateTime(), testConfig.getUpdateTime());

        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testConfig.getCreateTime(), testConfig.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setSystemPromptId("prompt-3");
        aiClientSystemPromptConfigMapper.updateById(testConfig);

        AiClientSystemPromptConfig updated = aiClientSystemPromptConfigMapper.queryById(testConfig.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        Assertions.assertTrue(
                ChronoUnit.SECONDS.between(originalUpdateTime, updated.getUpdateTime()) >= 0);

        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnSoftDelete() throws InterruptedException {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime beforeDelete = LocalDateTime.now();

        Thread.sleep(1000);

        aiClientSystemPromptConfigMapper.deleteById(testConfig);
        LocalDateTime afterDelete = LocalDateTime.now();

        AiClientSystemPromptConfig deleted = aiClientSystemPromptConfigMapper.queryById(id);
        Assertions.assertNull(deleted);

        AiClientSystemPromptConfig queriedByBusinessId = aiClientSystemPromptConfigMapper.queryByClientIdAndSystemPromptId(
                testConfig.getClientId(), testConfig.getSystemPromptId());
        Assertions.assertNull(queriedByBusinessId);

        Assertions.assertNotNull(testConfig.getDeleteTime());
        Assertions.assertTrue(
                testConfig.getDeleteTime().isAfter(beforeDelete) ||
                        testConfig.getDeleteTime().isEqual(beforeDelete));
        Assertions.assertTrue(
                testConfig.getDeleteTime().isBefore(afterDelete.plusSeconds(1)) ||
                        testConfig.getDeleteTime().isEqual(afterDelete.plusSeconds(1)));

        log.info("AutoFillFieldsOnSoftDelete test passed. DeleteTime: {}", testConfig.getDeleteTime());
    }

    @Test
    public void testUniqueConstraintOnInsert() {
        aiClientSystemPromptConfigMapper.insert(testConfig);

        AiClientSystemPromptConfig duplicateConfig = AiClientSystemPromptConfig.builder()
                .clientId(testConfig.getClientId())
                .systemPromptId("prompt-999")
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiClientSystemPromptConfigMapper.insert(duplicateConfig);
        });
        log.info("UniqueConstraintOnInsert test passed. Duplicate insert was rejected");
    }

    @Test
    public void testSoftDeletedRecordsNotQueried() {
        aiClientSystemPromptConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String clientId = testConfig.getClientId();
        String systemPromptId = testConfig.getSystemPromptId();

        aiClientSystemPromptConfigMapper.deleteById(testConfig);

        List<AiClientSystemPromptConfig> configsByClientId = aiClientSystemPromptConfigMapper.queryByClientId(clientId);
        Assertions.assertFalse(
                configsByClientId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiClientSystemPromptConfig> configsBySystemPromptId = aiClientSystemPromptConfigMapper.queryBySystemPromptId(
                systemPromptId);
        Assertions.assertFalse(
                configsBySystemPromptId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiClientSystemPromptConfig> allConfigs = aiClientSystemPromptConfigMapper.queryAll();
        Assertions.assertFalse(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        log.info("SoftDeletedRecordsNotQueried test passed. Soft deleted records are not included in queries");
    }
}
