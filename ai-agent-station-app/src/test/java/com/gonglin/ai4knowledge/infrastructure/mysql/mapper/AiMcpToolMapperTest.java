package com.gonglin.ai4knowledge.infrastructure.mysql.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.SseTransportConfig;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.StdioTransportConfig;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class AiMcpToolMapperTest {

    @Autowired
    private AiMcpToolMapper aiMcpToolMapper;

    @Autowired
    @Qualifier("jsonMapper")
    private ObjectMapper jsonMapper;

    private AiMcpTool testAiMcpTool;

    @BeforeEach
    public void setUp() throws Exception {
        SseTransportConfig sseConfig = SseTransportConfig.builder()
                .baseUri("http://localhost:12345")
                .sseEndpoint("/sse")
                .build();
        String sseConfigJson = jsonMapper.writeValueAsString(sseConfig);

        testAiMcpTool = AiMcpTool.builder()
                .mcpId("test-mcp-001")
                .mcpName("测试MCP工具")
                .description("这是一个测试用的MCP工具")
                .transportType("sse")
                .transportConfig(sseConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
    }

    @AfterEach
    public void tearDown() {
        if (testAiMcpTool != null && testAiMcpTool.getId() != null) {
            aiMcpToolMapper.deleteById(testAiMcpTool);
        }
    }

    private List<AiMcpTool> queryByStatus(Integer status) {
        AiMcpTool query = AiMcpTool.builder().status(status).build();
        return aiMcpToolMapper.query(query);
    }

    private List<AiMcpTool> queryByTransportType(String transportType) {
        AiMcpTool query = AiMcpTool.builder().transportType(transportType).build();
        return aiMcpToolMapper.query(query);
    }

    private List<AiMcpTool> queryAll() {
        return aiMcpToolMapper.query(AiMcpTool.builder().build());
    }

    @Test
    public void testInsert() {
        Integer result = aiMcpToolMapper.insert(testAiMcpTool);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());
        Assertions.assertNotNull(testAiMcpTool.getId());

        AiMcpTool queried = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        Assertions.assertNotNull(queried.getCreateTime());
        Assertions.assertNotNull(queried.getUpdateTime());
        log.info("Insert test passed. Generated ID: {}, CreateTime: {}, UpdateTime: {}",
                 testAiMcpTool.getId(), queried.getCreateTime(), queried.getUpdateTime());
    }

    @Test
    public void testQueryById() throws Exception {
        aiMcpToolMapper.insert(testAiMcpTool);
        Long id = testAiMcpTool.getId();

        AiMcpTool queried = aiMcpToolMapper.queryById(id);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiMcpTool.getMcpId(), queried.getMcpId());
        Assertions.assertEquals(testAiMcpTool.getMcpName(), queried.getMcpName());
        Assertions.assertEquals(testAiMcpTool.getDescription(), queried.getDescription());
        Assertions.assertEquals(testAiMcpTool.getTransportType(), queried.getTransportType());
        
        SseTransportConfig originalConfig = jsonMapper.readValue(testAiMcpTool.getTransportConfig(), SseTransportConfig.class);
        SseTransportConfig queriedConfig = jsonMapper.readValue(queried.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals(originalConfig.getBaseUri(), queriedConfig.getBaseUri());
        Assertions.assertEquals(originalConfig.getSseEndpoint(), queriedConfig.getSseEndpoint());
        
        Assertions.assertEquals(testAiMcpTool.getTimeout(), queried.getTimeout());
        Assertions.assertEquals(testAiMcpTool.getStatus(), queried.getStatus());
        log.info("QueryById test passed. Queried MCP Tool: {}", queried);
    }

    @Test
    public void testQueryByMcpId() throws Exception {
        aiMcpToolMapper.insert(testAiMcpTool);
        String mcpId = testAiMcpTool.getMcpId();

        AiMcpTool queried = aiMcpToolMapper.queryByMcpId(mcpId);
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(testAiMcpTool.getId(), queried.getId());
        Assertions.assertEquals(testAiMcpTool.getMcpName(), queried.getMcpName());
        Assertions.assertEquals(testAiMcpTool.getDescription(), queried.getDescription());
        Assertions.assertEquals(testAiMcpTool.getTransportType(), queried.getTransportType());
        
        SseTransportConfig originalConfig = jsonMapper.readValue(testAiMcpTool.getTransportConfig(), SseTransportConfig.class);
        SseTransportConfig queriedConfig = jsonMapper.readValue(queried.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals(originalConfig.getBaseUri(), queriedConfig.getBaseUri());
        Assertions.assertEquals(originalConfig.getSseEndpoint(), queriedConfig.getSseEndpoint());
        
        log.info("QueryByMcpId test passed. Queried MCP Tool: {}", queried);
    }

    @Test
    public void testUpdateById() throws InterruptedException {
        aiMcpToolMapper.insert(testAiMcpTool);
        Long id = testAiMcpTool.getId();
        AiMcpTool original = aiMcpToolMapper.queryById(id);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiMcpTool.setMcpName("更新后的MCP工具名称");
        testAiMcpTool.setDescription("更新后的描述信息");
        testAiMcpTool.setTimeout(300);
        testAiMcpTool.setStatus(0);

        Integer result = aiMcpToolMapper.updateById(testAiMcpTool);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiMcpTool updated = aiMcpToolMapper.queryById(id);
        Assertions.assertEquals("更新后的MCP工具名称", updated.getMcpName());
        Assertions.assertEquals("更新后的描述信息", updated.getDescription());
        Assertions.assertEquals(300, updated.getTimeout().intValue());
        Assertions.assertEquals(0, updated.getStatus().intValue());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateById test passed. Updated MCP Tool: {}", updated);
    }

    @Test
    public void testUpdateByMcpId() throws InterruptedException {
        aiMcpToolMapper.insert(testAiMcpTool);
        String mcpId = testAiMcpTool.getMcpId();
        AiMcpTool original = aiMcpToolMapper.queryByMcpId(mcpId);
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiMcpTool.setMcpName("通过Mcp ID更新的名称");
        testAiMcpTool.setDescription("通过Mcp ID更新的描述");
        testAiMcpTool.setTransportType("stdio");

        Integer result = aiMcpToolMapper.updateByMcpId(testAiMcpTool);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiMcpTool updated = aiMcpToolMapper.queryByMcpId(mcpId);
        Assertions.assertEquals("通过Mcp ID更新的名称", updated.getMcpName());
        Assertions.assertEquals("通过Mcp ID更新的描述", updated.getDescription());
        Assertions.assertEquals("stdio", updated.getTransportType());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("UpdateByMcpId test passed. Updated MCP Tool: {}", updated);
    }

    @Test
    public void testDeleteById() {
        aiMcpToolMapper.insert(testAiMcpTool);
        Long id = testAiMcpTool.getId();

        AiMcpTool beforeDelete = aiMcpToolMapper.queryById(id);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiMcpToolMapper.deleteById(testAiMcpTool);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiMcpTool afterDelete = aiMcpToolMapper.queryById(id);
        Assertions.assertNull(afterDelete);
        log.info("DeleteById test passed. MCP Tool with ID {} has been soft deleted", id);
    }

    @Test
    public void testDeleteByMcpId() {
        aiMcpToolMapper.insert(testAiMcpTool);
        String mcpId = testAiMcpTool.getMcpId();

        AiMcpTool beforeDelete = aiMcpToolMapper.queryByMcpId(mcpId);
        Assertions.assertNotNull(beforeDelete);
        Assertions.assertEquals(0, beforeDelete.getIsDeleted().intValue());

        Integer result = aiMcpToolMapper.deleteByMcpId(testAiMcpTool);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.intValue());

        AiMcpTool afterDelete = aiMcpToolMapper.queryByMcpId(mcpId);
        Assertions.assertNull(afterDelete);
        log.info("DeleteByMcpId test passed. MCP Tool with mcpId {} has been soft deleted", mcpId);
    }

    @Test
    public void testQueryByStatus() throws Exception {
        SseTransportConfig sseConfig = SseTransportConfig.builder()
                .baseUri("http://localhost:12346")
                .sseEndpoint("/sse")
                .build();
        String sseConfigJson = jsonMapper.writeValueAsString(sseConfig);

        AiMcpTool enabledTool = AiMcpTool.builder()
                .mcpId("test-mcp-enabled")
                .mcpName("启用的MCP工具")
                .description("这是一个启用的MCP工具")
                .transportType("sse")
                .transportConfig(sseConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(enabledTool);

        StdioTransportConfig stdioConfig = StdioTransportConfig.builder()
                .command("npx")
                .args(List.of("-y", "test"))
                .build();
        String stdioConfigJson = jsonMapper.writeValueAsString(stdioConfig);

        AiMcpTool disabledTool = AiMcpTool.builder()
                .mcpId("test-mcp-disabled")
                .mcpName("禁用的MCP工具")
                .description("这是一个禁用的MCP工具")
                .transportType("stdio")
                .transportConfig(stdioConfigJson)
                .timeout(180)
                .status(0)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(disabledTool);

        List<AiMcpTool> enabledTools = queryByStatus(1);
        Assertions.assertNotNull(enabledTools);
        Assertions.assertTrue(enabledTools.size() >= 1);
        Assertions.assertTrue(
                enabledTools.stream().anyMatch(tool -> "test-mcp-enabled".equals(tool.getMcpId())));
        log.info("QueryByStatus(1) test passed. Found {} enabled MCP tools", enabledTools.size());

        List<AiMcpTool> disabledTools = queryByStatus(0);
        Assertions.assertNotNull(disabledTools);
        Assertions.assertTrue(disabledTools.size() >= 1);
        Assertions.assertTrue(
                disabledTools.stream().anyMatch(tool -> "test-mcp-disabled".equals(tool.getMcpId())));
        log.info("QueryByStatus(0) test passed. Found {} disabled MCP tools", disabledTools.size());
    }

    @Test
    public void testQueryAll() throws Exception {
        aiMcpToolMapper.insert(testAiMcpTool);

        StdioTransportConfig stdioConfig = StdioTransportConfig.builder()
                .command("npx")
                .args(List.of("-y", "test"))
                .build();
        String stdioConfigJson = jsonMapper.writeValueAsString(stdioConfig);

        AiMcpTool anotherTool = AiMcpTool.builder()
                .mcpId("test-mcp-002")
                .mcpName("另一个测试MCP工具")
                .description("这是另一个测试MCP工具")
                .transportType("stdio")
                .transportConfig(stdioConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(anotherTool);

        List<AiMcpTool> allTools = queryAll();
        Assertions.assertNotNull(allTools);
        Assertions.assertTrue(allTools.size() >= 2);
        Assertions.assertTrue(
                allTools.stream().anyMatch(tool -> "test-mcp-001".equals(tool.getMcpId())));
        Assertions.assertTrue(
                allTools.stream().anyMatch(tool -> "test-mcp-002".equals(tool.getMcpId())));
        log.info("QueryAll test passed. Found {} MCP tools", allTools.size());
    }

    @Test
    public void testAutoFillFieldsOnInsert() {
        LocalDateTime beforeInsert = LocalDateTime.now();
        aiMcpToolMapper.insert(testAiMcpTool);
        LocalDateTime afterInsert = LocalDateTime.now();

        Assertions.assertNotNull(testAiMcpTool.getCreateTime());
        Assertions.assertNotNull(testAiMcpTool.getUpdateTime());
        Assertions.assertTrue(
                testAiMcpTool.getCreateTime().isAfter(beforeInsert) || testAiMcpTool.getCreateTime().isEqual(beforeInsert));
        Assertions.assertTrue(
                testAiMcpTool.getCreateTime().isBefore(afterInsert) || testAiMcpTool.getCreateTime().isEqual(afterInsert));
        Assertions.assertEquals(testAiMcpTool.getCreateTime(), testAiMcpTool.getUpdateTime());
        log.info("AutoFillFieldsOnInsert test passed. CreateTime: {}, UpdateTime: {}",
                 testAiMcpTool.getCreateTime(), testAiMcpTool.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnUpdate() throws InterruptedException {
        aiMcpToolMapper.insert(testAiMcpTool);
        AiMcpTool original = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        LocalDateTime originalUpdateTime = original.getUpdateTime();

        Thread.sleep(1000);

        testAiMcpTool.setMcpName("测试自动填充更新时间");
        aiMcpToolMapper.updateById(testAiMcpTool);

        AiMcpTool updated = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        Assertions.assertNotNull(updated.getUpdateTime());
        Assertions.assertTrue(
                updated.getUpdateTime().isAfter(originalUpdateTime) ||
                        updated.getUpdateTime().isEqual(originalUpdateTime));
        log.info("AutoFillFieldsOnUpdate test passed. Original UpdateTime: {}, New UpdateTime: {}",
                 originalUpdateTime, updated.getUpdateTime());
    }

    @Test
    public void testAutoFillFieldsOnSoftDelete() throws InterruptedException {
        aiMcpToolMapper.insert(testAiMcpTool);

        Thread.sleep(1000);

        aiMcpToolMapper.deleteById(testAiMcpTool);

        AiMcpTool deleted = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        Assertions.assertNull(deleted);

        AiMcpTool queriedByMcpId = aiMcpToolMapper.queryByMcpId(testAiMcpTool.getMcpId());
        Assertions.assertNull(queriedByMcpId);

        log.info("AutoFillFieldsOnSoftDelete test passed. MCP Tool has been soft deleted");
    }

    @Test
    public void testQueryWithMultipleConditions() throws Exception {
        aiMcpToolMapper.insert(testAiMcpTool);

        SseTransportConfig sseConfig = SseTransportConfig.builder()
                .baseUri("http://localhost:12347")
                .sseEndpoint("/sse")
                .build();
        String sseConfigJson = jsonMapper.writeValueAsString(sseConfig);

        AiMcpTool anotherTool = AiMcpTool.builder()
                .mcpId("test-mcp-003")
                .mcpName("多条件测试MCP工具")
                .description("多条件查询测试")
                .transportType("sse")
                .transportConfig(sseConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(anotherTool);

        AiMcpTool query = AiMcpTool.builder()
                .transportType("sse")
                .status(1)
                .build();
        List<AiMcpTool> result = aiMcpToolMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 2);
        Assertions.assertTrue(
                result.stream().allMatch(tool -> "sse".equals(tool.getTransportType())));
        Assertions.assertTrue(
                result.stream().allMatch(tool -> tool.getStatus().equals(1)));
        log.info("QueryWithMultipleConditions test passed. Found {} MCP tools matching conditions", result.size());
    }

    @Test
    public void testQueryWithNullConditions() {
        aiMcpToolMapper.insert(testAiMcpTool);

        List<AiMcpTool> result = aiMcpToolMapper.query(AiMcpTool.builder().build());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        log.info("QueryWithNullConditions test passed. Found {} MCP tools", result.size());
    }

    @Test
    public void testSoftDeleteBehavior() {
        aiMcpToolMapper.insert(testAiMcpTool);
        Long id = testAiMcpTool.getId();
        String mcpId = testAiMcpTool.getMcpId();

        Assertions.assertNotNull(aiMcpToolMapper.queryById(id));
        Assertions.assertNotNull(aiMcpToolMapper.queryByMcpId(mcpId));

        aiMcpToolMapper.deleteById(testAiMcpTool);

        Assertions.assertNull(aiMcpToolMapper.queryById(id));
        Assertions.assertNull(aiMcpToolMapper.queryByMcpId(mcpId));

        List<AiMcpTool> allTools = queryAll();
        boolean foundDeleted = allTools.stream()
                .anyMatch(tool -> id.equals(tool.getId()));
        Assertions.assertFalse(foundDeleted, "Soft deleted MCP tool should not appear in queryAll results");

        log.info("SoftDeleteBehavior test passed. MCP Tool is properly excluded from queries after soft delete");
    }

    @Test
    public void testUpdateTimeAutoFill() throws InterruptedException {
        aiMcpToolMapper.insert(testAiMcpTool);
        LocalDateTime createTime = testAiMcpTool.getCreateTime();
        LocalDateTime originalUpdateTime = testAiMcpTool.getUpdateTime();

        Thread.sleep(1000);

        testAiMcpTool.setDescription("更新描述");
        aiMcpToolMapper.updateById(testAiMcpTool);

        AiMcpTool updated = aiMcpToolMapper.queryById(testAiMcpTool.getId());

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
        AiMcpTool result = aiMcpToolMapper.queryById(999999L);
        Assertions.assertNull(result);

        result = aiMcpToolMapper.queryByMcpId("non-existent-mcp-id");
        Assertions.assertNull(result);

        log.info("QueryNonExistentRecord test passed. Returns null for non-existent records");
    }

    @Test
    public void testUpdateNonExistentRecord() {
        AiMcpTool nonExistent = AiMcpTool.builder()
                .id(999999L)
                .mcpId("non-existent-mcp")
                .mcpName("不存在的MCP工具")
                .status(1)
                .isDeleted(0)
                .build();

        Integer result = aiMcpToolMapper.updateById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiMcpToolMapper.updateByMcpId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("UpdateNonExistentRecord test passed. Returns 0 for updating non-existent records");
    }

    @Test
    public void testDeleteNonExistentRecord() {
        AiMcpTool nonExistent = AiMcpTool.builder()
                .id(999999L)
                .mcpId("non-existent-mcp")
                .build();

        Integer result = aiMcpToolMapper.deleteById(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        result = aiMcpToolMapper.deleteByMcpId(nonExistent);
        Assertions.assertEquals(0, result.intValue());

        log.info("DeleteNonExistentRecord test passed. Returns 0 for deleting non-existent records");
    }

    @Test
    public void testCreateTimeAndUpdateTimeAreEqualOnInsert() {
        aiMcpToolMapper.insert(testAiMcpTool);

        Assertions.assertNotNull(testAiMcpTool.getCreateTime());
        Assertions.assertNotNull(testAiMcpTool.getUpdateTime());
        Assertions.assertEquals(testAiMcpTool.getCreateTime(), testAiMcpTool.getUpdateTime(),
                                "CreateTime and UpdateTime should be equal on insert");
        log.info("CreateTimeAndUpdateTimeAreEqualOnInsert test passed. Both times: {}",
                 testAiMcpTool.getCreateTime());
    }

    @Test
    public void testUpdateTimeChangesOnUpdate() throws InterruptedException {
        aiMcpToolMapper.insert(testAiMcpTool);
        LocalDateTime createTime = testAiMcpTool.getCreateTime();
        LocalDateTime originalUpdateTime = testAiMcpTool.getUpdateTime();

        Thread.sleep(1000);

        testAiMcpTool.setDescription("更新描述");
        aiMcpToolMapper.updateById(testAiMcpTool);

        AiMcpTool updated = aiMcpToolMapper.queryById(testAiMcpTool.getId());

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
        aiMcpToolMapper.insert(testAiMcpTool);
        Long id = testAiMcpTool.getId();

        Assertions.assertNull(testAiMcpTool.getDeleteTime(),
                              "DeleteTime should be null before soft delete");

        Thread.sleep(1000);

        aiMcpToolMapper.deleteById(testAiMcpTool);

        AiMcpTool deleted = aiMcpToolMapper.queryById(id);
        Assertions.assertNull(deleted,
                              "Soft deleted MCP tool should not be returned by queryById");

        log.info("DeleteTimeIsSetOnSoftDelete test passed. MCP Tool has been soft deleted");
    }

    @Test
    public void testQueryByIdAndMcpIdConsistency() throws Exception {
        aiMcpToolMapper.insert(testAiMcpTool);
        Long id = testAiMcpTool.getId();
        String mcpId = testAiMcpTool.getMcpId();

        AiMcpTool byId = aiMcpToolMapper.queryById(id);
        AiMcpTool byMcpId = aiMcpToolMapper.queryByMcpId(mcpId);

        Assertions.assertNotNull(byId);
        Assertions.assertNotNull(byMcpId);
        Assertions.assertEquals(byId.getId(), byMcpId.getId());
        Assertions.assertEquals(byId.getMcpId(), byMcpId.getMcpId());
        Assertions.assertEquals(byId.getMcpName(), byMcpId.getMcpName());
        Assertions.assertEquals(byId.getTransportType(), byMcpId.getTransportType());
        
        SseTransportConfig byIdConfig = jsonMapper.readValue(byId.getTransportConfig(), SseTransportConfig.class);
        SseTransportConfig byMcpIdConfig = jsonMapper.readValue(byMcpId.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals(byIdConfig.getBaseUri(), byMcpIdConfig.getBaseUri());
        Assertions.assertEquals(byIdConfig.getSseEndpoint(), byMcpIdConfig.getSseEndpoint());

        log.info("QueryByIdAndMcpIdConsistency test passed. Both queries return same record");
    }

    @Test
    public void testQueryByMcpName() {
        aiMcpToolMapper.insert(testAiMcpTool);

        AiMcpTool query = AiMcpTool.builder().mcpName("测试MCP工具").build();
        List<AiMcpTool> result = aiMcpToolMapper.query(query);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
        Assertions.assertTrue(
                result.stream().anyMatch(tool -> "测试MCP工具".equals(tool.getMcpName())));
        log.info("QueryByMcpName test passed. Found {} MCP tools with name '测试MCP工具'", result.size());
    }

    @Test
    public void testDescriptionField() {
        String description = "这是一个详细的MCP工具描述，用于测试描述字段的功能";
        testAiMcpTool.setDescription(description);
        aiMcpToolMapper.insert(testAiMcpTool);

        AiMcpTool queried = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(description, queried.getDescription());
        log.info("DescriptionField test passed. Description: {}", queried.getDescription());
    }

    @Test
    public void testStatusToggle() {
        aiMcpToolMapper.insert(testAiMcpTool);
        Long id = testAiMcpTool.getId();

        AiMcpTool initial = aiMcpToolMapper.queryById(id);
        Assertions.assertEquals(1, initial.getStatus().intValue());

        testAiMcpTool.setStatus(0);
        aiMcpToolMapper.updateById(testAiMcpTool);

        AiMcpTool disabled = aiMcpToolMapper.queryById(id);
        Assertions.assertEquals(0, disabled.getStatus().intValue());

        testAiMcpTool.setStatus(1);
        aiMcpToolMapper.updateById(testAiMcpTool);

        AiMcpTool enabled = aiMcpToolMapper.queryById(id);
        Assertions.assertEquals(1, enabled.getStatus().intValue());

        log.info("StatusToggle test passed. Status toggled successfully");
    }

    @Test
    public void testTransportTypeSse() throws Exception {
        SseTransportConfig sseConfig = SseTransportConfig.builder()
                .baseUri("http://localhost:8080")
                .sseEndpoint("/sse")
                .build();
        String sseConfigJson = jsonMapper.writeValueAsString(sseConfig);

        AiMcpTool sseTool = AiMcpTool.builder()
                .mcpId("test-mcp-sse")
                .mcpName("SSE传输工具")
                .description("测试SSE传输类型")
                .transportType("sse")
                .transportConfig(sseConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(sseTool);

        AiMcpTool queried = aiMcpToolMapper.queryByMcpId("test-mcp-sse");
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("sse", queried.getTransportType());
        
        SseTransportConfig parsedConfig = jsonMapper.readValue(queried.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals("http://localhost:8080", parsedConfig.getBaseUri());
        Assertions.assertEquals("/sse", parsedConfig.getSseEndpoint());
        log.info("TransportTypeSse test passed. SSE tool configured correctly: {}", parsedConfig);
    }

    @Test
    public void testTransportTypeStdio() throws Exception {
        StdioTransportConfig stdioConfig = StdioTransportConfig.builder()
                .command("npx")
                .args(List.of("-y", "test-package"))
                .build();
        String stdioConfigJson = jsonMapper.writeValueAsString(stdioConfig);

        AiMcpTool stdioTool = AiMcpTool.builder()
                .mcpId("test-mcp-stdio")
                .mcpName("STDIO传输工具")
                .description("测试STDIO传输类型")
                .transportType("stdio")
                .transportConfig(stdioConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(stdioTool);

        AiMcpTool queried = aiMcpToolMapper.queryByMcpId("test-mcp-stdio");
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("stdio", queried.getTransportType());
        
        StdioTransportConfig parsedConfig = jsonMapper.readValue(queried.getTransportConfig(), StdioTransportConfig.class);
        Assertions.assertEquals("npx", parsedConfig.getCommand());
        Assertions.assertEquals(List.of("-y", "test-package"), parsedConfig.getArgs());
        log.info("TransportTypeStdio test passed. STDIO tool configured correctly: {}", parsedConfig);
    }

    @Test
    public void testTransportConfigJsonField() throws Exception {
        SseTransportConfig config = SseTransportConfig.builder()
                .baseUri("http://localhost:8080")
                .sseEndpoint("/sse")
                .build();
        String jsonConfig = jsonMapper.writeValueAsString(config);
        
        testAiMcpTool.setTransportConfig(jsonConfig);
        aiMcpToolMapper.insert(testAiMcpTool);

        AiMcpTool queried = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        Assertions.assertNotNull(queried);
        
        SseTransportConfig originalConfig = jsonMapper.readValue(testAiMcpTool.getTransportConfig(), SseTransportConfig.class);
        SseTransportConfig parsedConfig = jsonMapper.readValue(queried.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals(originalConfig.getBaseUri(), parsedConfig.getBaseUri());
        Assertions.assertEquals(originalConfig.getSseEndpoint(), parsedConfig.getSseEndpoint());
        log.info("TransportConfigJsonField test passed. JSON config stored correctly: {}", parsedConfig);
    }

    @Test
    public void testTimeoutField() {
        testAiMcpTool.setTimeout(300);
        aiMcpToolMapper.insert(testAiMcpTool);

        AiMcpTool queried = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(300, queried.getTimeout().intValue());
        log.info("TimeoutField test passed. Timeout: {}", queried.getTimeout());
    }

    @Test
    public void testTimeoutDefaultValue() {
        testAiMcpTool.setTimeout(null);
        aiMcpToolMapper.insert(testAiMcpTool);

        AiMcpTool queried = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals(180, queried.getTimeout().intValue());
        log.info("TimeoutDefaultValue test passed. Default timeout: {}", queried.getTimeout());
    }

    @Test
    public void testComplexTransportConfig() throws Exception {
        StdioTransportConfig complexConfig = StdioTransportConfig.builder()
                .command("D:\\\\Program Files\\\\nodejs\\\\npx.cmd")
                .args(List.of("-y", "@modelcontextprotocol/server-filesystem@2025.3.28", "D:\\\\itheima", "D:\\\\itheima"))
                .build();
        String complexConfigJson = jsonMapper.writeValueAsString(complexConfig);
        
        testAiMcpTool.setTransportConfig(complexConfigJson);
        testAiMcpTool.setTransportType("stdio");
        aiMcpToolMapper.insert(testAiMcpTool);

        AiMcpTool queried = aiMcpToolMapper.queryById(testAiMcpTool.getId());
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("stdio", queried.getTransportType());
        
        StdioTransportConfig originalConfig = jsonMapper.readValue(testAiMcpTool.getTransportConfig(), StdioTransportConfig.class);
        StdioTransportConfig parsedConfig = jsonMapper.readValue(queried.getTransportConfig(), StdioTransportConfig.class);
        Assertions.assertEquals(originalConfig.getCommand(), parsedConfig.getCommand());
        Assertions.assertEquals(4, parsedConfig.getArgs().size());
        log.info("ComplexTransportConfig test passed. Complex config stored correctly: {}", parsedConfig);
    }

    @Test
    public void testQueryByTransportType() throws Exception {
        SseTransportConfig sseConfig = SseTransportConfig.builder()
                .baseUri("http://localhost:8081")
                .sseEndpoint("/sse")
                .build();
        String sseConfigJson = jsonMapper.writeValueAsString(sseConfig);

        AiMcpTool sseTool = AiMcpTool.builder()
                .mcpId("test-mcp-sse-1")
                .mcpName("SSE工具1")
                .transportType("sse")
                .transportConfig(sseConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(sseTool);

        StdioTransportConfig stdioConfig = StdioTransportConfig.builder()
                .command("npx")
                .args(List.of("-y", "test"))
                .build();
        String stdioConfigJson = jsonMapper.writeValueAsString(stdioConfig);

        AiMcpTool stdioTool = AiMcpTool.builder()
                .mcpId("test-mcp-stdio-1")
                .mcpName("STDIO工具1")
                .transportType("stdio")
                .transportConfig(stdioConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(stdioTool);

        List<AiMcpTool> sseTools = queryByTransportType("sse");
        Assertions.assertNotNull(sseTools);
        Assertions.assertTrue(sseTools.size() >= 1);
        Assertions.assertTrue(
                sseTools.stream().allMatch(tool -> "sse".equals(tool.getTransportType())));
        log.info("QueryByTransportType(sse) test passed. Found {} SSE tools", sseTools.size());

        List<AiMcpTool> stdioTools = queryByTransportType("stdio");
        Assertions.assertNotNull(stdioTools);
        Assertions.assertTrue(stdioTools.size() >= 1);
        Assertions.assertTrue(
                stdioTools.stream().allMatch(tool -> "stdio".equals(tool.getTransportType())));
        log.info("QueryByTransportType(stdio) test passed. Found {} STDIO tools", stdioTools.size());
    }

    @Test
    public void testQueryByIdReturnsNull() {
        AiMcpTool result = aiMcpToolMapper.queryById(999999L);
        Assertions.assertNull(result, "queryById should return null for non-existent ID");
        log.info("QueryByIdReturnsNull test passed. Returns null for non-existent ID");
    }

    @Test
    public void testQueryByMcpIdReturnsNull() {
        AiMcpTool result = aiMcpToolMapper.queryByMcpId("non-existent-mcp-id");
        Assertions.assertNull(result, "queryByMcpId should return null for non-existent mcpId");
        log.info("QueryByMcpIdReturnsNull test passed. Returns null for non-existent mcpId");
    }

    @Test
    public void testInvalidTransportType() throws Exception {
        SseTransportConfig sseConfig = SseTransportConfig.builder()
                .baseUri("http://localhost:9999")
                .sseEndpoint("/sse")
                .build();
        String sseConfigJson = jsonMapper.writeValueAsString(sseConfig);

        AiMcpTool invalidTool = AiMcpTool.builder()
                .mcpId("test-mcp-invalid")
                .mcpName("无效传输类型工具")
                .transportType("invalid-type")
                .transportConfig(sseConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        
        aiMcpToolMapper.insert(invalidTool);

        AiMcpTool queried = aiMcpToolMapper.queryByMcpId("test-mcp-invalid");
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("invalid-type", queried.getTransportType());
        log.info("InvalidTransportType test passed. Invalid transport type stored as-is");
    }

    @Test
    public void testUpdateTransportConfig() throws InterruptedException, Exception {
        aiMcpToolMapper.insert(testAiMcpTool);
        Long id = testAiMcpTool.getId();

        Thread.sleep(1000);

        SseTransportConfig newConfig = SseTransportConfig.builder()
                .baseUri("http://localhost:9999")
                .sseEndpoint("/sse")
                .build();
        String newConfigJson = jsonMapper.writeValueAsString(newConfig);
        
        testAiMcpTool.setTransportConfig(newConfigJson);
        aiMcpToolMapper.updateById(testAiMcpTool);

        AiMcpTool updated = aiMcpToolMapper.queryById(id);
        
        SseTransportConfig parsedConfig = jsonMapper.readValue(updated.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals("http://localhost:9999", parsedConfig.getBaseUri());
        Assertions.assertEquals("/sse", parsedConfig.getSseEndpoint());
        log.info("UpdateTransportConfig test passed. Transport config updated successfully: {}", parsedConfig);
    }

    @Test
    public void testWeatherMcpTool() throws Exception {
        SseTransportConfig weatherConfig = SseTransportConfig.builder()
                .baseUri("http://localhost:12345")
                .sseEndpoint("/sse")
                .build();
        String weatherConfigJson = jsonMapper.writeValueAsString(weatherConfig);

        AiMcpTool weatherTool = AiMcpTool.builder()
                .mcpId("test-weather-mcp")
                .mcpName("test-天气信息查询")
                .description("用于查询任意地点的天气情况")
                .transportType("sse")
                .transportConfig(weatherConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(weatherTool);

        AiMcpTool queried = aiMcpToolMapper.queryByMcpId("test-weather-mcp");
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("test-天气信息查询", queried.getMcpName());
        Assertions.assertEquals("sse", queried.getTransportType());
        
        SseTransportConfig originalConfig = jsonMapper.readValue(weatherConfigJson, SseTransportConfig.class);
        SseTransportConfig parsedConfig = jsonMapper.readValue(queried.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals(originalConfig.getBaseUri(), parsedConfig.getBaseUri());
        Assertions.assertEquals(originalConfig.getSseEndpoint(), parsedConfig.getSseEndpoint());
        log.info("WeatherMcpTool test passed. Weather MCP tool configured correctly: {}", parsedConfig);
    }

    @Test
    public void testFilesystemMcpTool() throws Exception {
        StdioTransportConfig filesystemConfig = StdioTransportConfig.builder()
                .command("D:\\\\Program Files\\\\nodejs\\\\npx.cmd")
                .args(List.of("-y", "@modelcontextprotocol/server-filesystem@2025.3.28", "D:\\\\itheima", "D:\\\\itheima"))
                .build();
        String filesystemConfigJson = jsonMapper.writeValueAsString(filesystemConfig);

        AiMcpTool filesystemTool = AiMcpTool.builder()
                .mcpId("test-filesystem-mcp")
                .mcpName("test-filesystem")
                .description("用于操作D:\\\\itheima下的文件")
                .transportType("stdio")
                .transportConfig(filesystemConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(filesystemTool);

        AiMcpTool queried = aiMcpToolMapper.queryByMcpId("test-filesystem-mcp");
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("test-filesystem", queried.getMcpName());
        Assertions.assertEquals("stdio", queried.getTransportType());
        
        StdioTransportConfig originalConfig = jsonMapper.readValue(filesystemConfigJson, StdioTransportConfig.class);
        StdioTransportConfig parsedConfig = jsonMapper.readValue(queried.getTransportConfig(), StdioTransportConfig.class);
        Assertions.assertEquals(originalConfig.getCommand(), parsedConfig.getCommand());
        Assertions.assertEquals(originalConfig.getArgs().size(), parsedConfig.getArgs().size());
        log.info("FilesystemMcpTool test passed. Filesystem MCP tool configured correctly: {}", parsedConfig);
    }

    @Test
    public void testSearchMcpTool() throws Exception {
        StdioTransportConfig searchConfig = StdioTransportConfig.builder()
                .command("D:\\\\Program Files\\\\nodejs\\\\npx.cmd")
                .args(List.of("-y", "g-search-mcp"))
                .build();
        String searchConfigJson = jsonMapper.writeValueAsString(searchConfig);

        AiMcpTool searchTool = AiMcpTool.builder()
                .mcpId("test-search-mcp")
                .mcpName("test-g-search")
                .description("用于Google搜索")
                .transportType("stdio")
                .transportConfig(searchConfigJson)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(searchTool);

        AiMcpTool queried = aiMcpToolMapper.queryByMcpId("test-search-mcp");
        Assertions.assertNotNull(queried);
        Assertions.assertEquals("test-g-search", queried.getMcpName());
        Assertions.assertEquals("stdio", queried.getTransportType());
        
        StdioTransportConfig originalConfig = jsonMapper.readValue(searchConfigJson, StdioTransportConfig.class);
        StdioTransportConfig parsedConfig = jsonMapper.readValue(queried.getTransportConfig(), StdioTransportConfig.class);
        Assertions.assertEquals(originalConfig.getCommand(), parsedConfig.getCommand());
        Assertions.assertEquals(originalConfig.getArgs(), parsedConfig.getArgs());
        log.info("SearchMcpTool test passed. Search MCP tool configured correctly: {}", parsedConfig);
    }

    @Test
    public void testMultipleInsertsAndQueries() throws Exception {
        SseTransportConfig sseConfig1 = SseTransportConfig.builder()
                .baseUri("http://localhost:8081")
                .sseEndpoint("/sse")
                .build();
        String sseConfigJson1 = jsonMapper.writeValueAsString(sseConfig1);

        AiMcpTool tool1 = AiMcpTool.builder()
                .mcpId("test-mcp-101")
                .mcpName("MCP工具101")
                .description("第一个MCP工具")
                .transportType("sse")
                .transportConfig(sseConfigJson1)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(tool1);

        StdioTransportConfig stdioConfig2 = StdioTransportConfig.builder()
                .command("npx")
                .args(List.of("-y", "test"))
                .build();
        String stdioConfigJson2 = jsonMapper.writeValueAsString(stdioConfig2);

        AiMcpTool tool2 = AiMcpTool.builder()
                .mcpId("test-mcp-102")
                .mcpName("MCP工具102")
                .description("第二个MCP工具")
                .transportType("stdio")
                .transportConfig(stdioConfigJson2)
                .timeout(180)
                .status(1)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(tool2);

        SseTransportConfig sseConfig3 = SseTransportConfig.builder()
                .baseUri("http://localhost:8082")
                .sseEndpoint("/sse")
                .build();
        String sseConfigJson3 = jsonMapper.writeValueAsString(sseConfig3);

        AiMcpTool tool3 = AiMcpTool.builder()
                .mcpId("test-mcp-103")
                .mcpName("MCP工具103")
                .description("第三个MCP工具")
                .transportType("sse")
                .transportConfig(sseConfigJson3)
                .timeout(180)
                .status(0)
                .isDeleted(0)
                .build();
        aiMcpToolMapper.insert(tool3);

        List<AiMcpTool> allTools = queryAll();
        Assertions.assertTrue(allTools.size() >= 3);

        List<AiMcpTool> enabledTools = queryByStatus(1);
        Assertions.assertTrue(enabledTools.size() >= 2);

        List<AiMcpTool> sseTools = queryByTransportType("sse");
        Assertions.assertTrue(sseTools.size() >= 2);
        
        AiMcpTool tool1Queried = aiMcpToolMapper.queryByMcpId("test-mcp-101");
        SseTransportConfig tool1Config = jsonMapper.readValue(tool1Queried.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals("http://localhost:8081", tool1Config.getBaseUri());
        Assertions.assertEquals("/sse", tool1Config.getSseEndpoint());
        
        AiMcpTool tool2Queried = aiMcpToolMapper.queryByMcpId("test-mcp-102");
        StdioTransportConfig tool2Config = jsonMapper.readValue(tool2Queried.getTransportConfig(), StdioTransportConfig.class);
        Assertions.assertEquals("npx", tool2Config.getCommand());
        
        AiMcpTool tool3Queried = aiMcpToolMapper.queryByMcpId("test-mcp-103");
        SseTransportConfig tool3Config = jsonMapper.readValue(tool3Queried.getTransportConfig(), SseTransportConfig.class);
        Assertions.assertEquals("http://localhost:8082", tool3Config.getBaseUri());

        log.info("MultipleInsertsAndQueries test passed. Total: {}, Enabled: {}, SSE: {}", 
                 allTools.size(), enabledTools.size(), sseTools.size());
    }
}
