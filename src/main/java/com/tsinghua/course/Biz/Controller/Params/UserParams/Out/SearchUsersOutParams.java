package com.tsinghua.course.Biz.Controller.Params.UserParams.Out;

import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.List;

/**
 * @描述 查找多个用户的出参
 **/
public class SearchUsersOutParams extends CommonOutParams {
    // 查找到的用户信息列表
    private List<User> users;

    public SearchUsersOutParams(boolean success, List<User> users) {
        this.success = success;
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
