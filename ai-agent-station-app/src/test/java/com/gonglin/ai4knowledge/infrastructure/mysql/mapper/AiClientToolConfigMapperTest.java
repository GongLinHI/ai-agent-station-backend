package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientToolConfig;
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
public class AiClientToolConfigMapperTest {

    @Autowired
    private AiClientToolConfigMapper aiClientToolConfigMapper;

    private AiClientToolConfig testConfig;
    private java.util.List<AiClientToolConfig> allTestConfigs = new java.util.ArrayList<>();

    @BeforeEach
    public void setUp() {
        testConfig = AiClientToolConfig.builder()
                .clientId("client-100")
                .toolId("tool-100")
                .toolType("mcp")
                .isDeleted(0)
                .build();
        allTestConfigs.clear();
    }

    @AfterEach
    public void tearDown() {
        for (AiClientToolConfig config : allTestConfigs) {
            if (config != null && config.getId() != null) {
                try {
                    aiClientToolConfigMapper.deleteById(config);
                } catch (Exception e) {
                }
            }
        }
        allTestConfigs.clear();
    }

    @Test
    public void testInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        Integer result = aiClientToolConfigMapper.insert(testConfig);
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
        aiClientToolConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiClientToolConfig queried = aiClientToolConfigMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(testConfig.getClientId(), queried.getClientId());
        Assertions.assertEquals(testConfig.getToolId(), queried.getToolId());
        Assertions.assertEquals(testConfig.getToolType(), queried.getToolType());
        log.info("QueryById test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByClientIdAndToolId() {
        aiClientToolConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String toolId = testConfig.getToolId();

        AiClientToolConfig queried = aiClientToolConfigMapper.queryByClientIdAndToolId(clientId, toolId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(clientId, queried.getClientId());
        Assertions.assertEquals(toolId, queried.getToolId());
        log.info("QueryByClientIdAndToolId test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByClientId() {
        aiClientToolConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();

        AiClientToolConfig config2 = AiClientToolConfig.builder()
                .clientId(clientId)
                .toolId("tool-2")
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiClientToolConfigMapper.insert(config2);

        AiClientToolConfig config3 = AiClientToolConfig.builder()
                .clientId(clientId)
                .toolId("tool-3")
                .toolType("function call")
                .isDeleted(0)
                .build();
        aiClientToolConfigMapper.insert(config3);

        List<AiClientToolConfig> configs = aiClientToolConfigMapper.queryByClientId(clientId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByClientId test passed. Found {} configs for clientId {}", configs.size(), clientId);
    }

    @Test
    public void testQueryByToolId() {
        aiClientToolConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String toolId = testConfig.getToolId();

        AiClientToolConfig config2 = AiClientToolConfig.builder()
                .clientId("client-2")
                .toolId(toolId)
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiClientToolConfigMapper.insert(config2);
        allTestConfigs.add(config2);

        AiClientToolConfig config3 = AiClientToolConfig.builder()
                .clientId("client-3")
                .toolId(toolId)
                .toolType("function call")
                .isDeleted(0)
                .build();
        aiClientToolConfigMapper.insert(config3);
        allTestConfigs.add(config3);

        List<AiClientToolConfig> configs = aiClientToolConfigMapper.queryByToolId(toolId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByToolId test passed. Found {} configs for toolId {}", configs.size(), toolId);
    }

    @Test
    public void testQueryAll() {
        aiClientToolConfigMapper.insert(testConfig);

        AiClientToolConfig config2 = AiClientToolConfig.builder()
                .clientId("client-2")
                .toolId("tool-2")
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiClientToolConfigMapper.insert(config2);

        AiClientToolConfig config3 = AiClientToolConfig.builder()
                .clientId("client-3")
                .toolId("tool-3")
                .toolType("function call")
                .isDeleted(0)
                .build();
        aiClientToolConfigMapper.insert(config3);

        List<AiClientToolConfig> allConfigs = aiClientToolConfigMapper.queryAll();
        Assertions.assertNotNull(allConfigs);
        Assertions.assertTrue(allConfigs.size() >= 3);
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryAll test passed. Found {} configs", allConfigs.size());
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiClientToolConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setToolId("tool-2");
        testConfig.setToolType("function call");

        Integer result = aiClientToolConfigMapper.updateById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientToolConfig updated = aiClientToolConfigMapper.queryById(id);
        Assertions.assertEquals("tool-2", updated.getToolId());
        Assertions.assertEquals("function call", updated.getToolType());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        Assertions.assertTrue(
                ChronoUnit.SECONDS.between(originalUpdateTime, updated.getUpdateTime()) >= 0);
        log.info("UpdateById test passed. Updated config: {}", updated);
    }

    @Test
    public void testUpdateByClientIdAndToolId() throws InterruptedException {
        aiClientToolConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String clientId = testConfig.getClientId();
        String originalToolId = testConfig.getToolId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setToolType("function call");

        Integer result = aiClientToolConfigMapper.updateByClientIdAndToolId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientToolConfig updated = aiClientToolConfigMapper.queryByClientIdAndToolId(clientId, originalToolId);
        Assertions.assertNotNull(updated);

        AiClientToolConfig updatedNewToolId = aiClientToolConfigMapper.queryByClientIdAndToolId(clientId,
                                                                                                testConfig.getToolId());
        Assertions.assertNotNull(updatedNewToolId);
        Assertions.assertTrue(updatedNewToolId.getUpdateTime().isAfter(originalUpdateTime));
        log.info("UpdateByClientIdAndToolId test passed. Updated config: {}", updatedNewToolId);
    }

    @Test
    public void testDeleteById() {
        aiClientToolConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiClientToolConfig beforeDelete = aiClientToolConfigMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientToolConfigMapper.deleteById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientToolConfig afterDelete = aiClientToolConfigMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Config with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByClientIdAndToolId() {
        aiClientToolConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String toolId = testConfig.getToolId();

        AiClientToolConfig beforeDelete = aiClientToolConfigMapper.queryByClientIdAndToolId(clientId, toolId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientToolConfigMapper.deleteByClientIdAndToolId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientToolConfig afterDelete = aiClientToolConfigMapper.queryByClientIdAndToolId(clientId, toolId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByClientIdAndToolId test passed. Config has been soft deleted");
    }

    @Test
    public void testDeleteByClientId() {
        aiClientToolConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();

        AiClientToolConfig config2 = AiClientToolConfig.builder()
                .clientId(clientId)
                .toolId("tool-2")
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiClientToolConfigMapper.insert(config2);

        Integer result = aiClientToolConfigMapper.deleteByClientId(testConfig);
        Assertions.assertNotNull(result);

        List<AiClientToolConfig> configs = aiClientToolConfigMapper.queryByClientId(clientId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteByClientId test passed. Configs for clientId {} have been soft deleted", clientId);
    }

    @Test
    public void testDeleteByToolId() {
        aiClientToolConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String toolId = testConfig.getToolId();

        AiClientToolConfig config2 = AiClientToolConfig.builder()
                .clientId("client-2")
                .toolId(toolId)
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiClientToolConfigMapper.insert(config2);
        allTestConfigs.add(config2);

        Integer result = aiClientToolConfigMapper.deleteByToolId(testConfig);
        Assertions.assertNotNull(result);

        List<AiClientToolConfig> configs = aiClientToolConfigMapper.queryByToolId(toolId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteByToolId test passed. Configs for toolId {} have been soft deleted", toolId);
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiClientToolConfigMapper.insert(testConfig);
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
        aiClientToolConfigMapper.insert(testConfig);
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setToolType("function call");
        aiClientToolConfigMapper.updateById(testConfig);

        AiClientToolConfig updated = aiClientToolConfigMapper.queryById(testConfig.getId());
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
        aiClientToolConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        Long id = testConfig.getId();
        LocalDateTime beforeDelete = LocalDateTime.now();

        Thread.sleep(1000);

        aiClientToolConfigMapper.deleteById(testConfig);
        LocalDateTime afterDelete = LocalDateTime.now();

        AiClientToolConfig deleted = aiClientToolConfigMapper.queryById(id);
        Assertions.assertNull(deleted);

        AiClientToolConfig queriedByBusinessId = aiClientToolConfigMapper.queryByClientIdAndToolId(
                testConfig.getClientId(), testConfig.getToolId());
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
        aiClientToolConfigMapper.insert(testConfig);

        AiClientToolConfig duplicateConfig = AiClientToolConfig.builder()
                .clientId(testConfig.getClientId())
                .toolId(testConfig.getToolId())
                .toolType(testConfig.getToolType())
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiClientToolConfigMapper.insert(duplicateConfig);
        });
        log.info("UniqueConstraintOnInsert test passed. Duplicate insert was rejected");
    }

    @Test
    public void testToolTypeMcp() {
        testConfig.setToolType("mcp");
        aiClientToolConfigMapper.insert(testConfig);

        AiClientToolConfig queried = aiClientToolConfigMapper.queryById(testConfig.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("mcp", queried.getToolType());
        log.info("ToolTypeMcp test passed. ToolType: {}", queried.getToolType());
    }

    @Test
    public void testToolTypeFunctionCall() {
        testConfig.setToolType("function call");
        aiClientToolConfigMapper.insert(testConfig);

        AiClientToolConfig queried = aiClientToolConfigMapper.queryById(testConfig.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("function call", queried.getToolType());
        log.info("ToolTypeFunctionCall test passed. ToolType: {}", queried.getToolType());
    }

    @Test
    public void testSoftDeletedRecordsNotQueried() {
        aiClientToolConfigMapper.insert(testConfig);
        allTestConfigs.add(testConfig);
        String clientId = testConfig.getClientId();
        String toolId = testConfig.getToolId();

        aiClientToolConfigMapper.deleteById(testConfig);

        List<AiClientToolConfig> configsByClientId = aiClientToolConfigMapper.queryByClientId(clientId);
        Assertions.assertFalse(
                configsByClientId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiClientToolConfig> configsByToolId = aiClientToolConfigMapper.queryByToolId(toolId);
        Assertions.assertFalse(configsByToolId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiClientToolConfig> allConfigs = aiClientToolConfigMapper.queryAll();
        Assertions.assertFalse(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        log.info("SoftDeletedRecordsNotQueried test passed. Soft deleted records are not included in queries");
    }
}
