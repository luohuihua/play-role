package com.luohh.playrole.system.model;

import lombok.Data;

/**
 * 返回数据对象
 */
@Data
public class ResultData {
    /**
     * 状态码
     */
    private int statusCode;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;
    /**
     * 请求时间
     */
    private long timestamp;
}
