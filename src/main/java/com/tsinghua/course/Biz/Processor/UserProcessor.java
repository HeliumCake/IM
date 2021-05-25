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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /** 添加联系人 */
    public long addContact(String username, String add) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        User user = mongoTemplate.findOne(query1,User.class);

        Query query2 = new Query();
        query2.addCriteria(Criteria.where(KeyConstant.USERNAME).is(add));
        User contact = mongoTemplate.findOne(query2, User.class);
        if (contact == null)
            return 0;

        List<String> contacts = user.getContacts();
        if (!contacts.contains(contact.getId()))
            contacts.add(contact.getId());

        Update update = Update.update("contacts",contacts);
        UpdateResult result = mongoTemplate.updateFirst(query1,update,User.class);
        return result.getModifiedCount();
    }

    /** 删除联系人 */
    public long deleteContact(String username, String delete) {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        User user = mongoTemplate.findOne(query1,User.class);

        Query query2 = new Query();
        query2.addCriteria(Criteria.where(KeyConstant.USERNAME).is(delete));
        User contact = mongoTemplate.findOne(query2, User.class);
        if (contact == null)
            return 0;

        List<String> contacts = user.getContacts();
        contacts.remove(contact.getId());

        Update update = Update.update("contacts",contacts);
        UpdateResult result = mongoTemplate.updateFirst(query1,update,User.class);
        return result.getModifiedCount();
    }

    /** 浏览联系人列表 */
    public List<String> viewContacts(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        User user = mongoTemplate.findOne(query,User.class);

        return user.getContacts();
    }

    /** 获得ID与昵称对应关系 */
    public Map<String,String> getNicknameById(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).in(ids));
        List<User> users = mongoTemplate.find(query,User.class);

        Map<String,String> map = new HashMap<>();
        for (User user:users){
            map.put(user.getId(),user.getNickname());
        }
        return map;
    }

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

    public void setPrivateChatWith(String username, String other, String chatGroupId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = new Update().set(KeyConstant.PRIVATE_CHAT_MAP + "." + other, chatGroupId);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}
