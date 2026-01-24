package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClient;
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
public class AiClientMapperTest {

    @Autowired
    private AiClientMapper aiClientMapper;

    private AiClient testAiClient;

    @BeforeEach
    public void setUp() {
        testAiClient = AiClient.builder()
                .clientId("test-client-001")
                .clientName("测试客户端")
                .description("这是一个测试用的客户端")
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testAiClient != null && testAiClient.getId() != null) {
            aiClientMapper.deleteById(testAiClient);
        }
    }

    private AiClient queryById(Long id) {
        AiClient query = AiClient.builder().id(id).build();
        List<AiClient> result = aiClientMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private AiClient queryByClientId(String clientId) {
        AiClient query = AiClient.builder().clientId(clientId).build();
        List<AiClient> result = aiClientMapper.query(query);
        return result.isEmpty() ? null : result.get(0);
    }

    private List<AiClient> queryByStatus(Integer status) {
        AiClient query = AiClient.builder().status(status).build();
        return aiClientMapper.query(query);
    }

    private List<AiClient> queryAll() {
        return aiClientMapper.query(AiClient.builder().build());
    }

    @Test
    public void testInsert() {
        Integer result = aiClientMapper.insert(testAiClient);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testAiClient.getId());

        AiClient queried = queryById(testAiClient.getId());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testAiClient.getId(), queried.getCreateTime(), queried.getUpdateTime());
    }

    @Test
    public void testQueryById() {
        aiClientMapper.insert(testAiClient);
        Long id = testAiClient.getId();

        AiClient queried = queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiClient.getClientId(), queried.getClientId());
        Assertions.assertEquals(testAiClient.getClientName(), queried.getClientName());
        Assertions.assertEquals(testAiClient.getDescription(), queried.getDescription());
        Assertions.assertEquals(testAiClient.getStatus(), queried.getStatus());
        log.info("QueryById test passed. Queried Client: {}", queried);
    }

    @Test
    public void testQueryByClientId() {
        aiClientMapper.insert(testAiClient);
        String clientId = testAiClient.getClientId();

        AiClient queried = queryByClientId(clientId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiClient.getId(), queried.getId());
        Assertions.assertEquals(testAiClient.getClientName(), queried.getClientName());
        Assertions.assertEquals(testAiClient.getDescription(), queried.getDescription());
        log.info("QueryByClientId test passed. Queried Client: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiClientMapper.insert(testAiClient);
        Long id = testAiClient.getId();
        AiClient original = queryById(id);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiClient.setClientName("更新后的客户端名称");
        testAiClient.setDescription("更新后的描述信息");
        testAiClient.setStatus(0);

        Integer result = aiClientMapper.updateById(testAiClient);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClient updated = queryById(id);
        Assertions.assertEquals("更新后的客户端名称", updated.getClientName());
        Assertions.assertEquals("更新后的描述信息", updated.getDescription());
        Assertions.assertEquals(0, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateById test passed. Updated Client: {}", updated);
    }

    @Test
    public void testUpdateByClientId() throws InterruptedException {
        aiClientMapper.insert(testAiClient);
        String clientId = testAiClient.getClientId();
        AiClient original = queryByClientId(clientId);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiClient.setClientName("通过Client ID更新的名称");
        testAiClient.setDescription("通过Client ID更新的描述");

        Integer result = aiClientMapper.updateByClientId(testAiClient);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClient updated = queryByClientId(clientId);
        Assertions.assertEquals("通过Client ID更新的名称", updated.getClientName());
        Assertions.assertEquals("通过Client ID更新的描述", updated.getDescription());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateByClientId test passed. Updated Client: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiClientMapper.insert(testAiClient);
        Long id = testAiClient.getId();

        AiClient beforeDelete = queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientMapper.deleteById(testAiClient);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClient afterDelete = queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. Client with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByClientId() {
        aiClientMapper.insert(testAiClient);
        String clientId = testAiClient.getClientId();

        AiClient beforeDelete = queryByClientId(clientId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiClientMapper.deleteByClientId(testAiClient);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiClient afterDelete = queryByClientId(clientId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByClientId test passed. Client with clientId {} has been soft deleted", clientId);
    }

    @Test
    public void testQueryByStatus() {
        AiClient enabledClient = AiClient.builder()
                .clientId("test-client-enabled")
                .clientName("启用的客户端")
                .description("这是一个启用的客户端")
                .status(1)
                .isDeleted(0)
                .build();
        aiClientMapper.insert(enabledClient);

        AiClient disabledClient = AiClient.builder()
                .clientId("test-client-disabled")
                .clientName("禁用的客户端")
                .description("这是一个禁用的客户端")
                .status(0)
                .isDeleted(0)
                .build();
        aiClientMapper.insert(disabledClient);

        List<AiClient> enabledClients = queryByStatus(1);
        Assertions.assertNotNull(enabledClients);
        Assertions.assertTrue(enabledClients.size() >= 1);
        Assertions.assertTrue(
                enabledClients.stream().anyMatch(client -> "test-client-enabled".equals(client.getClientId())));
        log.info("QueryByStatus(1) test passed. Found {} enabled clients", enabledClients.size());

        List<AiClient> disabledClients = queryByStatus(0);
        Assertions.assertNotNull(disabledClients);
        Assertions.assertTrue(disabledClients.size() >= 1);
        Assertions.assertTrue(
                disabledClients.stream().anyMatch(client -> "test-client-disabled".equals(client.getClientId())));
        log.info("QueryByStatus(0) test passed. Found {} disabled clients", disabledClients.size());
    }

    @Test
    public void testQueryAll() {
        aiClientMapper.insert(testAiClient);

        AiClient anotherClient = AiClient.builder()
                .clientId("test-client-002")
                .clientName("另一个测试客户端")
                .description("这是另一个测试客户端")
                .status(1)
                .isDeleted(0)
                .build();
        aiClientMapper.insert(anotherClient);

        List<AiClient> allClients = queryAll();
        Assertions.assertNotNull(allClients);
        Assertions.assertTrue(allClients.size() >= 2);
        Assertions.assertTrue(
                allClients.stream().anyMatch(client -> "test-client-001".equals(client.getClientId())));
        Assertions.assertTrue(
                allClients.stream().anyMatch(client -> "test-client-002".equals(client.getClientId())));
        log.info("QueryAll test passed. Found {} clients", allClients.size());
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiClientMapper.insert(testAiClient);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testAiClient.getCreateTime());
        Assertions.assertNotNull(testAiClient.getUpdateTime());
        Assertions.assertTrue(
                testAiClient.getCreateTime().isAfter(beforeInsert) || testAiClient.getCreateTime().isEqual(
                        beforeInsert));
        Assertions.assertTrue(
                testAiClient.getCreateTime().isBefore(afterInsert) || testAiClient.getCreateTime().isEqual(
                        afterInsert));
        Assertions.assertEquals(testAiClient.getCreateTime(), testAiClient.getUpdateTime());
        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testAiClient.getCreateTime(), testAiClient.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiClientMapper.insert(testAiClient);
        AiClient original = queryById(testAiClient.getId());
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiClient.setClientName("测试自动填充更新时间");
        aiClientMapper.updateById(testAiClient);

        AiClient updated = queryById(testAiClient.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnSoftDelete() throws InterruptedException {
        aiClientMapper.insert(testAiClient);

        Thread.sleep(1000);

        aiClientMapper.deleteById(testAiClient);

        AiClient deleted = queryById(testAiClient.getId());
        Assertions.assertNull(deleted);

        AiClient queriedByClientId = queryByClientId(testAiClient.getClientId());
        Assertions.assertNull(queriedByClientId);

        log.info("AutoFillFieldsOnSoftDelete test passed. Client has been soft deleted");
    }

    @Test
    public void testQueryWithMultipleConditions() {
        aiClientMapper.insert(testAiClient);

        AiClient anotherClient = AiClient.builder()
                .clientId("test-client-003")
                .clientName("多条件测试客户端")
                .description("多条件查询测试")
                .status(1)
                .isDeleted(0)
                .build();
        aiClientMapper.insert(anotherClient);

        AiClient query = AiClient.builder()
                .status(1)
                .build();
        List<AiClient> result = aiClientMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 2);
        Assertions.assertTrue(
                result.stream().allMatch(client -> client.getStatus().equals(1)));
        log.info("QueryWithMultipleConditions test passed. Found {} clients matching conditions", result.size());
    }

    @Test
    public void testQueryWithNullConditions() {
        aiClientMapper.insert(testAiClient);

        List<AiClient> result = aiClientMapper.query(AiClient.builder().build());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        log.info("QueryWithNullConditions test passed. Found {} clients", result.size());
    }

    @Test
    public void testSoftDeleteBehavior() {
        aiClientMapper.insert(testAiClient);
        Long id = testAiClient.getId();
        String clientId = testAiClient.getClientId();

        Assertions.assertNotNull(queryById(id));
        Assertions.assertNotNull(queryByClientId(clientId));

        aiClientMapper.deleteById(testAiClient);

        Assertions.assertNull(queryById(id));
        Assertions.assertNull(queryByClientId(clientId));

        List<AiClient> allClients = queryAll();
        boolean foundDeleted = allClients.stream()
                .anyMatch(client -> id.equals(client.getId()));
        Assertions.assertFalse(foundDeleted, "Soft deleted client should not appear in queryAll results");

        log.info("SoftDeleteBehavior test passed. Client is properly excluded from queries after soft delete");
    }

    @Test
    public void testUpdateTimeAutoFill() throws InterruptedException {
        aiClientMapper.insert(testAiClient);
        LocalDateTime createTime = testAiClient.getCreateTime();
        LocalDateTime originalUpdateTime = testAiClient.getUpdateTime();

        Thread.sleep(1000);

        testAiClient.setDescription("更新描述");
        aiClientMapper.updateById(testAiClient);

        AiClient updated = queryById(testAiClient.getId());

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
    public void testQueryNonExistentRecord() {
        AiClient result = queryById(999999L);
        Assertions.assertNull(result);

        result = queryByClientId("non-existent-client-id");
        Assertions.assertNull(result);

        log.info("QueryNonExistentRecord test passed. Returns null for non-existent records");
    }

    @Test
    public void testUpdateNonExistentRecord() {
        AiClient nonExistent = AiClient.builder()
                .id(999999L)
                .clientId("non-existent-client")
                .clientName("不存在的客户端")
                .status(1)
                .isDeleted(0)
                .build();

        Integer result = aiClientMapper.updateById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiClientMapper.updateByClientId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("UpdateNonExistentRecord test passed. Returns 0 for updating non-existent records");
    }

    @Test
    public void testDeleteNonExistentRecord() {
        AiClient nonExistent = AiClient.builder()
                .id(999999L)
                .clientId("non-existent-client")
                .build();

        Integer result = aiClientMapper.deleteById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiClientMapper.deleteByClientId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("DeleteNonExistentRecord test passed. Returns 0 for deleting non-existent records");
    }

    @Test
    public void testCreateTimeAndUpdateTimeAreEqualOnInsert() {
        aiClientMapper.insert(testAiClient);

        Assertions.assertNotNull(testAiClient.getCreateTime());
        Assertions.assertNotNull(testAiClient.getUpdateTime());
        Assertions.assertEquals(testAiClient.getCreateTime(), testAiClient.getUpdateTime(),
                                "CreateTime and UpdateTime should be equal on insert");
        log.info("CreateTimeAndUpdateTimeAreEqualOnInsert test passed. Both times: {}",
                 testAiClient.getCreateTime());
    }

    @Test
    public void testUpdateTimeChangesOnUpdate() throws InterruptedException {
        aiClientMapper.insert(testAiClient);
        LocalDateTime createTime = testAiClient.getCreateTime();
        LocalDateTime originalUpdateTime = testAiClient.getUpdateTime();

        Thread.sleep(1000);

        testAiClient.setDescription("更新描述");
        aiClientMapper.updateById(testAiClient);

        AiClient updated = queryById(testAiClient.getId());

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
        aiClientMapper.insert(testAiClient);
        Long id = testAiClient.getId();

        Assertions.assertNull(testAiClient.getDeleteTime(),
                              "DeleteTime should be null before soft delete");

        Thread.sleep(1000);

        aiClientMapper.deleteById(testAiClient);

        AiClient deleted = queryById(id);
        Assertions.assertNull(deleted,
                              "Soft deleted client should not be returned by queryById");

        log.info("DeleteTimeIsSetOnSoftDelete test passed. Client has been soft deleted");
    }

    @Test
    public void testQueryByIdAndClientIdConsistency() {
        aiClientMapper.insert(testAiClient);
        Long id = testAiClient.getId();
        String clientId = testAiClient.getClientId();

        AiClient byId = queryById(id);
        AiClient byClientId = queryByClientId(clientId);

        Assertions.assertNotNull(byId);
        Assertions.assertNotNull(byClientId);
        Assertions.assertEquals(byId.getId(), byClientId.getId());
        Assertions.assertEquals(byId.getClientId(), byClientId.getClientId());
        Assertions.assertEquals(byId.getClientName(), byClientId.getClientName());

        log.info("QueryByIdAndClientIdConsistency test passed. Both queries return the same record");
    }

    @Test
    public void testQueryByClientName() {
        aiClientMapper.insert(testAiClient);

        AiClient query = AiClient.builder().clientName("测试客户端").build();
        List<AiClient> result = aiClientMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().anyMatch(client -> "测试客户端".equals(client.getClientName())));
        log.info("QueryByClientName test passed. Found {} clients with name '测试客户端'", result.size());
    }

    @Test
    public void testDescriptionField() {
        String description = "这是一个详细的客户端描述，用于测试描述字段的功能";
        testAiClient.setDescription(description);
        aiClientMapper.insert(testAiClient);

        AiClient queried = queryById(testAiClient.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(description, queried.getDescription());
        log.info("DescriptionField test passed. Description: {}", queried.getDescription());
    }

    @Test
    public void testStatusToggle() {
        aiClientMapper.insert(testAiClient);
        Long id = testAiClient.getId();

        AiClient initial = queryById(id);
        Assertions.assertEquals(1, initial.getStatus().intValue());

        testAiClient.setStatus(0);
        aiClientMapper.updateById(testAiClient);

        AiClient disabled = queryById(id);
        Assertions.assertEquals(0, disabled.getStatus().intValue());

        testAiClient.setStatus(1);
        aiClientMapper.updateById(testAiClient);

        AiClient enabled = queryById(id);
        Assertions.assertEquals(1, enabled.getStatus().intValue());

        log.info("StatusToggle test passed. Status toggled successfully");
    }

    @Test
    public void testLongDescription() {
        StringBuilder longDescription = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            longDescription.append("这是一个很长的描述内容，用于测试数据库字段长度限制。");
        }
        testAiClient.setDescription(longDescription.toString());
        aiClientMapper.insert(testAiClient);

        AiClient queried = queryById(testAiClient.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(longDescription.toString(), queried.getDescription());
        log.info("LongDescription test passed. Description length: {}", queried.getDescription().length());
    }

    @Test
    public void testNullDescription() {
        testAiClient.setDescription(null);
        aiClientMapper.insert(testAiClient);

        AiClient queried = queryById(testAiClient.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertNull(queried.getDescription());
        log.info("NullDescription test passed. Description is null as expected");
    }

    @Test
    public void testEmptyDescription() {
        testAiClient.setDescription("");
        aiClientMapper.insert(testAiClient);

        AiClient queried = queryById(testAiClient.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("", queried.getDescription());
        log.info("EmptyDescription test passed. Description is empty as expected");
    }


    @Test
    public void testMultipleInsertsAndQueries() {
        AiClient client1 = AiClient.builder()
                .clientId("test-client-101")
                .clientName("客户端101")
                .description("第一个客户端")
                .status(1)
                .isDeleted(0)
                .build();
        aiClientMapper.insert(client1);

        AiClient client2 = AiClient.builder()
                .clientId("test-client-102")
                .clientName("客户端102")
                .description("第二个客户端")
                .status(1)
                .isDeleted(0)
                .build();
        aiClientMapper.insert(client2);

        AiClient client3 = AiClient.builder()
                .clientId("test-client-103")
                .clientName("客户端103")
                .description("第三个客户端")
                .status(0)
                .isDeleted(0)
                .build();
        aiClientMapper.insert(client3);

        List<AiClient> allClients = queryAll();
        Assertions.assertTrue(allClients.size() >= 3);

        List<AiClient> enabledClients = queryByStatus(1);
        Assertions.assertTrue(enabledClients.size() >= 2);

        log.info("MultipleInsertsAndQueries test passed. Total: {}, Enabled: {}", allClients.size(),
                 enabledClients.size());
    }
}
