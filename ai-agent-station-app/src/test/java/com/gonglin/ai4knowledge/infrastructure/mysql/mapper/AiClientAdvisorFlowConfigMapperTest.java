package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClientAdvisorFlowConfig;
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
public class AiClientAdvisorFlowConfigMapperTest {

    @Autowired
    private AiClientAdvisorFlowConfigMapper aiClientAdvisorFlowConfigMapper;

    private AiClientAdvisorFlowConfig testConfig;

    @BeforeEach
    public void setUp() {
        testConfig = AiClientAdvisorFlowConfig.builder()
                .clientId("client-100")
                .advisorId("advisor-100")
                .sequence(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testConfig != null && testConfig.getId() != null) {
            aiClientAdvisorFlowConfigMapper.deleteById(testConfig);
        }
    }

    @Test
    public void testInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        Integer result = aiClientAdvisorFlowConfigMapper.insert(testConfig);
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
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiClientAdvisorFlowConfig queried = aiClientAdvisorFlowConfigMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(testConfig.getClientId(), queried.getClientId());
        Assertions.assertEquals(testConfig.getAdvisorId(), queried.getAdvisorId());
        Assertions.assertEquals(testConfig.getSequence(), queried.getSequence());
        log.info("QueryById test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByClientIdAndAdvisorIdAndSequence() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String advisorId = testConfig.getAdvisorId();
        Integer sequence = testConfig.getSequence();

        AiClientAdvisorFlowConfig queried = aiClientAdvisorFlowConfigMapper.queryByClientIdAndAdvisorIdAndSequence(
                clientId, advisorId, sequence);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(clientId, queried.getClientId());
        Assertions.assertEquals(advisorId, queried.getAdvisorId());
        Assertions.assertEquals(sequence, queried.getSequence());
        log.info("QueryByClientIdAndAdvisorIdAndSequence test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByClientId() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();

        AiClientAdvisorFlowConfig config2 = AiClientAdvisorFlowConfig.builder()
                .clientId(clientId)
                .advisorId("advisor-200")
                .sequence(2)
                .isDeleted(0)
                .build();
        aiClientAdvisorFlowConfigMapper.insert(config2);

        AiClientAdvisorFlowConfig config3 = AiClientAdvisorFlowConfig.builder()
                .clientId(clientId)
                .advisorId("advisor-300")
                .sequence(3)
                .isDeleted(0)
                .build();
        aiClientAdvisorFlowConfigMapper.insert(config3);

        List<AiClientAdvisorFlowConfig> configs = aiClientAdvisorFlowConfigMapper.queryByClientId(clientId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByClientId test passed. Found {} configs for clientId {}", configs.size(), clientId);
    }

    @Test
    public void testQueryByAdvisorId() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        String advisorId = testConfig.getAdvisorId();

        AiClientAdvisorFlowConfig config2 = AiClientAdvisorFlowConfig.builder()
                .clientId("client-200")
                .advisorId(advisorId)
                .sequence(2)
                .isDeleted(0)
                .build();
        aiClientAdvisorFlowConfigMapper.insert(config2);

        AiClientAdvisorFlowConfig config3 = AiClientAdvisorFlowConfig.builder()
                .clientId("client-300")
                .advisorId(advisorId)
                .sequence(3)
                .isDeleted(0)
                .build();
        aiClientAdvisorFlowConfigMapper.insert(config3);

        List<AiClientAdvisorFlowConfig> configs = aiClientAdvisorFlowConfigMapper.queryByAdvisorId(advisorId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByAdvisorId test passed. Found {} configs for advisorId {}", configs.size(), advisorId);
    }

    @Test
    public void testQueryAll() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);

        AiClientAdvisorFlowConfig config2 = AiClientAdvisorFlowConfig.builder()
                .clientId("client-200")
                .advisorId("advisor-200")
                .sequence(1)
                .isDeleted(0)
                .build();
        aiClientAdvisorFlowConfigMapper.insert(config2);

        AiClientAdvisorFlowConfig config3 = AiClientAdvisorFlowConfig.builder()
                .clientId("client-300")
                .advisorId("advisor-300")
                .sequence(1)
                .isDeleted(0)
                .build();
        aiClientAdvisorFlowConfigMapper.insert(config3);

        List<AiClientAdvisorFlowConfig> allConfigs = aiClientAdvisorFlowConfigMapper.queryAll();
        Assertions.assertNotNull(allConfigs);
        Assertions.assertTrue(allConfigs.size() >= 3);
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryAll test passed. Found {} configs", allConfigs.size());
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setSequence(2);

        Integer result = aiClientAdvisorFlowConfigMapper.updateById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientAdvisorFlowConfig updated = aiClientAdvisorFlowConfigMapper.queryById(id);
        Assertions.assertEquals(2, updated.getSequence().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        Assertions.assertTrue(
                ChronoUnit.SECONDS.between(originalUpdateTime, updated.getUpdateTime()) >= 0);
        log.info("UpdateById test passed. Updated config: {}", updated);
    }


    @Test
    public void testDeleteById() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiClientAdvisorFlowConfig beforeDelete = aiClientAdvisorFlowConfigMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientAdvisorFlowConfigMapper.deleteById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientAdvisorFlowConfig afterDelete = aiClientAdvisorFlowConfigMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Config with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByClientIdAndAdvisorIdAndSequence() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String advisorId = testConfig.getAdvisorId();
        Integer sequence = testConfig.getSequence();

        AiClientAdvisorFlowConfig beforeDelete = aiClientAdvisorFlowConfigMapper.queryByClientIdAndAdvisorIdAndSequence(
                clientId, advisorId, sequence);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientAdvisorFlowConfigMapper.deleteByClientIdAndAdvisorIdAndSequence(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClientAdvisorFlowConfig afterDelete = aiClientAdvisorFlowConfigMapper.queryByClientIdAndAdvisorIdAndSequence(
                clientId, advisorId, sequence);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByClientIdAndAdvisorIdAndSequence test passed. Config has been soft deleted");
    }

    @Test
    public void testDeleteByClientId() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();

        AiClientAdvisorFlowConfig config2 = AiClientAdvisorFlowConfig.builder()
                .clientId(clientId)
                .advisorId("advisor-200")
                .sequence(2)
                .isDeleted(0)
                .build();
        aiClientAdvisorFlowConfigMapper.insert(config2);

        Integer result = aiClientAdvisorFlowConfigMapper.deleteByClientId(testConfig);
        Assertions.assertNotNull(result);

        List<AiClientAdvisorFlowConfig> configs = aiClientAdvisorFlowConfigMapper.queryByClientId(clientId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteByClientId test passed. Configs for clientId {} have been soft deleted", clientId);
    }

    @Test
    public void testDeleteByAdvisorId() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        String advisorId = testConfig.getAdvisorId();

        AiClientAdvisorFlowConfig config2 = AiClientAdvisorFlowConfig.builder()
                .clientId("client-200")
                .advisorId(advisorId)
                .sequence(2)
                .isDeleted(0)
                .build();
        aiClientAdvisorFlowConfigMapper.insert(config2);

        Integer result = aiClientAdvisorFlowConfigMapper.deleteByAdvisorId(testConfig);
        Assertions.assertNotNull(result);

        List<AiClientAdvisorFlowConfig> configs = aiClientAdvisorFlowConfigMapper.queryByAdvisorId(advisorId);
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertFalse(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        log.info("DeleteByAdvisorId test passed. Configs for advisorId {} have been soft deleted", advisorId);
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
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
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setSequence(5);
        aiClientAdvisorFlowConfigMapper.updateById(testConfig);

        AiClientAdvisorFlowConfig updated = aiClientAdvisorFlowConfigMapper.queryById(testConfig.getId());
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
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime beforeDelete = LocalDateTime.now();

        Thread.sleep(1000);

        aiClientAdvisorFlowConfigMapper.deleteById(testConfig);
        LocalDateTime afterDelete = LocalDateTime.now();

        AiClientAdvisorFlowConfig deleted = aiClientAdvisorFlowConfigMapper.queryById(id);
        Assertions.assertNull(deleted);

        AiClientAdvisorFlowConfig queriedByBusinessId = aiClientAdvisorFlowConfigMapper.queryByClientIdAndAdvisorIdAndSequence(
                testConfig.getClientId(), testConfig.getAdvisorId(), testConfig.getSequence());
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
        aiClientAdvisorFlowConfigMapper.insert(testConfig);

        AiClientAdvisorFlowConfig duplicateConfig = AiClientAdvisorFlowConfig.builder()
                .clientId(testConfig.getClientId())
                .advisorId(testConfig.getAdvisorId())
                .sequence(2)
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiClientAdvisorFlowConfigMapper.insert(duplicateConfig);
        });
        log.info("UniqueConstraintOnInsert test passed. Duplicate insert was rejected");
    }

    @Test
    public void testUniqueConstraintOnSequence() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);

        AiClientAdvisorFlowConfig duplicateSequenceConfig = AiClientAdvisorFlowConfig.builder()
                .clientId(testConfig.getClientId())
                .advisorId("advisor-200")
                .sequence(testConfig.getSequence())
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiClientAdvisorFlowConfigMapper.insert(duplicateSequenceConfig);
        });
        log.info("UniqueConstraintOnSequence test passed. Duplicate sequence insert was rejected");
    }

    @Test
    public void testSoftDeletedRecordsNotQueried() {
        aiClientAdvisorFlowConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();
        String advisorId = testConfig.getAdvisorId();

        aiClientAdvisorFlowConfigMapper.deleteById(testConfig);

        List<AiClientAdvisorFlowConfig> configsByClientId = aiClientAdvisorFlowConfigMapper.queryByClientId(clientId);
        Assertions.assertFalse(
                configsByClientId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiClientAdvisorFlowConfig> configsByAdvisorId = aiClientAdvisorFlowConfigMapper.queryByAdvisorId(
                advisorId);
        Assertions.assertFalse(
                configsByAdvisorId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiClientAdvisorFlowConfig> allConfigs = aiClientAdvisorFlowConfigMapper.queryAll();
        Assertions.assertFalse(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        log.info("SoftDeletedRecordsNotQueried test passed. Soft deleted records are not included in queries");
    }
}
