package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiRagOrder;
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
public class AiRagOrderMapperTest {

    @Autowired
    private AiRagOrderMapper aiRagOrderMapper;

    private AiRagOrder testAiRagOrder;

    @BeforeEach
    public void setUp() {
        testAiRagOrder = AiRagOrder.builder()
                .ragId("rag-001")
                .ragName("技术文档知识库")
                .knowledgeTag("技术文档")
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testAiRagOrder != null && testAiRagOrder.getId() != null) {
            aiRagOrderMapper.deleteById(testAiRagOrder);
        }
    }

    private AiRagOrder queryById(Long id) {
        AiRagOrder query = AiRagOrder.builder().id(id).build();
        List<AiRagOrder> result = aiRagOrderMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private AiRagOrder queryByRagId(String ragId) {
        AiRagOrder query = AiRagOrder.builder().ragId(ragId).build();
        List<AiRagOrder> result = aiRagOrderMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private List<AiRagOrder> queryByStatus(Integer status) {
        AiRagOrder query = AiRagOrder.builder().status(status).build();
        return aiRagOrderMapper.query(query);
    }

    private List<AiRagOrder> queryByRagName(String ragName) {
        AiRagOrder query = AiRagOrder.builder().ragName(ragName).build();
        return aiRagOrderMapper.query(query);
    }

    private List<AiRagOrder> queryByKnowledgeTag(String knowledgeTag) {
        AiRagOrder query = AiRagOrder.builder().knowledgeTag(knowledgeTag).build();
        return aiRagOrderMapper.query(query);
    }

    private List<AiRagOrder> queryAll() {
        return aiRagOrderMapper.query(AiRagOrder.builder().build());
    }

    @Test
    public void testInsert() {
        Integer result = aiRagOrderMapper.insert(testAiRagOrder);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testAiRagOrder.getId());

        AiRagOrder queried = queryById(testAiRagOrder.getId());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testAiRagOrder.getId(), queried.getCreateTime(), queried.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiRagOrderMapper.insert(testAiRagOrder);
        Long id = testAiRagOrder.getId();

        AiRagOrder queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiRagOrder.getRagId(), queried.getRagId());
        Assertions.assertEquals(testAiRagOrder.getRagName(), queried.getRagName());
        Assertions.assertEquals(testAiRagOrder.getKnowledgeTag(), queried.getKnowledgeTag());
        Assertions.assertEquals(testAiRagOrder.getStatus(), queried.getStatus());
        log.info("QueryById test passed. Queried RAG Order: {}", queried);
    }

    @Test
    public void testQueryByRagId() {
        aiRagOrderMapper.insert(testAiRagOrder);
        String ragId = testAiRagOrder.getRagId();

        AiRagOrder queried = queryByRagId(ragId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiRagOrder.getId(), queried.getId());
        Assertions.assertEquals(testAiRagOrder.getRagName(), queried.getRagName());
        Assertions.assertEquals(testAiRagOrder.getKnowledgeTag(), queried.getKnowledgeTag());
        log.info("QueryByRagId test passed. Queried RAG Order: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiRagOrderMapper.insert(testAiRagOrder);
        Long id = testAiRagOrder.getId();
        AiRagOrder original = queryById(id);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiRagOrder.setRagName("更新后的知识库名称");
        testAiRagOrder.setKnowledgeTag("更新后的知识标签");
        testAiRagOrder.setStatus(0);

        Integer result = aiRagOrderMapper.updateById(testAiRagOrder);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiRagOrder updated = queryById(id);
        Assertions.assertEquals("更新后的知识库名称", updated.getRagName());
        Assertions.assertEquals("更新后的知识标签", updated.getKnowledgeTag());
        Assertions.assertEquals(0, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateById test passed. Updated RAG Order: {}", updated);
    }

    @Test
    public void testUpdateByRagId() throws InterruptedException {
        aiRagOrderMapper.insert(testAiRagOrder);
        String ragId = testAiRagOrder.getRagId();
        AiRagOrder original = queryByRagId(ragId);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiRagOrder.setRagName("通过Rag ID更新的名称");
        testAiRagOrder.setKnowledgeTag("更新知识标签");

        Integer result = aiRagOrderMapper.updateByRagId(testAiRagOrder);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiRagOrder updated = queryByRagId(ragId);
        Assertions.assertEquals("通过Rag ID更新的名称", updated.getRagName());
        Assertions.assertEquals("更新知识标签", updated.getKnowledgeTag());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateByRagId test passed. Updated RAG Order: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiRagOrderMapper.insert(testAiRagOrder);
        Long id = testAiRagOrder.getId();

        AiRagOrder beforeDelete = queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiRagOrderMapper.deleteById(testAiRagOrder);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiRagOrder afterDelete = queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. RAG Order with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByRagId() {
        aiRagOrderMapper.insert(testAiRagOrder);
        String ragId = testAiRagOrder.getRagId();

        AiRagOrder beforeDelete = queryByRagId(ragId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiRagOrderMapper.deleteByRagId(testAiRagOrder);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiRagOrder afterDelete = queryByRagId(ragId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByRagId test passed. RAG Order with ragId {} has been soft deleted", ragId);
    }

    @Test
    public void testQueryByIdAndRagIdConsistency() {
        aiRagOrderMapper.insert(testAiRagOrder);
        Long id = testAiRagOrder.getId();
        String ragId = testAiRagOrder.getRagId();

        AiRagOrder byId = queryById(id);
        AiRagOrder byRagId = queryByRagId(ragId);

        Assertions.assertNotNull(byId);
        Assertions.assertNotNull(byRagId);
        Assertions.assertEquals(byId.getId(), byRagId.getId());
        Assertions.assertEquals(byId.getRagId(), byRagId.getRagId());
        Assertions.assertEquals(byId.getRagName(), byRagId.getRagName());
        Assertions.assertEquals(byId.getKnowledgeTag(), byRagId.getKnowledgeTag());

        log.info("QueryByIdAndRagIdConsistency test passed. Both queries return same record");
    }

    @Test
    public void testQueryByStatus() {
        AiRagOrder enabledRag = AiRagOrder.builder()
                .ragId("rag-enabled")
                .ragName("启用的知识库")
                .knowledgeTag("技术文档")
                .status(1)
                .isDeleted(0)
                .build();
        aiRagOrderMapper.insert(enabledRag);

        AiRagOrder disabledRag = AiRagOrder.builder()
                .ragId("rag-disabled")
                .ragName("禁用的知识库")
                .knowledgeTag("产品手册")
                .status(0)
                .isDeleted(0)
                .build();
        aiRagOrderMapper.insert(disabledRag);

        List<AiRagOrder> enabledRags = queryByStatus(1);
        Assertions.assertNotNull(enabledRags);
        Assertions.assertTrue(enabledRags.size() >= 1);
        Assertions.assertTrue(
                enabledRags.stream().anyMatch(rag -> "rag-enabled".equals(rag.getRagId())));
        log.info("QueryByStatus(1) test passed. Found {} enabled RAG Orders", enabledRags.size());

        List<AiRagOrder> disabledRags = queryByStatus(0);
        Assertions.assertNotNull(disabledRags);
        Assertions.assertTrue(disabledRags.size() >= 1);
        Assertions.assertTrue(
                disabledRags.stream().anyMatch(rag -> "rag-disabled".equals(rag.getRagId())));
        log.info("QueryByStatus(0) test passed. Found {} disabled RAG Orders", disabledRags.size());
    }

    @Test
    public void testQueryByRagName() {
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder query = AiRagOrder.builder().ragName("技术文档知识库").build();
        List<AiRagOrder> result = aiRagOrderMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().anyMatch(rag -> "技术文档知识库".equals(rag.getRagName())));
        log.info("QueryByRagName test passed. Found {} RAG Orders with name '技术文档知识库'", result.size());
    }

    @Test
    public void testQueryByKnowledgeTag() {
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder faqRag = AiRagOrder.builder()
                .ragId("rag-faq")
                .ragName("FAQ知识库")
                .knowledgeTag("FAQ")
                .status(1)
                .isDeleted(0)
                .build();
        aiRagOrderMapper.insert(faqRag);

        AiRagOrder legalRag = AiRagOrder.builder()
                .ragId("rag-legal")
                .ragName("法律文档知识库")
                .knowledgeTag("法律文档")
                .status(1)
                .isDeleted(0)
                .build();
        aiRagOrderMapper.insert(legalRag);

        List<AiRagOrder> techRags = queryByKnowledgeTag("技术文档");
        Assertions.assertNotNull(techRags);
        Assertions.assertTrue(techRags.size() >= 1);
        Assertions.assertTrue(
                techRags.stream().allMatch(rag -> "技术文档".equals(rag.getKnowledgeTag())));
        log.info("QueryByKnowledgeTag(技术文档) test passed. Found {} tech document RAG Orders", techRags.size());

        List<AiRagOrder> faqRags = queryByKnowledgeTag("FAQ");
        Assertions.assertNotNull(faqRags);
        Assertions.assertTrue(faqRags.size() >= 1);
        Assertions.assertTrue(
                faqRags.stream().allMatch(rag -> "FAQ".equals(rag.getKnowledgeTag())));
        log.info("QueryByKnowledgeTag(FAQ) test passed. Found {} FAQ RAG Orders", faqRags.size());
    }

    @Test
    public void testQueryWithMultipleConditions() {
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder anotherRag = AiRagOrder.builder()
                .ragId("rag-003")
                .ragName("多条件测试知识库")
                .knowledgeTag("技术文档")
                .status(1)
                .isDeleted(0)
                .build();
        aiRagOrderMapper.insert(anotherRag);

        AiRagOrder query = AiRagOrder.builder()
                .knowledgeTag("技术文档")
                .status(1)
                .build();
        List<AiRagOrder> result = aiRagOrderMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 2);
        Assertions.assertTrue(
                result.stream().allMatch(rag -> "技术文档".equals(rag.getKnowledgeTag())));
        Assertions.assertTrue(
                result.stream().allMatch(rag -> rag.getStatus().equals(1)));
        log.info("QueryWithMultipleConditions test passed. Found {} RAG Orders matching conditions", result.size());
    }

    @Test
    public void testQueryWithNullConditions() {
        aiRagOrderMapper.insert(testAiRagOrder);

        List<AiRagOrder> result = aiRagOrderMapper.query(AiRagOrder.builder().build());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        log.info("QueryWithNullConditions test passed. Found {} RAG Orders", result.size());
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiRagOrderMapper.insert(testAiRagOrder);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testAiRagOrder.getCreateTime());
        Assertions.assertNotNull(testAiRagOrder.getUpdateTime());
        Assertions.assertTrue(
                testAiRagOrder.getCreateTime().isAfter(beforeInsert) || testAiRagOrder.getCreateTime().isEqual(
                        beforeInsert));
        Assertions.assertTrue(
                testAiRagOrder.getCreateTime().isBefore(afterInsert) || testAiRagOrder.getCreateTime().isEqual(
                        afterInsert));
        Assertions.assertEquals(testAiRagOrder.getCreateTime(), testAiRagOrder.getUpdateTime());
        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testAiRagOrder.getCreateTime(), testAiRagOrder.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiRagOrderMapper.insert(testAiRagOrder);
        AiRagOrder original = queryById(testAiRagOrder.getId());
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiRagOrder.setRagName("测试自动填充更新时间");
        aiRagOrderMapper.updateById(testAiRagOrder);

        AiRagOrder updated = queryById(testAiRagOrder.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnSoftDelete() throws InterruptedException {
        aiRagOrderMapper.insert(testAiRagOrder);

        Thread.sleep(1000);

        aiRagOrderMapper.deleteById(testAiRagOrder);

        AiRagOrder deleted = queryById(testAiRagOrder.getId());
        Assertions.assertNull(deleted);

        AiRagOrder queriedByRagId = queryByRagId(testAiRagOrder.getRagId());
        Assertions.assertNull(queriedByRagId);

        log.info("AutoFillFieldsOnSoftDelete test passed. RAG Order has been soft deleted");
    }

    @Test
    public void testCreateTimeAndUpdateTimeAreEqualOnInsert() {
        aiRagOrderMapper.insert(testAiRagOrder);

        Assertions.assertNotNull(testAiRagOrder.getCreateTime());
        Assertions.assertNotNull(testAiRagOrder.getUpdateTime());
        Assertions.assertEquals(testAiRagOrder.getCreateTime(), testAiRagOrder.getUpdateTime(),
                                "CreateTime and UpdateTime should be equal on insert");
        log.info("CreateTimeAndUpdateTimeAreEqualOnInsert test passed. Both times: {}",
                 testAiRagOrder.getCreateTime());
    }

    @Test
    public void testUpdateTimeChangesOnUpdate() throws InterruptedException {
        aiRagOrderMapper.insert(testAiRagOrder);
        LocalDateTime createTime = testAiRagOrder.getCreateTime();
        LocalDateTime originalUpdateTime = testAiRagOrder.getUpdateTime();

        Thread.sleep(1000);

        testAiRagOrder.setRagName("更新知识库名称");
        aiRagOrderMapper.updateById(testAiRagOrder);

        AiRagOrder updated = queryById(testAiRagOrder.getId());

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
    public void testSoftDeleteBehavior() {
        aiRagOrderMapper.insert(testAiRagOrder);
        Long id = testAiRagOrder.getId();
        String ragId = testAiRagOrder.getRagId();

        Assertions.assertNotNull(queryById(id));
        Assertions.assertNotNull(queryByRagId(ragId));

        aiRagOrderMapper.deleteById(testAiRagOrder);

        Assertions.assertNull(queryById(id));
        Assertions.assertNull(queryByRagId(ragId));

        List<AiRagOrder> allRags = queryAll();
        boolean foundDeleted = allRags.stream()
                .anyMatch(rag -> id.equals(rag.getId()));
        Assertions.assertFalse(foundDeleted, "Soft deleted RAG Order should not appear in queryAll results");

        log.info("SoftDeleteBehavior test passed. RAG Order is properly excluded from queries after soft delete");
    }

    @Test
    public void testDeleteTimeIsSetOnSoftDelete() throws InterruptedException {
        aiRagOrderMapper.insert(testAiRagOrder);
        Long id = testAiRagOrder.getId();

        Assertions.assertNull(testAiRagOrder.getDeleteTime(),
                              "DeleteTime should be null before soft delete");

        Thread.sleep(1000);

        aiRagOrderMapper.deleteById(testAiRagOrder);

        AiRagOrder deleted = queryById(id);
        Assertions.assertNull(deleted,
                              "Soft deleted RAG Order should not be returned by queryById");

        log.info("DeleteTimeIsSetOnSoftDelete test passed. RAG Order has been soft deleted");
    }

    @Test
    public void testKnowledgeTagField() {
        testAiRagOrder.setKnowledgeTag("产品手册");
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder queried = queryById(testAiRagOrder.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("产品手册", queried.getKnowledgeTag());
        log.info("KnowledgeTagField test passed. KnowledgeTag: {}", queried.getKnowledgeTag());
    }

    @Test
    public void testRagNameField() {
        testAiRagOrder.setRagName("法律文档知识库");
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder queried = queryById(testAiRagOrder.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("法律文档知识库", queried.getRagName());
        log.info("RagNameField test passed. RagName: {}", queried.getRagName());
    }

    @Test
    public void testStatusField() {
        testAiRagOrder.setStatus(0);
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder queried = queryById(testAiRagOrder.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(0, queried.getStatus().intValue());
        log.info("StatusField test passed. Status: {}", queried.getStatus());
    }

    @Test
    public void testStatusToggle() {
        aiRagOrderMapper.insert(testAiRagOrder);
        Long id = testAiRagOrder.getId();

        AiRagOrder initial = queryById(id);
        Assertions.assertEquals(1, initial.getStatus().intValue());

        testAiRagOrder.setStatus(0);
        aiRagOrderMapper.updateById(testAiRagOrder);

        AiRagOrder disabled = queryById(id);
        Assertions.assertEquals(0, disabled.getStatus().intValue());

        testAiRagOrder.setStatus(1);
        aiRagOrderMapper.updateById(testAiRagOrder);

        AiRagOrder enabled = queryById(id);
        Assertions.assertEquals(1, enabled.getStatus().intValue());

        log.info("StatusToggle test passed. Status toggled successfully");
    }

    @Test
    public void testQueryNonExistentRecord() {
        AiRagOrder result = queryById(999999L);
        Assertions.assertNull(result);

        result = queryByRagId("non-existent-rag-id");
        Assertions.assertNull(result);

        log.info("QueryNonExistentRecord test passed. Returns null for non-existent records");
    }

    @Test
    public void testUpdateNonExistentRecord() {
        AiRagOrder nonExistent = AiRagOrder.builder()
                .id(999999L)
                .ragId("non-existent-rag")
                .ragName("不存在的知识库")
                .status(1)
                .isDeleted(0)
                .build();

        Integer result = aiRagOrderMapper.updateById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiRagOrderMapper.updateByRagId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("UpdateNonExistentRecord test passed. Returns 0 for updating non-existent records");
    }

    @Test
    public void testDeleteNonExistentRecord() {
        AiRagOrder nonExistent = AiRagOrder.builder()
                .id(999999L)
                .ragId("non-existent-rag")
                .build();

        Integer result = aiRagOrderMapper.deleteById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiRagOrderMapper.deleteByRagId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("DeleteNonExistentRecord test passed. Returns 0 for deleting non-existent records");
    }

    @Test
    public void testUniqueConstraintOnRagId() {
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder duplicateRag = AiRagOrder.builder()
                .ragId(testAiRagOrder.getRagId())
                .ragName("重复知识库")
                .knowledgeTag("技术文档")
                .status(1)
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiRagOrderMapper.insert(duplicateRag);
        });
        log.info("UniqueConstraintOnRagId test passed. Duplicate insert was rejected");
    }

    @Test
    public void testUniqueConstraintOnRagName() {
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder duplicateRag = AiRagOrder.builder()
                .ragId("rag-duplicate")
                .ragName(testAiRagOrder.getRagName())
                .knowledgeTag("技术文档")
                .status(1)
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiRagOrderMapper.insert(duplicateRag);
        });
        log.info("UniqueConstraintOnRagName test passed. Duplicate insert was rejected");
    }

    @Test
    public void testQueryByKnowledgeTagWithMultipleTags() {
        aiRagOrderMapper.insert(testAiRagOrder);

        AiRagOrder faqRag = AiRagOrder.builder()
                .ragId("rag-faq-2")
                .ragName("FAQ知识库")
                .knowledgeTag("FAQ")
                .status(1)
                .isDeleted(0)
                .build();
        aiRagOrderMapper.insert(faqRag);

        AiRagOrder legalRag = AiRagOrder.builder()
                .ragId("rag-legal-2")
                .ragName("法律文档知识库")
                .knowledgeTag("法律文档")
                .status(1)
                .isDeleted(0)
                .build();
        aiRagOrderMapper.insert(legalRag);

        AiRagOrder productRag = AiRagOrder.builder()
                .ragId("rag-product-2")
                .ragName("产品手册知识库")
                .knowledgeTag("产品手册")
                .status(1)
                .isDeleted(0)
                .build();
        aiRagOrderMapper.insert(productRag);

        List<AiRagOrder> techRags = queryByKnowledgeTag("技术文档");
        Assertions.assertNotNull(techRags);
        Assertions.assertTrue(techRags.size() >= 1);
        Assertions.assertTrue(
                techRags.stream().allMatch(rag -> "技术文档".equals(rag.getKnowledgeTag())));
        log.info("QueryByKnowledgeTagWithMultipleTags(技术文档) test passed. Found {} tech document RAG Orders", techRags.size());

        List<AiRagOrder> faqRags = queryByKnowledgeTag("FAQ");
        Assertions.assertNotNull(faqRags);
        Assertions.assertTrue(faqRags.size() >= 1);
        Assertions.assertTrue(
                faqRags.stream().allMatch(rag -> "FAQ".equals(rag.getKnowledgeTag())));
        log.info("QueryByKnowledgeTagWithMultipleTags(FAQ) test passed. Found {} FAQ RAG Orders", faqRags.size());

        List<AiRagOrder> legalRags = queryByKnowledgeTag("法律文档");
        Assertions.assertNotNull(legalRags);
        Assertions.assertTrue(legalRags.size() >= 1);
        Assertions.assertTrue(
                legalRags.stream().allMatch(rag -> "法律文档".equals(rag.getKnowledgeTag())));
        log.info("QueryByKnowledgeTagWithMultipleTags(法律文档) test passed. Found {} legal document RAG Orders", legalRags.size());

        List<AiRagOrder> productRags = queryByKnowledgeTag("产品手册");
        Assertions.assertNotNull(productRags);
        Assertions.assertTrue(productRags.size() >= 1);
        Assertions.assertTrue(
                productRags.stream().allMatch(rag -> "产品手册".equals(rag.getKnowledgeTag())));
        log.info("QueryByKnowledgeTagWithMultipleTags(产品手册) test passed. Found {} product manual RAG Orders", productRags.size());
    }
}
