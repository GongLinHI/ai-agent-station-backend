package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AiModelException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AiModelException(String code) {
        super(code);
    }

    public AiModelException(String code, String message) {
        super(code, message);
    }

    public AiModelException(String code, Throwable cause) {
        super(code, cause);
    }

    public AiModelException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AiModelException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AiModelException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AiModelException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AiModelException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
