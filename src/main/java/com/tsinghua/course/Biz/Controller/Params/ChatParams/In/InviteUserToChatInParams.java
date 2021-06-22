package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;

@BizType(BizTypeEnum.INVITE_USER_TO_CHAT)
public class InviteUserToChatInParams extends BasicChatOperationInParams {
	/**
	 * 被邀请进入聊天的用户的用户名
	 */
	String invitedUser;

	public String getInvitedUser() {
		return invitedUser;
	}
}
