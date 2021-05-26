package com.tsinghua.course.Biz.Processor;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.result.UpdateResult;
import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Enum.ChatGroupType;
import com.tsinghua.course.Base.Model.ChatGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述 聊天原子处理器，处理所有与私聊或群聊相关的操作
 **/
@Component
public class ChatProcessor {
	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * 创建一个聊天，返回创建的聊天对象
	 */
	public ChatGroup createChat(ChatGroupType groupType, List<String> memberList) {
		ChatGroup chatGroup = new ChatGroup();
		chatGroup.setGroupType(groupType);
		chatGroup.setMemberList(ImmutableList.copyOf(memberList));
		List<String> admins = new ArrayList<>();
		admins.add(memberList.get(0));
		chatGroup.setAdminList(admins);
		chatGroup.setChats(new ArrayList<>());
		return mongoTemplate.insert(chatGroup);
	}

	/**
	 * 获得与另一用户的私聊聊天对象
	 */
	public ChatGroup getPrivateChatWith(String username, String other) {
		Query query = new Query();
		query.addCriteria(
				Criteria.where(KeyConstant.GROUP_TYPE).is(ChatGroupType.PRIVATE_CHAT).
						and(KeyConstant.MEMBER_LIST).all(username, other)
		);
		return mongoTemplate.findOne(query, ChatGroup.class);
	}

	/**
	 * 获得某一用户加入的所有群聊与私聊
	 */
	public List<ChatGroup> getChats(String username, ChatGroupType groupType) {
		Query query = new Query();
		query.addCriteria(
				Criteria.where(KeyConstant.GROUP_TYPE).is(groupType).
						and(KeyConstant.MEMBER_LIST).all(username)
		);
		return mongoTemplate.find(query, ChatGroup.class);
	}

	/**
	 * 获得某一用户加入的所有群聊
	 */
	public List<ChatGroup> getGroupChats(String username) {
		return getChats(username, ChatGroupType.PRIVATE_CHAT);
	}

	/**
	 * 获得某一用户加入的所有私聊
	 */
	public List<ChatGroup> getPrivateChats(String username) {
		return getChats(username, ChatGroupType.GROUP_CHAT);
	}

	/**
	 * 通过聊天id返回聊天对象，若不存在则返回null
	 */
	public ChatGroup getChatGroupById(String groupId) {
		Query query = new Query();
		query.addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		return mongoTemplate.findOne(query, ChatGroup.class);
	}

	/**
	 * 为指定聊天添加一条聊天消息，返回是否成功
	 */
	public boolean addChatMessage(String groupId, ChatGroup.ChatMessage message) {
		Query query = new Query();
		query.addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		UpdateResult result = mongoTemplate.updateFirst(query, new Update().push(KeyConstant.CHATS, message), ChatGroup.class);
		return result.getModifiedCount() > 0;
	}
}
