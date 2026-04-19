package com.budaos.modules.common.enums;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huj
 * @create 2022-05-10 16:19
 * @email hujun@budaos.com
 * @apiNote
 * 记得与字典empirical_rules同时维护
 * <ul>
 *     <li>1、章节阅读(每个章节知识点)，已阅读加</li>
 *     <li>2、查看视频，加</li>
 *     <li>3、查看音频，加</li>
 *     <li>4、正常签到一次，加</li>
 *     <li>5、迟到一次，扣</li>
 *     <li>6、旷课一次，扣</li>
 *     <li>7、请假一次，扣</li>
 *     <li>8、参与投票问卷，加</li>
 *     <li>9、参与头脑风暴，加</li>
 *     <li>10、参与答疑讨论，加</li>
 * </ul>
 */
public enum EmpiricalValueEnum {

    /** 章节阅读(每个章节知识点)，已阅读加 */
    TYPE_1("1", new BigDecimal("1"), "查看章节，加"),
    /** 查看视频，加 */
    TYPE_2("2", new BigDecimal("3"), "查看视频，加"),
    /** 查看音频，加 */
    TYPE_3("3", new BigDecimal("3"), "查看音频，加"),
    /** 正常签到一次，加 */
    TYPE_4("4", new BigDecimal("1"), "正常签到一次，加"),
    /** 迟到一次，扣 */
    TYPE_5("5", new BigDecimal("1"), "迟到一次，扣"),
    /** 旷课一次，扣 */
    TYPE_6("6", new BigDecimal("3"), "旷课一次，扣"),
    /** 请假一次，扣 */
    TYPE_7("7", new BigDecimal("0"), "请假一次，扣"),
    /** 参与投票问卷，加 */
    TYPE_8("8", new BigDecimal("1"), "参与投票问卷，加"),
    /** 参与头脑风暴，加 */
    TYPE_9("9", new BigDecimal("1"), "参与头脑风暴，加"),
    /** 参与答疑讨论，加 */
    TYPE_10("10", new BigDecimal("1"), "参与答疑讨论，加"),
    ;

    String code;
    BigDecimal value;
    String message;

    private static Map<String, EmpiricalValueEnum> valueMap = new HashMap<>();

    static {
        for(EmpiricalValueEnum ob : EmpiricalValueEnum.values()) {
            valueMap.put(ob.code, ob);
        }
    }

    EmpiricalValueEnum(String code, BigDecimal value) {
        this.value = value;
        this.code = code;
    }

    EmpiricalValueEnum(String code, BigDecimal value, String message) {
        this.value = value;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getValue() {
        return value;
    }
    public String getMessage() {
        return message;
    }

    public static BigDecimal getValueByCode(String key) {
        BigDecimal v = new BigDecimal("0");
        for(EmpiricalValueEnum ob : EmpiricalValueEnum.values()) {
            if (ob.code.equals(key)) {
                v = ob.getValue();
                break;
            }
        }
        return v;
    }

}
