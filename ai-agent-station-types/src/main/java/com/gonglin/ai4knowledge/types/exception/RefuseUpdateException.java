package com.gonglin.ai4knowledge.types.exception;

import com.gonglin.ai4knowledge.types.enums.ExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RefuseUpdateException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RefuseUpdateException(String code) {
        super(code);
    }

    public RefuseUpdateException(String code, String message) {
        super(code, message);
    }

    public RefuseUpdateException(String code, Throwable cause) {
        super(code, cause);
    }

    public RefuseUpdateException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RefuseUpdateException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public RefuseUpdateException(ExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public RefuseUpdateException(ExceptionCode exceptionCode, String customMessage) {
        super(exceptionCode, customMessage);
    }

    public RefuseUpdateException(ExceptionCode exceptionCode, String customMessage, Throwable cause) {
        super(exceptionCode, customMessage, cause);
    }
}
