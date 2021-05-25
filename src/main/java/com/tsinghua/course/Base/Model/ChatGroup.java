package com.tsinghua.course.Base.Model;

import com.tsinghua.course.Base.Enum.ChatGroupType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("ChatGroup")
public class ChatGroup
{
	/**
	 * 唯一标识符
	 */
	String id;

	/**
	 * 是否为群聊。若否，则代表这是一个双人会话
	 */
	ChatGroupType groupType;

	/**
	 * 聊天成员的id列表
	 */
	List<String> memberList;
	/**
	 * 聊天管理员的id列表
	 */
	List<String> adminList;

	/**
	 * 单条聊天消息类
	 */
	public static class ChatMessage
	{
		/**
		 * 创建时间
		 */
		Date timeCreated;
		/**
		 * 发送者id
		 */
		String senderId;
		/**
		 * 文本消息
		 */
		String text;
		/**
		 * 附件文件名称，为空表示无附件
		 */
		String attachmentName;
		/**
		 * 附件文件内容，长度限制 15MB
		 */
		Byte[] attachmentContent;

		public ChatMessage() {
			this.timeCreated = new Date();
		}

		public Date getTimeCreated() {
			return timeCreated;
		}

		public void setSenderId(String senderId) {
			this.senderId = senderId;
		}

		public String getSenderId() {
			return senderId;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public void setAttachmentName(String attachmentName) {
			this.attachmentName = attachmentName;
		}

		public String getAttachmentName() {
			return attachmentName;
		}

		public void setAttachmentContent(Byte[] attachmentContent) {
			this.attachmentContent = attachmentContent;
		}

		public Byte[] getAttachmentContent() {
			return attachmentContent;
		}
	}

	/**
	 * 聊天消息列表
	 */
	List<ChatMessage> chats;

	public String getId()
	{
		return id;
	}

	public ChatGroupType getGroupType()
	{
		return groupType;
	}

	public List<String> getMemberList()
	{
		return memberList;
	}

	public List<String> getAdminList()
	{
		return adminList;
	}

	public List<ChatMessage> getChats()
	{
		return chats;
	}

	public void setGroupType(ChatGroupType groupType)
	{
		this.groupType = groupType;
	}

	public void setMemberList(List<String> memberList)
	{
		this.memberList = memberList;
	}

	public void setAdminList(List<String> adminList)
	{
		this.adminList = adminList;
	}

	public void setChats(List<ChatMessage> chats)
	{
		this.chats = chats;
	}
}
