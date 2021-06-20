package com.tsinghua.course.Biz.Controller.Params.UserParams.Out;

import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.List;

/**
 * @描述 浏览联系人列表的出参
 **/
public class ContactsOutParams extends CommonOutParams {
    // 联系人列表
    private List<User> contacts;

    public ContactsOutParams(boolean success, List<User> contacts) {
        this.success = success;
        this.contacts = contacts;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }
}
