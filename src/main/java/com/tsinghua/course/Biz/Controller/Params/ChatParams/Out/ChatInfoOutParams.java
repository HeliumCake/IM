package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Enum.ChatGroupType;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.List;

public class ChatInfoOutParams extends CommonOutParams
{
	/**
	 * 唯一标识符
	 */
	String groupId;

	/**
	 * 群聊名称
	 */
	String groupName;

	/**
	 * 是否为群聊。若否，则代表这是一个双人会话
	 */
	ChatGroupType groupType;

	/**
	 * 聊天成员的id列表
	 */
	List<String> memberIdList;
	/**
	 * 聊天管理员的id列表
	 */
	List<String> adminIdList;

	/**
	 * 聊天消息总长度
	 */
	int chatSize;

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setGroupType(ChatGroupType groupType) {
		this.groupType = groupType;
	}

	public void setMemberIdList(List<String> memberIdList) {
		this.memberIdList = memberIdList;
	}

	public void setAdminIdList(List<String> adminIdList) {
		this.adminIdList = adminIdList;
	}

	public void setChatSize(int chatSize) {
		this.chatSize = chatSize;
	}
}
