package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AgentExecutionException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AgentExecutionException(String code) {
        super(code);
    }

    public AgentExecutionException(String code, String message) {
        super(code, message);
    }

    public AgentExecutionException(String code, Throwable cause) {
        super(code, cause);
    }

    public AgentExecutionException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AgentExecutionException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AgentExecutionException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AgentExecutionException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AgentExecutionException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
