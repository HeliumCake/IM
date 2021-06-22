package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.Enum.ChatGroupType;
import com.tsinghua.course.Base.Error.ChatWarnEnum;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Model.ChatGroup;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.*;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.ChatInfoOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.CreateChatOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.MultiChatInfoOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.MultiChatMessageOutParams;
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
		if (params.getMemberList().isEmpty()) {
			throw new CourseWarn(ChatWarnEnum.ILLEGAL_PARAMETER);
		}
		if (params.getGroupType() == null) {
			throw new CourseWarn(ChatWarnEnum.UNKNOWN_GROUP_TYPE);
		}

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
			String user1 = params.getMemberList().get(0);
			String user2 = params.getMemberList().get(1);
			ChatGroup chatGroup = chatProcessor.getPrivateChatWith(user1, user2);
			if (chatGroup != null) {
				result.setChatGroupId(chatGroup.getId());
				result.setSuccess(false);
				return result;
			}
		}
		ChatGroup chatGroup = chatProcessor.createChat(params.getGroupType(), params.getMemberList());
		result.setChatGroupId(chatGroup.getId());
		return result;
	}

	/**
	 * 通过给出参数的用户名与聊天id，尝试获得对应的聊天对象
	 * 若聊天id不存在，或者用户不处于此聊天中，则抛出相关异常
	 * 返回对应的聊天对象
	 */
	private ChatGroup ensureUserInChat(BasicChatOperationInParams params) throws CourseWarn
	{
		ChatGroup group = chatProcessor.getChatGroupById(params.getGroupId());
		if (group == null) {
			throw new CourseWarn(ChatWarnEnum.UNKNOWN_GROUP_ID);
		}
		group = chatProcessor.getChatGroupIfUserIsInById(params.getGroupId(), params.getUsername(), true, false);
		if (group == null) {
			throw new CourseWarn(ChatWarnEnum.NOT_IN_THE_CHAT);
		}
		return group;
	}

	/**
	 * 类似ensureUserInChat，但额外检查保证这不是私聊
	 */
	private ChatGroup ensureUserInGroupChat(BasicChatOperationInParams params) throws CourseWarn
	{
		ChatGroup group = this.ensureUserInChat(params);
		if (group.getGroupType() != ChatGroupType.GROUP_CHAT) {
			throw new CourseWarn(ChatWarnEnum.PRIVATE_CHAT_OPERATION_NOT_SUPPORT);
		}
		return group;
	}

	/**
	 * 类似ensureUserInGroupChat，但额外检查保证用户是管理员
	 */
	private ChatGroup ensureUserInGroupChatAndIsAdmin(BasicChatOperationInParams params) throws CourseWarn
	{
		this.ensureUserInGroupChat(params);
		ChatGroup group = chatProcessor.getChatGroupIfUserIsInById(params.getGroupId(), params.getUsername(), true, true);
		if (group == null) {
			throw new CourseWarn(ChatWarnEnum.NOT_ADMIN);
		}
		return group;
	}

	/**
	 * 由ChatGroup生成QueryChatInfoOutParams
	 */
	private ChatInfoOutParams generateChatInfoOutParams(ChatGroup group) {
		ChatInfoOutParams result = new ChatInfoOutParams();
		result.setGroupId(group.getId());
		result.setGroupName(group.getGroupName());
		result.setGroupType(group.getGroupType());
		result.setMemberList(group.getMemberList());
		result.setAdminList(group.getAdminList());
		result.setChatSize(group.getChats().size());
		result.setSuccess(true);
		return result;
	}

	/**
	 * 获得指定聊天的信息
	 */
	@NeedLogin
	@BizType(BizTypeEnum.QUERY_CHAT_INFO)
	public ChatInfoOutParams queryChatInfo(QueryChatInfoInParams params) throws CourseWarn {
		ChatGroup group = this.ensureUserInChat(params);
		return this.generateChatInfoOutParams(group);
	}

	/**
	 * 离开指定聊天
	 */
	@NeedLogin
	@BizType(BizTypeEnum.QUIT_CHAT)
	public CommonOutParams quitChat(QuitChatInParams params) throws CourseWarn {
		ChatGroup group = this.ensureUserInGroupChat(params);
		CommonOutParams result = new CommonOutParams();
		result.setSuccess(chatProcessor.quitChat(params.getGroupId(), params.getUsername()));
		return result;
	}

	/**
	 * 删除指定聊天
	 */
	@NeedLogin
	@BizType(BizTypeEnum.DELETE_CHAT)
	public CommonOutParams deleteChat(DeleteChatInParams params) throws CourseWarn {
		this.ensureUserInGroupChatAndIsAdmin(params);
		CommonOutParams result = new CommonOutParams();
		result.setSuccess(chatProcessor.deleteChat(params.getGroupId()));
		return result;
	}

	/**
	 * 修改聊天名
	 */
	@NeedLogin
	@BizType(BizTypeEnum.MODIFY_CHAT_NAME)
	public CommonOutParams modifyGroupName(ModifyGroupNameInParams params) throws CourseWarn {
		this.ensureUserInGroupChatAndIsAdmin(params);
		CommonOutParams result = new CommonOutParams();
		result.setSuccess(chatProcessor.modifyGroupName(params.getGroupId(), params.getNewName()));
		return result;
	}

	/**
	 * 添加指定聊天的聊天消息
	 */
	@NeedLogin
	@BizType(BizTypeEnum.ADD_CHAT_MESSAGE)
	public CommonOutParams addChatMessage(AddChatMessageInParams params) throws CourseWarn {
		this.ensureUserInChat(params);
		CommonOutParams result = new CommonOutParams();
		ChatGroup.ChatMessage message = new ChatGroup.ChatMessage();
		message.setSenderId(ThreadUtil.getUsername());
		message.setText(params.getText());
		message.setAttachmentName(params.getAttachmentName());
		message.setAttachmentType(params.getAttachmentType());
		result.setSuccess(chatProcessor.addChatMessage(params.getGroupId(), message));
		return result;
	}

	/**
	 * 获得指定聊天的范围内的聊天消息
	 */
	@NeedLogin
	@BizType(BizTypeEnum.GET_CHAT_MESSAGE)
	public MultiChatMessageOutParams getChatMessage(GetChatMessageInParams params) throws CourseWarn {
		ChatGroup group = this.ensureUserInChat(params);
		List<ChatGroup.ChatMessage> messages = new ArrayList<>();
		int l = params.getLowerBound() != -1 ? params.getLowerBound() : 0;
		int r = params.getUpperBound() != -1 ? params.getUpperBound() : group.getChats().size();
		if (l < 0 || group.getChats().size() < r) {
			throw new CourseWarn(ChatWarnEnum.OUT_OF_CHAT_INDEX_BOUND);
		}
		for (int i = l; i < r; i++) {
			messages.add(group.getChats().get(i));
		}
		MultiChatMessageOutParams result = new MultiChatMessageOutParams();
		result.setMessages(messages);
		result.setSuccess(!messages.isEmpty());
		return result;
	}

	/**
	 * 删除指定聊天的一个聊天消息
	 */
	@NeedLogin
	@BizType(BizTypeEnum.DELETE_CHAT_MESSAGE)
	public CommonOutParams deleteChatMessage(DeleteChatMessageInParams params) throws CourseWarn {
		this.ensureUserInChat(params);
		CommonOutParams result = new CommonOutParams();
		result.setSuccess(chatProcessor.deleteChatMessage(params.getGroupId(), params.getMessageId()));
		return result;
	}

	/**
	 * 邀请某人进入聊天
	 */
	@NeedLogin
	@BizType(BizTypeEnum.INVITE_USER_TO_CHAT)
	public CommonOutParams inviteUserToChat(InviteUserToChatInParams params) throws CourseWarn {
		this.ensureUserInChat(params);
		if (userProcessor.getUserByUsername(params.getInvitedUser()) == null) {
			throw new CourseWarn(ChatWarnEnum.UNKNOWN_USERNAME);
		}
		if (chatProcessor.getChatGroupIfUserIsInById(params.getGroupId(), params.getInvitedUser(), true, false) != null) {
			throw new CourseWarn(ChatWarnEnum.USER_ALREADY_IN_THE_CHAT);
		}
		CommonOutParams result = new CommonOutParams();
		result.setSuccess(chatProcessor.addUserToChat(params.getGroupId(), params.getInvitedUser()));
		return result;
	}

	/**
	 * 获得所有聊天的基本信息
	 */
	@NeedLogin
	@BizType(BizTypeEnum.GET_ALL_CHAT_INFO)
	public MultiChatInfoOutParams getAllChatInfoOfUser(GetAllChatInfoInParams params) {
		List<ChatGroup> groups = chatProcessor.getAllChatInfoOfUser(params.getUsername());
		for (ChatGroup group : groups) {
			group.setChats(null);  // 出参不需要聊天信息
		}
		MultiChatInfoOutParams result = new MultiChatInfoOutParams();
		result.setChats(groups);
		return result;
	}
}
