package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AiApiException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AiApiException(String code) {
        super(code);
    }

    public AiApiException(String code, String message) {
        super(code, message);
    }

    public AiApiException(String code, Throwable cause) {
        super(code, cause);
    }

    public AiApiException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AiApiException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AiApiException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AiApiException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AiApiException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
