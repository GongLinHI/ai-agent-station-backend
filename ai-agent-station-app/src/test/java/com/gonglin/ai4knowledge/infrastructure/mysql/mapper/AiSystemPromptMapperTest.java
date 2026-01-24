package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;
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
public class AiSystemPromptMapperTest {

    @Autowired
    private AiSystemPromptMapper aiSystemPromptMapper;

    private AiSystemPrompt testAiSystemPrompt;

    @BeforeEach
    public void setUp() {
        testAiSystemPrompt = AiSystemPrompt.builder()
                .promptId("test-prompt-001")
                .promptName("测试提示词")
                .promptContent("你是一个专业的AI助手，请帮助用户解决问题。")
                .description("这是一个测试用的系统提示词")
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testAiSystemPrompt != null && testAiSystemPrompt.getId() != null) {
            aiSystemPromptMapper.deleteById(testAiSystemPrompt);
        }
    }

    private AiSystemPrompt queryById(Long id) {
        AiSystemPrompt query = AiSystemPrompt.builder().id(id).build();
        List<AiSystemPrompt> result = aiSystemPromptMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private AiSystemPrompt queryByPromptId(String promptId) {
        AiSystemPrompt query = AiSystemPrompt.builder().promptId(promptId).build();
        List<AiSystemPrompt> result = aiSystemPromptMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private List<AiSystemPrompt> queryByPromptName(String promptName) {
        AiSystemPrompt query = AiSystemPrompt.builder().promptName(promptName).build();
        return aiSystemPromptMapper.query(query);
    }

    private List<AiSystemPrompt> queryByStatus(Integer status) {
        AiSystemPrompt query = AiSystemPrompt.builder().status(status).build();
        return aiSystemPromptMapper.query(query);
    }

    private List<AiSystemPrompt> queryAll() {
        return aiSystemPromptMapper.query(AiSystemPrompt.builder().build());
    }

    @Test
    public void testInsert() {
        Integer result = aiSystemPromptMapper.insert(testAiSystemPrompt);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testAiSystemPrompt.getId());

        AiSystemPrompt queried = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testAiSystemPrompt.getId(), queried.getCreateTime(), queried.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        Long id = testAiSystemPrompt.getId();

        AiSystemPrompt queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiSystemPrompt.getPromptId(), queried.getPromptId());
        Assertions.assertEquals(testAiSystemPrompt.getPromptName(), queried.getPromptName());
        Assertions.assertEquals(testAiSystemPrompt.getPromptContent(), queried.getPromptContent());
        Assertions.assertEquals(testAiSystemPrompt.getDescription(), queried.getDescription());
        Assertions.assertEquals(testAiSystemPrompt.getStatus(), queried.getStatus());
        log.info("QueryById test passed. Queried SystemPrompt: {}", queried);
    }

    @Test
    public void testQueryByPromptId() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        String promptId = testAiSystemPrompt.getPromptId();

        AiSystemPrompt queried = queryByPromptId(promptId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiSystemPrompt.getId(), queried.getId());
        Assertions.assertEquals(testAiSystemPrompt.getPromptName(), queried.getPromptName());
        Assertions.assertEquals(testAiSystemPrompt.getPromptContent(), queried.getPromptContent());
        log.info("QueryByPromptId test passed. Queried SystemPrompt: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        Long id = testAiSystemPrompt.getId();
        AiSystemPrompt original = queryById(id);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiSystemPrompt.setPromptName("更新后的提示词名称");
        testAiSystemPrompt.setDescription("更新后的描述信息");
        testAiSystemPrompt.setStatus(0);

        Integer result = aiSystemPromptMapper.updateById(testAiSystemPrompt);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiSystemPrompt updated = queryById(id);
        Assertions.assertEquals("更新后的提示词名称", updated.getPromptName());
        Assertions.assertEquals("更新后的描述信息", updated.getDescription());
        Assertions.assertEquals(0, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateById test passed. Updated SystemPrompt: {}", updated);
    }

    @Test
    public void testUpdateByPromptId() throws InterruptedException {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        String promptId = testAiSystemPrompt.getPromptId();
        AiSystemPrompt original = queryByPromptId(promptId);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiSystemPrompt.setPromptName("通过Prompt ID更新的名称");
        testAiSystemPrompt.setDescription("通过Prompt ID更新的描述");

        Integer result = aiSystemPromptMapper.updateByPromptId(testAiSystemPrompt);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiSystemPrompt updated = queryByPromptId(promptId);
        Assertions.assertEquals("通过Prompt ID更新的名称", updated.getPromptName());
        Assertions.assertEquals("通过Prompt ID更新的描述", updated.getDescription());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateByPromptId test passed. Updated SystemPrompt: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        Long id = testAiSystemPrompt.getId();

        AiSystemPrompt beforeDelete = queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiSystemPromptMapper.deleteById(testAiSystemPrompt);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiSystemPrompt afterDelete = queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. SystemPrompt with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByPromptId() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        String promptId = testAiSystemPrompt.getPromptId();

        AiSystemPrompt beforeDelete = queryByPromptId(promptId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiSystemPromptMapper.deleteByPromptId(testAiSystemPrompt);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiSystemPrompt afterDelete = queryByPromptId(promptId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByPromptId test passed. SystemPrompt with promptId {} has been soft deleted", promptId);
    }

    @Test
    public void testSoftDeleteBehavior() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        Long id = testAiSystemPrompt.getId();
        String promptId = testAiSystemPrompt.getPromptId();

        Assertions.assertNotNull(queryById(id));
        Assertions.assertNotNull(queryByPromptId(promptId));

        aiSystemPromptMapper.deleteById(testAiSystemPrompt);

        Assertions.assertNull(queryById(id));
        Assertions.assertNull(queryByPromptId(promptId));

        List<AiSystemPrompt> allPrompts = queryAll();
        boolean foundDeleted = allPrompts.stream()
                .anyMatch(prompt -> id.equals(prompt.getId()));
        Assertions.assertFalse(foundDeleted, "Soft deleted prompt should not appear in queryAll results");

        log.info("SoftDeleteBehavior test passed. SystemPrompt is properly excluded from queries after soft delete");
    }

    @Test
    public void testQueryByPromptName() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt query = AiSystemPrompt.builder().promptName("测试提示词").build();
        List<AiSystemPrompt> result = aiSystemPromptMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().anyMatch(prompt -> "测试提示词".equals(prompt.getPromptName())));
        log.info("QueryByPromptName test passed. Found {} prompts with name '测试提示词'", result.size());
    }

    @Test
    public void testQueryByStatus() {
        AiSystemPrompt enabledPrompt = AiSystemPrompt.builder()
                .promptId("test-prompt-enabled")
                .promptName("启用的提示词")
                .promptContent("启用的提示词内容")
                .description("这是一个启用的提示词")
                .status(1)
                .isDeleted(0)
                .build();
        aiSystemPromptMapper.insert(enabledPrompt);

        AiSystemPrompt disabledPrompt = AiSystemPrompt.builder()
                .promptId("test-prompt-disabled")
                .promptName("禁用的提示词")
                .promptContent("禁用的提示词内容")
                .description("这是一个禁用的提示词")
                .status(0)
                .isDeleted(0)
                .build();
        aiSystemPromptMapper.insert(disabledPrompt);

        List<AiSystemPrompt> enabledPrompts = queryByStatus(1);
        Assertions.assertNotNull(enabledPrompts);
        Assertions.assertTrue(enabledPrompts.size() >= 1);
        Assertions.assertTrue(
                enabledPrompts.stream().anyMatch(prompt -> "test-prompt-enabled".equals(prompt.getPromptId())));
        log.info("QueryByStatus(1) test passed. Found {} enabled prompts", enabledPrompts.size());

        List<AiSystemPrompt> disabledPrompts = queryByStatus(0);
        Assertions.assertNotNull(disabledPrompts);
        Assertions.assertTrue(disabledPrompts.size() >= 1);
        Assertions.assertTrue(
                disabledPrompts.stream().anyMatch(prompt -> "test-prompt-disabled".equals(prompt.getPromptId())));
        log.info("QueryByStatus(0) test passed. Found {} disabled prompts", disabledPrompts.size());
    }

    @Test
    public void testQueryAll() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt anotherPrompt = AiSystemPrompt.builder()
                .promptId("test-prompt-002")
                .promptName("另一个测试提示词")
                .promptContent("另一个测试提示词内容")
                .description("这是另一个测试提示词")
                .status(1)
                .isDeleted(0)
                .build();
        aiSystemPromptMapper.insert(anotherPrompt);

        List<AiSystemPrompt> allPrompts = queryAll();
        Assertions.assertNotNull(allPrompts);
        Assertions.assertTrue(allPrompts.size() >= 2);
        Assertions.assertTrue(
                allPrompts.stream().anyMatch(prompt -> "test-prompt-001".equals(prompt.getPromptId())));
        Assertions.assertTrue(
                allPrompts.stream().anyMatch(prompt -> "test-prompt-002".equals(prompt.getPromptId())));
        log.info("QueryAll test passed. Found {} prompts", allPrompts.size());
    }

    @Test
    public void testQueryWithMultipleConditions() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt anotherPrompt = AiSystemPrompt.builder()
                .promptId("test-prompt-003")
                .promptName("多条件测试提示词")
                .promptContent("多条件查询测试")
                .description("多条件查询测试")
                .status(1)
                .isDeleted(0)
                .build();
        aiSystemPromptMapper.insert(anotherPrompt);

        AiSystemPrompt query = AiSystemPrompt.builder()
                .status(1)
                .build();
        List<AiSystemPrompt> result = aiSystemPromptMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 2);
        Assertions.assertTrue(
                result.stream().allMatch(prompt -> prompt.getStatus().equals(1)));
        log.info("QueryWithMultipleConditions test passed. Found {} prompts matching conditions", result.size());
    }

    @Test
    public void testQueryWithNullConditions() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        List<AiSystemPrompt> result = aiSystemPromptMapper.query(AiSystemPrompt.builder().build());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        log.info("QueryWithNullConditions test passed. Found {} prompts", result.size());
    }

    @Test
    public void testQueryByIdAndPromptIdConsistency() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        Long id = testAiSystemPrompt.getId();
        String promptId = testAiSystemPrompt.getPromptId();

        AiSystemPrompt byId = queryById(id);
        AiSystemPrompt byPromptId = queryByPromptId(promptId);

        Assertions.assertNotNull(byId);
        Assertions.assertNotNull(byPromptId);
        Assertions.assertEquals(byId.getId(), byPromptId.getId());
        Assertions.assertEquals(byId.getPromptId(), byPromptId.getPromptId());
        Assertions.assertEquals(byId.getPromptName(), byPromptId.getPromptName());

        log.info("QueryByIdAndPromptIdConsistency test passed. Both queries return the same record");
    }

    @Test
    public void testPromptContentField() {
        String promptContent = "你是一个专业的AI助手，擅长帮助用户解决各种问题。请根据用户的需求提供准确、有用的回答。";
        testAiSystemPrompt.setPromptContent(promptContent);
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt queried = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(promptContent, queried.getPromptContent());
        log.info("PromptContentField test passed. PromptContent: {}", queried.getPromptContent());
    }

    @Test
    public void testDescriptionField() {
        String description = "这是一个详细的系统提示词描述，用于测试描述字段的功能";
        testAiSystemPrompt.setDescription(description);
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt queried = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(description, queried.getDescription());
        log.info("DescriptionField test passed. Description: {}", queried.getDescription());
    }

    @Test
    public void testLongDescription() {
        StringBuilder longDescription = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            longDescription.append("这是一个很长的描述内容，用于测试数据库字段长度限制。");
        }
        testAiSystemPrompt.setDescription(longDescription.toString());
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt queried = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(longDescription.toString(), queried.getDescription());
        log.info("LongDescription test passed. Description length: {}", queried.getDescription().length());
    }

    @Test
    public void testNullDescription() {
        testAiSystemPrompt.setDescription(null);
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt queried = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertNull(queried.getDescription());
        log.info("NullDescription test passed. Description is null as expected");
    }

    @Test
    public void testEmptyDescription() {
        testAiSystemPrompt.setDescription("");
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt queried = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("", queried.getDescription());
        log.info("EmptyDescription test passed. Description is empty as expected");
    }

    @Test
    public void testStatusToggle() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        Long id = testAiSystemPrompt.getId();

        AiSystemPrompt initial = queryById(id);
        Assertions.assertEquals(1, initial.getStatus().intValue());

        testAiSystemPrompt.setStatus(0);
        aiSystemPromptMapper.updateById(testAiSystemPrompt);

        AiSystemPrompt disabled = queryById(id);
        Assertions.assertEquals(0, disabled.getStatus().intValue());

        testAiSystemPrompt.setStatus(1);
        aiSystemPromptMapper.updateById(testAiSystemPrompt);

        AiSystemPrompt enabled = queryById(id);
        Assertions.assertEquals(1, enabled.getStatus().intValue());

        log.info("StatusToggle test passed. Status toggled successfully");
    }

    @Test
    public void testLongPromptContent() {
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            longContent.append("这是一段很长的提示词内容，用于测试TEXT类型字段的存储能力。");
        }
        testAiSystemPrompt.setPromptContent(longContent.toString());
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt queried = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(longContent.toString(), queried.getPromptContent());
        log.info("LongPromptContent test passed. PromptContent length: {}", queried.getPromptContent().length());
    }

    @Test
    public void testSpecialCharactersInPromptContent() {
        String specialContent = "你是一个AI助手，请处理以下特殊字符：\n换行符\t制表符\"双引号\'单引号\\反斜杠";
        testAiSystemPrompt.setPromptContent(specialContent);
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt queried = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(specialContent, queried.getPromptContent());
        log.info("SpecialCharactersInPromptContent test passed. Special characters stored correctly");
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testAiSystemPrompt.getCreateTime());
        Assertions.assertNotNull(testAiSystemPrompt.getUpdateTime());
        Assertions.assertTrue(
                testAiSystemPrompt.getCreateTime().isAfter(beforeInsert) || testAiSystemPrompt.getCreateTime().isEqual(
                        beforeInsert));
        Assertions.assertTrue(
                testAiSystemPrompt.getCreateTime().isBefore(afterInsert) || testAiSystemPrompt.getCreateTime().isEqual(
                        afterInsert));
        Assertions.assertEquals(testAiSystemPrompt.getCreateTime(), testAiSystemPrompt.getUpdateTime());
        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testAiSystemPrompt.getCreateTime(), testAiSystemPrompt.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        AiSystemPrompt original = queryById(testAiSystemPrompt.getId());
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiSystemPrompt.setDescription("测试自动填充更新时间");
        aiSystemPromptMapper.updateById(testAiSystemPrompt);

        AiSystemPrompt updated = queryById(testAiSystemPrompt.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testCreateTimeAndUpdateTimeAreEqualOnInsert() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        Assertions.assertNotNull(testAiSystemPrompt.getCreateTime());
        Assertions.assertNotNull(testAiSystemPrompt.getUpdateTime());
        Assertions.assertEquals(testAiSystemPrompt.getCreateTime(), testAiSystemPrompt.getUpdateTime(),
                                "CreateTime and UpdateTime should be equal on insert");
        log.info("CreateTimeAndUpdateTimeAreEqualOnInsert test passed. Both times: {}",
                 testAiSystemPrompt.getCreateTime());
    }

    @Test
    public void testUpdateTimeChangesOnUpdate() throws InterruptedException {
        aiSystemPromptMapper.insert(testAiSystemPrompt);
        LocalDateTime createTime = testAiSystemPrompt.getCreateTime();
        LocalDateTime originalUpdateTime = testAiSystemPrompt.getUpdateTime();

        Thread.sleep(1000);

        testAiSystemPrompt.setDescription("更新描述");
        aiSystemPromptMapper.updateById(testAiSystemPrompt);

        AiSystemPrompt updated = queryById(testAiSystemPrompt.getId());

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
    public void testQueryNonExistentRecord() {
        AiSystemPrompt result = queryById(999999L);
        Assertions.assertNull(result);

        result = queryByPromptId("non-existent-prompt-id");
        Assertions.assertNull(result);

        log.info("QueryNonExistentRecord test passed. Returns null for non-existent records");
    }

    @Test
    public void testUpdateNonExistentRecord() {
        AiSystemPrompt nonExistent = AiSystemPrompt.builder()
                .id(999999L)
                .promptId("non-existent-prompt")
                .promptName("不存在的提示词")
                .status(1)
                .isDeleted(0)
                .build();

        Integer result = aiSystemPromptMapper.updateById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiSystemPromptMapper.updateByPromptId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("UpdateNonExistentRecord test passed. Returns 0 for updating non-existent records");
    }

    @Test
    public void testDeleteNonExistentRecord() {
        AiSystemPrompt nonExistent = AiSystemPrompt.builder()
                .id(999999L)
                .promptId("non-existent-prompt")
                .build();

        Integer result = aiSystemPromptMapper.deleteById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiSystemPromptMapper.deleteByPromptId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("DeleteNonExistentRecord test passed. Returns 0 for deleting non-existent records");
    }

    @Test
    public void testUniqueConstraintOnInsert() {
        aiSystemPromptMapper.insert(testAiSystemPrompt);

        AiSystemPrompt duplicatePrompt = AiSystemPrompt.builder()
                .promptId(testAiSystemPrompt.getPromptId())
                .promptName("重复提示词")
                .promptContent("重复提示词内容")
                .description("重复提示词描述")
                .status(1)
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiSystemPromptMapper.insert(duplicatePrompt);
        });
        log.info("UniqueConstraintOnInsert test passed. Duplicate insert was rejected");
    }

    @Test
    public void testMultipleInsertsAndQueries() {
        AiSystemPrompt prompt1 = AiSystemPrompt.builder()
                .promptId("test-prompt-101")
                .promptName("提示词101")
                .promptContent("第一个提示词内容")
                .description("第一个提示词")
                .status(1)
                .isDeleted(0)
                .build();
        aiSystemPromptMapper.insert(prompt1);

        AiSystemPrompt prompt2 = AiSystemPrompt.builder()
                .promptId("test-prompt-102")
                .promptName("提示词102")
                .promptContent("第二个提示词内容")
                .description("第二个提示词")
                .status(1)
                .isDeleted(0)
                .build();
        aiSystemPromptMapper.insert(prompt2);

        AiSystemPrompt prompt3 = AiSystemPrompt.builder()
                .promptId("test-prompt-103")
                .promptName("提示词103")
                .promptContent("第三个提示词内容")
                .description("第三个提示词")
                .status(0)
                .isDeleted(0)
                .build();
        aiSystemPromptMapper.insert(prompt3);

        List<AiSystemPrompt> allPrompts = queryAll();
        Assertions.assertTrue(allPrompts.size() >= 3);

        List<AiSystemPrompt> enabledPrompts = queryByStatus(1);
        Assertions.assertTrue(enabledPrompts.size() >= 2);

        log.info("MultipleInsertsAndQueries test passed. Total: {}, Enabled: {}", allPrompts.size(),
                 enabledPrompts.size());
    }
}
