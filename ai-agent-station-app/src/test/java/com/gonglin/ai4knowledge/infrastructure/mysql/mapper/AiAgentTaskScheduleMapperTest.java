package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAgentTaskSchedule;
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
public class AiAgentTaskScheduleMapperTest {

    @Autowired
    private AiAgentTaskScheduleMapper aiAgentTaskScheduleMapper;

    private AiAgentTaskSchedule testSchedule;

    @BeforeEach
    public void setUp() {
        testSchedule = AiAgentTaskSchedule.builder()
                .agentId("agent-1")
                .taskName("测试任务")
                .description("这是一个测试任务")
                .cronExpression("0 0/30 * * * ?")
                .taskParam("{\"param1\":\"value1\"}")
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testSchedule != null && testSchedule.getId() != null) {
            aiAgentTaskScheduleMapper.deleteById(testSchedule);
        }
    }

    @Test
    public void testInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        Integer result = aiAgentTaskScheduleMapper.insert(testSchedule);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testSchedule.getId());
        Assertions.assertNotNull(testSchedule.getCreateTime());
        Assertions.assertNotNull(testSchedule.getUpdateTime());

        Assertions.assertTrue(
                testSchedule.getCreateTime().isAfter(beforeInsert.minusSeconds(1)) ||
                        testSchedule.getCreateTime().isEqual(beforeInsert.minusSeconds(1)));
        Assertions.assertTrue(
                testSchedule.getCreateTime().isBefore(afterInsert.plusSeconds(1)) ||
                        testSchedule.getCreateTime().isEqual(afterInsert.plusSeconds(1)));

        Assertions.assertEquals(testSchedule.getCreateTime(), testSchedule.getUpdateTime());

        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testSchedule.getId(), testSchedule.getCreateTime(), testSchedule.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiAgentTaskScheduleMapper.insert(testSchedule);
        Long id = testSchedule.getId();

        AiAgentTaskSchedule queried = aiAgentTaskScheduleMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testSchedule.getId(), queried.getId());
        Assertions.assertEquals(testSchedule.getAgentId(), queried.getAgentId());
        Assertions.assertEquals(testSchedule.getTaskName(), queried.getTaskName());
        Assertions.assertEquals(testSchedule.getDescription(), queried.getDescription());
        Assertions.assertEquals(testSchedule.getCronExpression(), queried.getCronExpression());
        log.info("QueryById test passed. Queried schedule: {}", queried);
    }

    @Test
    public void testQueryByAgentId() {
        aiAgentTaskScheduleMapper.insert(testSchedule);
        String agentId = testSchedule.getAgentId();

        AiAgentTaskSchedule schedule2 = AiAgentTaskSchedule.builder()
                .agentId(agentId)
                .taskName("测试任务2")
                .description("这是第二个测试任务")
                .cronExpression("0 0/15 * * * ?")
                .taskParam("{\"param2\":\"value2\"}")
                .status(1)
                .isDeleted(0)
                .build();
        aiAgentTaskScheduleMapper.insert(schedule2);

        List<AiAgentTaskSchedule> schedules = aiAgentTaskScheduleMapper.queryByAgentId(agentId);
        Assertions.assertNotNull(schedules);
        Assertions.assertTrue(schedules.size() >= 2);
        Assertions.assertTrue(schedules.stream().anyMatch(s -> s.getId().equals(testSchedule.getId())));
        Assertions.assertTrue(schedules.stream().anyMatch(s -> s.getId().equals(schedule2.getId())));
        log.info("QueryByAgentId test passed. Found {} schedules for agentId {}", schedules.size(), agentId);
    }

    @Test
    public void testQueryByStatus() {
        AiAgentTaskSchedule enabledSchedule = AiAgentTaskSchedule.builder()
                .agentId("agent-1")
                .taskName("启用的任务")
                .description("这是一个启用的任务")
                .cronExpression("0 0/30 * * * ?")
                .taskParam("{\"enabled\":\"true\"}")
                .status(1)
                .isDeleted(0)
                .build();
        aiAgentTaskScheduleMapper.insert(enabledSchedule);

        AiAgentTaskSchedule disabledSchedule = AiAgentTaskSchedule.builder()
                .agentId("agent-1")
                .taskName("禁用的任务")
                .description("这是一个禁用的任务")
                .cronExpression("0 0/60 * * * ?")
                .taskParam("{\"disabled\":\"true\"}")
                .status(0)
                .isDeleted(0)
                .build();
        aiAgentTaskScheduleMapper.insert(disabledSchedule);

        List<AiAgentTaskSchedule> enabledSchedules = aiAgentTaskScheduleMapper.queryByStatus(1);
        Assertions.assertNotNull(enabledSchedules);
        Assertions.assertTrue(enabledSchedules.size() >= 1);
        Assertions.assertTrue(
                enabledSchedules.stream().anyMatch(s -> "启用的任务".equals(s.getTaskName())));
        log.info("QueryByStatus(1) test passed. Found {} enabled schedules", enabledSchedules.size());

        List<AiAgentTaskSchedule> disabledSchedules = aiAgentTaskScheduleMapper.queryByStatus(0);
        Assertions.assertNotNull(disabledSchedules);
        Assertions.assertTrue(disabledSchedules.size() >= 1);
        Assertions.assertTrue(
                disabledSchedules.stream().anyMatch(s -> "禁用的任务".equals(s.getTaskName())));
        log.info("QueryByStatus(0) test passed. Found {} disabled schedules", disabledSchedules.size());
    }

    @Test
    public void testQueryAll() {
        aiAgentTaskScheduleMapper.insert(testSchedule);

        AiAgentTaskSchedule schedule2 = AiAgentTaskSchedule.builder()
                .agentId("agent-2")
                .taskName("另一个测试任务")
                .description("这是另一个测试任务")
                .cronExpression("0 0/45 * * * ?")
                .taskParam("{\"another\":\"value\"}")
                .status(1)
                .isDeleted(0)
                .build();
        aiAgentTaskScheduleMapper.insert(schedule2);

        List<AiAgentTaskSchedule> allSchedules = aiAgentTaskScheduleMapper.queryAll();
        Assertions.assertNotNull(allSchedules);
        Assertions.assertTrue(allSchedules.size() >= 2);
        Assertions.assertTrue(allSchedules.stream().anyMatch(s -> "测试任务".equals(s.getTaskName())));
        Assertions.assertTrue(allSchedules.stream().anyMatch(s -> "另一个测试任务".equals(s.getTaskName())));
        log.info("QueryAll test passed. Found {} schedules", allSchedules.size());
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiAgentTaskScheduleMapper.insert(testSchedule);
        Long id = testSchedule.getId();
        LocalDateTime originalUpdateTime = testSchedule.getUpdateTime();

        Thread.sleep(1000);

        testSchedule.setTaskName("更新后的任务名称");
        testSchedule.setDescription("更新后的描述");
        testSchedule.setCronExpression("0 0/20 * * * ?");
        testSchedule.setStatus(0);

        Integer result = aiAgentTaskScheduleMapper.updateById(testSchedule);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAgentTaskSchedule updated = aiAgentTaskScheduleMapper.queryById(id);
        Assertions.assertEquals("更新后的任务名称", updated.getTaskName());
        Assertions.assertEquals("更新后的描述", updated.getDescription());
        Assertions.assertEquals("0 0/20 * * * ?", updated.getCronExpression());
        Assertions.assertEquals(0, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        Assertions.assertTrue(
                ChronoUnit.SECONDS.between(originalUpdateTime, updated.getUpdateTime()) >= 0);
        log.info("UpdateById test passed. Updated schedule: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiAgentTaskScheduleMapper.insert(testSchedule);
        Long id = testSchedule.getId();

        AiAgentTaskSchedule beforeDelete = aiAgentTaskScheduleMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiAgentTaskScheduleMapper.deleteById(testSchedule);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAgentTaskSchedule afterDelete = aiAgentTaskScheduleMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Schedule with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByAgentId() {
        aiAgentTaskScheduleMapper.insert(testSchedule);
        String agentId = testSchedule.getAgentId();

        AiAgentTaskSchedule schedule2 = AiAgentTaskSchedule.builder()
                .agentId(agentId)
                .taskName("任务2")
                .description("任务2描述")
                .cronExpression("0 0/25 * * * ?")
                .status(1)
                .isDeleted(0)
                .build();
        aiAgentTaskScheduleMapper.insert(schedule2);

        AiAgentTaskSchedule beforeDelete = aiAgentTaskScheduleMapper.queryById(testSchedule.getId());
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiAgentTaskScheduleMapper.deleteByAgentId(testSchedule);
        Assertions.assertNotNull(result);

        List<AiAgentTaskSchedule> schedules = aiAgentTaskScheduleMapper.queryByAgentId(agentId);
        Assertions.assertFalse(schedules.stream().anyMatch(s -> s.getId().equals(testSchedule.getId())));
        log.info("DeleteByAgentId test passed. Schedules for agentId {} have been soft deleted", agentId);
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiAgentTaskScheduleMapper.insert(testSchedule);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testSchedule.getCreateTime());
        Assertions.assertNotNull(testSchedule.getUpdateTime());

        Assertions.assertTrue(
                testSchedule.getCreateTime().isAfter(beforeInsert.minusSeconds(1)) ||
                        testSchedule.getCreateTime().isEqual(beforeInsert.minusSeconds(1)));
        Assertions.assertTrue(
                testSchedule.getCreateTime().isBefore(afterInsert.plusSeconds(1)) ||
                        testSchedule.getCreateTime().isEqual(afterInsert.plusSeconds(1)));

        Assertions.assertEquals(testSchedule.getCreateTime(), testSchedule.getUpdateTime());

        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testSchedule.getCreateTime(), testSchedule.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiAgentTaskScheduleMapper.insert(testSchedule);
        LocalDateTime originalUpdateTime = testSchedule.getUpdateTime();

        Thread.sleep(1000);

        testSchedule.setTaskName("测试自动填充更新时间");
        aiAgentTaskScheduleMapper.updateById(testSchedule);

        AiAgentTaskSchedule updated = aiAgentTaskScheduleMapper.queryById(testSchedule.getId());
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
        aiAgentTaskScheduleMapper.insert(testSchedule);
        Long id = testSchedule.getId();
        LocalDateTime beforeDelete = LocalDateTime.now();

        Thread.sleep(1000);

        aiAgentTaskScheduleMapper.deleteById(testSchedule);
        LocalDateTime afterDelete = LocalDateTime.now();

        AiAgentTaskSchedule deleted = aiAgentTaskScheduleMapper.queryById(id);
        Assertions.assertNull(deleted);

        Assertions.assertNotNull(testSchedule.getDeleteTime());
        Assertions.assertTrue(
                testSchedule.getDeleteTime().isAfter(beforeDelete) ||
                        testSchedule.getDeleteTime().isEqual(beforeDelete));
        Assertions.assertTrue(
                testSchedule.getDeleteTime().isBefore(afterDelete.plusSeconds(1)) ||
                        testSchedule.getDeleteTime().isEqual(afterDelete.plusSeconds(1)));

        log.info("AutoFillFieldsOnSoftDelete test passed. DeleteTime: {}", testSchedule.getDeleteTime());
    }

    @Test
    public void testTaskParamJsonField() {
        testSchedule.setTaskParam("{\"city\":\"长沙\",\"date\":\"2026-01-10\"}");
        aiAgentTaskScheduleMapper.insert(testSchedule);

        AiAgentTaskSchedule queried = aiAgentTaskScheduleMapper.queryById(testSchedule.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("{\"city\":\"长沙\",\"date\":\"2026-01-10\"}", queried.getTaskParam());
        log.info("TaskParamJsonField test passed. TaskParam: {}", queried.getTaskParam());
    }

    @Test
    public void testCronExpressionField() {
        testSchedule.setCronExpression("0 0/10 * * * ?");
        aiAgentTaskScheduleMapper.insert(testSchedule);

        AiAgentTaskSchedule queried = aiAgentTaskScheduleMapper.queryById(testSchedule.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("0 0/10 * * * ?", queried.getCronExpression());
        log.info("CronExpressionField test passed. CronExpression: {}", queried.getCronExpression());
    }

    @Test
    public void testSoftDeletedRecordsNotQueried() {
        aiAgentTaskScheduleMapper.insert(testSchedule);
        String agentId = testSchedule.getAgentId();

        aiAgentTaskScheduleMapper.deleteById(testSchedule);

        List<AiAgentTaskSchedule> schedulesByAgentId = aiAgentTaskScheduleMapper.queryByAgentId(agentId);
        Assertions.assertFalse(schedulesByAgentId.stream().anyMatch(s -> s.getId().equals(testSchedule.getId())));

        List<AiAgentTaskSchedule> allSchedules = aiAgentTaskScheduleMapper.queryAll();
        Assertions.assertFalse(allSchedules.stream().anyMatch(s -> s.getId().equals(testSchedule.getId())));

        log.info("SoftDeletedRecordsNotQueried test passed. Soft deleted records are not included in queries");
    }
}
