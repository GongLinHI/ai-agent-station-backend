package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AiAdvisorException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AiAdvisorException(String code) {
        super(code);
    }

    public AiAdvisorException(String code, String message) {
        super(code, message);
    }

    public AiAdvisorException(String code, Throwable cause) {
        super(code, cause);
    }

    public AiAdvisorException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AiAdvisorException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AiAdvisorException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AiAdvisorException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AiAdvisorException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
