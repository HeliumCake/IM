package com.tsinghua.course.Base.Error;

/**
 * @描述 聊天操作警告枚举
 **/
public enum ChatWarnEnum implements ExceptionInterface {
    ILLEGAL_PARAMETER("ChatWarn000", "参数不正确"),
    PRIVATE_CHAT_DUPLICATION("ChatWarn001", "已存在该私聊"),
    UNKNOWN_USERNAME("ChatWarn002", "未知用户名"),
    UNKNOWN_GROUP_ID("ChatWarn003", "未知聊天id"),
    OUT_OF_CHAT_INDEX_BOUND("ChatWarn004", "非法的聊天消息索引范围"),
    NOT_IN_THE_CHAT("ChatWarn005", "你不在这个聊天里"),
    USER_ALREADY_IN_THE_CHAT("ChatWarn006", "用户已在这个聊天里"),
    PRIVATE_CHAT_OPERATION_NOT_SUPPORT("ChatWarn007", "私聊不正常的操作"),
    UNKNOWN_GROUP_TYPE("ChatWarn008", "未知聊天类型"),
    NOT_ADMIN("ChatWarn009", "你不是管理员"),
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
