package com.budaos.modules.common;

/**
 * @ClassName: EmptyUtils
 * @Description: 判空工具类
 * @author: yaozhenhua
 * @date: 2018/12/24 16:53
 */
public class EmptyUtils {

    /**
     * @Title: isEmpty
     * @Description: 判断对象是否为空
     * @param obj
     * @return
     * @return Integer
     */
    public static boolean isEmpty(Object obj) {
        return null == obj || "".equals(obj);
    }

}