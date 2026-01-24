package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientModelConfig;
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
public class AiClientModelConfigMapperTest {

    @Autowired
    private AiClientModelConfigMapper aiClientModelConfigMapper;

    private AiClientModelConfig testConfig;
    private java.util.List<AiClientModelConfig> allTestConfigs = new java.util.ArrayList<>();

    @BeforeEach
    public void setUp() {
        testConfig = AiClientModelConfig.builder()
                .clientId("client-100")
                .modelId("model-100")
                .isDeleted(0)
                .build();
        allTestConfigs.clear();
    }

    @AfterEach
    public void tearDown() {
        for (AiClientModelConfig config : allTestConfigs) {
            if (config != null && config.getId() != null) {
                try {
                    aiClientModelConfigMapper.deleteById(config);
                } catch (Exception e) {
                }
            }
        }
        allTestConfigs.clear();
    }

    @Test
    public void testInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        Integer result = aiClientModelConfigMapper.insert(testConfig);
        LocalDateTime afterInsert = LocalDateTime.now();
        allTestConfigs.add(testConfig);

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
        aiClientModelConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiClientModelConfig queried = aiClientModelConfigMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(testConfig.getClientId(), queried.getClientId());
        Assertions.assertEquals(testConfig.getModelId(), queried.getModelId());
        log.info("QueryById test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByClientIdAndModelId() {
        aiClientModelConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String modelId = testConfig.getModelId();

        AiClientModelConfig queried = aiClientModelConfigMapper.queryByClientIdAndModelId(clientId, modelId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(clientId, queried.getClientId());
        Assertions.assertEquals(modelId, queried.getModelId());
        log.info("QueryByClientIdAndModelId test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByClientId() {
        aiClientModelConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();

        AiClientModelConfig config2 = AiClientModelConfig.builder()
                .clientId(clientId)
                .modelId("model-2")
                .isDeleted(0)
                .build();
        aiClientModelConfigMapper.insert(config2);

        AiClientModelConfig config3 = AiClientModelConfig.builder()
                .clientId(clientId)
                .modelId("model-3")
                .isDeleted(0)
                .build();
        aiClientModelConfigMapper.insert(config3);

        List<AiClientModelConfig> configs = aiClientModelConfigMapper.queryByClientId(clientId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByClientId test passed. Found {} configs for clientId {}", configs.size(), clientId);
    }

    @Test
    public void testQueryByModelId() {
        aiClientModelConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String modelId = testConfig.getModelId();

        AiClientModelConfig config2 = AiClientModelConfig.builder()
                .clientId("client-2")
                .modelId(modelId)
                .isDeleted(0)
                .build();
        aiClientModelConfigMapper.insert(config2);
        allTestConfigs.add(config2);

        AiClientModelConfig config3 = AiClientModelConfig.builder()
                .clientId("client-3")
                .modelId(modelId)
                .isDeleted(0)
                .build();
        aiClientModelConfigMapper.insert(config3);
        allTestConfigs.add(config3);

        List<AiClientModelConfig> configs = aiClientModelConfigMapper.queryByModelId(modelId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByModelId test passed. Found {} configs for modelId {}", configs.size(), modelId);
    }

    @Test
    public void testQueryAll() {
        aiClientModelConfigMapper.insert(testConfig);

        AiClientModelConfig config2 = AiClientModelConfig.builder()
                .clientId("client-2")
                .modelId("model-2")
                .isDeleted(0)
                .build();
        aiClientModelConfigMapper.insert(config2);

        AiClientModelConfig config3 = AiClientModelConfig.builder()
                .clientId("client-3")
                .modelId("model-3")
                .isDeleted(0)
                .build();
        aiClientModelConfigMapper.insert(config3);

        List<AiClientModelConfig> allConfigs = aiClientModelConfigMapper.queryAll();
        Assertions.assertNotNull(allConfigs);
        Assertions.assertTrue(allConfigs.size() >= 3);
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryAll test passed. Found {} configs", allConfigs.size());
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiClientModelConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setModelId("model-2");

        Integer result = aiClientModelConfigMapper.updateById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientModelConfig updated = aiClientModelConfigMapper.queryById(id);
        Assertions.assertEquals("model-2", updated.getModelId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        Assertions.assertTrue(
                ChronoUnit.SECONDS.between(originalUpdateTime, updated.getUpdateTime()) >= 0);
        log.info("UpdateById test passed. Updated config: {}", updated);
    }

    @Test
    public void testUpdateByClientIdAndModelId() throws InterruptedException {
        aiClientModelConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String clientId = testConfig.getClientId();
        String originalModelId = testConfig.getModelId();

        AiClientModelConfig beforeUpdate = aiClientModelConfigMapper.queryByClientIdAndModelId(clientId,
                                                                                               originalModelId);
        LocalDateTime originalUpdateTime = beforeUpdate.getUpdateTime();

        Thread.sleep(1000);

        AiClientModelConfig updateConfig = AiClientModelConfig.builder()
                .clientId(clientId)
                .modelId(originalModelId)
                .build();
        aiClientModelConfigMapper.updateByClientIdAndModelId(updateConfig);

        AiClientModelConfig updated = aiClientModelConfigMapper.queryByClientIdAndModelId(clientId, originalModelId);
        Assertions.assertNotNull(updated);
        Assertions.assertTrue(updated.getUpdateTime().isAfter(originalUpdateTime));
        log.info("UpdateByClientIdAndModelId test passed. Updated config: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiClientModelConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiClientModelConfig beforeDelete = aiClientModelConfigMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientModelConfigMapper.deleteById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientModelConfig afterDelete = aiClientModelConfigMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Config with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByClientIdAndModelId() {
        aiClientModelConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String modelId = testConfig.getModelId();

        AiClientModelConfig beforeDelete = aiClientModelConfigMapper.queryByClientIdAndModelId(clientId, modelId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientModelConfigMapper.deleteByClientIdAndModelId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientModelConfig afterDelete = aiClientModelConfigMapper.queryByClientIdAndModelId(clientId, modelId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByClientIdAndModelId test passed. Config has been soft deleted");
    }

    @Test
    public void testDeleteByClientId() {
        aiClientModelConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String clientId = testConfig.getClientId();

        AiClientModelConfig config2 = AiClientModelConfig.builder()
                .clientId(clientId)
                .modelId("model-2")
                .isDeleted(0)
                .build();
        aiClientModelConfigMapper.insert(config2);
        allTestConfigs.add(config2);

        Integer result = aiClientModelConfigMapper.deleteByClientId(testConfig);
        Assertions.assertNotNull(result);

        List<AiClientModelConfig> configs = aiClientModelConfigMapper.queryByClientId(clientId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteByClientId test passed. Configs for clientId {} have been soft deleted", clientId);
    }

    @Test
    public void testDeleteByModelId() {
        aiClientModelConfigMapper.insert(testConfig);
        String modelId = testConfig.getModelId();

        AiClientModelConfig config2 = AiClientModelConfig.builder()
                .clientId("client-2")
                .modelId(modelId)
                .isDeleted(0)
                .build();
        aiClientModelConfigMapper.insert(config2);

        Integer result = aiClientModelConfigMapper.deleteByModelId(testConfig);
        Assertions.assertNotNull(result);

        List<AiClientModelConfig> configs = aiClientModelConfigMapper.queryByModelId(modelId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteByModelId test passed. Configs for modelId {} have been soft deleted", modelId);
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiClientModelConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
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
        aiClientModelConfigMapper.insert(testConfig);
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setModelId("model-3");
        aiClientModelConfigMapper.updateById(testConfig);

        AiClientModelConfig updated = aiClientModelConfigMapper.queryById(testConfig.getId());
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
        aiClientModelConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime beforeDelete = LocalDateTime.now();

        Thread.sleep(1000);

        aiClientModelConfigMapper.deleteById(testConfig);
        LocalDateTime afterDelete = LocalDateTime.now();

        AiClientModelConfig deleted = aiClientModelConfigMapper.queryById(id);
        Assertions.assertNull(deleted);

        AiClientModelConfig queriedByBusinessId = aiClientModelConfigMapper.queryByClientIdAndModelId(
                testConfig.getClientId(), testConfig.getModelId());
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
        aiClientModelConfigMapper.insert(testConfig);

        AiClientModelConfig duplicateConfig = AiClientModelConfig.builder()
                .clientId(testConfig.getClientId())
                .modelId(testConfig.getModelId())
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiClientModelConfigMapper.insert(duplicateConfig);
        });
        log.info("UniqueConstraintOnInsert test passed. Duplicate insert was rejected");
    }

    @Test
    public void testSoftDeletedRecordsNotQueried() {
        aiClientModelConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String clientId = testConfig.getClientId();
        String modelId = testConfig.getModelId();

        aiClientModelConfigMapper.deleteById(testConfig);

        List<AiClientModelConfig> configsByClientId = aiClientModelConfigMapper.queryByClientId(clientId);
        Assertions.assertFalse(
                configsByClientId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiClientModelConfig> configsByModelId = aiClientModelConfigMapper.queryByModelId(modelId);
        Assertions.assertFalse(configsByModelId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiClientModelConfig> allConfigs = aiClientModelConfigMapper.queryAll();
        Assertions.assertFalse(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        log.info("SoftDeletedRecordsNotQueried test passed. Soft deleted records are not included in queries");
    }
}
