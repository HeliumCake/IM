package com.tsinghua.course.Base.Error;

/**
 * @描述 聊天操作警告枚举
 **/
public enum ChatWarnEnum implements ExceptionInterface {
    ILLEGAL_PARAMETER("ChatWarn000", "参数不正确"),
    PRIVATE_CHAT_DUPLICATION("ChatWarn001", "已存在该私聊"),
    UNKNOWN_USERNAME("ChatWarn002", "未知用户名"),
    ;

	ChatWarnEnum(String code, String msg) {
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
