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

/**
 * @描述 用户原子处理器，所有与用户相关的原子操作都在此处理器中执行
 **/
@Component
public class UserProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

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

    public long updatePassword(String username, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = Update.update("password",password);
        UpdateResult result = mongoTemplate.updateFirst(query,update,User.class);
        return result.getModifiedCount();
    }
}
