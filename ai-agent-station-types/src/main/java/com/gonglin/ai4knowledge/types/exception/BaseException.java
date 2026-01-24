package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;

    public BaseException(String code) {
        this.code = code;
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BaseException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public BaseException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode.getMessage(), cause);
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public BaseException(ExceptionCode exceptionCode, String customMessage) {
        super(customMessage);
        this.code = exceptionCode.getCode();
        this.message = customMessage;
    }

    public BaseException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.code = exceptionCode.getCode();
        this.message = customMessage;
    }
}
