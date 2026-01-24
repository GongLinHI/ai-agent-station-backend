package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NetworkException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NetworkException(String code) {
        super(code);
    }

    public NetworkException(String code, String message) {
        super(code, message);
    }

    public NetworkException(String code, Throwable cause) {
        super(code, cause);
    }

    public NetworkException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public NetworkException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public NetworkException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public NetworkException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public NetworkException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}