package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserException(String code) {
        super(code);
    }

    public UserException(String code, String message) {
        super(code, message);
    }

    public UserException(String code, Throwable cause) {
        super(code, cause);
    }

    public UserException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public UserException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public UserException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public UserException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public UserException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
