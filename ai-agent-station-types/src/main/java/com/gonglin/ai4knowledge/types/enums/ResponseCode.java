package com.gonglin.ai4knowledge.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    FAIL("0001", "失败");
    private String code;
    private String info;
}
