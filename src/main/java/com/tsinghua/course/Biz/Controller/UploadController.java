package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.Constant.FileConstant;
import com.tsinghua.course.Base.Enum.FileType;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UploadWarnEnum;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.UploadParams.In.UploadInParams;
import com.tsinghua.course.Biz.Controller.Params.UploadParams.Out.UploadOutParams;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

/**
 * @描述 上传控制器，用于执行上传文件的业务
 **/
@Component
public class UploadController {

    /** 上传文件业务 */
    @NeedLogin
    @BizType(BizTypeEnum.UPLOAD)
    public UploadOutParams upload(UploadInParams inParams) throws Exception {
        FileType fileType = inParams.getFileType();
        FileUpload file = inParams.getFile();
        String filename = file.getFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        filename = UUID.randomUUID().toString() + suffix;
        FileChannel inputChannel = new FileInputStream(file.getFile()).getChannel();
        FileChannel outputChannel;
        try {
            switch (fileType){
                case PICTURE:
                    outputChannel = new FileOutputStream(FileConstant.IMAGE_PATH+"/"+filename).getChannel();
                    outputChannel.transferFrom(inputChannel,0,inputChannel.size());
                    break;
                case VIDEO:
                    outputChannel = new FileOutputStream(FileConstant.VIDEO_PATH+"/"+filename).getChannel();
                    outputChannel.transferFrom(inputChannel,0,inputChannel.size());
                    break;
                case AUDIO:
                    outputChannel = new FileOutputStream(FileConstant.AUDIO_PATH+"/"+filename).getChannel();
                    outputChannel.transferFrom(inputChannel,0,inputChannel.size());
                    break;
            }
        }catch (IOException e){
            throw new CourseWarn(UploadWarnEnum.UPLOAD_FAILED);
        }

        return new UploadOutParams(fileType,filename);
    }
}
