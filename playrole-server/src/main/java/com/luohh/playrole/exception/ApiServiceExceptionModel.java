package com.luohh.playrole.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiServiceExceptionModel {
    /**
     * 异常码
     */
    private int code;
    /**
     * 信息
     */
    private String msg;
}