package com.budaos.utils.tool;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;

public class CommonUtil {
    public static boolean isNull(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNull(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isNull(Map<?, ?> paramMap) {
        return null == paramMap || paramMap.isEmpty();
    }

    public static boolean isNull(Object[] array) {
        return null == array || array.length == 0;
    }
}
