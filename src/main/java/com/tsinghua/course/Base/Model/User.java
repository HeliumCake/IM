package com.tsinghua.course.Base.Model;

import com.tsinghua.course.Base.Enum.UserType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * @描述 对应mongodb中的User集合，mongodb是非关系型数据库，可以存储的对象类型很丰富，使用起来方便很多
 **/
@Document("User")
public class User {
    // mongodb唯一id
    String id;
    // 用户名
    String username;
    // 密码
    String password;
    // 用户类型
    UserType userType;

    // 私聊聊天id映射，储存着用户名对应着的私聊id
    Map<String, String> privateChatMap;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Map<String, String> getPrivateChatMap() {
        return privateChatMap;
    }

    public void setPrivateChatMap(Map<String, String> privateChatMap) {
        this.privateChatMap = privateChatMap;
    }
}
