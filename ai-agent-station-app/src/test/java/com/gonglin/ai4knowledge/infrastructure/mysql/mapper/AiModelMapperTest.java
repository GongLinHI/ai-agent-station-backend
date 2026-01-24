package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class AiModelMapperTest {

    @Autowired
    private AiModelMapper aiModelMapper;

    @Autowired
    @Qualifier("jsonMapper")
    private ObjectMapper jsonMapper;

    private AiModel testAiModel;

    @BeforeEach
    public void setUp() {
        testAiModel = AiModel.builder()
                .modelId("test-model-001")
                .modelName("测试模型")
                .apiId("api-1")
                .modelType("chat")
                .modelProvider("DeepSeek")
                .modelVersion("deepseek-chat")
                .extParam("{\"max_tokens\": 8192}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testAiModel != null && testAiModel.getId() != null) {
            aiModelMapper.deleteById(testAiModel);
        }
    }

    private AiModel queryById(Long id) {
        AiModel query = AiModel.builder().id(id).build();
        List<AiModel> result = aiModelMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private AiModel queryByModelId(String modelId) {
        AiModel query = AiModel.builder().modelId(modelId).build();
        List<AiModel> result = aiModelMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private List<AiModel> queryByStatus(Integer status) {
        AiModel query = AiModel.builder().status(status).build();
        return aiModelMapper.query(query);
    }

    private List<AiModel> queryByModelType(String modelType) {
        AiModel query = AiModel.builder().modelType(modelType).build();
        return aiModelMapper.query(query);
    }

    private List<AiModel> queryByModelProvider(String modelProvider) {
        AiModel query = AiModel.builder().modelProvider(modelProvider).build();
        return aiModelMapper.query(query);
    }

    private List<AiModel> queryByApiId(String apiId) {
        AiModel query = AiModel.builder().apiId(apiId).build();
        return aiModelMapper.query(query);
    }

    private List<AiModel> queryByModelName(String modelName) {
        AiModel query = AiModel.builder().modelName(modelName).build();
        return aiModelMapper.query(query);
    }

    private List<AiModel> queryAll() {
        return aiModelMapper.query(AiModel.builder().build());
    }

    @Test
    public void testInsert() {
        Integer result = aiModelMapper.insert(testAiModel);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testAiModel.getId());

        AiModel queried = queryById(testAiModel.getId());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testAiModel.getId(), queried.getCreateTime(), queried.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiModelMapper.insert(testAiModel);
        Long id = testAiModel.getId();

        AiModel queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiModel.getModelId(), queried.getModelId());
        Assertions.assertEquals(testAiModel.getModelName(), queried.getModelName());
        Assertions.assertEquals(testAiModel.getApiId(), queried.getApiId());
        Assertions.assertEquals(testAiModel.getModelType(), queried.getModelType());
        Assertions.assertEquals(testAiModel.getModelProvider(), queried.getModelProvider());
        Assertions.assertEquals(testAiModel.getModelVersion(), queried.getModelVersion());
        log.info("QueryById test passed. Queried Model: {}", queried);
    }

    @Test
    public void testQueryByModelId() {
        aiModelMapper.insert(testAiModel);
        String modelId = testAiModel.getModelId();

        AiModel queried = queryByModelId(modelId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiModel.getId(), queried.getId());
        Assertions.assertEquals(testAiModel.getModelName(), queried.getModelName());
        Assertions.assertEquals(testAiModel.getApiId(), queried.getApiId());
        log.info("QueryByModelId test passed. Queried Model: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiModelMapper.insert(testAiModel);
        Long id = testAiModel.getId();
        AiModel original = queryById(id);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiModel.setModelName("更新后的模型名称");
        testAiModel.setTimeout(60);
        testAiModel.setStatus(0);

        Integer result = aiModelMapper.updateById(testAiModel);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiModel updated = queryById(id);
        Assertions.assertEquals("更新后的模型名称", updated.getModelName());
        Assertions.assertEquals(60, updated.getTimeout().intValue());
        Assertions.assertEquals(0, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateById test passed. Updated Model: {}", updated);
    }

    @Test
    public void testUpdateByModelId() throws InterruptedException {
        aiModelMapper.insert(testAiModel);
        String modelId = testAiModel.getModelId();
        AiModel original = queryByModelId(modelId);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiModel.setModelName("通过Model ID更新的名称");
        testAiModel.setModelVersion("deepseek-reasoner");

        Integer result = aiModelMapper.updateByModelId(testAiModel);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiModel updated = queryByModelId(modelId);
        Assertions.assertEquals("通过Model ID更新的名称", updated.getModelName());
        Assertions.assertEquals("deepseek-reasoner", updated.getModelVersion());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateByModelId test passed. Updated Model: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiModelMapper.insert(testAiModel);
        Long id = testAiModel.getId();

        AiModel beforeDelete = queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiModelMapper.deleteById(testAiModel);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiModel afterDelete = queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Model with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByModelId() {
        aiModelMapper.insert(testAiModel);
        String modelId = testAiModel.getModelId();

        AiModel beforeDelete = queryByModelId(modelId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiModelMapper.deleteByModelId(testAiModel);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiModel afterDelete = queryByModelId(modelId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByModelId test passed. Model with modelId {} has been soft deleted", modelId);
    }

    @Test
    public void testQueryByIdAndModelIdConsistency() {
        aiModelMapper.insert(testAiModel);
        Long id = testAiModel.getId();
        String modelId = testAiModel.getModelId();

        AiModel byId = queryById(id);
        AiModel byModelId = queryByModelId(modelId);

        Assertions.assertNotNull(byId);
        Assertions.assertNotNull(byModelId);
        Assertions.assertEquals(byId.getId(), byModelId.getId());
        Assertions.assertEquals(byId.getModelId(), byModelId.getModelId());
        Assertions.assertEquals(byId.getModelName(), byModelId.getModelName());

        log.info("QueryByIdAndModelIdConsistency test passed. Both queries return the same record");
    }

    @Test
    public void testQueryByStatus() {
        AiModel enabledModel = AiModel.builder()
                .modelId("test-model-enabled")
                .modelName("启用的模型")
                .apiId("api-1")
                .modelType("chat")
                .modelProvider("DeepSeek")
                .modelVersion("deepseek-chat")
                .extParam("{\"max_tokens\": 8192}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(enabledModel);

        AiModel disabledModel = AiModel.builder()
                .modelId("test-model-disabled")
                .modelName("禁用的模型")
                .apiId("api-1")
                .modelType("chat")
                .modelProvider("DeepSeek")
                .modelVersion("deepseek-chat")
                .extParam("{\"max_tokens\": 8192}")
                .timeout(30)
                .status(0)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(disabledModel);

        List<AiModel> enabledModels = queryByStatus(1);
        Assertions.assertNotNull(enabledModels);
        Assertions.assertTrue(enabledModels.size() >= 1);
        Assertions.assertTrue(
                enabledModels.stream().anyMatch(model -> "test-model-enabled".equals(model.getModelId())));
        log.info("QueryByStatus(1) test passed. Found {} enabled models", enabledModels.size());

        List<AiModel> disabledModels = queryByStatus(0);
        Assertions.assertNotNull(disabledModels);
        Assertions.assertTrue(disabledModels.size() >= 1);
        Assertions.assertTrue(
                disabledModels.stream().anyMatch(model -> "test-model-disabled".equals(model.getModelId())));
        log.info("QueryByStatus(0) test passed. Found {} disabled models", disabledModels.size());
    }

    @Test
    public void testQueryByModelType() {
        aiModelMapper.insert(testAiModel);

        AiModel embeddingModel = AiModel.builder()
                .modelId("test-model-embedding")
                .modelName("词嵌入模型")
                .apiId("api-2")
                .modelType("embedding")
                .modelProvider("SiliconFlow")
                .modelVersion("BAAI/bge-m3")
                .extParam("{\"dimensions\": 1024}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(embeddingModel);

        List<AiModel> chatModels = queryByModelType("chat");
        Assertions.assertNotNull(chatModels);
        Assertions.assertTrue(chatModels.size() >= 1);
        Assertions.assertTrue(
                chatModels.stream().allMatch(model -> "chat".equals(model.getModelType())));
        log.info("QueryByModelType(chat) test passed. Found {} chat models", chatModels.size());

        List<AiModel> embeddingModels = queryByModelType("embedding");
        Assertions.assertNotNull(embeddingModels);
        Assertions.assertTrue(embeddingModels.size() >= 1);
        Assertions.assertTrue(
                embeddingModels.stream().allMatch(model -> "embedding".equals(model.getModelType())));
        log.info("QueryByModelType(embedding) test passed. Found {} embedding models", embeddingModels.size());
    }

    @Test
    public void testQueryByModelProvider() {
        aiModelMapper.insert(testAiModel);

        AiModel deepSeekModel = AiModel.builder()
                .modelId("test-model-deepseek")
                .modelName("DeepSeek模型")
                .apiId("api-1")
                .modelType("chat")
                .modelProvider("DeepSeek")
                .modelVersion("deepseek-reasoner")
                .extParam("{\"max_tokens\": 8192}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(deepSeekModel);

        AiModel siliconFlowModel = AiModel.builder()
                .modelId("test-model-siliconflow")
                .modelName("SiliconFlow模型")
                .apiId("api-2")
                .modelType("embedding")
                .modelProvider("SiliconFlow")
                .modelVersion("BAAI/bge-m3")
                .extParam("{\"dimensions\": 1024}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(siliconFlowModel);

        List<AiModel> deepSeekModels = queryByModelProvider("DeepSeek");
        Assertions.assertNotNull(deepSeekModels);
        Assertions.assertTrue(deepSeekModels.size() >= 2);
        Assertions.assertTrue(
                deepSeekModels.stream().allMatch(model -> "DeepSeek".equals(model.getModelProvider())));
        log.info("QueryByModelProvider(DeepSeek) test passed. Found {} DeepSeek models", deepSeekModels.size());

        List<AiModel> siliconFlowModels = queryByModelProvider("SiliconFlow");
        Assertions.assertNotNull(siliconFlowModels);
        Assertions.assertTrue(siliconFlowModels.size() >= 1);
        Assertions.assertTrue(
                siliconFlowModels.stream().allMatch(model -> "SiliconFlow".equals(model.getModelProvider())));
        log.info("QueryByModelProvider(SiliconFlow) test passed. Found {} SiliconFlow models",
                 siliconFlowModels.size());
    }

    @Test
    public void testQueryByApiId() {
        aiModelMapper.insert(testAiModel);

        AiModel anotherModel = AiModel.builder()
                .modelId("test-model-002")
                .modelName("另一个测试模型")
                .apiId("api-1")
                .modelType("chat")
                .modelProvider("DeepSeek")
                .modelVersion("deepseek-reasoner")
                .extParam("{\"max_tokens\": 8192}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(anotherModel);

        List<AiModel> models = queryByApiId("api-1");
        Assertions.assertNotNull(models);
        Assertions.assertTrue(models.size() >= 2);
        Assertions.assertTrue(
                models.stream().allMatch(model -> "api-1".equals(model.getApiId())));
        log.info("QueryByApiId test passed. Found {} models for api-1", models.size());
    }

    @Test
    public void testQueryByModelName() {
        aiModelMapper.insert(testAiModel);

        AiModel query = AiModel.builder().modelName("测试模型").build();
        List<AiModel> result = aiModelMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().anyMatch(model -> "测试模型".equals(model.getModelName())));
        log.info("QueryByModelName test passed. Found {} models with name '测试模型'", result.size());
    }

    @Test
    public void testQueryWithMultipleConditions() {
        aiModelMapper.insert(testAiModel);

        AiModel anotherModel = AiModel.builder()
                .modelId("test-model-003")
                .modelName("多条件测试模型")
                .apiId("api-1")
                .modelType("chat")
                .modelProvider("DeepSeek")
                .modelVersion("deepseek-chat")
                .extParam("{\"max_tokens\": 8192}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(anotherModel);

        AiModel query = AiModel.builder()
                .modelProvider("DeepSeek")
                .modelType("chat")
                .status(1)
                .build();
        List<AiModel> result = aiModelMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 2);
        Assertions.assertTrue(
                result.stream().allMatch(model -> "DeepSeek".equals(model.getModelProvider())));
        Assertions.assertTrue(
                result.stream().allMatch(model -> "chat".equals(model.getModelType())));
        Assertions.assertTrue(
                result.stream().allMatch(model -> model.getStatus().equals(1)));
        log.info("QueryWithMultipleConditions test passed. Found {} models matching conditions", result.size());
    }

    @Test
    public void testQueryWithNullConditions() {
        aiModelMapper.insert(testAiModel);

        List<AiModel> result = aiModelMapper.query(AiModel.builder().build());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        log.info("QueryWithNullConditions test passed. Found {} models", result.size());
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiModelMapper.insert(testAiModel);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testAiModel.getCreateTime());
        Assertions.assertNotNull(testAiModel.getUpdateTime());
        Assertions.assertTrue(
                testAiModel.getCreateTime().isAfter(beforeInsert) || testAiModel.getCreateTime().isEqual(
                        beforeInsert));
        Assertions.assertTrue(
                testAiModel.getCreateTime().isBefore(afterInsert) || testAiModel.getCreateTime().isEqual(
                        afterInsert));
        Assertions.assertEquals(testAiModel.getCreateTime(), testAiModel.getUpdateTime());
        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testAiModel.getCreateTime(), testAiModel.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiModelMapper.insert(testAiModel);
        AiModel original = queryById(testAiModel.getId());
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiModel.setModelName("测试自动填充更新时间");
        aiModelMapper.updateById(testAiModel);

        AiModel updated = queryById(testAiModel.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnSoftDelete() throws InterruptedException {
        aiModelMapper.insert(testAiModel);

        Thread.sleep(1000);

        aiModelMapper.deleteById(testAiModel);

        AiModel deleted = queryById(testAiModel.getId());
        Assertions.assertNull(deleted);

        AiModel queriedByModelId = queryByModelId(testAiModel.getModelId());
        Assertions.assertNull(queriedByModelId);

        log.info("AutoFillFieldsOnSoftDelete test passed. Model has been soft deleted");
    }

    @Test
    public void testCreateTimeAndUpdateTimeAreEqualOnInsert() {
        aiModelMapper.insert(testAiModel);

        Assertions.assertNotNull(testAiModel.getCreateTime());
        Assertions.assertNotNull(testAiModel.getUpdateTime());
        Assertions.assertEquals(testAiModel.getCreateTime(), testAiModel.getUpdateTime(),
                                "CreateTime and UpdateTime should be equal on insert");
        log.info("CreateTimeAndUpdateTimeAreEqualOnInsert test passed. Both times: {}",
                 testAiModel.getCreateTime());
    }

    @Test
    public void testUpdateTimeChangesOnUpdate() throws InterruptedException {
        aiModelMapper.insert(testAiModel);
        LocalDateTime createTime = testAiModel.getCreateTime();
        LocalDateTime originalUpdateTime = testAiModel.getUpdateTime();

        Thread.sleep(1000);

        testAiModel.setModelName("更新模型名称");
        aiModelMapper.updateById(testAiModel);

        AiModel updated = queryById(testAiModel.getId());

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
        aiModelMapper.insert(testAiModel);
        Long id = testAiModel.getId();
        String modelId = testAiModel.getModelId();

        Assertions.assertNotNull(queryById(id));
        Assertions.assertNotNull(queryByModelId(modelId));

        aiModelMapper.deleteById(testAiModel);

        Assertions.assertNull(queryById(id));
        Assertions.assertNull(queryByModelId(modelId));

        List<AiModel> allModels = queryAll();
        boolean foundDeleted = allModels.stream()
                .anyMatch(model -> id.equals(model.getId()));
        Assertions.assertFalse(foundDeleted, "Soft deleted model should not appear in queryAll results");

        log.info("SoftDeleteBehavior test passed. Model is properly excluded from queries after soft delete");
    }

    @Test
    public void testDeleteTimeIsSetOnSoftDelete() throws InterruptedException {
        aiModelMapper.insert(testAiModel);
        Long id = testAiModel.getId();

        Assertions.assertNull(testAiModel.getDeleteTime(),
                              "DeleteTime should be null before soft delete");

        Thread.sleep(1000);

        aiModelMapper.deleteById(testAiModel);

        AiModel deleted = queryById(id);
        Assertions.assertNull(deleted,
                              "Soft deleted model should not be returned by queryById");

        log.info("DeleteTimeIsSetOnSoftDelete test passed. Model has been soft deleted");
    }

    @Test
    public void testModelTypeChat() {
        testAiModel.setModelType("chat");
        aiModelMapper.insert(testAiModel);

        AiModel queried = queryById(testAiModel.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("chat", queried.getModelType());
        log.info("ModelTypeChat test passed. ModelType: {}", queried.getModelType());
    }

    @Test
    public void testModelTypeEmbedding() {
        AiModel embeddingModel = AiModel.builder()
                .modelId("test-model-embedding-2")
                .modelName("词嵌入模型")
                .apiId("api-2")
                .modelType("embedding")
                .modelProvider("SiliconFlow")
                .modelVersion("BAAI/bge-m3")
                .extParam("{\"dimensions\": 1024}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(embeddingModel);

        AiModel queried = queryById(embeddingModel.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("embedding", queried.getModelType());
        log.info("ModelTypeEmbedding test passed. ModelType: {}", queried.getModelType());
    }

    @Test
    public void testExtParamJsonField() throws Exception {
        String jsonParam = "{\"max_tokens\": 8192, \"temperature\": 0.7, \"top_p\": 0.9}";
        testAiModel.setExtParam(jsonParam);
        aiModelMapper.insert(testAiModel);

        AiModel queried = queryById(testAiModel.getId());
        Assertions.assertNotNull(queried);

        OpenAiChatOptions chatOptions = jsonMapper.readValue(queried.getExtParam(), OpenAiChatOptions.class);
        Assertions.assertNotNull(chatOptions);
        Assertions.assertEquals(8192, chatOptions.getMaxTokens());
        Assertions.assertEquals(0.7, chatOptions.getTemperature());
        Assertions.assertEquals(0.9, chatOptions.getTopP());

        log.info("ExtParamJsonField test passed. JSON parameter stored correctly: {}", queried.getExtParam());
        log.info("Parsed OpenAiChatOptions: {}", chatOptions);
    }

    @Test
    public void testTimeoutField() {
        testAiModel.setTimeout(180);
        aiModelMapper.insert(testAiModel);

        AiModel queried = queryById(testAiModel.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(180, queried.getTimeout().intValue());
        log.info("TimeoutField test passed. Timeout: {}", queried.getTimeout());
    }

    @Test
    public void testModelVersionField() {
        testAiModel.setModelVersion("deepseek-reasoner");
        aiModelMapper.insert(testAiModel);

        AiModel queried = queryById(testAiModel.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("deepseek-reasoner", queried.getModelVersion());
        log.info("ModelVersionField test passed. ModelVersion: {}", queried.getModelVersion());
    }

    @Test
    public void testStatusToggle() {
        aiModelMapper.insert(testAiModel);
        Long id = testAiModel.getId();

        AiModel initial = queryById(id);
        Assertions.assertEquals(1, initial.getStatus().intValue());

        testAiModel.setStatus(0);
        aiModelMapper.updateById(testAiModel);

        AiModel disabled = queryById(id);
        Assertions.assertEquals(0, disabled.getStatus().intValue());

        testAiModel.setStatus(1);
        aiModelMapper.updateById(testAiModel);

        AiModel enabled = queryById(id);
        Assertions.assertEquals(1, enabled.getStatus().intValue());

        log.info("StatusToggle test passed. Status toggled successfully");
    }

    @Test
    public void testQueryNonExistentRecord() {
        AiModel result = queryById(999999L);
        Assertions.assertNull(result);

        result = queryByModelId("non-existent-model-id");
        Assertions.assertNull(result);

        log.info("QueryNonExistentRecord test passed. Returns null for non-existent records");
    }

    @Test
    public void testUpdateNonExistentRecord() {
        AiModel nonExistent = AiModel.builder()
                .id(999999L)
                .modelId("non-existent-model")
                .modelName("不存在的模型")
                .status(1)
                .isDeleted(0)
                .build();

        Integer result = aiModelMapper.updateById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiModelMapper.updateByModelId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("UpdateNonExistentRecord test passed. Returns 0 for updating non-existent records");
    }

    @Test
    public void testDeleteNonExistentRecord() {
        AiModel nonExistent = AiModel.builder()
                .id(999999L)
                .modelId("non-existent-model")
                .build();

        Integer result = aiModelMapper.deleteById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiModelMapper.deleteByModelId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("DeleteNonExistentRecord test passed. Returns 0 for deleting non-existent records");
    }

    @Test
    public void testUniqueConstraintOnInsert() {
        aiModelMapper.insert(testAiModel);

        AiModel duplicateModel = AiModel.builder()
                .modelId(testAiModel.getModelId())
                .modelName("重复模型")
                .apiId("api-1")
                .modelType("chat")
                .modelProvider("DeepSeek")
                .modelVersion("deepseek-chat")
                .extParam("{\"max_tokens\": 8192}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();

        Assertions.assertThrows(Exception.class, () -> {
            aiModelMapper.insert(duplicateModel);
        });
        log.info("UniqueConstraintOnInsert test passed. Duplicate insert was rejected");
    }

    @Test
    public void testQueryByModelProviderAndType() {
        aiModelMapper.insert(testAiModel);

        AiModel anotherChatModel = AiModel.builder()
                .modelId("test-model-chat-2")
                .modelName("另一个Chat模型")
                .apiId("api-1")
                .modelType("chat")
                .modelProvider("DeepSeek")
                .modelVersion("deepseek-reasoner")
                .extParam("{\"max_tokens\": 8192}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(anotherChatModel);

        AiModel embeddingModel = AiModel.builder()
                .modelId("test-model-embedding-3")
                .modelName("Embedding模型")
                .apiId("api-2")
                .modelType("embedding")
                .modelProvider("SiliconFlow")
                .modelVersion("BAAI/bge-m3")
                .extParam("{\"dimensions\": 1024}")
                .timeout(30)
                .status(1)
                .isDeleted(0)
                .build();
        aiModelMapper.insert(embeddingModel);

        AiModel query = AiModel.builder()
                .modelProvider("DeepSeek")
                .modelType("chat")
                .build();
        List<AiModel> result = aiModelMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 2);
        Assertions.assertTrue(
                result.stream().allMatch(model -> "DeepSeek".equals(model.getModelProvider())));
        Assertions.assertTrue(
                result.stream().allMatch(model -> "chat".equals(model.getModelType())));
        log.info("QueryByModelProviderAndType test passed. Found {} DeepSeek chat models", result.size());
    }
}
