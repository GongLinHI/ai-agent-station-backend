package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AiMcpToolException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AiMcpToolException(String code) {
        super(code);
    }

    public AiMcpToolException(String code, String message) {
        super(code, message);
    }

    public AiMcpToolException(String code, Throwable cause) {
        super(code, cause);
    }

    public AiMcpToolException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AiMcpToolException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AiMcpToolException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public AiMcpToolException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public AiMcpToolException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
