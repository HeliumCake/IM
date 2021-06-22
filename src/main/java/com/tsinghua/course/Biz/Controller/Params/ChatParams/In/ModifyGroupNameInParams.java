package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;

@BizType(BizTypeEnum.MODIFY_CHAT_NAME)
public class ModifyGroupNameInParams extends BasicChatOperationInParams {
	/**
	 * 聊天的新名字
	 */
	String newName;

	public String getNewName() {
		return newName;
	}
}
