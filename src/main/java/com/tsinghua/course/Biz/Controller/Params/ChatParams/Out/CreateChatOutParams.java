package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

public class CreateChatOutParams extends CommonOutParams
{
	/**
	 * 群聊的 id
	 */
	private String chatGroupId;

	public void setChatGroupId(String chatGroupId)
	{
		this.chatGroupId = chatGroupId;
	}

	public String getChatGroupId()
	{
		return chatGroupId;
	}
}
