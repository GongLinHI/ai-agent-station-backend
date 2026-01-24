package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RefuseInsertException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RefuseInsertException(String code) {
        super(code);
    }

    public RefuseInsertException(String code, String message) {
        super(code, message);
    }

    public RefuseInsertException(String code, Throwable cause) {
        super(code, cause);
    }

    public RefuseInsertException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RefuseInsertException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public RefuseInsertException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public RefuseInsertException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public RefuseInsertException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
