package com.gonglin.ai4knowledge.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionCode {

    SUCCESS("0000", "成功"),
    UNKNOW_ERROR("0001", "未知错误"),
    NETWORK_ERROR("0007", "网络异常"),

    ARMORY_DATA_LOAD_FAILED("0100", "装配数据加载失败"),
    ARMORY_FAILED("0101", "AI Agent 自动装配失败"),
    ARMORY_WARMUP_FAILED("0102", "AI Agent 自动预热失败"),

    EXECUTE_FAILED("0200", "AI Agent 执行失败"),
    EXECUTE_SEND_FAILED("0201", "SSE结果发送失败"),
    EXECUTE_SETUP_FAILED("0202", "AI Agent 执行设置失败"),

    RESOURCE_READ_FAILED("0300", "读取资源文件失败"),
    RESOURCE_WRITE_FAILED("0301", "写入资源文件失败"),

    MCP_INIT_FAILED("0400", "MCP 客户端初始化失败"),
    MCP_PARSE_FAILED("0401", "MCP 配置解析失败"),
    MCP_STDIO_INIT_FAILED("0402", "STDIO MCP 客户端初始化失败"),
    MCP_SSE_INIT_FAILED("0403", "SSE MCP 客户端初始化失败"),

    //    PARAM_ERROR("0500", "参数错误"),
    PARAM_NULL("0501", "参数为空"),
    PARAM_INVALID("0502", "参数无效"),

    DATA_NOT_FOUND("0600", "数据不存在"),
    DATA_QUERY_FAILED("0601", "数据查询失败"),
    DUPLICATE_KEY_ERROR("0700", "数据库唯一键冲突异常"),

    REFUSE_INSERT("0800", "拒绝插入"),
    REFUSE_UPDATE("0801", "拒绝更新"),
    REFUSE_DELETE("0802", "拒绝删除"),
    REFUSE_QUERY("0803", "拒绝查询"),

    AI_CLIENT_NOT_FOUND("0900", "AI客户端不存在"),
    AI_CLIENT_ID_NOT_FOUND("0901", "AI客户端ID不存在"),

    AI_API_NOT_FOUND("1000", "AI API不存在"),
    AI_API_ID_NOT_FOUND("1001", "AI API ID不存在"),

    AI_MODEL_NOT_FOUND("1100", "AI模型不存在"),
    AI_MODEL_ID_NOT_FOUND("1101", "AI模型ID不存在"),
    AI_MODEL_HAS_ASSOCIATIONS("1102", "AI模型存在关联配置，无法删除"),

    AI_ADVISOR_NOT_FOUND("1200", "AI顾问不存在"),
    AI_ADVISOR_ID_NOT_FOUND("1201", "AI顾问ID不存在"),
    AI_ADVISOR_MULTIPLE_FOUND("1202", "存在多个AI顾问"),

    AI_SYSTEM_PROMPT_NOT_FOUND("1300", "AI系统提示词不存在"),
    AI_SYSTEM_PROMPT_ID_NOT_FOUND("1301", "AI系统提示词ID不存在"),

    MCP_TOOL_NOT_FOUND("1400", "MCP工具不存在"),
    MCP_TOOL_ID_NOT_FOUND("1401", "MCP工具ID不存在"),

    RAG_ORDER_NOT_FOUND("1500", "RAG知识库不存在"),
    RAG_ORDER_ID_NOT_FOUND("1501", "RAG知识库ID不存在");

    private String code;
    private String message;
}
