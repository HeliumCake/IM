package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.Date;

public class ChatMessageOutParams extends CommonOutParams {
	// 见 com.tsinghua.course.Base.Model.ChatGroup.ChatMessage

	/**
	 * 唯一标识符
	 */
	private long id;
	/**
	 * 创建时间
	 */
	private Date timeCreated;
	/**
	 * 发送者id
	 */
	private String senderId;
	/**
	 * 文本消息
	 */
	private String text;

	/**
	 * 附件文件名
	 */
	private String attachmentName;

	/**
	 * 附件文件内容
	 */
	private Byte[] attachmentContent;

	public void setId(long id)
	{
		this.id = id;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public void setAttachmentContent(Byte[] attachmentContent) {
		this.attachmentContent = attachmentContent;
	}
}
