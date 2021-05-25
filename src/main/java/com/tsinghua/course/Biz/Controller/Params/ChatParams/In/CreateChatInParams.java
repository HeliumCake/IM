package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Base.Enum.ChatGroupType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

import java.util.List;

@BizType(BizTypeEnum.CREATE_CHAT)
public class CreateChatInParams extends CommonInParams {
	/**
	 * 是否为群聊。若否，则代表这是一个双人会话
	 */
	@Required
	private ChatGroupType groupType;

	/**
	 * 聊天成员的id列表。username 应包含于其中
	 */
	@Required
	private List<String> memberList;

	public ChatGroupType getGroupType() {
		return groupType;
	}

	public List<String> getMemberList() {
		return memberList;
	}
}
