package com.tsinghua.course.Base.Model;

import com.tsinghua.course.Base.Enum.MomentType;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 对应mongodb中的Moment集合
 **/
@Document("Moment")
public class Moment {
    // mongodb唯一id
    String id;
    // 发布者id
    String publisher;
    // 动态类型
    MomentType momentType;
    // 动态文本
    String text;
    /** 存储的时间 */
    String time;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MomentType getMomentType() {
        return momentType;
    }

    public void setMomentType(MomentType momentType) {
        this.momentType = momentType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
