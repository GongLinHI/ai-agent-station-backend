package com.gonglin.ai4knowledge.handler;

import com.gonglin.ai4knowledge.api.Response;
import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import com.gonglin.ai4knowledge.types.exception.AgentExecutionException;
import com.gonglin.ai4knowledge.types.exception.AppRuntimeException;
import com.gonglin.ai4knowledge.types.exception.ArmoryException;
import com.gonglin.ai4knowledge.types.exception.AiAdvisorException;
import com.gonglin.ai4knowledge.types.exception.AiApiException;
import com.gonglin.ai4knowledge.types.exception.AiClientException;
import com.gonglin.ai4knowledge.types.exception.AiMcpToolException;
import com.gonglin.ai4knowledge.types.exception.AiModelException;
import com.gonglin.ai4knowledge.types.exception.AiRagOrderException;
import com.gonglin.ai4knowledge.types.exception.AiSystemPromptException;
import com.gonglin.ai4knowledge.types.exception.BaseException;
import com.gonglin.ai4knowledge.types.exception.McpException;
import com.gonglin.ai4knowledge.types.exception.NetworkException;
import com.gonglin.ai4knowledge.types.exception.RefuseDeleteException;
import com.gonglin.ai4knowledge.types.exception.RefuseInsertException;
import com.gonglin.ai4knowledge.types.exception.RefuseQueryException;
import com.gonglin.ai4knowledge.types.exception.RefuseUpdateException;
import com.gonglin.ai4knowledge.types.exception.ResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleBaseException(BaseException e) {
        log.error("业务异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ArmoryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleArmoryException(ArmoryException e) {
        log.error("装配异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());

    }

    @ExceptionHandler(AgentExecutionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAgentExecutionException(AgentExecutionException e) {
        log.error("执行异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(McpException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleMcpException(McpException e) {
        log.error("MCP异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ResourceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleResourceException(ResourceException e) {
        log.error("资源异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NetworkException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleNetworkException(NetworkException e) {
        log.error("网络异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AppRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAppRuntimeException(AppRuntimeException e) {
        log.error("应用运行时异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Boolean> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数异常：{}", e.getMessage(), e);
        return Response.failure(ExceptionCode.PARAM_INVALID.getCode(),
                                ExceptionCode.PARAM_INVALID.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常：{}", e.getMessage(), e);
        return Response.failure(ExceptionCode.PARAM_NULL.getCode(),
                                ExceptionCode.PARAM_NULL.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return Response.failure(ExceptionCode.UNKNOW_ERROR.getCode(),
                                ExceptionCode.UNKNOW_ERROR.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Response<Boolean> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("数据库唯一键冲突异常：{}", e.getMessage(), e);
        return Response.failure(ExceptionCode.DUPLICATE_KEY_ERROR.getCode(),
                                ExceptionCode.DUPLICATE_KEY_ERROR.getMessage());
    }

    @ExceptionHandler(RefuseDeleteException.class)
    public Response<Boolean> handleRefuseDeleteException(RefuseDeleteException e) {
        log.error("拒绝删除异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RefuseInsertException.class)
    public Response<Boolean> handleRefuseInsertException(RefuseInsertException e) {
        log.error("拒绝插入异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RefuseQueryException.class)
    public Response<Boolean> handleRefuseQueryException(RefuseQueryException e) {
        log.error("拒绝查询异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RefuseUpdateException.class)
    public Response<Boolean> handleRefuseUpdateException(RefuseUpdateException e) {
        log.error("拒绝更新异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AiClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAiClientException(AiClientException e) {
        log.error("AI客户端异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AiApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAiApiException(AiApiException e) {
        log.error("AI API异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AiModelException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAiModelException(AiModelException e) {
        log.error("AI模型异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AiAdvisorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAiAdvisorException(AiAdvisorException e) {
        log.error("AI顾问异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AiSystemPromptException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAiSystemPromptException(AiSystemPromptException e) {
        log.error("AI系统提示词异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AiMcpToolException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAiMcpToolException(AiMcpToolException e) {
        log.error("MCP工具异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AiRagOrderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Boolean> handleAiRagOrderException(AiRagOrderException e) {
        log.error("RAG知识库异常：{}", e.getMessage(), e);
        return Response.failure(e.getCode(), e.getMessage());
    }
}