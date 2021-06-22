package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

public abstract class BasicChatOperationInParams extends CommonInParams {
	/**
	 * 聊天会话的id
	 */
	@Required
	private String groupId;

	public String getGroupId()
	{
		return groupId;
	}
}
