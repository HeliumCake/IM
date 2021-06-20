package com.tsinghua.course.Biz.Processor;

import com.mongodb.client.result.UpdateResult;
import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Enum.MomentType;
import com.tsinghua.course.Base.Model.Moment;
import com.tsinghua.course.Base.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @描述 动态原子处理器，所有与动态相关的原子操作都在此处理器中执行
 **/
@Component
public class MomentProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    /** 根据发布者从数据库中获取动态 */
    public List<Moment> getMomentByPublisher(String publisher) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.PUBLISHER).is(publisher));
        return mongoTemplate.find(query, Moment.class);
    }

    /** 根据发布者列表从数据库中获取动态 */
    public List<Moment> getMomentByContacts(List<String> contacts) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.PUBLISHER).in(contacts));
        return mongoTemplate.find(query, Moment.class);
    }

    /** 向数据库中添加动态 */
    public Moment addMoment(String publisher, MomentType momentType, String text, List<String> pictures,
                            String video) {
        Moment moment = new Moment();
        moment.setPublisher(publisher);
        moment.setMomentType(momentType);
        moment.setText(text);
        moment.setPictures(pictures);
        moment.setVideo(video);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(new Date());
        moment.setTime(timeStr);
        return mongoTemplate.insert(moment);
    }

    /** 点赞动态 */
    public long thumbMoment(String username, String id) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        User user = mongoTemplate.findOne(query1, User.class);

        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(id));
        Moment moment = mongoTemplate.findOne(query, Moment.class);
        if (moment == null)
            return 0;

        List<String> thumbs = moment.getThumbs();
        if (!thumbs.contains(user.getId()))
            thumbs.add(user.getId());

        Update update = Update.update(KeyConstant.THUMBS,thumbs);
        UpdateResult result = mongoTemplate.updateFirst(query,update,Moment.class);
        return result.getModifiedCount();
    }

    /** 回复动态 */
    public long replyMoment(String username, String id, String text) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        User user = mongoTemplate.findOne(query1, User.class);

        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(id));
        Moment moment = mongoTemplate.findOne(query, Moment.class);
        if (moment == null)
            return 0;

        List<Moment.Reply> replies = moment.getReplies();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(new Date());
        Moment.Reply reply = new Moment.Reply(user.getId(),text,timeStr);
        replies.add(reply);

        Update update = Update.update(KeyConstant.REPLIES,replies);
        UpdateResult result = mongoTemplate.updateFirst(query,update,Moment.class);
        return result.getModifiedCount();
    }
}
