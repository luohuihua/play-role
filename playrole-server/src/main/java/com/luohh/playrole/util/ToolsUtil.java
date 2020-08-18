package com.luohh.playrole.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用工具类
 */
public class ToolsUtil {
    public static String ACCESSTOKEN="ACCESSTOKEN";
    /**
     * 返回是否转字符串
     */
    public static final List<Class> RETURN_PARAM_LIST = new ArrayList<>();

    static {
        RETURN_PARAM_LIST.add(Integer.class);
        RETURN_PARAM_LIST.add(Long.class);
        RETURN_PARAM_LIST.add(String.class);
        RETURN_PARAM_LIST.add(Float.class);
        RETURN_PARAM_LIST.add(Double.class);
        RETURN_PARAM_LIST.add(Boolean.class);
    }
}
