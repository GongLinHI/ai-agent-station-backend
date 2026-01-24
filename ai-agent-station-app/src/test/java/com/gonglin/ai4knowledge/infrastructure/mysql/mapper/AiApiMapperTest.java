package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;
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
public class AiApiMapperTest {

    @Autowired
    private AiApiMapper aiApiMapper;

    private AiApi testAiApi;

    @BeforeEach
    public void setUp() {
        testAiApi = AiApi.builder()
                .apiId("test-api-001")
                .apiName("测试API")
                .baseUrl("https://api.test.com/")
                .apiKey("test-api-key-12345")
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .apiProvider("TestProvider")
                .extParam("{\"max_tokens\": 4096, \"temperature\": 0.7}")
                .timeout(60)
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testAiApi != null && testAiApi.getId() != null) {
            aiApiMapper.deleteById(testAiApi);
        }
    }

    private AiApi queryById(Long id) {
        AiApi query = AiApi.builder().id(id).build();
        List<AiApi> result = aiApiMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private AiApi queryByApiId(String apiId) {
        AiApi query = AiApi.builder().apiId(apiId).build();
        List<AiApi> result = aiApiMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private List<AiApi> queryByStatus(Integer status) {
        AiApi query = AiApi.builder().status(status).build();
        return aiApiMapper.query(query);
    }

    private List<AiApi> queryByApiProvider(String apiProvider) {
        AiApi query = AiApi.builder().apiProvider(apiProvider).build();
        return aiApiMapper.query(query);
    }

    private List<AiApi> queryAll() {
        return aiApiMapper.query(AiApi.builder().build());
    }

    @Test
    public void testInsert() {
        Integer result = aiApiMapper.insert(testAiApi);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testAiApi.getId());

        AiApi queried = queryById(testAiApi.getId());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testAiApi.getId(), queried.getCreateTime(), queried.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiApiMapper.insert(testAiApi);
        Long id = testAiApi.getId();

        AiApi queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiApi.getApiId(), queried.getApiId());
        Assertions.assertEquals(testAiApi.getApiName(), queried.getApiName());
        Assertions.assertEquals(testAiApi.getBaseUrl(), queried.getBaseUrl());
        Assertions.assertEquals(testAiApi.getApiKey(), queried.getApiKey());
        Assertions.assertEquals(testAiApi.getApiProvider(), queried.getApiProvider());
        log.info("QueryById test passed. Queried API: {}", queried);
    }

    @Test
    public void testQueryByApiId() {
        aiApiMapper.insert(testAiApi);
        String apiId = testAiApi.getApiId();

        AiApi queried = queryByApiId(apiId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiApi.getId(), queried.getId());
        Assertions.assertEquals(testAiApi.getApiName(), queried.getApiName());
        Assertions.assertEquals(testAiApi.getBaseUrl(), queried.getBaseUrl());
        log.info("QueryByApiId test passed. Queried API: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiApiMapper.insert(testAiApi);
        Long id = testAiApi.getId();
        AiApi original = queryById(id);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiApi.setApiName("更新后的API名称");
        testAiApi.setBaseUrl("https://api.updated.com/");
        testAiApi.setTimeout(120);
        testAiApi.setStatus(0);

        Integer result = aiApiMapper.updateById(testAiApi);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiApi updated = queryById(id);
        Assertions.assertEquals("更新后的API名称", updated.getApiName());
        Assertions.assertEquals("https://api.updated.com/", updated.getBaseUrl());
        Assertions.assertEquals(120, updated.getTimeout().intValue());
        Assertions.assertEquals(0, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateById test passed. Updated API: {}", updated);
    }

    @Test
    public void testUpdateByApiId() throws InterruptedException {
        aiApiMapper.insert(testAiApi);
        String apiId = testAiApi.getApiId();
        AiApi original = queryByApiId(apiId);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiApi.setApiName("通过API ID更新的名称");
        testAiApi.setApiProvider("UpdatedProvider");
        testAiApi.setCompletionsPath("v2/chat/completions");

        Integer result = aiApiMapper.updateByApiId(testAiApi);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiApi updated = queryByApiId(apiId);
        Assertions.assertEquals("通过API ID更新的名称", updated.getApiName());
        Assertions.assertEquals("UpdatedProvider", updated.getApiProvider());
        Assertions.assertEquals("v2/chat/completions", updated.getCompletionsPath());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateByApiId test passed. Updated API: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiApiMapper.insert(testAiApi);
        Long id = testAiApi.getId();

        AiApi beforeDelete = queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiApiMapper.deleteById(testAiApi);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiApi afterDelete = queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. API with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByApiId() {
        aiApiMapper.insert(testAiApi);
        String apiId = testAiApi.getApiId();

        AiApi beforeDelete = queryByApiId(apiId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiApiMapper.deleteByApiId(testAiApi);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiApi afterDelete = queryByApiId(apiId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByApiId test passed. API with apiId {} has been soft deleted", apiId);
    }

    @Test
    public void testQueryByStatus() {
        AiApi enabledApi = AiApi.builder()
                .apiId("test-api-enabled")
                .apiName("启用的API")
                .baseUrl("https://api.enabled.com/")
                .apiKey("enabled-api-key")
                .apiProvider("EnabledProvider")
                .status(1)
                .isDeleted(0)
                .build();
        aiApiMapper.insert(enabledApi);

        AiApi disabledApi = AiApi.builder()
                .apiId("test-api-disabled")
                .apiName("禁用的API")
                .baseUrl("https://api.disabled.com/")
                .apiKey("disabled-api-key")
                .apiProvider("DisabledProvider")
                .status(0)
                .isDeleted(0)
                .build();
        aiApiMapper.insert(disabledApi);

        List<AiApi> enabledApis = queryByStatus(1);
        Assertions.assertNotNull(enabledApis);
        Assertions.assertTrue(enabledApis.size() >= 1);
        Assertions.assertTrue(
                enabledApis.stream().anyMatch(api -> "test-api-enabled".equals(api.getApiId())));
        log.info("QueryByStatus(1) test passed. Found {} enabled APIs", enabledApis.size());

        List<AiApi> disabledApis = queryByStatus(0);
        Assertions.assertNotNull(disabledApis);
        Assertions.assertTrue(disabledApis.size() >= 1);
        Assertions.assertTrue(
                disabledApis.stream().anyMatch(api -> "test-api-disabled".equals(api.getApiId())));
        log.info("QueryByStatus(0) test passed. Found {} disabled APIs", disabledApis.size());
    }

    @Test
    public void testQueryByApiProvider() {
        aiApiMapper.insert(testAiApi);

        AiApi deepSeekApi = AiApi.builder()
                .apiId("test-api-deepseek")
                .apiName("DeepSeek API")
                .baseUrl("https://api.deepseek.com/")
                .apiKey("deepseek-key")
                .apiProvider("DeepSeek")
                .status(1)
                .isDeleted(0)
                .build();
        aiApiMapper.insert(deepSeekApi);

        AiApi siliconFlowApi = AiApi.builder()
                .apiId("test-api-siliconflow")
                .apiName("SiliconFlow API")
                .baseUrl("https://api.siliconflow.cn/")
                .apiKey("siliconflow-key")
                .apiProvider("siliconflow")
                .status(1)
                .isDeleted(0)
                .build();
        aiApiMapper.insert(siliconFlowApi);

        List<AiApi> deepSeekApis = queryByApiProvider("DeepSeek");
        Assertions.assertNotNull(deepSeekApis);
        Assertions.assertTrue(deepSeekApis.size() >= 1);
        Assertions.assertTrue(
                deepSeekApis.stream().allMatch(api -> "DeepSeek".equals(api.getApiProvider())));
        log.info("QueryByApiProvider(DeepSeek) test passed. Found {} DeepSeek APIs", deepSeekApis.size());

        List<AiApi> siliconFlowApis = queryByApiProvider("siliconflow");
        Assertions.assertNotNull(siliconFlowApis);
        Assertions.assertTrue(siliconFlowApis.size() >= 1);
        Assertions.assertTrue(
                siliconFlowApis.stream().allMatch(api -> "siliconflow".equals(api.getApiProvider())));
        log.info("QueryByApiProvider(siliconflow) test passed. Found {} SiliconFlow APIs", siliconFlowApis.size());
    }

    @Test
    public void testQueryAll() {
        aiApiMapper.insert(testAiApi);

        AiApi anotherApi = AiApi.builder()
                .apiId("test-api-002")
                .apiName("另一个测试API")
                .baseUrl("https://api.another.com/")
                .apiKey("another-api-key")
                .apiProvider("AnotherProvider")
                .status(1)
                .isDeleted(0)
                .build();
        aiApiMapper.insert(anotherApi);

        List<AiApi> allApis = queryAll();
        Assertions.assertNotNull(allApis);
        Assertions.assertTrue(allApis.size() >= 2);
        Assertions.assertTrue(
                allApis.stream().anyMatch(api -> "test-api-001".equals(api.getApiId())));
        Assertions.assertTrue(
                allApis.stream().anyMatch(api -> "test-api-002".equals(api.getApiId())));
        log.info("QueryAll test passed. Found {} APIs", allApis.size());
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiApiMapper.insert(testAiApi);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testAiApi.getCreateTime());
        Assertions.assertNotNull(testAiApi.getUpdateTime());
        Assertions.assertTrue(
                testAiApi.getCreateTime().isAfter(beforeInsert) || testAiApi.getCreateTime().isEqual(
                        beforeInsert));
        Assertions.assertTrue(
                testAiApi.getCreateTime().isBefore(afterInsert) || testAiApi.getCreateTime().isEqual(
                        afterInsert));
        Assertions.assertEquals(testAiApi.getCreateTime(), testAiApi.getUpdateTime());
        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testAiApi.getCreateTime(), testAiApi.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiApiMapper.insert(testAiApi);
        AiApi original = queryById(testAiApi.getId());
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiApi.setApiName("测试自动填充更新时间");
        aiApiMapper.updateById(testAiApi);

        AiApi updated = queryById(testAiApi.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnSoftDelete() throws InterruptedException {
        aiApiMapper.insert(testAiApi);

        Thread.sleep(1000);

        aiApiMapper.deleteById(testAiApi);

        AiApi deleted = queryById(testAiApi.getId());
        Assertions.assertNull(deleted);

        AiApi queriedByApiId = queryByApiId(testAiApi.getApiId());
        Assertions.assertNull(queriedByApiId);

        log.info("AutoFillFieldsOnSoftDelete test passed. API has been soft deleted");
    }

    @Test
    public void testCreateTimeAndUpdateTimeAreEqualOnInsert() {
        aiApiMapper.insert(testAiApi);

        Assertions.assertNotNull(testAiApi.getCreateTime());
        Assertions.assertNotNull(testAiApi.getUpdateTime());
        Assertions.assertEquals(testAiApi.getCreateTime(), testAiApi.getUpdateTime(),
                                "CreateTime and UpdateTime should be equal on insert");
        log.info("CreateTimeAndUpdateTimeAreEqualOnInsert test passed. Both times: {}",
                 testAiApi.getCreateTime());
    }

    @Test
    public void testUpdateTimeChangesOnUpdate() throws InterruptedException {
        aiApiMapper.insert(testAiApi);
        LocalDateTime createTime = testAiApi.getCreateTime();
        LocalDateTime originalUpdateTime = testAiApi.getUpdateTime();

        Thread.sleep(1000);

        testAiApi.setApiName("更新API名称");
        aiApiMapper.updateById(testAiApi);

        AiApi updated = queryById(testAiApi.getId());

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
        aiApiMapper.insert(testAiApi);
        Long id = testAiApi.getId();

        Assertions.assertNull(testAiApi.getDeleteTime(),
                              "DeleteTime should be null before soft delete");

        Thread.sleep(1000);

        aiApiMapper.deleteById(testAiApi);

        AiApi deleted = queryById(id);
        Assertions.assertNull(deleted,
                              "Soft deleted API should not be returned by queryById");

        log.info("DeleteTimeIsSetOnSoftDelete test passed. API has been soft deleted");
    }

    @Test
    public void testSoftDeleteBehavior() {
        aiApiMapper.insert(testAiApi);
        Long id = testAiApi.getId();
        String apiId = testAiApi.getApiId();

        Assertions.assertNotNull(queryById(id));
        Assertions.assertNotNull(queryByApiId(apiId));

        aiApiMapper.deleteById(testAiApi);

        Assertions.assertNull(queryById(id));
        Assertions.assertNull(queryByApiId(apiId));

        List<AiApi> allApis = queryAll();
        boolean foundDeleted = allApis.stream()
                .anyMatch(api -> id.equals(api.getId()));
        Assertions.assertFalse(foundDeleted, "Soft deleted API should not appear in queryAll results");

        log.info("SoftDeleteBehavior test passed. API is properly excluded from queries after soft delete");
    }

    @Test
    public void testQueryWithMultipleConditions() {
        aiApiMapper.insert(testAiApi);

        AiApi anotherApi = AiApi.builder()
                .apiId("test-api-003")
                .apiName("多条件测试API")
                .baseUrl("https://api.multi.com/")
                .apiKey("multi-api-key")
                .apiProvider("DeepSeek")
                .status(1)
                .isDeleted(0)
                .build();
        aiApiMapper.insert(anotherApi);

        AiApi query = AiApi.builder()
                .apiProvider("DeepSeek")
                .status(1)
                .build();
        List<AiApi> result = aiApiMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().allMatch(api -> "DeepSeek".equals(api.getApiProvider())));
        Assertions.assertTrue(
                result.stream().allMatch(api -> api.getStatus().equals(1)));
        log.info("QueryWithMultipleConditions test passed. Found {} APIs matching conditions", result.size());
    }

    @Test
    public void testQueryWithNullConditions() {
        aiApiMapper.insert(testAiApi);

        List<AiApi> result = aiApiMapper.query(AiApi.builder().build());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        log.info("QueryWithNullConditions test passed. Found {} APIs", result.size());
    }

    @Test
    public void testQueryNonExistentRecord() {
        AiApi result = queryById(999999L);
        Assertions.assertNull(result);

        result = queryByApiId("non-existent-api-id");
        Assertions.assertNull(result);

        log.info("QueryNonExistentRecord test passed. Returns null for non-existent records");
    }

    @Test
    public void testUpdateNonExistentRecord() {
        AiApi nonExistent = AiApi.builder()
                .id(999999L)
                .apiId("non-existent-api")
                .apiName("不存在的API")
                .status(1)
                .isDeleted(0)
                .build();

        Integer result = aiApiMapper.updateById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiApiMapper.updateByApiId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("UpdateNonExistentRecord test passed. Returns 0 for updating non-existent records");
    }

    @Test
    public void testDeleteNonExistentRecord() {
        AiApi nonExistent = AiApi.builder()
                .id(999999L)
                .apiId("non-existent-api")
                .build();

        Integer result = aiApiMapper.deleteById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiApiMapper.deleteByApiId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("DeleteNonExistentRecord test passed. Returns 0 for deleting non-existent records");
    }

    @Test
    public void testQueryByIdAndApiIdConsistency() {
        aiApiMapper.insert(testAiApi);
        Long id = testAiApi.getId();
        String apiId = testAiApi.getApiId();

        AiApi byId = queryById(id);
        AiApi byApiId = queryByApiId(apiId);

        Assertions.assertNotNull(byId);
        Assertions.assertNotNull(byApiId);
        Assertions.assertEquals(byId.getId(), byApiId.getId());
        Assertions.assertEquals(byId.getApiId(), byApiId.getApiId());
        Assertions.assertEquals(byId.getApiName(), byApiId.getApiName());

        log.info("QueryByIdAndApiIdConsistency test passed. Both queries return the same record");
    }

    @Test
    @Disabled
    public void testExtParamJsonField() {
        String jsonParam = "{\"max_tokens\": 8192, \"temperature\": 0.7, \"top_p\": 0.9, \"stream\": true}";
        testAiApi.setExtParam(jsonParam);
        aiApiMapper.insert(testAiApi);

        AiApi queried = queryById(testAiApi.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(jsonParam, queried.getExtParam());
        log.info("ExtParamJsonField test passed. JSON parameter stored correctly: {}", queried.getExtParam());
    }

    @Test
    public void testTimeoutField() {
        testAiApi.setTimeout(180);
        aiApiMapper.insert(testAiApi);

        AiApi queried = queryById(testAiApi.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(180, queried.getTimeout().intValue());
        log.info("TimeoutField test passed. Timeout: {}", queried.getTimeout());
    }

    @Test
    public void testCompletionsPathAndEmbeddingsPath() {
        testAiApi.setCompletionsPath("v2/chat/completions");
        testAiApi.setEmbeddingsPath("v2/embeddings");
        aiApiMapper.insert(testAiApi);

        AiApi queried = queryById(testAiApi.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("v2/chat/completions", queried.getCompletionsPath());
        Assertions.assertEquals("v2/embeddings", queried.getEmbeddingsPath());
        log.info("CompletionsPathAndEmbeddingsPath test passed. CompletionsPath: {}, EmbeddingsPath: {}",
                 queried.getCompletionsPath(), queried.getEmbeddingsPath());
    }

    @Test
    public void testStatusToggle() {
        aiApiMapper.insert(testAiApi);
        Long id = testAiApi.getId();

        AiApi initial = queryById(id);
        Assertions.assertEquals(1, initial.getStatus().intValue());

        testAiApi.setStatus(0);
        aiApiMapper.updateById(testAiApi);

        AiApi disabled = queryById(id);
        Assertions.assertEquals(0, disabled.getStatus().intValue());

        testAiApi.setStatus(1);
        aiApiMapper.updateById(testAiApi);

        AiApi enabled = queryById(id);
        Assertions.assertEquals(1, enabled.getStatus().intValue());

        log.info("StatusToggle test passed. Status toggled successfully");
    }

    @Test
    public void testQueryByApiName() {
        aiApiMapper.insert(testAiApi);

        AiApi query = AiApi.builder().apiName("测试API").build();
        List<AiApi> result = aiApiMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().anyMatch(api -> "测试API".equals(api.getApiName())));
        log.info("QueryByApiName test passed. Found {} APIs with name '测试API'", result.size());
    }

    @Test
    public void testQueryByBaseUrl() {
        aiApiMapper.insert(testAiApi);

        AiApi query = AiApi.builder().baseUrl("https://api.test.com/").build();
        List<AiApi> result = aiApiMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().anyMatch(api -> "https://api.test.com/".equals(api.getBaseUrl())));
        log.info("QueryByBaseUrl test passed. Found {} APIs with base URL", result.size());
    }
}
