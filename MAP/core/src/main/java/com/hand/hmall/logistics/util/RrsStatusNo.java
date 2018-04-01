package com.hand.hmall.logistics.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘宏玺
 * @version 0.1
 * @name RrsStatusNo
 * @description 物流状态转换顺序用工具类
 * @date 2017年5月26日10:52:23
 */
public class RrsStatusNo {
    private static Map<String, Long> StatusMap = new HashMap<String, Long>();

    static {
        StatusMap.put("TMS_ACCEPT", 1L);
        StatusMap.put("TMS_CANVASS", 2L);
        StatusMap.put("TMS_TRUNK_START", 3L);
        StatusMap.put("TMS_TRUNK_END", 4L);
        StatusMap.put("TMS_APPOINTMENT", 5L);
        StatusMap.put("TMS_BRANCH_START", 6L);
        StatusMap.put("TMS_BRANCH_END", 7L);
        StatusMap.put("TMS_SIGN", 8L);
        StatusMap.put("TMS_INSTANLL_START", 9L);
        StatusMap.put("TMS_INSTANLL_END", 10L);
    }

    public static Long getStatusNo(String statusCode) {
        return StatusMap.get(statusCode);
    }
}
