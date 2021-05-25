package com.tsinghua.course.Base.Enum;

/**
 * 聊天会话的类型
 */
public enum ChatGroupType
{
	PRIVATE_CHAT("双人聊天"),
	GROUP_CHAT("群聊");

	ChatGroupType(String name) {
		this.name = name;
	}

	String name;

	public String getName() {
		return name;
	}
}
