package com.tsinghua.course.Base.Error;

/**
 * @描述 用户操作警告枚举
 **/
public enum UserWarnEnum implements ExceptionInterface {
    LOGIN_FAILED("UserWarn001", "用户或密码不正确"),

    NEED_LOGIN("UserWarn002", "用户未登录或登录已过期"),

    PERMISSION_DENIED("UserWarn003", "无权限访问对应内容"),

    REGISTER_DUPLICATION("UserWarn004", "用户名已存在"),

    REGISTER_LACK("UserWarn005", "缺少用户名或密码"),

    REGISTER_FAILED("UserWarn006", "注册失败"),

    PASSWORD_LACK("UserWarn007", "新密码不能为空"),

    PASSWORD_FAILED("UserWarn008", "修改密码失败"),

    USERNAME_FAILED("UserWarn009", "修改用户名失败"),

    NICKNAME_FAILED("UserWarn010", "修改昵称失败"),

    AVATAR_FAILED("UserWarn011","修改头像失败"),

    USER_NOT_EXIST("UserWarn012", "用户不存在")
    ;

    UserWarnEnum(String code, String msg) {
        errorCode = code;
        errorMsg = msg;
    }

    private String errorCode;
    private String errorMsg;
    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMsg;
    }
}
