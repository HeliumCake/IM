package com.tsinghua.course.Biz.Controller.Params.UserParams.Out;

import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查找用户的出参
 **/
public class SearchOutParams extends CommonOutParams {
    // 查找到的用户信息
    private User user;

    public SearchOutParams(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
