package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.Enum.MomentType;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.MomentWarnEnum;
import com.tsinghua.course.Base.Model.Moment;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.In.MomentInParams;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.Out.MomentOutParams;
import com.tsinghua.course.Biz.Processor.MomentProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @描述 动态控制器，用于执行动态相关的业务
 **/
@Component
public class MomentController {

    @Autowired
    MomentProcessor momentProcessor;
    @Autowired
    UserProcessor userProcessor;

    /** 发布动态业务 */
    @NeedLogin
    @BizType(BizTypeEnum.MOMENT_PUBLISH)
    public CommonOutParams momentPublish(MomentInParams inParams) throws Exception {
        String username = inParams.getUsername();
        MomentType momentType = inParams.getMomentType();
        String text = inParams.getText();
        User user = userProcessor.getUserByUsername(username);

        Moment moment = new Moment();
        moment.setPublisher(user.getId());
        moment.setMomentType(momentType);
        moment.setText(text);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(new Date());
        moment.setTime(timeStr);

        /** 发布动态 */
        if (momentProcessor.addMoment(moment) == null)
        /** 写入数据库失败 */
            throw new CourseWarn(MomentWarnEnum.MOMENT_PUBLISH_FAILED);

        return new CommonOutParams(true);
    }

    /** 浏览单个用户的动态业务 */
    @NeedLogin
    @BizType(BizTypeEnum.MOMENT_VIEW_USER)
    public MomentOutParams momentViewUser(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();
        User user = userProcessor.getUserByUsername(username);

        /** 浏览动态 */
        List<Moment> moments = momentProcessor.getMomentByPublisher(user.getId());
        if (moments.isEmpty())
        /** 读取数据库失败 */
            throw new CourseWarn(MomentWarnEnum.MOMENT_VIEW_FAILED);

        for (Moment moment:moments){
            moment.setPublisher(username);
        }

        return new MomentOutParams(true, moments);
    }
}
