package com.budaos.modules.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author huj
 * @create 2021-12-17 15:46
 * @email 1552281464@qq.com
 */
public class DateUtils {

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }


}
