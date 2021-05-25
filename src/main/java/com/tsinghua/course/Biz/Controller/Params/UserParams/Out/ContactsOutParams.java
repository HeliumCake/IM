package com.tsinghua.course.Biz.Controller.Params.UserParams.Out;

import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.List;

/**
 * @描述 浏览联系人列表的出参
 **/
public class ContactsOutParams extends CommonOutParams {
    // 联系人昵称列表
    private List<String> contacts;

    public ContactsOutParams(boolean success, List<String> contacts) {
        this.success = success;
        this.contacts = contacts;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }
}
