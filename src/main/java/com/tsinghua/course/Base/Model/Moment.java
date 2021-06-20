package com.tsinghua.course.Base.Model;

import com.tsinghua.course.Base.Enum.MomentType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

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
    // 动态图片名列表
    List<String> pictures;
    // 动态视频名
    String video;
    // 点赞列表
    List<String> thumbs = new ArrayList<>();
    /** 单条回复类 */
    public static class Reply {
        // 发送者id
        String sender;
        // 回复文本
        String text;
        // 回复时间
        String time;

        public Reply(String sender, String text, String time) {
            this.sender = sender;
            this.text = text;
            this.time = time;
        }
        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
    List<Reply> replies = new ArrayList<>();

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

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public MomentType getMomentType() {
        return momentType;
    }

    public void setMomentType(MomentType momentType) {
        this.momentType = momentType;
    }

    public List<String> getThumbs() {
        return thumbs;
    }

    public void setThumbs(List<String> thumbs) {
        this.thumbs = thumbs;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
