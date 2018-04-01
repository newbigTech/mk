package com.hand.hmall.util;

import com.hand.hmall.dto.Common;

/**
 * @author peng.chen03@hand-china.com
 * @version 0.1
 * @name Util
 * @description get postfix of the path
 * @date 2017年5月26日10:52:23
 */
public class Util {
    /**
     * get postfix of the path
     *
     * @param path
     * @return
     */
    public static String getPostfix(String path) {
        if (path == null || Common.EMPTY.equals(path.trim())) {
            return Common.EMPTY;
        }
        if (path.contains(Common.POINT)) {
            return path.substring(path.lastIndexOf(Common.POINT) + 1, path.length());
        }
        return Common.EMPTY;
    }


}
