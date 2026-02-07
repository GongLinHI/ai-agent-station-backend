package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AuthException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthException(String code) {
        super(code);
    }

    public AuthException(String code, String message) {
        super(code, message);
    }

    public AuthException(String code, Throwable cause) {
        super(code, cause);
    }

    public AuthException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AuthException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AuthException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AuthException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
