package com.budaos.common.validator;

import com.budaos.common.exception.BudaosException;
import org.apache.commons.lang.StringUtils;

/**
 * Title:数据校验
 * Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new BudaosException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new BudaosException(message);
        }
    }
}
