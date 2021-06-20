package com.tsinghua.course.Biz.Controller.Params.UploadParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Base.Enum.FileType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import io.netty.handler.codec.http.multipart.FileUpload;

/**
 * @描述 上传文件的入参
 **/
@BizType(BizTypeEnum.UPLOAD)
public class UploadInParams extends CommonInParams {
    // 文件类型
    @Required
    private FileType fileType;
    // 文件
    @Required
    private FileUpload file;

    public FileUpload getFile() {
        return file;
    }

    public void setFile(FileUpload file) {
        this.file = file;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
