package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.Enum.ChatGroupType;
import com.tsinghua.course.Base.Error.ChatWarnEnum;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Model.ChatGroup;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.CreateChatInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.CreateChatOutParams;
import com.tsinghua.course.Biz.Processor.ChatProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatController {
	@Autowired
	ChatProcessor chatProcessor;

	@Autowired
	private UserProcessor userProcessor;

	@NeedLogin
	@BizType(BizTypeEnum.CREATE_CHAT)
	public CreateChatOutParams createChat(CreateChatInParams params) throws CourseWarn
	{
		CreateChatOutParams result = new CreateChatOutParams();
		for (String chatter: params.getMemberList()) {
			if (userProcessor.getUserByUsername(chatter) == null) {
				throw new CourseWarn(ChatWarnEnum.UNKNOWN_USERNAME);
			}
		}

		// 若创建的是双人聊天，则需要判断是否已存在该聊天
		if (params.getGroupType() == ChatGroupType.PRIVATE_CHAT) {
			if (params.getMemberList().size() != 2) {
				throw new CourseWarn(ChatWarnEnum.ILLEGAL_PARAMETER);
			}
			ChatGroup chatGroup = userProcessor.getPrivateChatWith(params.getMemberList().get(0), params.getMemberList().get(1));
			if (chatGroup != null) {
				result.setChatGroupId(chatGroup.getId());
				result.setSuccess(false);
				return result;
			}
		}
		ChatGroup chatGroup = chatProcessor.createChat(params.getGroupType(), params.getMemberList());
		result.setChatGroupId(chatGroup.getId());
		result.setSuccess(true);

		if (params.getGroupType() == ChatGroupType.PRIVATE_CHAT) {
			userProcessor.setPrivateChatWith(params.getMemberList().get(0), params.getMemberList().get(1), chatGroup.getId());
			userProcessor.setPrivateChatWith(params.getMemberList().get(1), params.getMemberList().get(0), chatGroup.getId());
		}
		return result;
	}
}
