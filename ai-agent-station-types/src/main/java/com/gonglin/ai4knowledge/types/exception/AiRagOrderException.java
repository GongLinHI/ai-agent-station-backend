package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AiRagOrderException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AiRagOrderException(String code) {
        super(code);
    }

    public AiRagOrderException(String code, String message) {
        super(code, message);
    }

    public AiRagOrderException(String code, Throwable cause) {
        super(code, cause);
    }

    public AiRagOrderException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AiRagOrderException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AiRagOrderException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AiRagOrderException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AiRagOrderException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
