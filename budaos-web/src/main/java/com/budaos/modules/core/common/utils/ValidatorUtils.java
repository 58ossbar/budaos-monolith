package com.budaos.modules.core.common.utils;

import com.budaos.common.exception.BudaosException;

/**
 * 校验工具类
 * @author zhuq
 *
 */
public class ValidatorUtils {
	/**
	  *  校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws BudaosException  校验不通过，则报BudaosException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws BudaosException {
    	//todo
    }
}
