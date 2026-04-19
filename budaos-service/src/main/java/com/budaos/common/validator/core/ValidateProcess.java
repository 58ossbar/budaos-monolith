package com.budaos.common.validator.core;


import com.budaos.common.exception.ValidateException;
import com.budaos.utils.tool.CharUtil;
import com.budaos.utils.tool.CommonUtil;
import com.budaos.utils.tool.RegexUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class ValidateProcess {

    private final static int[] factorArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    private final static char[] parityBit = {'1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2'};

    private final static String REGEX_AREA = "^[0-9]{2}$";
    private final static String REGEX_DATE8 = "^[0-9]{8}$";
    private final static String REGEX_IP = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d|\\*)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d|\\*)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d|\\*)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d|\\*)";
    //private final static String REGEX_DATE = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
    //private final static String REGEX_CHINESE = "^[\\u4e00-\\u9fa5]{1,}$";
    private final static String REGEX_ENGLISH = "^[a-zA-z]{1,}$";
    private final static String REGEX_PHONE = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
    private final static String REGEX_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    private final static int MIN_YEAR = 1700;
    private final static int MAX_YEAR = 2500;

    private final static Map<Integer, String> zoneNum = new HashMap<>();

    private final static String NotNullErrorMsg = "非空校验失败";
    private final static String RegexErrorMsg = "正则校验失败";
    private final static String MaxErrorMsg = "最大值校验失败";
    private final static String MinErrorMsg = "最小值校验失败";
    private final static String MaxLengthErrorMsg = "最大长度校验失败";
    private final static String MinLengthErrorMsg = "最小长度校验失败";
    private final static String DateFormatErrorMsg = "日期格式校验失败";
    private final static String IdCardErrorMsg = "身份证校验失败";
    private final static String IpErrorMsg = "身份证校验失败";
    private final static String ChineseErrorMsg = "中文校验失败";
    private final static String EnglishErrorMsg = "英文校验失败";
    private final static String EmailErrorMsg = "邮箱校验失败";
    private final static String PhoneNumErrorMsg = "手机号校验失败";

    static {
        zoneNum.put(11, "北京");
        zoneNum.put(12, "天津");
        zoneNum.put(13, "河北");
        zoneNum.put(14, "山西");
        zoneNum.put(15, "内蒙古");
        zoneNum.put(21, "辽宁");
        zoneNum.put(22, "吉林");
        zoneNum.put(23, "黑龙江");
        zoneNum.put(31, "上海");
        zoneNum.put(32, "江苏");
        zoneNum.put(33, "浙江");
        zoneNum.put(34, "安徽");
        zoneNum.put(35, "福建");
        zoneNum.put(36, "江西");
        zoneNum.put(37, "山东");
        zoneNum.put(41, "河南");
        zoneNum.put(42, "湖北");
        zoneNum.put(43, "湖南");
        zoneNum.put(44, "广东");
        zoneNum.put(45, "广西");
        zoneNum.put(46, "海南");
        zoneNum.put(50, "重庆");
        zoneNum.put(51, "四川");
        zoneNum.put(52, "贵州");
        zoneNum.put(53, "云南");
        zoneNum.put(54, "西藏");
        zoneNum.put(61, "陕西");
        zoneNum.put(62, "甘肃");
        zoneNum.put(63, "青海");
        zoneNum.put(64, "新疆");
        zoneNum.put(71, "台湾");
        zoneNum.put(81, "香港");
        zoneNum.put(82, "澳门");
        zoneNum.put(91, "外国");
    }

    static void notNull(Object value) {
        if (null == value) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(String value) {
        if (CommonUtil.isNull(value)) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(Number number) {
        if (null == number) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(Collection<?> value) {
        if (CommonUtil.isNull(value)) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(Map<?, ?> value) {
        if (CommonUtil.isNull(value)) throw new ValidateException(NotNullErrorMsg);
    }

    static void notNull(Object[] value) {
        if (CommonUtil.isNull(value)) throw new ValidateException(NotNullErrorMsg);
    }

    static void regex(String regex, String value) {
        regex(regex, value, RegexErrorMsg + ", regex:" + regex + ", value:" + value);
    }

    private static void regex(String regex, String value, String msg) {
        if (!CommonUtil.isNull(value)) {
            if (!RegexUtil.test(regex, value)) {
                throw new ValidateException(msg);
            }
        }
    }

    static void max(int max, int value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(long max, long value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(float max, float value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(double max, double value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(byte max, byte value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void max(short max, short value) {
        if (value > max) throw new ValidateException(MaxErrorMsg + ", max:" + max + ", value:" + value);
    }

    static void maxLength(int max, String value) {
        if (!CommonUtil.isNull(value)) {
            if (value.length() > max)
                throw new ValidateException(MaxLengthErrorMsg + ", max:" + max + ", value:" + value);
        }
    }

    static void maxLength(int max, Collection<?> value) {
        if (null != value) {
            if (value.size() > max)
                throw new ValidateException(MaxLengthErrorMsg + ", max:" + max + ", value:" + value.size());
        }
    }

    static void maxLength(int max, Map<?, ?> value) {
        if (null != value) {
            if (value.size() > max)
                throw new ValidateException(MaxLengthErrorMsg + ", max:" + max + ", value:" + value.size());
        }
    }

    static void maxLength(int max, Object[] value) {
        if (null != value) {
            if (value.length > max)
                throw new ValidateException(MaxLengthErrorMsg + ", max:" + max + ", value:" + value.length);
        }
    }

    static void min(int min, int value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(long min, long value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(float min, float value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(double min, double value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(byte min, byte value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void min(short min, short value) {
        if (value < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
    }

    static void minLength(int min, String value) {
        if (!CommonUtil.isNull(value)) {
            if (value.length() < min) throw new ValidateException(MinErrorMsg + ", min:" + min + ", value:" + value);
        }
    }

    static void minLength(int min, Collection<?> value) {
        if (null != value) {
            if (value.size() < min)
                throw new ValidateException(MinLengthErrorMsg + ", min:" + min + ", value:" + value.size());
        }
    }

    static void minLength(int min, Map<?, ?> value) {
        if (null != value) {
            if (value.size() < min)
                throw new ValidateException(MinLengthErrorMsg + ", min:" + min + ", value:" + value.size());
        }
    }

    static void minLength(int min, Object[] value) {
        if (null != value) {
            if (value.length < min)
                throw new ValidateException(MinLengthErrorMsg + ", min:" + min + ", value:" + value.length);
        }
    }

    static void date(String format, String value) {
        if (!CommonUtil.isNull(value)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                simpleDateFormat.parse(value);
            } catch (ParseException e) {
                throw new ValidateException(DateFormatErrorMsg + ", format:" + format + ", value:" + value);
            }
        }
    }

    /**
     * 身份证15位编码规则：dddddd yymmdd xx p
     * dddddd：地区码
     * yymmdd: 出生年月日
     * xx: 顺序类编码
     * p: 性别，奇数为男，偶数为女
     * <p/>
     * 身份证18位编码规则：dddddd yyyymmdd xxx y
     * dddddd：地区码
     * yyyymmdd: 出生年月日
     * xxx:顺序类编码，奇数为男，偶数为女
     * y: 校验码，该位数值可通过前17位计算获得
     * <p/>
     * 18位号码加权因子为(从右到左) wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2,1 ]
     * 验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]
     * 校验位计算公式：Y_P = mod( ∑(Ai×wi),11 )
     * i为身份证号码从右往左数的 2...18 位; Y_P为校验码所在校验码数组位置
     */
    static void idCard(String value) {
        if (!CommonUtil.isNull(value)) {
            String idCard = value.toLowerCase();
            int length = idCard.length();
            //校验位数
            if (length != 15 && length != 18) {
                throw new ValidateException(IdCardErrorMsg + ", value:" + value);
            }
            //校验区域
            if (!isArea(idCard.substring(0, 2))) {
                throw new ValidateException(IdCardErrorMsg + ", value:" + value);
            }
            //校验日期
            if (15 == length && !isDate6(idCard.substring(6, 12))) {
                throw new ValidateException(IdCardErrorMsg + ", value:" + value);
            }
            if (18 == length && !isDate8(idCard.substring(6, 14))) {
                throw new ValidateException(IdCardErrorMsg + ", value:" + value);
            }
            //校验18位校验码
            if (18 == length) {
                char[] idCardArray = idCard.toCharArray();
                int sum = 0;
                for (int i = 0; i < idCardArray.length - 1; i++) {
                    if (idCardArray[i] < '0' || idCardArray[i] > '9') {
                        throw new ValidateException(IdCardErrorMsg + ", value:" + value);
                    }
                    sum += (idCardArray[i] - '0') * factorArr[i];
                }
                if (idCardArray[idCardArray.length - 1] != parityBit[sum % 11]) {
                    throw new ValidateException(IdCardErrorMsg + ", value:" + value);
                }
            }
        }
    }

    private static boolean isArea(String area) {
        return RegexUtil.test(REGEX_AREA, area) && zoneNum.containsKey(Integer.valueOf(area));
    }

    private static boolean isDate6(String date) {
        return isDate8("19" + date);
    }

    private static boolean isDate8(String date) {
        if (!RegexUtil.test(REGEX_DATE8, date)) {
            return false;
        }
        int[] iaMonthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) iaMonthDays[1] = 29;
        return !(year < MIN_YEAR || year > MAX_YEAR) && !(month < 1 || month > 12) && !(day < 1 || day > iaMonthDays[month - 1]);
    }


    static void isIp(String value) {
        regex(REGEX_IP, value, IpErrorMsg + ", value:" + value);
    }

    static void chinese(String value) {
        boolean ret = CharUtil.isChinese(value);
        if (!ret) {
            throw new ValidateException(ChineseErrorMsg + ", value:" + value);
        }
    }

    static void english(String value) {
        regex(REGEX_ENGLISH, value, EnglishErrorMsg + ", value:" + value);
    }

    static void phone(String value) {
        regex(REGEX_PHONE, value, PhoneNumErrorMsg + ", value:" + value);
    }

    static void email(String value) {
        regex(REGEX_EMAIL, value, EmailErrorMsg + ", value:" + value);
    }
}
