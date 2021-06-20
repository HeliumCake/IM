package com.tsinghua.course.Biz.Controller.Params.UploadParams.Out;

import com.tsinghua.course.Base.Enum.FileType;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;


/**
 * @描述 上传文件的出参
 **/
public class UploadOutParams extends CommonOutParams {
    // 文件类型
    private FileType fileType;
    // 文件名
    private String filename;

    public UploadOutParams(FileType fileType, String filename) {
        this.fileType = fileType;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
