package com.tsinghua.course.Biz.Controller.Params.MomentParams.Out;

import com.tsinghua.course.Base.Model.Moment;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.List;

/**
 * @描述 浏览动态的出参
 **/
public class MomentOutParams extends CommonOutParams {
    // 动态类型
    private List<Moment> moments;

    public MomentOutParams(boolean success, List<Moment> moments) {
        this.success = success;
        this.moments = moments;
    }

    public List<Moment> getMoments() {
        return moments;
    }

    public void setText(List<Moment> moments) {
        this.moments = moments;
    }
}
