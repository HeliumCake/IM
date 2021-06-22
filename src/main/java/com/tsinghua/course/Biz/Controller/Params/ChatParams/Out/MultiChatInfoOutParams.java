package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Model.ChatGroup;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.List;

public class MultiChatInfoOutParams extends CommonOutParams
{
	/**
	 * 聊天信息列表
	 */
	List<ChatGroup> chats;

	public void setChats(List<ChatGroup> chats) {
		this.chats = chats;
	}
}
