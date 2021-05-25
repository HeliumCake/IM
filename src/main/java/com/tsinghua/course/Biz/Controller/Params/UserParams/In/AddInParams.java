package com.tsinghua.course.Biz.Controller.Params.UserParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 添加联系人的入参
 **/
@BizType(BizTypeEnum.USER_ADD)
public class AddInParams extends CommonInParams {
    // 要添加的用户名
    @Required
    private String add;

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }
}
