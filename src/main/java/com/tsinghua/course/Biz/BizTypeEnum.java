package com.tsinghua.course.Biz;

import com.tsinghua.course.Biz.Controller.*;

/**
 * @描述 业务类型枚举，所有的业务类型都需要枚举在此类中
 **/
public enum BizTypeEnum {
    /** 以下为用户业务类型 */
    USER_LOGIN(UserController.class, "/user/login", "用户登录"),
    USER_REGISTER(UserController.class, "/user/register", "用户注册"),
    USER_PASSWORD(UserController.class, "/user/password", "修改密码"),
    USER_USERNAME(UserController.class, "/user/username", "修改用户名"),
    USER_NICKNAME(UserController.class, "/user/nickname", "修改昵称"),
    USER_AVATAR(UserController.class, "/user/avatar", "修改头像"),
    USER_SEARCH(UserController.class, "/user/search", "查找用户"),
    USER_SEARCH_USERS(UserController.class, "/user/searchUsers", "查找多个用户"),
    USER_ADD(UserController.class, "/user/add", "添加联系人"),
    USER_DELETE(UserController.class, "/user/delete", "删除联系人"),
    USER_VIEW(UserController.class, "/user/view", "浏览联系人列表"),

    /** 以下为动态业务类型 */
    MOMENT_PUBLISH(MomentController.class, "/moment/publish", "发布动态"),
    MOMENT_VIEW_USER(MomentController.class, "/moment/view/user", "浏览单个用户的动态"),
    MOMENT_VIEW_CONTACTS(MomentController.class, "/moment/view/contacts", "浏览所有联系人的动态"),
    MOMENT_THUMB(MomentController.class, "/moment/thumb", "点赞动态"),
    MOMENT_REPLY(MomentController.class, "/moment/reply", "回复动态"),

    UPLOAD(UploadController.class, "/upload", "上传文件"),

    /** 聊天会话相关 */
    CREATE_CHAT(ChatController.class, "/chat/create", "创建聊天"),
    QUIT_CHAT(ChatController.class, "/chat/quit", "退出聊天"),
    INVITE_USER_TO_CHAT(ChatController.class, "/chat/inviteUser", "邀请某人进入聊天"),
    DELETE_CHAT(ChatController.class, "/chat/delete", "管理员删除聊天"),
    MODIFY_CHAT_NAME(ChatController.class, "/chat/modifyName", "管理员修改聊天名称"),
    GET_ALL_CHAT_INFO(ChatController.class, "/chat/getAll", "获得某人属于的所有群聊"),
    QUERY_CHAT_INFO(ChatController.class, "/chat/info", "获得指定聊天的信息"),
    ADD_CHAT_MESSAGE(ChatController.class, "/chat/addMessage", "添加指定聊天的聊天消息"),
    GET_CHAT_MESSAGE(ChatController.class, "/chat/getMessage", "获得指定聊天的范围内的聊天消息"),
    DELETE_CHAT_MESSAGE(ChatController.class, "/chat/deleteMessage", "删除指定聊天的一个聊天消息"),

    /** 定时任务业务测试 */
    LOG_TEST(TimerController.class, null, "定时日志测试"),

    /** 测试业务，在书写正式代码时可以删除，在书写正式代码前先运行测试业务，如果测试业务无问题说明各模块正常 */
    LOGIN_TEST(TestController.class, "/test/loginPermission", "登录控制测试"),
    ADMIN_TEST(TestController.class, "/test/adminPermission", "管理员权限控制测试"),
    REDIS_TEST(TestController.class, "/test/redis", "redis缓存测试"),
    TIMER_TEST(TestController.class, "/test/timer", "定时器测试"),
    ERROR_TEST(TestController.class, "/test/error", "内部报错测试"),
    FILE_UPLOAD_TEST(TestController.class, "/test/upload", "文件上传测试"),
    FILE_DOWNLOAD_TEST(TestController.class, "/test/url", "获取文件下载的路径"),
    MULTI_RETURN_TEST(TestController.class, "/test/multiParams", "返回多个参数的测试"),
    MONGODB_TEST(TestController.class, "/test/mongodb", "mongodb数据库功能测试")
    ;

    BizTypeEnum(Class<?> controller, String httpPath, String description) {
        this.controller = controller;
        this.description = description;
        this.httpPath = httpPath;
    }

    /** 执行业务具体的类 */
    Class<?> controller;
    /** 业务对应的http请求路径 */
    String httpPath;
    /** 业务描述 */
    String description;

    public Class<?> getControllerClass() {
        return controller;
    }

    public String getDescription() {
        return description;
    }

    public String getHttpPath() {
        return httpPath;
    }
}
