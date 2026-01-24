package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AiClientException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AiClientException(String code) {
        super(code);
    }

    public AiClientException(String code, String message) {
        super(code, message);
    }

    public AiClientException(String code, Throwable cause) {
        super(code, cause);
    }

    public AiClientException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AiClientException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AiClientException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AiClientException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AiClientException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
