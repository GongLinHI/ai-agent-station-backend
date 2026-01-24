package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RefuseQueryException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RefuseQueryException(String code) {
        super(code);
    }

    public RefuseQueryException(String code, String message) {
        super(code, message);
    }

    public RefuseQueryException(String code, Throwable cause) {
        super(code, cause);
    }

    public RefuseQueryException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RefuseQueryException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public RefuseQueryException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public RefuseQueryException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public RefuseQueryException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
