package com.tsinghua.course.Base.Constant;

/**
 * @描述 用于key的常量列表，一般对应于类中的属性名称，如User类中的username属性
 **/
public class KeyConstant {
    /** 以下为全局通用key */
    // 参数
    public static final String PARAMS = "params";
    // 路径
    public static final String PATH = "path";
    // 操作类型
    public static final String BIZ_TYPE = "bizType";

    /** 以下为通用关键key */
    // 用户名
    public static final String ID = "id";

    /** 以下为User关键key */
    // 用户名
    public static final String USERNAME = "username";
    // 私聊聊天id映射
    public static final String PRIVATE_CHAT_MAP = "privateChatMap";

    /** 以下为Moment关键key */
    // 发布者
    public static final String PUBLISHER = "publisher";

    /** 以下为ChatGroup关键key */
    // 用户名
    public static final String GROUP_TYPE = "groupType";
    // 聊天成员的id列表
    public static final String MEMBER_LIST = "memberList";
    // 聊天消息列表
    public static final String CHATS = "chats";
}
