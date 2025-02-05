package com.tsinghua.course.Biz.Processor;

import com.mongodb.client.result.UpdateResult;
import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Enum.ChatGroupType;
import com.tsinghua.course.Base.Model.ChatGroup;
import com.tsinghua.course.Base.Model.User;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @描述 聊天原子处理器，处理所有与私聊或群聊相关的操作
 **/
@Component
public class ChatProcessor {
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	private UserProcessor userProcessor;

	/**
	 * 确保一个聊天存在管理员
	 */
	private void ensureAdminExists(ChatGroup chatGroup) {
		if (chatGroup.getAdminIdList().isEmpty() && !chatGroup.getMemberIdList().isEmpty()) {
			chatGroup.getAdminIdList().add(chatGroup.getMemberIdList().get(0));
			Query query = new Query();
			query.addCriteria(Criteria.where(KeyConstant.ID).is(chatGroup.getId()));
			mongoTemplate.updateFirst(query, new Update().set(KeyConstant.ADMIN_ID_LIST, chatGroup.getAdminIdList()), ChatGroup.class);
		}
	}

	/**
	 * 创建一个聊天，返回创建的聊天对象
	 */
	public ChatGroup createChat(ChatGroupType groupType, List<String> memberIdList) {
		if (memberIdList.isEmpty()) {
			throw new RuntimeException("Creating chat group with empty member list");
		}
		ChatGroup chatGroup = new ChatGroup();
		User firstMember = userProcessor.getUserById(memberIdList.get(0));
		chatGroup.setGroupName(firstMember.getNickname() + "的群聊");
		chatGroup.setGroupType(groupType);
		chatGroup.getMemberIdList().addAll(memberIdList);
		ChatGroup insertedChatGroup = mongoTemplate.insert(chatGroup);
		this.ensureAdminExists(insertedChatGroup);
		return insertedChatGroup;
	}

	/**
	 * 获得与另一用户的私聊聊天对象
	 */
	public ChatGroup getPrivateChatWith(String userId, String otherId) {
		Query query = new Query().addCriteria(
				Criteria.where(KeyConstant.GROUP_TYPE).is(ChatGroupType.PRIVATE_CHAT).
						and(KeyConstant.MEMBER_ID_LIST).all(userId, otherId)
		);
		return mongoTemplate.findOne(query, ChatGroup.class);
	}

	/**
	 * 通过聊天id返回聊天对象，且保证用户在成员列表中，若不存在则返回null
	 */
	public ChatGroup getChatGroupIfUserIsInById(String groupId, String userId, boolean checkMember, boolean checkAdmin) {
		Query query = new Query().addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		if (checkMember) {
			query.addCriteria(Criteria.where(KeyConstant.MEMBER_ID_LIST).all(userId));
		}
		if (checkAdmin) {
			query.addCriteria(Criteria.where(KeyConstant.ADMIN_ID_LIST).all(userId));
		}
		return mongoTemplate.findOne(query, ChatGroup.class);
	}

	/**
	 * 通过聊天id返回聊天对象，若不存在则返回null
	 */
	public ChatGroup getChatGroupById(String groupId) {
		// userId为null是安全的，因为当后两个参数为false时userId不会被使用
		return getChatGroupIfUserIsInById(groupId, null, false, false);
	}

	/**
	 * 为指定聊天添加一条聊天消息，返回是否成功
	 */
	public boolean addChatMessage(String groupId, ChatGroup.ChatMessage message) {
		ChatGroup group = getChatGroupById(groupId);
		if (group == null)
		{
			return false;
		}
		long id = 0;
		if (!group.getChats().isEmpty())
		{
			id = group.getChats().get(group.getChats().size() - 1).getId() + 1;
		}
		message.setId(id);
		Query query = new Query();
		query.addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		UpdateResult result = mongoTemplate.updateFirst(query, new Update().push(KeyConstant.CHATS, message), ChatGroup.class);
		return result.getModifiedCount() > 0;
	}

	/**
	 * 为指定聊天删除指定聊天信息id的一条聊天信息，返回是否成功
	 */
	public boolean deleteChatMessage(String groupId, int messageId)
	{
		Query query = new Query().addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		Document document = new Document();
		document.put(KeyConstant.ID, messageId);
		UpdateResult result = mongoTemplate.updateFirst(query, new Update().pull(KeyConstant.CHATS, document), ChatGroup.class);
		return result.getModifiedCount() > 0;
	}

	/**
	 * 用户退出聊天
	 */
	public boolean quitChat(String groupId, String userId) {
		Query query = new Query().addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		UpdateResult result1 = mongoTemplate.updateFirst(query, new Update().pull(KeyConstant.MEMBER_ID_LIST, userId), ChatGroup.class);
		UpdateResult result2 = mongoTemplate.updateFirst(query, new Update().pull(KeyConstant.ADMIN_ID_LIST, userId), ChatGroup.class);

		// 检查该群聊是否还有人
		ChatGroup group = this.getChatGroupById(groupId);
		if (group.getMemberIdList().isEmpty()) {
			deleteChat(groupId);
		}
		else {
			this.ensureAdminExists(group);
		}

		return result1.getModifiedCount() > 0;
	}

	/**
	 * 删除聊天
	 */
	public boolean deleteChat(String groupId)
	{
		Query query = new Query().addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		return mongoTemplate.remove(query, ChatGroup.class).getDeletedCount() > 0;
	}

	/**
	 * 修改聊天名
	 */
	public boolean modifyGroupName(String groupId, String newName)
	{
		Query query = new Query().addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		return mongoTemplate.updateFirst(query, new Update().set(KeyConstant.GROUP_NAME, newName), ChatGroup.class).getModifiedCount() > 0;
	}

	/**
	 * 将用户添加至聊天的成员中
	 */
	public boolean addUserToChat(String groupId, String invitedUserId)
	{
		Query query = new Query().addCriteria(Criteria.where(KeyConstant.ID).is(groupId));
		return mongoTemplate.updateFirst(query, new Update().push(KeyConstant.MEMBER_ID_LIST, invitedUserId), ChatGroup.class).getModifiedCount() > 0;
	}

	/**
	 * 获得用户所在的所有聊天
	 */
	public List<ChatGroup> getAllChatInfoOfUser(String userId)
	{
		Query query = new Query().addCriteria(Criteria.where(KeyConstant.MEMBER_ID_LIST).all(userId));
		return mongoTemplate.find(query, ChatGroup.class);
	}
}
