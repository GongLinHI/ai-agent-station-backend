/*
 Navicat Premium Data Transfer

 Source Server         : ai-agent-mysql
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:13306
 Source Schema         : ai-agent-station

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 07/02/2026 22:47:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_advisor
-- ----------------------------
DROP TABLE IF EXISTS `ai_advisor`;
CREATE TABLE `ai_advisor`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `advisor_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '顾问ID（业务用）',
  `advisor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '顾问名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `advisor_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '顾问类型(PromptChatMemory/RagAnswer/SimpleLoggerAdvisor等)',
  `ext_param` json NULL COMMENT '扩展参数配置(json)',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_advisor_business_id`(`advisor_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '顾问配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_advisor
-- ----------------------------
INSERT INTO `ai_advisor` VALUES (1, 'advisor-1', '内存上下文记忆', NULL, 'promptChatMemory', '{\"maxMessages\": 2000}', 1, '2025-05-04 08:23:25', '2026-01-16 12:26:09', 0, NULL);
INSERT INTO `ai_advisor` VALUES (2, 'advisor-2', '全部知识库', NULL, 'QuestionAnswer', '{\"topK\": \"4\", \"filterExpression\": \"knowledge == \'知识库名称\'\"}', 1, '2025-05-04 08:23:25', '2026-01-16 11:46:17', 0, NULL);
INSERT INTO `ai_advisor` VALUES (3, 'advisor-3', '日志', NULL, 'simpleLogger', NULL, 1, '2026-01-16 12:25:55', '2026-01-16 12:25:55', 0, NULL);

-- ----------------------------
-- Table structure for ai_agent
-- ----------------------------
DROP TABLE IF EXISTS `ai_agent`;
CREATE TABLE `ai_agent`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '智能体ID（业务用）',
  `agent_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '智能体名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `channel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道类型(agent，chat_stream)',
  `strategy` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行策略(auto、flow)',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_agent_name`(`agent_name` ASC) USING BTREE,
  UNIQUE INDEX `uk_agent_business_id`(`agent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI智能体配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_agent
-- ----------------------------
INSERT INTO `ai_agent` VALUES (1, 'agent-1', '自动汇总天气信息', '自动汇总指定地点的天气信息、空气质量信息，并写入文件中', 'agent', 'flow', 1, '2025-05-04 19:48:05', '2026-01-13 21:48:47', 0, NULL);
INSERT INTO `ai_agent` VALUES (2, 'agent-2', '智能对话体（MCP）', '智能对话，工具服务', 'chat_stream', 'auto', 1, '2025-05-07 09:18:57', '2026-01-13 21:48:45', 0, NULL);
INSERT INTO `ai_agent` VALUES (3, 'agent-3', 'Auto Agent', '能够自主规划并执行任务的Agent', 'agent', 'auto', 1, '2026-01-16 12:35:16', '2026-01-16 12:35:24', 0, NULL);

-- ----------------------------
-- Table structure for ai_agent_flow_config
-- ----------------------------
DROP TABLE IF EXISTS `ai_agent_flow_config`;
CREATE TABLE `ai_agent_flow_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '智能体ID(业务用)',
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端ID(业务用)',
  `client_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端类型',
  `client_step_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '当前步骤的task prompt',
  `sequence` int NOT NULL COMMENT '序列号(执行顺序)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_agent_client_seq`(`agent_id` ASC, `client_id` ASC, `sequence` ASC) USING BTREE,
  INDEX `idx_agent_client_not_deleted`(`agent_id` ASC, `is_deleted` ASC, `sequence` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '智能体-客户端关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_agent_flow_config
-- ----------------------------
INSERT INTO `ai_agent_flow_config` VALUES (1, 'agent-1', 'client-1', NULL, NULL, 1, '2026-01-07 20:28:06', '2026-01-12 14:58:30', 0, NULL, 1);
INSERT INTO `ai_agent_flow_config` VALUES (2, 'agent-1', 'client-2', NULL, NULL, 2, '2026-01-07 20:30:32', '2026-01-14 15:06:39', 0, NULL, 1);
INSERT INTO `ai_agent_flow_config` VALUES (3, 'agent-1', 'client-3', NULL, NULL, 3, '2026-01-07 20:30:39', '2026-01-14 15:06:42', 0, NULL, 1);
INSERT INTO `ai_agent_flow_config` VALUES (4, 'agent-2', 'client-4', NULL, NULL, 1, '2026-01-07 20:32:11', '2026-01-12 14:58:30', 0, NULL, 1);
INSERT INTO `ai_agent_flow_config` VALUES (7, 'agent-3', 'client-5', 'Analyzer', '**原始用户需求:**\r\n\r\n{{userMessage}}\r\n\r\n**当前执行步骤:** 第 {{step}} 步 (最大 {{maxStep}} 步)\r\n\r\n**历史执行记录:**\r\n\r\n{{stepSummaryList}}\r\n\r\n**当前任务:**\r\n\r\n{{currentTask}}\r\n\r\n**分析要求:**\r\n请深入分析用户的具体需求，制定明确的执行策略：\r\n\r\n1. 理解用户真正想要什么（如：具体的学习计划、项目列表、技术方案等）\r\n2. 分析需要哪些具体的执行步骤（如：搜索信息、检索项目、生成内容等）\r\n3. 制定能够产生实际结果的执行策略\r\n4. 确保策略能够直接回答用户的问题\r\n\r\n**输出格式要求:**\r\n\r\n任务状态分析: [当前任务完成情况的详细分析]\r\n执行历史评估: [对已完成工作的质量和效果评估]\r\n下一步策略: [具体的执行计划，包括需要调用的工具和生成的内容]\r\n完成度评估: [0-100]%\r\n任务状态: [CONTINUE/COMPLETED]', 1, '2026-01-16 12:39:01', '2026-01-17 11:38:25', 0, NULL, 1);
INSERT INTO `ai_agent_flow_config` VALUES (8, 'agent-3', 'client-6', 'Executor', '**用户原始需求:**\r\n\r\n{{userMessage}}\r\n\r\n**分析师策略:**\r\n\r\n{{analysisResult}}\r\n\r\n**执行指令:** 你是一个精准任务执行器，需要根据用户需求和分析师策略，实际执行具体的任务。\r\n**执行要求:**\r\n\r\n1. 直接执行用户的具体需求（如搜索、获取信息、生成内容等）\r\n2. 如果需要搜索信息，请实际进行搜索和检索\r\n3. 如果需要生成计划、列表等，请直接生成完整内容\r\n4. 提供具体的执行结果，而不只是描述过程\r\n5. 确保执行结果能直接回答用户的问题\r\n\r\n**输出格式:**\r\n\r\n**执行目标:** [明确的执行目标]\r\n**执行过程:** [实际执行的步骤和调用的工具]\r\n**执行结果:** [具体的执行成果和获得的信息/内容]\r\n**质量检查:** [对执行结果的质量评估]', 2, '2026-01-16 12:39:01', '2026-01-17 11:37:17', 0, NULL, 1);
INSERT INTO `ai_agent_flow_config` VALUES (9, 'agent-3', 'client-7', 'Supervisor', '**用户原始需求:**\r\n\r\n{{userMessage}}\r\n\r\n**执行结果:**\r\n\r\n{{executionResult}}\r\n\r\n**监督要求:**\r\n请严格评估执行结果是否真正满足了用户的原始需求：\r\n\r\n1. 检查是否直接回答了用户的问题\r\n2. 评估内容的完整性和实用性\r\n3. 确认是否提供了用户期望的具体结果（如学习计划、项目列表等）\r\n4. 判断是否只是描述过程而没有给出实际答案\r\n\r\n**输出格式:**\r\n\r\n需求匹配度: [执行结果与用户原始需求的匹配程度分析]\r\n内容完整性: [内容是否完整、具体、实用]\r\n问题识别: [发现的问题和不足，特别是是否偏离了用户真正的需求]\r\n改进建议: [具体的改进建议，确保能直接满足用户需求]\r\n质量评分: [1-10分的质量评分]\r\n是否通过: [PASS/FAIL/OPTIMIZE]', 3, '2026-01-16 12:39:01', '2026-01-17 11:36:37', 0, NULL, 1);
INSERT INTO `ai_agent_flow_config` VALUES (10, 'agent-3', 'client-8', 'Reactor', '基于以下执行过程，请直接回答用户的原始问题，提供最终的答案和结果：\r\n\r\n**用户原始问题:**\r\n\r\n{{userMessage}}\r\n\r\n**执行历史和过程:**\r\n\r\n{{stepSummaryList}}\r\n\r\n**对于已完成任务的要求:**\r\n\r\n1. 直接回答用户的原始问题\r\n2. 基于执行过程中获得的信息和结果\r\n3. 提供具体、实用的最终答案\r\n4. 如果是要求制定计划、列表等，请直接给出完整的内容\r\n5. 避免只描述执行过程，重点是最终答案\r\n6. 以MD语法的表格形式，优化展示结果数据\r\n\r\n**对于未完成任务的要求:**\r\n\r\n虽然任务未完全执行完成，但请基于已有的执行过程，尽力回答用户的原始问题：\r\n\r\n1. 基于已有信息，尽力回答用户的原始问题\r\n2. 如果信息不足，说明哪些部分无法完成并给出原因\r\n3. 提供已能确定的部分答案\r\n4. 给出完成剩余部分的具体建议\r\n5. 以MD语法的表格形式，优化展示结果数据', 4, '2026-01-16 12:39:01', '2026-01-17 11:36:13', 0, NULL, 1);

-- ----------------------------
-- Table structure for ai_agent_task_schedule
-- ----------------------------
DROP TABLE IF EXISTS `ai_agent_task_schedule`;
CREATE TABLE `ai_agent_task_schedule`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '智能体ID(业务用)',
  `task_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务描述',
  `cron_expression` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '时间表达式(如: 0/3 * * * * *)',
  `task_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '任务入参配置(JSON格式)',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:无效,1:有效)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_agent_id`(`agent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '智能体任务调度配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_agent_task_schedule
-- ----------------------------
INSERT INTO `ai_agent_task_schedule` VALUES (1, 'agent-1', '自动查询气象信息', '自动查询气象信息', '0 0/30 * * * ?', '查询长沙的天气信息', 1, '2025-05-05 15:58:58', '2026-01-12 15:00:48', 0, NULL);

-- ----------------------------
-- Table structure for ai_api
-- ----------------------------
DROP TABLE IF EXISTS `ai_api`;
CREATE TABLE `ai_api`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `api_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'API ID（业务用）',
  `api_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'API名称',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '基础URL',
  `api_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'API密钥',
  `completions_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'v1/chat/completions' COMMENT '完成路径',
  `embeddings_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'v1/embeddings' COMMENT '嵌入路径',
  `api_provider` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型提供商(openai/azure等)',
  `ext_param` json NULL COMMENT '拓展参数，json格式',
  `timeout` int NULL DEFAULT 180 COMMENT '超时时间(秒)',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_api_business_id`(`api_id` ASC) USING BTREE,
  INDEX `idx_api_provider`(`api_provider` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI接口模型配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_api
-- ----------------------------
INSERT INTO `ai_api` VALUES (1, 'api-1', 'DeepSeek API', 'https://api.deepseek.com/', 'sk-c58a74e3c645467eab377b01b2b29ce9', 'v1/chat/completions', 'v1/embeddings', 'DeepSeek', NULL, 30, 1, '2025-05-02 07:30:51', '2026-01-15 19:39:19', 0, NULL);
INSERT INTO `ai_api` VALUES (2, 'api-2', 'siliconflow API', 'https://api.siliconflow.cn/', 'sk-fqxfpupthbtjotojseclmfjwplhtzswufmhoyffjfezlssdh', 'v1/chat/completions', 'v1/embeddings', 'siliconflow', NULL, 30, 1, '2025-05-02 07:30:51', '2026-01-09 16:17:27', 0, NULL);

-- ----------------------------
-- Table structure for ai_client
-- ----------------------------
DROP TABLE IF EXISTS `ai_client`;
CREATE TABLE `ai_client`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端ID（业务用）',
  `client_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端名称',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_client_name`(`client_name` ASC) USING BTREE,
  UNIQUE INDEX `uk_client_business_id`(`client_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI客户端配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_client
-- ----------------------------
INSERT INTO `ai_client` VALUES (1, 'client-1', '任务规划与提示词优化', '提示词优化，分为角色、动作、规则、目标等。', 1, '2025-05-04 19:47:56', '2026-01-09 16:17:32', 0, NULL);
INSERT INTO `ai_client` VALUES (2, 'client-2', '天气信息查询', '自动查询任意地点的天气、空气质量等信息', 1, '2025-05-05 10:05:43', '2026-01-09 16:17:32', 0, NULL);
INSERT INTO `ai_client` VALUES (3, 'client-3', '文件操作服务', '文件操作服务', 1, '2025-05-05 13:15:57', '2026-01-09 16:17:32', 0, NULL);
INSERT INTO `ai_client` VALUES (4, 'client-4', '流式对话客户端', '流式对话客户端', 1, '2025-05-07 09:21:04', '2026-01-09 16:17:32', 0, NULL);
INSERT INTO `ai_client` VALUES (5, 'client-5', 'AutoAgent Task Analyzer Prompt', '任务分析与规划', 1, '2026-01-16 12:01:24', '2026-01-16 12:13:57', 0, NULL);
INSERT INTO `ai_client` VALUES (6, 'client-6', 'AutoAgent Precision Executor', '任务执行器', 1, '2026-01-16 12:01:24', '2026-01-16 12:02:03', 0, NULL);
INSERT INTO `ai_client` VALUES (7, 'client-7', 'AutoAgent Quality Supervisor', '质量监督员', 1, '2026-01-16 12:03:39', '2026-01-16 12:03:39', 0, NULL);
INSERT INTO `ai_client` VALUES (8, 'client-8', 'AutoAgent React', '响应助手', 1, '2026-01-16 12:11:07', '2026-01-16 12:11:07', 0, NULL);

-- ----------------------------
-- Table structure for ai_client_advisor_flow_config
-- ----------------------------
DROP TABLE IF EXISTS `ai_client_advisor_flow_config`;
CREATE TABLE `ai_client_advisor_flow_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端ID(业务用)',
  `advisor_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '顾问ID(业务用)',
  `sequence` int NOT NULL COMMENT '序列号(执行顺序)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_client_advisor`(`client_id` ASC, `advisor_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_client_sequence`(`client_id` ASC, `sequence` ASC) USING BTREE,
  INDEX `idx_client_advisor_not_deleted`(`client_id` ASC, `is_deleted` ASC, `sequence` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户端-顾问关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_client_advisor_flow_config
-- ----------------------------
INSERT INTO `ai_client_advisor_flow_config` VALUES (1, 'client-1', 'advisor-1', 1, '2025-05-04 08:36:38', '2025-05-04 08:36:38', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (2, 'client-2', 'advisor-1', 1, '2026-01-07 20:45:56', '2026-01-07 20:45:56', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (3, 'client-3', 'advisor-1', 1, '2026-01-07 20:46:03', '2026-01-07 20:46:03', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (4, 'client-4', 'advisor-1', 1, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (5, 'client-5', 'advisor-1', 2, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (6, 'client-6', 'advisor-1', 2, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (7, 'client-7', 'advisor-1', 2, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (8, 'client-8', 'advisor-1', 2, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (9, 'client-5', 'advisor-3', 1, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (10, 'client-6', 'advisor-3', 1, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (11, 'client-7', 'advisor-3', 1, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);
INSERT INTO `ai_client_advisor_flow_config` VALUES (12, 'client-8', 'advisor-3', 1, '2026-01-07 20:46:10', '2026-01-07 20:46:10', 0, NULL, 1);

-- ----------------------------
-- Table structure for ai_client_model_config
-- ----------------------------
DROP TABLE IF EXISTS `ai_client_model_config`;
CREATE TABLE `ai_client_model_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端ID(业务用)',
  `model_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型ID(业务用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_client_model`(`client_id` ASC, `model_id` ASC) USING BTREE,
  INDEX `idx_client_model_not_deleted`(`client_id` ASC, `is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI客户端，零部件；模型配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_client_model_config
-- ----------------------------
INSERT INTO `ai_client_model_config` VALUES (1, 'client-1', 'model-1', '2025-05-02 17:23:22', '2026-01-07 21:09:24', 0, NULL, 1);
INSERT INTO `ai_client_model_config` VALUES (2, 'client-2', 'model-1', '2025-05-02 17:23:22', '2026-01-07 21:09:24', 0, NULL, 1);
INSERT INTO `ai_client_model_config` VALUES (3, 'client-3', 'model-1', '2025-05-05 13:16:18', '2026-01-07 21:09:24', 0, NULL, 1);
INSERT INTO `ai_client_model_config` VALUES (4, 'client-4', 'model-2', '2025-05-07 09:22:25', '2026-01-07 21:09:24', 0, NULL, 1);
INSERT INTO `ai_client_model_config` VALUES (5, 'client-5', 'model-4', '2026-01-16 14:10:08', '2026-01-16 14:06:58', 0, NULL, 1);
INSERT INTO `ai_client_model_config` VALUES (6, 'client-6', 'model-4', '2026-01-16 14:10:08', '2026-01-16 14:06:58', 0, NULL, 1);
INSERT INTO `ai_client_model_config` VALUES (7, 'client-7', 'model-4', '2026-01-16 14:10:08', '2026-01-16 14:06:58', 0, NULL, 1);
INSERT INTO `ai_client_model_config` VALUES (8, 'client-8', 'model-4', '2026-01-16 14:10:08', '2026-01-16 14:06:58', 0, NULL, 1);

-- ----------------------------
-- Table structure for ai_client_system_prompt_config
-- ----------------------------
DROP TABLE IF EXISTS `ai_client_system_prompt_config`;
CREATE TABLE `ai_client_system_prompt_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端ID(业务用)',
  `system_prompt_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统提示词ID(业务用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_client_id`(`client_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI客户端，零部件；模型配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_client_system_prompt_config
-- ----------------------------
INSERT INTO `ai_client_system_prompt_config` VALUES (1, 'client-1', 'prompt-1', '2025-05-04 20:59:46', '2026-01-07 22:33:47', 0, NULL, 1);
INSERT INTO `ai_client_system_prompt_config` VALUES (2, 'client-2', 'prompt-2', '2025-05-04 20:59:46', '2026-01-07 22:33:47', 0, NULL, 1);
INSERT INTO `ai_client_system_prompt_config` VALUES (3, 'client-3', 'prompt-3', '2026-01-07 22:33:36', '2026-01-07 22:33:47', 0, NULL, 1);
INSERT INTO `ai_client_system_prompt_config` VALUES (4, 'client-4', 'prompt-4', '2026-01-07 22:33:39', '2026-01-07 22:33:47', 0, NULL, 1);
INSERT INTO `ai_client_system_prompt_config` VALUES (5, 'client-5', 'prompt-5', '2026-01-16 14:14:25', NULL, 0, NULL, 1);
INSERT INTO `ai_client_system_prompt_config` VALUES (6, 'client-6', 'prompt-6', '2026-01-16 14:14:34', NULL, 0, NULL, 1);
INSERT INTO `ai_client_system_prompt_config` VALUES (7, 'client-7', 'prompt-7', '2026-01-16 14:14:38', NULL, 0, NULL, 1);
INSERT INTO `ai_client_system_prompt_config` VALUES (8, 'client-8', 'prompt-8', '2026-01-16 14:15:09', NULL, 0, NULL, 1);

-- ----------------------------
-- Table structure for ai_client_tool_config
-- ----------------------------
DROP TABLE IF EXISTS `ai_client_tool_config`;
CREATE TABLE `ai_client_tool_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户端ID(业务用)',
  `tool_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'MCP ID/ function call ID(业务用)',
  `tool_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工具类型(mcp/function call)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_client_tool`(`client_id` ASC, `tool_id` ASC) USING BTREE,
  INDEX `idx_client_tool_not_deleted`(`client_id` ASC, `is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户端-MCP关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_client_tool_config
-- ----------------------------
INSERT INTO `ai_client_tool_config` VALUES (1, 'client-2', 'mcp-1', 'MCP', '2025-05-04 07:31:54', '2026-01-07 21:40:39', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (2, 'client-3', 'mcp-2', 'MCP', '2025-05-04 07:31:54', '2026-01-07 21:40:39', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (3, 'client-4', 'mcp-1', 'MCP', '2025-05-05 13:16:31', '2026-01-07 21:40:39', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (4, 'client-4', 'mcp-2', 'MCP', '2025-05-05 13:16:31', '2026-01-07 21:40:39', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (5, 'client-4', 'mcp-3', 'MCP', '2026-01-07 21:48:36', '2026-01-07 21:40:39', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (6, 'client-5', 'mcp-1', 'MCP', '2026-01-07 21:50:00', '2026-01-07 21:50:00', 0, NULL, 0);
INSERT INTO `ai_client_tool_config` VALUES (7, 'client-5', 'mcp-2', 'MCP', '2026-01-07 21:50:00', '2026-01-07 21:50:00', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (8, 'client-5', 'mcp-3', 'MCP', '2026-01-07 21:50:00', '2026-01-07 21:50:00', 0, NULL, 0);
INSERT INTO `ai_client_tool_config` VALUES (9, 'client-6', 'mcp-1', 'MCP', '2026-01-07 21:51:00', '2026-01-07 21:51:00', 0, NULL, 0);
INSERT INTO `ai_client_tool_config` VALUES (10, 'client-6', 'mcp-2', 'MCP', '2026-01-07 21:51:00', '2026-01-07 21:51:00', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (11, 'client-6', 'mcp-3', 'MCP', '2026-01-07 21:51:00', '2026-01-07 21:51:00', 0, NULL, 0);
INSERT INTO `ai_client_tool_config` VALUES (12, 'client-7', 'mcp-1', 'MCP', '2026-01-07 21:52:00', '2026-01-07 21:52:00', 0, NULL, 0);
INSERT INTO `ai_client_tool_config` VALUES (13, 'client-7', 'mcp-2', 'MCP', '2026-01-07 21:52:00', '2026-01-07 21:52:00', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (14, 'client-7', 'mcp-3', 'MCP', '2026-01-07 21:52:00', '2026-01-07 21:52:00', 0, NULL, 0);
INSERT INTO `ai_client_tool_config` VALUES (15, 'client-8', 'mcp-1', 'MCP', '2026-01-07 21:53:00', '2026-01-07 21:53:00', 0, NULL, 0);
INSERT INTO `ai_client_tool_config` VALUES (16, 'client-8', 'mcp-2', 'MCP', '2026-01-07 21:53:00', '2026-01-07 21:53:00', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (17, 'client-8', 'mcp-3', 'MCP', '2026-01-07 21:53:00', '2026-01-07 21:53:00', 0, NULL, 0);
INSERT INTO `ai_client_tool_config` VALUES (18, 'client-5', 'mcp-4', 'MCP', '2026-01-17 13:09:04', '2026-01-07 21:50:00', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (19, 'client-6', 'mcp-4', 'MCP', '2026-01-17 13:09:04', '2026-01-07 21:50:00', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (20, 'client-7', 'mcp-4', 'MCP', '2026-01-17 13:09:04', '2026-01-07 21:50:00', 0, NULL, 1);
INSERT INTO `ai_client_tool_config` VALUES (21, 'client-8', 'mcp-4', 'MCP', '2026-01-17 13:09:04', '2026-01-07 21:50:00', 0, NULL, 1);

-- ----------------------------
-- Table structure for ai_mcp_tool
-- ----------------------------
DROP TABLE IF EXISTS `ai_mcp_tool`;
CREATE TABLE `ai_mcp_tool`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mcp_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'MCP服务ID（业务用）',
  `mcp_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'MCP名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `transport_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '传输类型(sse/stdio)',
  `transport_config` json NULL COMMENT '传输配置(JSON格式)',
  `timeout` int NULL DEFAULT 180 COMMENT '请求超时时间(秒)',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_mcp_name`(`mcp_name` ASC) USING BTREE,
  UNIQUE INDEX `uk_mcp_business_id`(`mcp_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'MCP客户端配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_mcp_tool
-- ----------------------------
INSERT INTO `ai_mcp_tool` VALUES (1, 'mcp-1', '天气信息查询', '用于查询任意地点的天气情况', 'sse', '{\"baseUri\": \"http://localhost:12345\"}', 60, 1, '2025-05-02 18:43:28', '2026-01-17 12:35:26', 0, NULL);
INSERT INTO `ai_mcp_tool` VALUES (2, 'mcp-2', 'filesystem', '用于操作D:\\itheima下的文件', 'stdio', '{\"args\": [\"-y\", \"@modelcontextprotocol/server-filesystem@2025.3.28\", \"D:\\\\itheima\", \"D:\\\\itheima\"], \"command\": \"D:\\\\Program Files\\\\nodejs\\\\npx.cmd\"}', 60, 1, '2025-05-05 13:14:42', '2026-01-17 12:14:52', 0, NULL);
INSERT INTO `ai_mcp_tool` VALUES (3, 'mcp-3', 'g-search', '用于Google搜索', 'stdio', '{\"args\": [\"-y\", \"g-search-mcp\"], \"command\": \"D:\\\\Program Files\\\\nodejs\\\\npx.cmd\"}', 60, 1, '2025-05-05 13:14:42', '2026-01-17 12:35:30', 0, NULL);
INSERT INTO `ai_mcp_tool` VALUES (4, 'mcp-4', 'EnhancedBing', '用于Bing搜索', 'stdio', '{\"args\": [\"-y\", \"bing-cn-mcp-enhanced\"], \"command\": \"D:\\\\Program Files\\\\nodejs\\\\npx.cmd\"}', 1200, 1, '2026-01-17 13:04:32', '2026-01-17 14:01:00', 0, NULL);

-- ----------------------------
-- Table structure for ai_model
-- ----------------------------
DROP TABLE IF EXISTS `ai_model`;
CREATE TABLE `ai_model`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `model_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型ID（业务用，model-x）',
  `model_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型名称',
  `api_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'API ID',
  `model_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型类型（chat、embedding等）',
  `model_provider` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型提供商(openai/azure等)',
  `model_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'gpt-4.1' COMMENT '模型版本',
  `ext_param` json NULL COMMENT '拓展参数，json格式，如chat模型的temperature、maxTokens；embedding模型的dimensions',
  `timeout` int NULL DEFAULT 180 COMMENT '超时时间(秒)',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_model_business_id`(`model_id` ASC) USING BTREE,
  INDEX `idx_model_provider_type`(`model_provider` ASC, `model_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI接口模型配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_model
-- ----------------------------
INSERT INTO `ai_model` VALUES (1, 'model-1', '智能体对话', 'api-1', 'chat', 'DeepSeek', 'deepseek-chat', '{\"max_tokens\": 8192}', 30, 1, '2025-05-02 07:30:51', '2026-01-16 11:58:20', 0, NULL);
INSERT INTO `ai_model` VALUES (2, 'model-2', '流式对话', 'api-1', 'chat', 'DeepSeek', 'deepseek-reasoner', '{\"max_tokens\": 8192}', 30, 1, '2025-05-02 07:30:51', '2026-01-16 11:58:21', 0, NULL);
INSERT INTO `ai_model` VALUES (3, 'model-3', '词嵌入', 'api-2', 'embedding', 'SiliconFlow', 'BAAI/bge-m3', NULL, 30, 1, '2026-01-07 21:02:38', '2026-01-16 11:58:27', 0, NULL);
INSERT INTO `ai_model` VALUES (4, 'model-4', 'AutoAgent专用', 'api-1', 'chat', 'DeepSeek', 'deepseek-chat', '{\"max_tokens\": 8192}', 180, 1, '2026-01-16 11:54:19', '2026-01-17 01:20:47', 0, NULL);

-- ----------------------------
-- Table structure for ai_model_tool_config
-- ----------------------------
DROP TABLE IF EXISTS `ai_model_tool_config`;
CREATE TABLE `ai_model_tool_config`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `model_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型ID(业务用)',
  `tool_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'MCP ID/ function call ID(业务用)',
  `tool_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工具类型(mcp/function call)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_model_tool`(`model_id` ASC, `tool_id` ASC) USING BTREE,
  INDEX `idx_model_tool_not_deleted`(`model_id` ASC, `is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI客户端，零部件；模型工具配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_model_tool_config
-- ----------------------------
INSERT INTO `ai_model_tool_config` VALUES (1, 'model-1', 'mcp-3', 'MCP', '2025-05-02 17:23:22', '2026-01-07 22:01:27', 0, NULL, 1);
INSERT INTO `ai_model_tool_config` VALUES (2, 'model-2', 'mcp-3', 'MCP', '2025-05-02 17:23:22', '2026-01-07 22:01:27', 0, NULL, 1);

-- ----------------------------
-- Table structure for ai_rag_order
-- ----------------------------
DROP TABLE IF EXISTS `ai_rag_order`;
CREATE TABLE `ai_rag_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rag_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '知识库ID（业务用）',
  `rag_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '知识库名称',
  `knowledge_tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '知识标签',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_rag_name`(`rag_name` ASC) USING BTREE,
  UNIQUE INDEX `uk_rag_business_id`(`rag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '知识库配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_rag_order
-- ----------------------------

-- ----------------------------
-- Table structure for ai_system_prompt
-- ----------------------------
DROP TABLE IF EXISTS `ai_system_prompt`;
CREATE TABLE `ai_system_prompt`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `prompt_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示词ID（业务用）',
  `prompt_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示词名称',
  `prompt_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示词内容',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_prompt_name`(`prompt_name` ASC) USING BTREE,
  UNIQUE INDEX `uk_prompt_business_id`(`prompt_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统提示词配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_system_prompt
-- ----------------------------
INSERT INTO `ai_system_prompt` VALUES (1, 'prompt-1', '提示词优化', '# 角色\r\n                        你是一个智能任务规划助手，名叫 AutoAgent Planning。\r\n                        \r\n                        # 说明\r\n                        你是任务规划助手，根据用户需求，拆解任务列表，制定执行计划。每次执行前，必须先输出本轮思考过程，再生成具体的任务列表。\r\n                        \r\n                        # 技能\r\n                        - 擅长将用户任务拆解为具体、独立的任务列表\r\n                        - 对简单任务，避免过度拆解\r\n                        - 对复杂任务，合理拆解为多个有逻辑关联的子任务\r\n                        \r\n                        # 处理需求\r\n                        ## 拆解任务\r\n                        - 深度推理分析用户输入，识别核心需求及潜在挑战\r\n                        - 将复杂问题分解为可管理、可执行、独立且清晰的子任务\r\n                        - 任务按顺序或因果逻辑组织，上下任务逻辑连贯\r\n                        - 拆解最多不超过5个任务\r\n                        \r\n                        ## 输出格式\r\n                        请按以下格式输出任务计划：\r\n                        \r\n                        **任务规划：**\r\n                        1. [任务1描述]\r\n                        2. [任务2描述]\r\n                        3. [任务3描述]\r\n                        ...\r\n                        \r\n                        **执行策略：**\r\n                        [整体执行策略说明]\r\n                        \r\n                        今天是 {current_date}。', '提示词优化，拆分执行动作', 1, '2025-05-04 21:14:24', '2026-01-15 23:06:18', 0, NULL);
INSERT INTO `ai_system_prompt` VALUES (2, 'prompt-2', '查询xx天气和空气质量并存盘', '你是一个 AI Agent 智能体，可以根据用户输入信息查询指定地点的天气、空气质量等信息，总结并写入\"D:\\itheima\\【城市名称】-【日期】天气情况汇总.txt\"，\r\n【城市名称】需要依据用户输入替换，【日期】也需要替换成系统日期。\r\n\r\n今天是 {today}。\r\n\r\n你擅长使用Planning模式，帮助用户生成质量更高的文章。\r\n\r\n你的规划应该包括以下几个方面：\r\n1.  分析用户输入的内容，查询指定城市的经纬度信息\r\n2. 依据经纬度信息查询对应的天气、空气质量等信息\r\n3. 根据返回的信息，总结查询到的信息，并从穿衣、出行等方面提供建议\r\n4. 写入指定目录下的txt文件，如果文件不存在则新建后再写入。\r\n', '你是一个 AI Agent 智能体，可以根据用户输入信息查询指定地点的天气、空气质量等信息，总结并写入\"D:\\itheima\\【城市名称】-【日期】天气情况汇总.txt\"，\r\n【城市名称】需要依据用户输入替换，【日期】也需要替换成系统日期。\r\n\r\n今天是 {today}。\r\n\r\n你擅长使用Planning模式，帮助用户生成质量更高的文章。\r\n\r\n你的规划应该包括以下几个方面：\r\n1.  分析用户输入的内容，查询指定城市的经纬度信息\r\n2. 依据经纬度信息查询对应的天气、空气质量等信息\r\n3. 根据返回的信息，总结查询到的信息，并从穿衣、出行等方面提供建议\r\n4. 写入指定目录下的txt文件，如果文件不存在则新建后再写入。\r\n查询xx天气和空气质量并存盘', 1, '2025-05-04 21:14:24', '2026-01-09 16:17:39', 0, NULL);
INSERT INTO `ai_system_prompt` VALUES (3, 'prompt-3', '文件操作提示词', '你是一个可以操作特定目录下文件的助手，你需要根据上下文信息完成对应的文件操作。', '文件操作提示词', 1, '2026-01-07 22:32:10', '2026-01-09 16:17:39', 0, NULL);
INSERT INTO `ai_system_prompt` VALUES (4, 'prompt-4', '通用提示词', '你是一个专家级的助手。需要根据用户提供的信息回答问题。', 'SystemPrompt', 1, '2025-05-07 12:05:36', '2026-01-09 16:17:39', 0, NULL);
INSERT INTO `ai_system_prompt` VALUES (6, 'prompt-5', 'AutoAgent Task Analyzer Prompt', ' # 角色\r\n\r\n你是一个专业的任务分析师，名叫 AutoAgent Task Analyzer。\r\n\r\n# 核心职责\r\n\r\n你负责分析任务的当前状态、执行历史和下一步行动计划：\r\n\r\n1. **状态分析**: 深度分析当前任务完成情况和执行历史\r\n2. **进度评估**: 评估任务完成进度和质量\r\n3. **策略制定**: 制定下一步最优执行策略\r\n4. **完成判断**: 准确判断任务是否已完成\r\n\r\n# 分析原则\r\n\r\n- **全面性**: 综合考虑所有执行历史和当前状态\r\n- **准确性**: 准确评估任务完成度和质量\r\n- **前瞻性**: 预测可能的问题和最优路径\r\n- **效率性**: 优化执行路径，避免重复工作\r\n\r\n# 输出格式\r\n\r\n任务状态分析: [当前任务完成情况的详细分析]\r\n执行历史评估: [对已完成工作的质量和效果评估]\r\n下一步策略: [具体的执行计划，包括需要调用的工具和生成的内容]\r\n完成度评估: [0-100]%\r\n任务状态: [CONTINUE/COMPLETED]', 'AutoAgent Task Analyzer提示词', 1, '2026-01-16 12:13:18', '2026-01-16 13:54:21', 0, NULL);
INSERT INTO `ai_system_prompt` VALUES (7, 'prompt-6', 'AutoAgent Precision Executor Prompt', ' # 角色\r\n\r\n你是一个精准任务执行器，名叫 AutoAgent Precision Executor。\r\n\r\n# 核心能力\r\n\r\n你专注于精准执行具体的任务步骤：\r\n\r\n1. **精准执行**: 严格按照分析师的策略执行任务\r\n2. **工具使用**: 熟练使用各种工具完成复杂操作\r\n3. **质量控制**: 确保每一步执行的准确性和完整性\r\n4. **结果记录**: 详细记录执行过程和结果\r\n\r\n# 执行原则\r\n\r\n- **专注性**: 专注于当前分配的具体任务\r\n- **精准性**: 确保执行结果的准确性和质量\r\n- **完整性**: 完整执行所有必要的步骤\r\n- **可追溯性**: 详细记录执行过程便于后续分析\r\n\r\n# 输出格式\r\n\r\n**执行目标:** [明确的执行目标]\r\n**执行过程:** [实际执行的步骤和调用的工具]\r\n**执行结果:** [具体的执行成果和获得的信息/内容]\r\n**质量检查:** [对执行结果的质量评估]', 'AutoAgent Precision Executor提示词', 1, '2026-01-16 12:14:57', '2026-01-16 13:56:41', 0, NULL);
INSERT INTO `ai_system_prompt` VALUES (8, 'prompt-7', 'AutoAgent Quality Supervisor Prompt', ' # 角色\r\n\r\n你是一个专业的质量监督员，名叫 AutoAgent Quality Supervisor。\r\n\r\n# 核心职责\r\n\r\n你负责监督和评估执行质量：\r\n\r\n1. **质量评估**: 评估执行结果的准确性和完整性\r\n2. **问题识别**: 识别执行过程中的问题和不足\r\n3. **改进建议**: 提供具体的改进建议和优化方案\r\n4. **标准制定**: 制定质量标准和评估指标\r\n\r\n# 评估标准\r\n\r\n- **准确性**: 结果是否准确无误\r\n- **完整性**: 是否遗漏重要信息\r\n- **相关性**: 是否符合用户需求\r\n- **可用性**: 结果是否实用有效\r\n\r\n# 输出格式\r\n\r\n需求匹配度: [执行结果与用户原始需求的匹配程度分析]\r\n内容完整性: [内容是否完整、具体、实用]\r\n问题识别: [发现的问题和不足，特别是是否偏离了用户真正的需求]\r\n改进建议: [具体的改进建议，确保能直接满足用户需求]\r\n质量评分: [1-10分的质量评分]\r\n是否通过: [PASS/FAIL/OPTIMIZE]', 'AutoAgent Quality Supervisor提示词', 1, '2026-01-16 12:20:00', '2026-01-16 13:57:25', 0, NULL);
INSERT INTO `ai_system_prompt` VALUES (9, 'prompt-8', 'AutoAgent React Prompt', '# 角色\r\n\r\n你是一个智能响应助手，名叫 AutoAgent React。\r\n\r\n# 说明\r\n\r\n你负责对用户的即时问题进行快速响应和处理，适用于简单的查询和交互。\r\n\r\n# 处理方式\r\n\r\n- 对于简单问题，直接给出答案\r\n- 对于需要工具的问题，调用相应工具获取信息\r\n- 保持响应的及时性和准确性\r\n\r\n今天是 {{today}}。', 'AutoAgent React提示词', 1, '2026-01-16 12:21:49', '2026-01-17 11:34:23', 0, NULL);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `perm_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限业务ID',
  `perm_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限编码',
  `perm_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_perm_id`(`perm_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_perm_code`(`perm_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '550e8400-e29b-41d4-a716-446655440003', 'admin:all', '管理员全部权限', '后台管理全量权限', 1, '2026-02-07 16:26:19', '2026-02-07 16:26:19', 0, NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色业务ID',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_id`(`role_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '550e8400-e29b-41d4-a716-446655440001', 'admin', '管理员', '系统管理员', 1, '2026-02-07 16:26:19', '2026-02-07 16:26:19', 0, NULL);
INSERT INTO `role` VALUES (2, '550e8400-e29b-41d4-a716-446655440002', 'user', '普通用户', '默认角色', 1, '2026-02-07 16:26:19', '2026-02-07 16:26:19', 0, NULL);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色业务ID',
  `perm_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限业务ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_perm`(`role_id` ASC, `perm_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_perm_id`(`perm_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色-权限关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003', '2026-02-07 16:26:19', '2026-02-07 16:26:19', 0, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户业务ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录名',
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码哈希(BCrypt)',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最近登录时间',
  `last_login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近登录IP',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '550e8400-e29b-41d4-a716-446655440004', 'admin', '$2a$10$EYsorbBDYdc.pTvC6s0XB.nc424bFgz3xG3iqPZ2GCEJx9gwKPagC', '管理员', NULL, NULL, 1, NULL, NULL, '2026-02-07 16:26:19', '2026-02-07 22:46:36', 0, NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户业务ID',
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色业务ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '软删除(0:未删,1:已删)',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id` ASC, `role_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440001', '2026-02-07 16:26:19', '2026-02-07 16:26:19', 0, NULL);
INSERT INTO `user_role` VALUES (2, 'dda17f3c-decd-448f-80c1-911749b629c1', '550e8400-e29b-41d4-a716-446655440002', '2026-02-07 22:45:18', '2026-02-07 22:45:18', 0, NULL);

SET FOREIGN_KEY_CHECKS = 1;
