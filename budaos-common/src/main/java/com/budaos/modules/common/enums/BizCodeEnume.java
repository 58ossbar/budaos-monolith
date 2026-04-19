package com.budaos.modules.common.enums;

/**
 * 错误码和错误信息定义类
 * @author huj
 * @create 2022-04-22 9:34
 * @email hujun@budaos.com
 * @apiNote
 * <ul>
 *     <li>1、错误码定义规则为5为数字</li>
 *     <li>2、前两位表示业务场景，最后三位表示错误码。例如：10001。10:通用 001:系统未知异常</li>
 *     <li>2、维护错误码后需要维护错误描述，将他们定义为枚举形式</li>
 * </ul>
 * 错误码列表：
 * <ul>
 *     <li>10：通用</li>
 *     <li>11：教学包模块</li>
 *     <li>12：活动模块</li>
 *     <li>13：课堂模块、131：课堂成员模块、132：课堂小组模块</li>
 *     <li>其它等</li>
 * </ul>
 */
public enum BizCodeEnume {

	/** 操作成功 */
    SUCCESS(0, "操作成功"),
    /** 系统未知异常 */
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    /** 参数格式校验失败 */
    VAILD_EXCEPTION(10001, "参数格式校验失败"),
    /** 必填参数不能为空 */
    PARAM_MISSING(10002, "必填参数不能为空"),
    /** 参数不合法 */
    PARAM_INVALID(10003, "参数不合法"),
    /** 非法操作 */
    ILLEGAL_OPERATION(10003, "非法操作"),
    /** 无效的记录（数据库中不存在该记录） */
    INVALID_RECORD(10004, "无效的记录"),
    /** 账户已被停用 */
    THE_ACCOUNT_HAS_BEEN_DEACTIVATED(10004, "账号已停用，请与系统管理员联系！"),
    /** 没有权限，无法进行该操作 */
    WITHOUT_PERMISSION(10005, "没有权限，无法进行该操作！"),

    /** 该教学包已不存在，无法进行后续操作 */
    WITHOUT_PACKAGE(11000, "该教学包已不存在，无法进行后续操作"),
    /** 没有权限，教学包已经被管理员移交给他人管理 */
    NO_OPERATION_PERMISSION_PKG(11001, "没有权限，教学包已经被管理员移交给他人管理"),
    /** 没有权限，无法操作教学包 */
    NO_AUTH_YOUR_PKG_DEVOLVED(11002, "没有权限，无法操作教学包"),
    /** 已发布的版本中，不允许再次编辑内容 */
    EDIT_CONTENT_IS_NOT_ALLOWED(11010, "已发布的版本中，不允许再次编辑内容"),

    /** 课堂已不存在，无法进行后续操作 */
    INEFFECTIVE_CLASSROOM(13000, "课堂已不存在，无法进行后续操作"),
    /** 课堂已结束，请与管理员联系！ */
    CLASS_IS_OVER(13000, "课堂已结束，请与管理员联系！"),
    
    ;

    private int code;

    private String msg;

    BizCodeEnume(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
