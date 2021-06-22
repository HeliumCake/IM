package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Model.ChatGroup;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.List;
import java.util.Map;

public class MultiChatInfoOutParams extends CommonOutParams
{
	/**
	 * 聊天信息列表，其中的chats域会被设置为null
	 */
	List<ChatGroup> chats;

	/**
	 * 储存着每个聊天的最后一条聊天信息
	 * 键：ChatGroup的id
	 * 值：对应ChatGroup的最后一条聊天信息数据
	 */
	Map<String, ChatGroup.ChatMessage> lastMessage;

	public void setChats(List<ChatGroup> chats) {
		this.chats = chats;
	}

	public void setLastMessage(Map<String, ChatGroup.ChatMessage> lastMessage) {
		this.lastMessage = lastMessage;
	}
}
