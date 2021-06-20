package com.tsinghua.course.Base.Error;

/**
 * @描述 上传操作警告枚举
 **/
public enum UploadWarnEnum implements ExceptionInterface {
    UPLOAD_FAILED("UploadWarn001", "上传文件失败"),
    ;

    UploadWarnEnum(String code, String msg) {
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
