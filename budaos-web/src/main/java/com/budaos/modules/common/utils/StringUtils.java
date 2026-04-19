package com.budaos.modules.common.utils;

/**
 * 字符串工具类
 * @author huj
 * @create 2021-12-17 15:28
 * @email 1552281464@qq.com
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }


}
