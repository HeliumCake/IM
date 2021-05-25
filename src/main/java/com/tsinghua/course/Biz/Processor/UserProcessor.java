package com.tsinghua.course.Biz.Processor;

import com.mongodb.client.result.UpdateResult;
import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.ChatGroup;
import com.tsinghua.course.Base.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @描述 用户原子处理器，所有与用户相关的原子操作都在此处理器中执行
 **/
@Component
public class UserProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ChatProcessor chatProcessor;

    /** 根据用户名从数据库中获取用户 */
    public User getUserByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        return mongoTemplate.findOne(query, User.class);
    }

    /** 向数据库中添加用户 */
    public User addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return mongoTemplate.insert(user);
    }

    /** 修改密码 */
    public long updatePassword(String username, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = Update.update("password",password);
        UpdateResult result = mongoTemplate.updateFirst(query,update,User.class);
        return result.getModifiedCount();
    }

    /** 获得与另一用户的私聊聊天对象 */
    public ChatGroup getPrivateChatWith(String username, String other) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        User user = mongoTemplate.findOne(query, User.class);
        String groupId = user.getPrivateChatMap().get(other);
        if (groupId == null) {
            return null;
        }
        return chatProcessor.getChatGroupById(groupId);
    }

    /** 设置与另一用户的私聊聊天对象 */
    public void setPrivateChatWith(String username, String other, String chatGroupId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = new Update().set(KeyConstant.PRIVATE_CHAT_MAP + "." + other, chatGroupId);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}
