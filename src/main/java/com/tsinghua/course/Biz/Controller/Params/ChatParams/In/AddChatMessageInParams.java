package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Base.Enum.FileType;
import com.tsinghua.course.Biz.BizTypeEnum;

@BizType(BizTypeEnum.ADD_CHAT_MESSAGE)
public class AddChatMessageInParams extends BasicChatOperationInParams {
	// 见 com.tsinghua.course.Base.Model.ChatGroup.ChatMessage

	/**
	 * 文本消息
	 */
	@Required
	private String text;

	/**
	 * 附件文件名
	 */
	private String attachmentName;

	/**
	 * 附件文件类型
	 */
	private FileType attachmentType;

	public String getText() {
		return text;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public FileType getAttachmentType() {
		return attachmentType;
	}
}
