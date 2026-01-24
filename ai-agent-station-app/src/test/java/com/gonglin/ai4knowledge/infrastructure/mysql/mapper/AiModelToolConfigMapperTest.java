package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModelToolConfig;
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
public class AiModelToolConfigMapperTest {

    @Autowired
    private AiModelToolConfigMapper aiModelToolConfigMapper;

    private AiModelToolConfig testConfig;

    @BeforeEach
    public void setUp() {
        testConfig = AiModelToolConfig.builder()
                .modelId("model-1")
                .toolId("tool-1")
                .toolType("mcp")
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testConfig != null && testConfig.getId() != null) {
            aiModelToolConfigMapper.deleteById(testConfig);
        }
    }

    @Test
    public void testInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        Integer result = aiModelToolConfigMapper.insert(testConfig);
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
        aiModelToolConfigMapper.insert(testConfig);
        Integer id = testConfig.getId();

        AiModelToolConfig queried = aiModelToolConfigMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(testConfig.getModelId(), queried.getModelId());
        Assertions.assertEquals(testConfig.getToolId(), queried.getToolId());
        Assertions.assertEquals(testConfig.getToolType(), queried.getToolType());
        log.info("QueryById test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByModelIdAndToolId() {
        aiModelToolConfigMapper.insert(testConfig);
        String modelId = testConfig.getModelId();
        String toolId = testConfig.getToolId();

        AiModelToolConfig queried = aiModelToolConfigMapper.queryByModelIdAndToolId(modelId, toolId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(modelId, queried.getModelId());
        Assertions.assertEquals(toolId, queried.getToolId());
        log.info("QueryByModelIdAndToolId test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByModelId() {
        aiModelToolConfigMapper.insert(testConfig);
        String modelId = testConfig.getModelId();

        AiModelToolConfig config2 = AiModelToolConfig.builder()
                .modelId(modelId)
                .toolId("tool-20")
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiModelToolConfigMapper.insert(config2);

        AiModelToolConfig config3 = AiModelToolConfig.builder()
                .modelId(modelId)
                .toolId("tool-30")
                .toolType("function call")
                .isDeleted(0)
                .build();
        aiModelToolConfigMapper.insert(config3);

        List<AiModelToolConfig> configs = aiModelToolConfigMapper.queryByModelId(modelId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByModelId test passed. Found {} configs for modelId {}", configs.size(), modelId);
    }

    @Test
    public void testQueryByToolId() {
        aiModelToolConfigMapper.insert(testConfig);
        String toolId = testConfig.getToolId();

        AiModelToolConfig config2 = AiModelToolConfig.builder()
                .modelId("model-2")
                .toolId(toolId)
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiModelToolConfigMapper.insert(config2);

        AiModelToolConfig config3 = AiModelToolConfig.builder()
                .modelId("model-3")
                .toolId(toolId)
                .toolType("function call")
                .isDeleted(0)
                .build();
        aiModelToolConfigMapper.insert(config3);

        List<AiModelToolConfig> configs = aiModelToolConfigMapper.queryByToolId(toolId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByToolId test passed. Found {} configs for toolId {}", configs.size(), toolId);
    }

    @Test
    public void testQueryAll() {
        aiModelToolConfigMapper.insert(testConfig);

        AiModelToolConfig config2 = AiModelToolConfig.builder()
                .modelId("model-2")
                .toolId("tool-2")
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiModelToolConfigMapper.insert(config2);

        AiModelToolConfig config3 = AiModelToolConfig.builder()
                .modelId("model-3")
                .toolId("tool-3")
                .toolType("function call")
                .isDeleted(0)
                .build();
        aiModelToolConfigMapper.insert(config3);

        List<AiModelToolConfig> allConfigs = aiModelToolConfigMapper.queryAll();
        Assertions.assertNotNull(allConfigs);
        Assertions.assertTrue(allConfigs.size() >= 3);
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryAll test passed. Found {} configs", allConfigs.size());
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiModelToolConfigMapper.insert(testConfig);
        Integer id = testConfig.getId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setToolId("tool-2");
        testConfig.setToolType("function call");

        Integer result = aiModelToolConfigMapper.updateById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiModelToolConfig updated = aiModelToolConfigMapper.queryById(id);
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
    public void testUpdateByModelIdAndToolId() throws InterruptedException {
        aiModelToolConfigMapper.insert(testConfig);
        String modelId = testConfig.getModelId();
        String originalToolId = testConfig.getToolId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setToolType("function call");
        Integer result = aiModelToolConfigMapper.updateByModelIdAndToolId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());


        AiModelToolConfig updatedNewToolId = aiModelToolConfigMapper.queryByModelIdAndToolId(modelId, originalToolId);
        Assertions.assertNotNull(updatedNewToolId);
        Assertions.assertEquals("function call", updatedNewToolId.getToolType());
        Assertions.assertTrue(updatedNewToolId.getUpdateTime().isAfter(originalUpdateTime));
        log.info("UpdateByModelIdAndToolId test passed. Updated config: {}", updatedNewToolId);
    }

    @Test
    public void testDeleteById() {
        aiModelToolConfigMapper.insert(testConfig);
        Integer id = testConfig.getId();

        AiModelToolConfig beforeDelete = aiModelToolConfigMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiModelToolConfigMapper.deleteById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiModelToolConfig afterDelete = aiModelToolConfigMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Config with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByModelIdAndToolId() {
        aiModelToolConfigMapper.insert(testConfig);
        String modelId = testConfig.getModelId();
        String toolId = testConfig.getToolId();

        AiModelToolConfig beforeDelete = aiModelToolConfigMapper.queryByModelIdAndToolId(modelId, toolId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiModelToolConfigMapper.deleteByModelIdAndToolId(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiModelToolConfig afterDelete = aiModelToolConfigMapper.queryByModelIdAndToolId(modelId, toolId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByModelIdAndToolId test passed. Config has been soft deleted");
    }

    @Test
    public void testDeleteByModelId() {
        aiModelToolConfigMapper.insert(testConfig);
        String modelId = testConfig.getModelId();

        AiModelToolConfig config2 = AiModelToolConfig.builder()
                .modelId(modelId)
                .toolId("tool-2")
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiModelToolConfigMapper.insert(config2);

        Integer result = aiModelToolConfigMapper.deleteByModelId(testConfig);
        Assertions.assertNotNull(result);

        List<AiModelToolConfig> configs = aiModelToolConfigMapper.queryByModelId(modelId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteByModelId test passed. Configs for modelId {} have been soft deleted", modelId);
    }

    @Test
    public void testDeleteByToolId() {
        aiModelToolConfigMapper.insert(testConfig);
        String toolId = testConfig.getToolId();

        AiModelToolConfig config2 = AiModelToolConfig.builder()
                .modelId("model-2")
                .toolId(toolId)
                .toolType("mcp")
                .isDeleted(0)
                .build();
        aiModelToolConfigMapper.insert(config2);

        Integer result = aiModelToolConfigMapper.deleteByToolId(testConfig);
        Assertions.assertNotNull(result);

        List<AiModelToolConfig> configs = aiModelToolConfigMapper.queryByToolId(toolId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteByToolId test passed. Configs for toolId {} have been soft deleted", toolId);
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiModelToolConfigMapper.insert(testConfig);
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
        aiModelToolConfigMapper.insert(testConfig);
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setToolType("function call");
        aiModelToolConfigMapper.updateById(testConfig);

        AiModelToolConfig updated = aiModelToolConfigMapper.queryById(testConfig.getId());
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
        aiModelToolConfigMapper.insert(testConfig);
        Integer id = testConfig.getId();
        LocalDateTime beforeDelete = LocalDateTime.now();

        Thread.sleep(1000);

        aiModelToolConfigMapper.deleteById(testConfig);
        LocalDateTime afterDelete = LocalDateTime.now();

        AiModelToolConfig deleted = aiModelToolConfigMapper.queryById(id);
        Assertions.assertNull(deleted);

        AiModelToolConfig queriedByBusinessId = aiModelToolConfigMapper.queryByModelIdAndToolId(
                testConfig.getModelId(), testConfig.getToolId());
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
        aiModelToolConfigMapper.insert(testConfig);

        AiModelToolConfig duplicateConfig = AiModelToolConfig.builder()
                .modelId(testConfig.getModelId())
                .toolId(testConfig.getToolId())
                .toolType(testConfig.getToolType())
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiModelToolConfigMapper.insert(duplicateConfig);
        });
        log.info("UniqueConstraintOnInsert test passed. Duplicate insert was rejected");
    }

    @Test
    public void testToolTypeMcp() {
        testConfig.setToolType("mcp");
        aiModelToolConfigMapper.insert(testConfig);

        AiModelToolConfig queried = aiModelToolConfigMapper.queryById(testConfig.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("mcp", queried.getToolType());
        log.info("ToolTypeMcp test passed. ToolType: {}", queried.getToolType());
    }

    @Test
    public void testToolTypeFunctionCall() {
        testConfig.setToolType("function call");
        aiModelToolConfigMapper.insert(testConfig);

        AiModelToolConfig queried = aiModelToolConfigMapper.queryById(testConfig.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("function call", queried.getToolType());
        log.info("ToolTypeFunctionCall test passed. ToolType: {}", queried.getToolType());
    }

    @Test
    public void testSoftDeletedRecordsNotQueried() {
        aiModelToolConfigMapper.insert(testConfig);
        String modelId = testConfig.getModelId();
        String toolId = testConfig.getToolId();

        aiModelToolConfigMapper.deleteById(testConfig);

        List<AiModelToolConfig> configsByModelId = aiModelToolConfigMapper.queryByModelId(modelId);
        Assertions.assertFalse(configsByModelId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiModelToolConfig> configsByToolId = aiModelToolConfigMapper.queryByToolId(toolId);
        Assertions.assertFalse(configsByToolId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiModelToolConfig> allConfigs = aiModelToolConfigMapper.queryAll();
        Assertions.assertFalse(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        log.info("SoftDeletedRecordsNotQueried test passed. Soft deleted records are not included in queries");
    }
}
