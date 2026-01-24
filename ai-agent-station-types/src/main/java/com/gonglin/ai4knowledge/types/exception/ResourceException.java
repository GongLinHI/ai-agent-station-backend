package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ResourceException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceException(String code) {
        super(code);
    }

    public ResourceException(String code, String message) {
        super(code, message);
    }

    public ResourceException(String code, Throwable cause) {
        super(code, cause);
    }

    public ResourceException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ResourceException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public ResourceException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public ResourceException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public ResourceException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}