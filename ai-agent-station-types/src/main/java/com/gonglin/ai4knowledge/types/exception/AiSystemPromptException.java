package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AiSystemPromptException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AiSystemPromptException(String code) {
        super(code);
    }

    public AiSystemPromptException(String code, String message) {
        super(code, message);
    }

    public AiSystemPromptException(String code, Throwable cause) {
        super(code, cause);
    }

    public AiSystemPromptException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AiSystemPromptException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AiSystemPromptException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AiSystemPromptException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AiSystemPromptException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
