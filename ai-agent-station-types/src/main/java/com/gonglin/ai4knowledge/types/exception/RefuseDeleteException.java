package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RefuseDeleteException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RefuseDeleteException(String code) {
        super(code);
    }

    public RefuseDeleteException(String code, String message) {
        super(code, message);
    }

    public RefuseDeleteException(String code, Throwable cause) {
        super(code, cause);
    }

    public RefuseDeleteException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RefuseDeleteException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public RefuseDeleteException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public RefuseDeleteException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public RefuseDeleteException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
