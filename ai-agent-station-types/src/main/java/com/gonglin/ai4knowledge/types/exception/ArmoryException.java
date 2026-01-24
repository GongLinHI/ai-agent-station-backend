package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ArmoryException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ArmoryException(String code) {
        super(code);
    }

    public ArmoryException(String code, String message) {
        super(code, message);
    }

    public ArmoryException(String code, Throwable cause) {
        super(code, cause);
    }

    public ArmoryException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ArmoryException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public ArmoryException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public ArmoryException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public ArmoryException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
