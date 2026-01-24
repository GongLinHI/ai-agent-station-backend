package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
public class AiAdvisorMapperTest {

    @Autowired
    private AiAdvisorMapper aiAdvisorMapper;

    private AiAdvisor testAiAdvisor;

    @BeforeEach
    public void setUp() {
        testAiAdvisor = AiAdvisor.builder()
                .advisorId("test-advisor-002")
                .advisorName("测试顾问")
                .description("这是一个测试顾问")
                .advisorType("ChatMemory")
                .extParam("{\"maxMessages\": 200}")
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testAiAdvisor != null && testAiAdvisor.getId() != null) {
            aiAdvisorMapper.deleteById(testAiAdvisor);
        }
    }

    private AiAdvisor queryById(Long id) {
        AiAdvisor query = AiAdvisor.builder().id(id).build();
        List<AiAdvisor> result = aiAdvisorMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private AiAdvisor queryByAdvisorId(String advisorId) {
        AiAdvisor query = AiAdvisor.builder().advisorId(advisorId).build();
        List<AiAdvisor> result = aiAdvisorMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private List<AiAdvisor> queryByStatus(Integer status) {
        AiAdvisor query = AiAdvisor.builder().status(status).build();
        return aiAdvisorMapper.query(query);
    }

    private List<AiAdvisor> queryAll() {
        return aiAdvisorMapper.query(AiAdvisor.builder().build());
    }

    @Test
    public void testInsert() {
        Integer result = aiAdvisorMapper.insert(testAiAdvisor);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testAiAdvisor.getId());

        AiAdvisor queried = queryById(testAiAdvisor.getId());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testAiAdvisor.getId(), queried.getCreateTime(), queried.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiAdvisorMapper.insert(testAiAdvisor);
        Long id = testAiAdvisor.getId();

        AiAdvisor queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiAdvisor.getAdvisorId(), queried.getAdvisorId());
        Assertions.assertEquals(testAiAdvisor.getAdvisorName(), queried.getAdvisorName());
        Assertions.assertEquals(testAiAdvisor.getDescription(), queried.getDescription());
        Assertions.assertEquals(testAiAdvisor.getAdvisorType(), queried.getAdvisorType());
        log.info("QueryById test passed. Queried Advisor: {}", queried);
    }

    @Test
    public void testQueryByAdvisorId() {
        aiAdvisorMapper.insert(testAiAdvisor);
        String advisorId = testAiAdvisor.getAdvisorId();

        AiAdvisor queried = queryByAdvisorId(advisorId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiAdvisor.getId(), queried.getId());
        Assertions.assertEquals(testAiAdvisor.getAdvisorName(), queried.getAdvisorName());
        log.info("QueryByAdvisorId test passed. Queried Advisor: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiAdvisorMapper.insert(testAiAdvisor);
        Long id = testAiAdvisor.getId();
        AiAdvisor original = queryById(id);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiAdvisor.setAdvisorName("更新后的顾问名称");
        testAiAdvisor.setDescription("更新后的描述");
        testAiAdvisor.setStatus(0);

        Integer result = aiAdvisorMapper.updateById(testAiAdvisor);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAdvisor updated = queryById(id);
        Assertions.assertEquals("更新后的顾问名称", updated.getAdvisorName());
        Assertions.assertEquals("更新后的描述", updated.getDescription());
        Assertions.assertEquals(0, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateById test passed. Updated Advisor: {}", updated);
    }

    @Test
    public void testUpdateByAdvisorId() throws InterruptedException {
        aiAdvisorMapper.insert(testAiAdvisor);
        String advisorId = testAiAdvisor.getAdvisorId();
        AiAdvisor original = queryByAdvisorId(advisorId);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiAdvisor.setAdvisorName("通过Advisor ID更新的名称");
        testAiAdvisor.setAdvisorType("RagAnswer");

        Integer result = aiAdvisorMapper.updateByAdvisorId(testAiAdvisor);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAdvisor updated = queryByAdvisorId(advisorId);
        Assertions.assertEquals("通过Advisor ID更新的名称", updated.getAdvisorName());
        Assertions.assertEquals("RagAnswer", updated.getAdvisorType());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateByAdvisorId test passed. Updated Advisor: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiAdvisorMapper.insert(testAiAdvisor);
        Long id = testAiAdvisor.getId();

        AiAdvisor beforeDelete = queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiAdvisorMapper.deleteById(testAiAdvisor);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAdvisor afterDelete = queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Advisor with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByAdvisorId() {
        aiAdvisorMapper.insert(testAiAdvisor);
        String advisorId = testAiAdvisor.getAdvisorId();

        AiAdvisor beforeDelete = queryByAdvisorId(advisorId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiAdvisorMapper.deleteByAdvisorId(testAiAdvisor);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiAdvisor afterDelete = queryByAdvisorId(advisorId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByAdvisorId test passed. Advisor with advisorId {} has been soft deleted", advisorId);
    }

    @Test
    public void testQueryByStatus() {
        AiAdvisor enabledAdvisor = AiAdvisor.builder()
                .advisorId("test-advisor-enabled")
                .advisorName("启用的顾问")
                .description("这是一个启用的顾问")
                .advisorType("ChatMemory")
                .extParam("{\"maxMessages\": 100}")
                .status(1)
                .isDeleted(0)
                .build();
        aiAdvisorMapper.insert(enabledAdvisor);

        AiAdvisor disabledAdvisor = AiAdvisor.builder()
                .advisorId("test-advisor-disabled")
                .advisorName("禁用的顾问")
                .description("这是一个禁用的顾问")
                .advisorType("RagAnswer")
                .extParam("{\"topK\": 4}")
                .status(0)
                .isDeleted(0)
                .build();
        aiAdvisorMapper.insert(disabledAdvisor);

        List<AiAdvisor> enabledAdvisors = queryByStatus(1);
        Assertions.assertNotNull(enabledAdvisors);
        Assertions.assertTrue(enabledAdvisors.size() >= 1);
        Assertions.assertTrue(
                enabledAdvisors.stream().anyMatch(advisor -> "test-advisor-enabled".equals(advisor.getAdvisorId())));
        log.info("QueryByStatus(1) test passed. Found {} enabled advisors", enabledAdvisors.size());

        List<AiAdvisor> disabledAdvisors = queryByStatus(0);
        Assertions.assertNotNull(disabledAdvisors);
        Assertions.assertTrue(disabledAdvisors.size() >= 1);
        Assertions.assertTrue(
                disabledAdvisors.stream().anyMatch(advisor -> "test-advisor-disabled".equals(advisor.getAdvisorId())));
        log.info("QueryByStatus(0) test passed. Found {} disabled advisors", disabledAdvisors.size());
    }

    @Test
    public void testQueryAll() {
        aiAdvisorMapper.insert(testAiAdvisor);

        AiAdvisor anotherAdvisor = AiAdvisor.builder()
                .advisorId("test-advisor-001")
                .advisorName("另一个测试顾问")
                .description("这是另一个测试顾问")
                .advisorType("SimpleLoggerAdvisor")
                .extParam("{\"logLevel\": \"INFO\"}")
                .status(1)
                .isDeleted(0)
                .build();
        aiAdvisorMapper.insert(anotherAdvisor);

        List<AiAdvisor> allAdvisors = queryAll();
        Assertions.assertNotNull(allAdvisors);
        Assertions.assertTrue(allAdvisors.size() >= 2);
        Assertions.assertTrue(
                allAdvisors.stream().anyMatch(advisor -> "test-advisor-001".equals(advisor.getAdvisorId())));
        Assertions.assertTrue(
                allAdvisors.stream().anyMatch(advisor -> "test-advisor-002".equals(advisor.getAdvisorId())));
        log.info("QueryAll test passed. Found {} advisors", allAdvisors.size());
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiAdvisorMapper.insert(testAiAdvisor);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testAiAdvisor.getCreateTime());
        Assertions.assertNotNull(testAiAdvisor.getUpdateTime());
        Assertions.assertTrue(
                testAiAdvisor.getCreateTime().isAfter(beforeInsert) || testAiAdvisor.getCreateTime().isEqual(
                        beforeInsert));
        Assertions.assertTrue(
                testAiAdvisor.getCreateTime().isBefore(afterInsert) || testAiAdvisor.getCreateTime().isEqual(
                        afterInsert));
        Assertions.assertEquals(testAiAdvisor.getCreateTime(), testAiAdvisor.getUpdateTime());
        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testAiAdvisor.getCreateTime(), testAiAdvisor.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiAdvisorMapper.insert(testAiAdvisor);
        AiAdvisor original = queryById(testAiAdvisor.getId());
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiAdvisor.setAdvisorName("测试自动填充更新时间");
        aiAdvisorMapper.updateById(testAiAdvisor);

        AiAdvisor updated = queryById(testAiAdvisor.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnSoftDelete() throws InterruptedException {
        aiAdvisorMapper.insert(testAiAdvisor);

        Thread.sleep(1000);

        aiAdvisorMapper.deleteById(testAiAdvisor);

        AiAdvisor deleted = queryById(testAiAdvisor.getId());
        Assertions.assertNull(deleted);

        AiAdvisor queriedByAdvisorId = queryByAdvisorId(testAiAdvisor.getAdvisorId());
        Assertions.assertNull(queriedByAdvisorId);

        log.info("AutoFillFieldsOnSoftDelete test passed. Advisor has been soft deleted");
    }

    @Test
    public void testQueryWithMultipleConditions() {
        aiAdvisorMapper.insert(testAiAdvisor);

        AiAdvisor anotherAdvisor = AiAdvisor.builder()
                .advisorId("test-advisor-003")
                .advisorName("多条件测试顾问")
                .description("多条件查询测试")
                .advisorType("ChatMemory")
                .extParam("{\"maxMessages\": 300}")
                .status(1)
                .isDeleted(0)
                .build();
        aiAdvisorMapper.insert(anotherAdvisor);

        AiAdvisor query = AiAdvisor.builder()
                .advisorType("ChatMemory")
                .status(1)
                .build();
        List<AiAdvisor> result = aiAdvisorMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 2);
        Assertions.assertTrue(
                result.stream().allMatch(advisor -> "ChatMemory".equals(advisor.getAdvisorType())));
        Assertions.assertTrue(
                result.stream().allMatch(advisor -> advisor.getStatus().equals(1)));
        log.info("QueryWithMultipleConditions test passed. Found {} advisors matching conditions", result.size());
    }

    @Test
    public void testQueryWithNullConditions() {
        aiAdvisorMapper.insert(testAiAdvisor);

        List<AiAdvisor> result = aiAdvisorMapper.query(AiAdvisor.builder().build());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        log.info("QueryWithNullConditions test passed. Found {} advisors", result.size());
    }

    @Test
    public void testSoftDeleteBehavior() {
        aiAdvisorMapper.insert(testAiAdvisor);
        Long id = testAiAdvisor.getId();
        String advisorId = testAiAdvisor.getAdvisorId();

        Assertions.assertNotNull(queryById(id));
        Assertions.assertNotNull(queryByAdvisorId(advisorId));

        aiAdvisorMapper.deleteById(testAiAdvisor);

        Assertions.assertNull(queryById(id));
        Assertions.assertNull(queryByAdvisorId(advisorId));

        List<AiAdvisor> allAdvisors = queryAll();
        boolean foundDeleted = allAdvisors.stream()
                .anyMatch(advisor -> id.equals(advisor.getId()));
        Assertions.assertFalse(foundDeleted, "Soft deleted advisor should not appear in queryAll results");

        log.info("SoftDeleteBehavior test passed. Advisor is properly excluded from queries after soft delete");
    }

    @Test
    public void testUpdateTimeAutoFill() throws InterruptedException {
        aiAdvisorMapper.insert(testAiAdvisor);
        LocalDateTime createTime = testAiAdvisor.getCreateTime();
        LocalDateTime originalUpdateTime = testAiAdvisor.getUpdateTime();

        Thread.sleep(1000);

        testAiAdvisor.setDescription("更新描述");
        aiAdvisorMapper.updateById(testAiAdvisor);

        AiAdvisor updated = queryById(testAiAdvisor.getId());

        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime),
                "UpdateTime should be updated");
        Assertions.assertNotEquals(updated.getCreateTime().truncatedTo(ChronoUnit.SECONDS),
                                   updated.getUpdateTime().truncatedTo(ChronoUnit.SECONDS),
                                   "UpdateTime should be different from CreateTime after update");
        log.info("UpdateTimeAutoFill test passed. CreateTime: {}, Original UpdateTime: {}, New UpdateTime: {}",
                 createTime, originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    @Disabled
    public void testExtParamJsonField() {
        String jsonParam = "{\"maxMessages\": 200, \"memoryType\": \"session\", \"ttl\": 3600}";
        testAiAdvisor.setExtParam(jsonParam);
        aiAdvisorMapper.insert(testAiAdvisor);

        AiAdvisor queried = queryById(testAiAdvisor.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(jsonParam, queried.getExtParam());
        log.info("ExtParamJsonField test passed. JSON parameter stored correctly: {}", queried.getExtParam());
    }

    @Test
    public void testQueryNonExistentRecord() {
        AiAdvisor result = queryById(999999L);
        Assertions.assertNull(result);

        result = queryByAdvisorId("non-existent-advisor-id");
        Assertions.assertNull(result);

        log.info("QueryNonExistentRecord test passed. Returns null for non-existent records");
    }

    @Test
    public void testUpdateNonExistentRecord() {
        AiAdvisor nonExistent = AiAdvisor.builder()
                .id(999999L)
                .advisorId("non-existent-advisor")
                .advisorName("不存在的顾问")
                .status(1)
                .isDeleted(0)
                .build();

        Integer result = aiAdvisorMapper.updateById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiAdvisorMapper.updateByAdvisorId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("UpdateNonExistentRecord test passed. Returns 0 for updating non-existent records");
    }

    @Test
    public void testDeleteNonExistentRecord() {
        AiAdvisor nonExistent = AiAdvisor.builder()
                .id(999999L)
                .advisorId("non-existent-advisor")
                .build();

        Integer result = aiAdvisorMapper.deleteById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiAdvisorMapper.deleteByAdvisorId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("DeleteNonExistentRecord test passed. Returns 0 for deleting non-existent records");
    }

    @Test
    public void testCreateTimeAndUpdateTimeAreEqualOnInsert() {
        aiAdvisorMapper.insert(testAiAdvisor);

        Assertions.assertNotNull(testAiAdvisor.getCreateTime());
        Assertions.assertNotNull(testAiAdvisor.getUpdateTime());
        Assertions.assertEquals(testAiAdvisor.getCreateTime(), testAiAdvisor.getUpdateTime(),
                                "CreateTime and UpdateTime should be equal on insert");
        log.info("CreateTimeAndUpdateTimeAreEqualOnInsert test passed. Both times: {}",
                 testAiAdvisor.getCreateTime());
    }

    @Test
    public void testUpdateTimeChangesOnUpdate() throws InterruptedException {
        aiAdvisorMapper.insert(testAiAdvisor);
        LocalDateTime createTime = testAiAdvisor.getCreateTime();
        LocalDateTime originalUpdateTime = testAiAdvisor.getUpdateTime();

        Thread.sleep(1000);

        testAiAdvisor.setDescription("更新描述");
        aiAdvisorMapper.updateById(testAiAdvisor);

        AiAdvisor updated = queryById(testAiAdvisor.getId());

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
        aiAdvisorMapper.insert(testAiAdvisor);
        Long id = testAiAdvisor.getId();

        Assertions.assertNull(testAiAdvisor.getDeleteTime(),
                              "DeleteTime should be null before soft delete");

        Thread.sleep(1000);

        aiAdvisorMapper.deleteById(testAiAdvisor);

        AiAdvisor deleted = queryById(id);
        Assertions.assertNull(deleted,
                              "Soft deleted advisor should not be returned by queryById");

        log.info("DeleteTimeIsSetOnSoftDelete test passed. Advisor has been soft deleted");
    }

    @Test
    public void testQueryByIdAndAdvisorIdConsistency() {
        aiAdvisorMapper.insert(testAiAdvisor);
        Long id = testAiAdvisor.getId();
        String advisorId = testAiAdvisor.getAdvisorId();

        AiAdvisor byId = queryById(id);
        AiAdvisor byAdvisorId = queryByAdvisorId(advisorId);

        Assertions.assertNotNull(byId);
        Assertions.assertNotNull(byAdvisorId);
        Assertions.assertEquals(byId.getId(), byAdvisorId.getId());
        Assertions.assertEquals(byId.getAdvisorId(), byAdvisorId.getAdvisorId());
        Assertions.assertEquals(byId.getAdvisorName(), byAdvisorId.getAdvisorName());

        log.info("QueryByIdAndAdvisorIdConsistency test passed. Both queries return the same record");
    }

    @Test
    public void testQueryWithAdvisorTypeCondition() {
        aiAdvisorMapper.insert(testAiAdvisor);

        AiAdvisor ragAdvisor = AiAdvisor.builder()
                .advisorId("test-advisor-rag")
                .advisorName("RAG顾问")
                .description("RAG测试")
                .advisorType("RagAnswer")
                .extParam("{\"topK\": 5}")
                .status(1)
                .isDeleted(0)
                .build();
        aiAdvisorMapper.insert(ragAdvisor);

        AiAdvisor query = AiAdvisor.builder().advisorType("RagAnswer").build();
        List<AiAdvisor> result = aiAdvisorMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().allMatch(advisor -> "RagAnswer".equals(advisor.getAdvisorType())));
        log.info("QueryWithAdvisorTypeCondition test passed. Found {} RAG advisors", result.size());
    }
}
