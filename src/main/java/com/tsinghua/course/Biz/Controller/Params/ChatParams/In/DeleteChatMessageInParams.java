package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;

@BizType(BizTypeEnum.DELETE_CHAT_MESSAGE)
public class DeleteChatMessageInParams extends BasicChatOperationInParams {
	/**
	 * 截取聊天消息的索引
	 */
	@Required
	private int messageId;

	public int getMessageId() {
		return messageId;
	}
}
