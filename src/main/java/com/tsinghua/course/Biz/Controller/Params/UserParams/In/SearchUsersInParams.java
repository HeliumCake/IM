package com.tsinghua.course.Biz.Controller.Params.UserParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

import java.util.List;

/**
 * @描述 查找多个用户的入参
 **/
@BizType(BizTypeEnum.USER_SEARCH_USERS)
public class SearchUsersInParams extends CommonInParams {
    // 要查找的用户id列表
    @Required
    private List<String> users;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
