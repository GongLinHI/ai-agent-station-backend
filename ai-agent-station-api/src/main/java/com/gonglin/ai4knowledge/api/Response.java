package com.gonglin.ai4knowledge.api;

import com.gonglin.ai4knowledge.types.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {
    /**
     * 响应码（0000表示成功，其他表示失败）
     */
    private String code;

    /**
     * 响应信息（描述操作结果）
     */
    private String message;

    /**
     * 响应数据（泛型，具体类型由调用方决定）
     */
    private T data;

    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }

    public static <T> Response<T> success(T data, String message) {
        return Response.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(message)
                .data(data)
                .build();
    }

    public static Response<Boolean> success() {
        return Response.<Boolean>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getInfo())
                .build();
    }

    public static Response<Boolean> failure(String code, String message) {
        return Response.<Boolean>builder()
                .code(code)
                .message(message)
                .build();
    }

    public static Response<Boolean> failure(String message) {
        return Response.<Boolean>builder()
                .code(ResponseCode.FAIL.getCode())
                .message(message)
                .build();
    }

    public static Response<Boolean> failure() {
        return Response.<Boolean>builder()
                .code(ResponseCode.FAIL.getCode())
                .message(ResponseCode.FAIL.getInfo())
                .build();
    }
}
