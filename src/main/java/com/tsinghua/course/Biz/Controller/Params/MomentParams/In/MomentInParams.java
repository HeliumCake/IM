package com.tsinghua.course.Biz.Controller.Params.MomentParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Base.Enum.MomentType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

import java.util.List;

/**
 * @描述 发布动态的入参
 **/
@BizType(BizTypeEnum.MOMENT_PUBLISH)
public class MomentInParams extends CommonInParams {
    // 动态类型
    @Required
    private MomentType momentType;
    // 动态文本
    private String text;
    // 动态图片名列表
    List<String> pictures;
    // 动态视频名
    String video;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public MomentType getMomentType() {
        return momentType;
    }

    public void setMomentType(MomentType momentType) {
        this.momentType = momentType;
    }
}
