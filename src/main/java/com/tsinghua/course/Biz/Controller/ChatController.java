package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.Enum.ChatGroupType;
import com.tsinghua.course.Base.Error.ChatWarnEnum;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Model.ChatGroup;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.AddChatMessageInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.CreateChatInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.GetChatMessageInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.QueryChatInfoInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.ChatMessageOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.CreateChatOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.QueryChatInfoOutParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Processor.ChatProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

		if (params.getGroupType() == ChatGroupType.PRIVATE_CHAT) {
			userProcessor.setPrivateChatWith(params.getMemberList().get(0), params.getMemberList().get(1), chatGroup.getId());
			userProcessor.setPrivateChatWith(params.getMemberList().get(1), params.getMemberList().get(0), chatGroup.getId());
		}
		return result;
	}

	/**
	 * 获得指定聊天的信息
	 */
	@NeedLogin
	@BizType(BizTypeEnum.QUERY_CHAT_INFO)
	public QueryChatInfoOutParams queryChatInfo(QueryChatInfoInParams params) throws CourseWarn {
		ChatGroup group = chatProcessor.getChatGroupById(params.getGroupId());
		if (group == null) {
			throw new CourseWarn(ChatWarnEnum.UNKNOWN_GROUP_ID);
		}
		QueryChatInfoOutParams result = new QueryChatInfoOutParams();
		result.setGroupId(group.getId());
		result.setGroupType(group.getGroupType());
		result.setMemberList(group.getMemberList());
		result.setAdminList(group.getAdminList());
		result.setChatSize(group.getChats().size());
		return result;
	}

	/**
	 * 添加指定聊天的聊天消息
	 */
	@NeedLogin
	@BizType(BizTypeEnum.ADD_CHAT_MESSAGE)
	public CommonOutParams addChatMessage(AddChatMessageInParams params) throws CourseWarn {
		ChatGroup group = chatProcessor.getChatGroupById(params.getGroupId());
		if (group == null) {
			throw new CourseWarn(ChatWarnEnum.UNKNOWN_GROUP_ID);
		}
		CommonOutParams result = new CommonOutParams();
		ChatGroup.ChatMessage message = new ChatGroup.ChatMessage();
		message.setSenderId(ThreadUtil.getUsername());
		message.setText(params.getText());
		message.setAttachmentName(params.getAttachmentName());
		message.setAttachmentContent(params.getAttachmentContent());
		result.setSuccess(chatProcessor.addChatMessage(params.getGroupId(), message));
		return result;
	}

	/**
	 * 获得指定聊天的聊天消息
	 */
	@NeedLogin
	@BizType(BizTypeEnum.GET_CHAT_MESSAGE)
	public List<ChatMessageOutParams> getChatMessage(GetChatMessageInParams params) throws CourseWarn {
		List<ChatMessageOutParams> result = new ArrayList<>();
		ChatGroup group = chatProcessor.getChatGroupById(params.getGroupId());
		if (group == null) {
			throw new CourseWarn(ChatWarnEnum.UNKNOWN_GROUP_ID);
		}
		int l = params.getLowerBound() != -1 ? params.getLowerBound() : 0;
		int r = params.getUpperBound() != -1 ? params.getUpperBound() : group.getChats().size();
		if (l < 0 || group.getChats().size() < r) {
			throw new CourseWarn(ChatWarnEnum.OUT_OF_CHAT_INDEX_BOUND);
		}
		for (int i = l; i < r; i++) {
			ChatGroup.ChatMessage message = group.getChats().get(i);
			ChatMessageOutParams outParams = new ChatMessageOutParams();
			outParams.setSenderId(message.getSenderId());
			outParams.setText(message.getText());
			outParams.setTimeCreated(message.getTimeCreated());
			outParams.setAttachmentName(message.getAttachmentName());
			outParams.setAttachmentContent(message.getAttachmentContent());
			result.add(outParams);
		}
		return result;
	}
}
