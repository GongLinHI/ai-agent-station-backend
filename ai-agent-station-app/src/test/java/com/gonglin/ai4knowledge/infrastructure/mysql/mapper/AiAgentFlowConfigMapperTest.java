package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentFlowConfig;
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
public class AiAgentFlowConfigMapperTest {

    @Autowired
    private AiAgentFlowConfigMapper aiAgentFlowConfigMapper;

    private AiAgentFlowConfig testConfig;

    @BeforeEach
    public void setUp() {
        testConfig = AiAgentFlowConfig.builder()
                .agentId("agent-3")
                .clientId("client-100")
                .sequence(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testConfig != null && testConfig.getId() != null) {
            aiAgentFlowConfigMapper.deleteById(testConfig);
        }
    }

    @Test
    public void testInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        Integer result = aiAgentFlowConfigMapper.insert(testConfig);
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
        aiAgentFlowConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiAgentFlowConfig queried = aiAgentFlowConfigMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(testConfig.getAgentId(), queried.getAgentId());
        Assertions.assertEquals(testConfig.getClientId(), queried.getClientId());
        Assertions.assertEquals(testConfig.getSequence(), queried.getSequence());
        log.info("QueryById test passed. Queried config: {}", queried);
    }

    @Test
    public void testQueryByAgentIdAndClientIdAndSequence() {
        aiAgentFlowConfigMapper.insert(testConfig);
        String agentId = testConfig.getAgentId();
        String clientId = testConfig.getClientId();
        Integer sequence = testConfig.getSequence();

        AiAgentFlowConfig queried = aiAgentFlowConfigMapper.queryByAgentIdAndClientIdAndSequence(agentId, clientId,
                                                                                                 sequence);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testConfig.getId(), queried.getId());
        Assertions.assertEquals(agentId, queried.getAgentId());
        Assertions.assertEquals(clientId, queried.getClientId());
        Assertions.assertEquals(sequence, queried.getSequence());
        log.info("QueryByAgentIdAndClientIdAndSequence test passed. Queried config: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiAgentFlowConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setSequence(2);

        Integer result = aiAgentFlowConfigMapper.updateById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAgentFlowConfig updated = aiAgentFlowConfigMapper.queryById(id);
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
        aiAgentFlowConfigMapper.insert(testConfig);
        Long id = testConfig.getId();

        AiAgentFlowConfig beforeDelete = aiAgentFlowConfigMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiAgentFlowConfigMapper.deleteById(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAgentFlowConfig afterDelete = aiAgentFlowConfigMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Config with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByAgentIdAndClientIdAndSequence() {
        aiAgentFlowConfigMapper.insert(testConfig);
        String agentId = testConfig.getAgentId();
        String clientId = testConfig.getClientId();
        Integer sequence = testConfig.getSequence();

        AiAgentFlowConfig beforeDelete = aiAgentFlowConfigMapper.queryByAgentIdAndClientIdAndSequence(agentId, clientId,
                                                                                                      sequence);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiAgentFlowConfigMapper.deleteByAgentIdAndClientIdAndSequence(testConfig);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAgentFlowConfig afterDelete = aiAgentFlowConfigMapper.queryByAgentIdAndClientIdAndSequence(agentId, clientId,
                                                                                                     sequence);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByAgentIdAndClientIdAndSequence test passed. Config has been soft deleted");
    }

    @Test
    public void testQueryByAgentId() {
        aiAgentFlowConfigMapper.insert(testConfig);
        String agentId = testConfig.getAgentId();

        AiAgentFlowConfig config2 = AiAgentFlowConfig.builder()
                .agentId(agentId)
                .clientId("client-2")
                .sequence(1)
                .isDeleted(0)
                .build();
        aiAgentFlowConfigMapper.insert(config2);

        AiAgentFlowConfig config3 = AiAgentFlowConfig.builder()
                .agentId(agentId)
                .clientId("client-3")
                .sequence(2)
                .isDeleted(0)
                .build();
        aiAgentFlowConfigMapper.insert(config3);

        List<AiAgentFlowConfig> configs = aiAgentFlowConfigMapper.queryByAgentId(agentId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByAgentId test passed. Found {} configs for agentId {}", configs.size(), agentId);
    }

    @Test
    public void testQueryByClientId() {
        aiAgentFlowConfigMapper.insert(testConfig);
        String clientId = testConfig.getClientId();

        AiAgentFlowConfig config2 = AiAgentFlowConfig.builder()
                .agentId("agent-2")
                .clientId(clientId)
                .sequence(1)
                .isDeleted(0)
                .build();
        aiAgentFlowConfigMapper.insert(config2);

        AiAgentFlowConfig config3 = AiAgentFlowConfig.builder()
                .agentId("agent-3")
                .clientId(clientId)
                .sequence(2)
                .isDeleted(0)
                .build();
        aiAgentFlowConfigMapper.insert(config3);

        List<AiAgentFlowConfig> configs = aiAgentFlowConfigMapper.queryByClientId(clientId);
        Assertions.assertNotNull(configs);
        Assertions.assertTrue(configs.size() >= 3);
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(configs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryByClientId test passed. Found {} configs for clientId {}", configs.size(), clientId);
    }

    @Test
    public void testQueryAll() {
        aiAgentFlowConfigMapper.insert(testConfig);

        AiAgentFlowConfig config2 = AiAgentFlowConfig.builder()
                .agentId("agent-2")
                .clientId("client-2")
                .sequence(1)
                .isDeleted(0)
                .build();
        aiAgentFlowConfigMapper.insert(config2);

        AiAgentFlowConfig config3 = AiAgentFlowConfig.builder()
                .agentId("agent-3")
                .clientId("client-3")
                .sequence(1)
                .isDeleted(0)
                .build();
        aiAgentFlowConfigMapper.insert(config3);

        List<AiAgentFlowConfig> allConfigs = aiAgentFlowConfigMapper.queryAll();
        Assertions.assertNotNull(allConfigs);
        Assertions.assertTrue(allConfigs.size() >= 3);
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config2.getId())));
        Assertions.assertTrue(allConfigs.stream().anyMatch(config -> config.getId().equals(config3.getId())));
        log.info("QueryAll test passed. Found {} configs", allConfigs.size());
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiAgentFlowConfigMapper.insert(testConfig);
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
        aiAgentFlowConfigMapper.insert(testConfig);
        LocalDateTime originalUpdateTime = testConfig.getUpdateTime();

        Thread.sleep(1000);

        testConfig.setSequence(5);
        aiAgentFlowConfigMapper.updateById(testConfig);

        AiAgentFlowConfig updated = aiAgentFlowConfigMapper.queryById(testConfig.getId());
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
        aiAgentFlowConfigMapper.insert(testConfig);
        Long id = testConfig.getId();
        LocalDateTime beforeDelete = LocalDateTime.now();

        Thread.sleep(1000);

        aiAgentFlowConfigMapper.deleteById(testConfig);
        LocalDateTime afterDelete = LocalDateTime.now();

        AiAgentFlowConfig deleted = aiAgentFlowConfigMapper.queryById(id);
        Assertions.assertNull(deleted);

        AiAgentFlowConfig queriedByBusinessId = aiAgentFlowConfigMapper.queryByAgentIdAndClientIdAndSequence(
                testConfig.getAgentId(), testConfig.getClientId(), testConfig.getSequence());
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
        aiAgentFlowConfigMapper.insert(testConfig);

        AiAgentFlowConfig duplicateConfig = AiAgentFlowConfig.builder()
                .agentId(testConfig.getAgentId())
                .clientId(testConfig.getClientId())
                .sequence(testConfig.getSequence())
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiAgentFlowConfigMapper.insert(duplicateConfig);
        });
        log.info("UniqueConstraintOnInsert test passed. Duplicate insert was rejected");
    }

    @Test
    public void testSoftDeletedRecordsNotQueried() {
        aiAgentFlowConfigMapper.insert(testConfig);
        String agentId = testConfig.getAgentId();
        String clientId = testConfig.getClientId();

        aiAgentFlowConfigMapper.deleteById(testConfig);

        List<AiAgentFlowConfig> configsByAgentId = aiAgentFlowConfigMapper.queryByAgentId(agentId);
        Assertions.assertFalse(configsByAgentId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiAgentFlowConfig> configsByClientId = aiAgentFlowConfigMapper.queryByClientId(clientId);
        Assertions.assertFalse(
                configsByClientId.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        List<AiAgentFlowConfig> allConfigs = aiAgentFlowConfigMapper.queryAll();
        Assertions.assertFalse(allConfigs.stream().anyMatch(config -> config.getId().equals(testConfig.getId())));

        log.info("SoftDeletedRecordsNotQueried test passed. Soft deleted records are not included in queries");
    }
}
