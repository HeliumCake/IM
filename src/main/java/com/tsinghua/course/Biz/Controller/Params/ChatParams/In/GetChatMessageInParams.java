package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;

@BizType(BizTypeEnum.GET_CHAT_MESSAGE)
public class GetChatMessageInParams extends BasicChatOperationInParams {
	/**
	 * 截取聊天消息列表的下界
	 */
	private int lowerBound = -1;
	/**
	 * 截取聊天消息列表的上界
	 */
	private int upperBound = -1;

	public int getLowerBound() {
		return lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}
}
