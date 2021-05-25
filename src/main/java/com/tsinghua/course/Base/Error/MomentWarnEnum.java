package com.tsinghua.course.Base.Error;

/**
 * @描述 动态操作警告枚举
 **/
public enum MomentWarnEnum implements ExceptionInterface {
    MOMENT_PUBLISH_FAILED("MomentWarn001", "发布动态失败"),
    MOMENT_VIEW_FAILED("MomentWarn002", "浏览动态失败"),
    ;

    MomentWarnEnum(String code, String msg) {
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
