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
import com.tsinghua.course.Biz.Controller.Params.MomentParams.In.*;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.Out.MomentOutParams;
import com.tsinghua.course.Biz.Processor.MomentProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

        /** 发布动态 */
        if (momentProcessor.addMoment(user.getId(), momentType, text) == null)
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
        List<String> contacts = user.getContacts();
        contacts.add(user.getId());

        /** 浏览动态 */
        List<Moment> moments = momentProcessor.getMomentByPublisher(user.getId());
        Map<String,String> map = userProcessor.getNicknameById(contacts);
        if (moments.isEmpty())
        /** 读取数据库失败 */
            throw new CourseWarn(MomentWarnEnum.MOMENT_VIEW_FAILED);

        for (Moment moment:moments){
            moment.setPublisher(map.get(moment.getPublisher()));
            List<Moment.Reply> replies = moment.getReplies();
            List<String> thumbs = moment.getThumbs();
            for (Moment.Reply reply:replies){
                reply.setSender(map.get(reply.getSender()));
            }
            for (int i=0;i<thumbs.size();++i){
                thumbs.set(i, map.get(thumbs.get(i)));
            }
            moment.setReplies(replies);
            moment.setThumbs(thumbs);
        }

        return new MomentOutParams(true, moments);
    }

    /** 浏览所有联系人的动态业务 */
    @NeedLogin
    @BizType(BizTypeEnum.MOMENT_VIEW_CONTACTS)
    public MomentOutParams momentViewContacts(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();
        User user = userProcessor.getUserByUsername(username);
        List<String> contacts = user.getContacts();
        contacts.add(user.getId());

        /** 浏览动态 */
        List<Moment> moments = momentProcessor.getMomentByContacts(contacts);
        Map<String,String> map = userProcessor.getNicknameById(contacts);
        if (moments.isEmpty())
        /** 读取数据库失败 */
            throw new CourseWarn(MomentWarnEnum.MOMENT_VIEW_FAILED);

        for (Moment moment:moments){
            moment.setPublisher(map.get(moment.getPublisher()));
            List<Moment.Reply> replies = moment.getReplies();
            List<String> thumbs = moment.getThumbs();
            for (Moment.Reply reply:replies){
                reply.setSender(map.get(reply.getSender()));
            }
            for (int i=0;i<thumbs.size();++i){
                thumbs.set(i, map.get(thumbs.get(i)));
            }
            moment.setReplies(replies);
            moment.setThumbs(thumbs);
        }

        return new MomentOutParams(true, moments);
    }

    /** 点赞动态业务 */
    @NeedLogin
    @BizType(BizTypeEnum.MOMENT_THUMB)
    public CommonOutParams momentThumb(MomentThumbInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String id = inParams.getId();

        /** 点赞动态 */
        if (momentProcessor.thumbMoment(username, id) == 0)
        /** 写入数据库失败 */
            throw new CourseWarn(MomentWarnEnum.MOMENT_THUMB_FAILED);

        return new CommonOutParams(true);
    }

    /** 回复动态业务 */
    @NeedLogin
    @BizType(BizTypeEnum.MOMENT_REPLY)
    public CommonOutParams momentReply(MomentReplyInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String id = inParams.getId();
        String text = inParams.getText();

        /** 点赞动态 */
        if (momentProcessor.replyMoment(username, id, text) == 0)
        /** 写入数据库失败 */
            throw new CourseWarn(MomentWarnEnum.MOMENT_REPLY_FAILED);

        return new CommonOutParams(true);
    }
}
