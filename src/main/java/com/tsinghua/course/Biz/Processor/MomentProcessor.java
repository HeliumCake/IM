package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.Moment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

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
    public List<Moment> getMomentByPublisher(List<String> publishers) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.PUBLISHER).in(publishers));
        return mongoTemplate.find(query, Moment.class);
    }

    /** 向数据库中添加动态 */
    public Moment addMoment(Moment moment) {
        return mongoTemplate.insert(moment);
    }
}
