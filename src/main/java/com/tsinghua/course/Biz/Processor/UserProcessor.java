package com.tsinghua.course.Biz.Processor;

import com.mongodb.client.result.UpdateResult;
import com.tsinghua.course.Base.Constant.KeyConstant;
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

    /** 根据id从数据库中获取用户 */
    public User getUserById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(id));
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
        Update update = Update.update(KeyConstant.PASSWORD,password);
        UpdateResult result = mongoTemplate.updateFirst(query,update,User.class);
        return result.getModifiedCount();
    }

    /** 修改用户名 */
    public long updateUsername(String username, String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = Update.update(KeyConstant.USERNAME,name);
        UpdateResult result = mongoTemplate.updateFirst(query,update,User.class);
        return result.getModifiedCount();
    }

    /** 修改昵称 */
    public long updateNickname(String username, String nickname) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = Update.update(KeyConstant.NICKNAME,nickname);
        UpdateResult result = mongoTemplate.updateFirst(query,update,User.class);
        return result.getModifiedCount();
    }

    /** 修改头像 */
    public long updateAvatar(String username, String avatar) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = Update.update(KeyConstant.AVATAR,avatar);
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

        List<String> contacts1 = user.getContacts();
        if (!contacts1.contains(contact.getId()))
            contacts1.add(contact.getId());
        List<String> contacts2 = contact.getContacts();
        if (!contacts2.contains(user.getId()))
            contacts2.add(user.getId());

        Update update1 = Update.update(KeyConstant.CONTACTS,contacts1);
        Update update2 = Update.update(KeyConstant.CONTACTS,contacts2);
        UpdateResult result1 = mongoTemplate.updateFirst(query1,update1,User.class);
        UpdateResult result2 = mongoTemplate.updateFirst(query2,update2,User.class);
        return result1.getModifiedCount()+result2.getModifiedCount();
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

        List<String> contacts1 = user.getContacts();
        contacts1.remove(contact.getId());
        List<String> contacts2 = contact.getContacts();
        contacts2.remove(user.getId());

        Update update1 = Update.update(KeyConstant.CONTACTS,contacts1);
        Update update2 = Update.update(KeyConstant.CONTACTS,contacts2);
        UpdateResult result1 = mongoTemplate.updateFirst(query1,update1,User.class);
        UpdateResult result2 = mongoTemplate.updateFirst(query2,update2,User.class);
        return result1.getModifiedCount()+result2.getModifiedCount();
    }

    /** 浏览联系人列表 */
    public List<User> viewContacts(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        User user = mongoTemplate.findOne(query,User.class);
        List<String> contact_ids = user.getContacts();
        List<User> contacts = new ArrayList<>();
        for (String id : contact_ids){
            User contact = getUserById(id);
            contact.setPassword(null);
            contact.setContacts(null);
            contacts.add(contact);
        }

        return contacts;
    }

    /** 获得ID与昵称头像对应关系 */
    public Map<String,List<String>> getNicknameAvatarById(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).in(ids));
        List<User> users = mongoTemplate.find(query,User.class);

        Map<String,List<String>> map = new HashMap<>();
        for (User user:users){
            List<String> tmp = new ArrayList<>();
            tmp.add(user.getNickname());
            tmp.add(user.getAvatar());
            map.put(user.getId(),tmp);
        }
        return map;
    }
}
