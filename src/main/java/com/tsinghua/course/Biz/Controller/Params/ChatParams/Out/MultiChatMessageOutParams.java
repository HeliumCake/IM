package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Model.ChatGroup;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.List;

public class MultiChatMessageOutParams extends CommonOutParams
{
	/**
	 * 消息信息列表
	 */
	private List<ChatGroup.ChatMessage> messages;

	public void setMessages(List<ChatGroup.ChatMessage> messages) {
		this.messages = messages;
	}
}
