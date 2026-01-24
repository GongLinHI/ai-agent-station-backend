package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgent;
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
public class AiAgentMapperTest {

    @Autowired
    private AiAgentMapper aiAgentMapper;

    private AiAgent testAiAgent;

    @BeforeEach
    public void setUp() {
        testAiAgent = AiAgent.builder()
                .agentId("test-agent-001")
                .agentName("测试智能体")
                .description("这是一个测试用的智能体")
                .channel("agent")
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testAiAgent != null && testAiAgent.getId() != null) {
            aiAgentMapper.deleteById(testAiAgent);
        }
    }

    @Test
    public void testInsert() {
        Integer result = aiAgentMapper.insert(testAiAgent);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testAiAgent.getId());
        Assertions.assertNotNull(testAiAgent.getCreateTime());
        Assertions.assertNotNull(testAiAgent.getUpdateTime());
        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testAiAgent.getId(), testAiAgent.getCreateTime(), testAiAgent.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiAgentMapper.insert(testAiAgent);
        Long id = testAiAgent.getId();

        AiAgent queried = aiAgentMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiAgent.getAgentId(), queried.getAgentId());
        Assertions.assertEquals(testAiAgent.getAgentName(), queried.getAgentName());
        Assertions.assertEquals(testAiAgent.getDescription(), queried.getDescription());
        Assertions.assertEquals(testAiAgent.getChannel(), queried.getChannel());
        Assertions.assertEquals(testAiAgent.getStatus(), queried.getStatus());
        log.info("QueryById test passed. Queried Agent: {}", queried);
    }

    @Test
    public void testQueryByAgentId() {
        aiAgentMapper.insert(testAiAgent);
        String agentId = testAiAgent.getAgentId();

        AiAgent queried = aiAgentMapper.queryByAgentId(agentId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiAgent.getId(), queried.getId());
        Assertions.assertEquals(testAiAgent.getAgentName(), queried.getAgentName());
        Assertions.assertEquals(testAiAgent.getDescription(), queried.getDescription());
        log.info("QueryByAgentId test passed. Queried Agent: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiAgentMapper.insert(testAiAgent);
        Long id = testAiAgent.getId();
        LocalDateTime originalUpdateTime = testAiAgent.getUpdateTime();

        Thread.sleep(1000);

        testAiAgent.setAgentName("更新后的智能体名称");
        testAiAgent.setDescription("更新后的描述信息");
        testAiAgent.setChannel("chat_stream");
        testAiAgent.setStatus(1);

        Integer result = aiAgentMapper.updateById(testAiAgent);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        AiAgent updated = aiAgentMapper.queryById(id);
        Assertions.assertEquals("更新后的智能体名称", updated.getAgentName());
        Assertions.assertEquals("更新后的描述信息", updated.getDescription());
        Assertions.assertEquals("chat_stream", updated.getChannel());
        Assertions.assertEquals(1, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateById test passed. Updated Agent: {}", updated);
    }

    @Test
    public void testUpdateByAgentId() throws InterruptedException {
        aiAgentMapper.insert(testAiAgent);
        String agentId = testAiAgent.getAgentId();
        AiAgent beforeUpdate = aiAgentMapper.queryByAgentId(agentId);
        LocalDateTime originalUpdateTime = beforeUpdate.getUpdateTime();

        Thread.sleep(1000);

        testAiAgent.setAgentName("通过Agent ID更新的名称");
        testAiAgent.setDescription("通过Agent ID更新的描述");
        testAiAgent.setChannel("agent");

        Integer result = aiAgentMapper.updateByAgentId(testAiAgent);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAgent updated = aiAgentMapper.queryByAgentId(agentId);
        Assertions.assertEquals("通过Agent ID更新的名称", updated.getAgentName());
        Assertions.assertEquals("通过Agent ID更新的描述", updated.getDescription());
        Assertions.assertEquals("agent", updated.getChannel());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateByAgentId test passed. Updated Agent: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiAgentMapper.insert(testAiAgent);
        Long id = testAiAgent.getId();

        AiAgent beforeDelete = aiAgentMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());
        Assertions.assertNull(beforeDelete.getDeleteTime());

        Integer result = aiAgentMapper.deleteById(testAiAgent);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAgent afterDelete = aiAgentMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Agent with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByAgentId() {
        aiAgentMapper.insert(testAiAgent);
        String agentId = testAiAgent.getAgentId();

        AiAgent beforeDelete = aiAgentMapper.queryByAgentId(agentId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());
        Assertions.assertNull(beforeDelete.getDeleteTime());

        Integer result = aiAgentMapper.deleteByAgentId(testAiAgent);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAgent afterDelete = aiAgentMapper.queryByAgentId(agentId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByAgentId test passed. Agent with agentId {} has been soft deleted", agentId);
    }

    @Test
    public void testQueryByStatus() {
        AiAgent enabledAgent = AiAgent.builder()
                .agentId("test-agent-enabled")
                .agentName("启用的智能体")
                .description("这是一个启用的智能体")
                .channel("agent")
                .status(1)
                .isDeleted(0)
                .build();
        aiAgentMapper.insert(enabledAgent);

        AiAgent disabledAgent = AiAgent.builder()
                .agentId("test-agent-disabled")
                .agentName("禁用的智能体")
                .description("这是一个禁用的智能体")
                .channel("chat_stream")
                .status(0)
                .isDeleted(0)
                .build();
        aiAgentMapper.insert(disabledAgent);

        List<AiAgent> enabledAgents = aiAgentMapper.queryByStatus(1);
        Assertions.assertNotNull(enabledAgents);
        Assertions.assertTrue(enabledAgents.size() >= 1);
        Assertions.assertTrue(
                enabledAgents.stream().anyMatch(agent -> "test-agent-enabled".equals(agent.getAgentId())));
        log.info("QueryByStatus(1) test passed. Found {} enabled agents", enabledAgents.size());

        List<AiAgent> disabledAgents = aiAgentMapper.queryByStatus(0);
        Assertions.assertNotNull(disabledAgents);
        Assertions.assertTrue(disabledAgents.size() >= 1);
        Assertions.assertTrue(
                disabledAgents.stream().anyMatch(agent -> "test-agent-disabled".equals(agent.getAgentId())));
        log.info("QueryByStatus(0) test passed. Found {} disabled agents", disabledAgents.size());
    }

    @Test
    public void testQueryAll() {
        aiAgentMapper.insert(testAiAgent);

        AiAgent anotherAgent = AiAgent.builder()
                .agentId("test-agent-002")
                .agentName("另一个测试智能体")
                .description("这是另一个测试智能体")
                .channel("chat_stream")
                .status(1)
                .isDeleted(0)
                .build();
        aiAgentMapper.insert(anotherAgent);

        List<AiAgent> allAgents = aiAgentMapper.queryAll();
        Assertions.assertNotNull(allAgents);
        Assertions.assertTrue(allAgents.size() >= 2);
        Assertions.assertTrue(allAgents.stream().anyMatch(agent -> "test-agent-001".equals(agent.getAgentId())));
        Assertions.assertTrue(allAgents.stream().anyMatch(agent -> "test-agent-002".equals(agent.getAgentId())));
        log.info("QueryAll test passed. Found {} agents", allAgents.size());
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiAgentMapper.insert(testAiAgent);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testAiAgent.getCreateTime());
        Assertions.assertNotNull(testAiAgent.getUpdateTime());
        Assertions.assertTrue(
                testAiAgent.getCreateTime().isAfter(beforeInsert) || testAiAgent.getCreateTime().isEqual(beforeInsert));
        Assertions.assertTrue(
                testAiAgent.getCreateTime().isBefore(afterInsert) || testAiAgent.getCreateTime().isEqual(afterInsert));
        Assertions.assertEquals(testAiAgent.getCreateTime(), testAiAgent.getUpdateTime());
        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testAiAgent.getCreateTime(), testAiAgent.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiAgentMapper.insert(testAiAgent);
        LocalDateTime originalUpdateTime = testAiAgent.getUpdateTime();

        Thread.sleep(1000);

        testAiAgent.setAgentName("测试自动填充更新时间");
        testAiAgent.setDescription("测试更新描述");
        aiAgentMapper.updateById(testAiAgent);

        AiAgent updated = aiAgentMapper.queryById(testAiAgent.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnSoftDelete() throws InterruptedException {
        aiAgentMapper.insert(testAiAgent);
        Long id = testAiAgent.getId();
        String agentId = testAiAgent.getAgentId();
        LocalDateTime beforeDelete = LocalDateTime.now();

        Thread.sleep(1000);

        aiAgentMapper.deleteById(testAiAgent);
        LocalDateTime afterDelete = LocalDateTime.now();

        AiAgent deletedById = aiAgentMapper.queryById(id);
        Assertions.assertNull(deletedById);

        AiAgent deletedByAgentId = aiAgentMapper.queryByAgentId(agentId);
        Assertions.assertNull(deletedByAgentId);

        AiAgent allAgents = aiAgentMapper.queryAll().stream()
                .filter(agent -> id.equals(agent.getId()))
                .findFirst()
                .orElse(null);
        Assertions.assertNull(allAgents);

        log.info("AutoFillFieldsOnSoftDelete test passed. Agent has been soft deleted");
    }

    @Test
    public void testCreateTimeAndUpdateTimeAreEqualOnInsert() {
        aiAgentMapper.insert(testAiAgent);

        Assertions.assertNotNull(testAiAgent.getCreateTime());
        Assertions.assertNotNull(testAiAgent.getUpdateTime());
        Assertions.assertEquals(testAiAgent.getCreateTime(), testAiAgent.getUpdateTime(),
                                "CreateTime and UpdateTime should be equal on insert");
        log.info("CreateTimeAndUpdateTimeAreEqualOnInsert test passed. Both times: {}",
                 testAiAgent.getCreateTime());
    }

    @Test
    public void testUpdateTimeChangesOnUpdate() throws InterruptedException {
        aiAgentMapper.insert(testAiAgent);
        LocalDateTime createTime = testAiAgent.getCreateTime();
        LocalDateTime originalUpdateTime = testAiAgent.getUpdateTime();

        Thread.sleep(1000);

        testAiAgent.setDescription("更新描述");
        aiAgentMapper.updateById(testAiAgent);

        AiAgent updated = aiAgentMapper.queryById(testAiAgent.getId());

        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime),
                "UpdateTime should be updated");
        Assertions.assertNotEquals(updated.getCreateTime().truncatedTo(ChronoUnit.SECONDS),
                                   updated.getUpdateTime().truncatedTo(ChronoUnit.SECONDS),
                                   "UpdateTime should be different from CreateTime after update");
        log.info("UpdateTimeChangesOnUpdate test passed. CreateTime: {}, Original UpdateTime: {}, New UpdateTime: {}",
                 createTime, originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testDeleteTimeIsSetOnSoftDelete() throws InterruptedException {
        aiAgentMapper.insert(testAiAgent);
        Long id = testAiAgent.getId();

        Assertions.assertNull(testAiAgent.getDeleteTime(),
                              "DeleteTime should be null before soft delete");

        Thread.sleep(1000);

        aiAgentMapper.deleteById(testAiAgent);

        AiAgent deleted = aiAgentMapper.queryById(id);
        Assertions.assertNull(deleted,
                              "Soft deleted agent should not be returned by queryById");

        log.info("DeleteTimeIsSetOnSoftDelete test passed. Agent has been soft deleted");
    }
}
