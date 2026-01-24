package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class McpException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public McpException(String code) {
        super(code);
    }

    public McpException(String code, String message) {
        super(code, message);
    }

    public McpException(String code, Throwable cause) {
        super(code, cause);
    }

    public McpException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public McpException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public McpException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public McpException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public McpException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}