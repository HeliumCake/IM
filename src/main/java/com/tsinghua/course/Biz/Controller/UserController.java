package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UserWarnEnum;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Controller.Params.UserParams.In.*;
import com.tsinghua.course.Biz.Controller.Params.UserParams.Out.ContactsOutParams;
import com.tsinghua.course.Biz.Controller.Params.UserParams.Out.SearchOutParams;
import com.tsinghua.course.Biz.Controller.Params.UserParams.Out.SearchUsersOutParams;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.HttpSession;
import com.tsinghua.course.Frame.Util.SecureUtil;
import com.tsinghua.course.Frame.Util.SocketUtil;
import com.tsinghua.course.Frame.Util.ThreadUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @描述 用户控制器，用于执行用户相关的业务
 **/
@Component
public class UserController {

    @Autowired
    UserProcessor userProcessor;

    /** 用户登录业务 */
    @BizType(BizTypeEnum.USER_LOGIN)
    public CommonOutParams userLogin(LoginInParams inParams) throws Exception {
        String username = inParams.getUsername();
        if (username == null)
            throw new CourseWarn(UserWarnEnum.LOGIN_FAILED);
        User user = userProcessor.getUserByUsername(username);
        String hashedInputPassword = SecureUtil.getHashedPassword(user.getId(), inParams.getPassword());
        if (user == null || !user.getPassword().equals(hashedInputPassword))
            throw new CourseWarn(UserWarnEnum.LOGIN_FAILED);

        /** 登录成功，记录登录状态 */
        ChannelHandlerContext ctx =  ThreadUtil.getCtx();
        /** ctx不为空记录WebSocket状态，否则记录http状态 */
        if (ctx != null)
            SocketUtil.setUserSocket(username, ctx);
        else {
            HttpSession httpSession = ThreadUtil.getHttpSession();
            if (httpSession != null) {
                httpSession.setUsername(username);
            }
        }
        return new CommonOutParams(true);
    }

    /** 用户注册业务 */
    @BizType(BizTypeEnum.USER_REGISTER)
    public CommonOutParams userRegister(RegisterInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String password = inParams.getPassword();
        if (username == null || password == null)
            throw new CourseWarn(UserWarnEnum.REGISTER_LACK);
        User user = userProcessor.getUserByUsername(username);
        if (user != null)
            throw new CourseWarn(UserWarnEnum.REGISTER_DUPLICATION);

        /** 记录注册信息 */
        if (userProcessor.addUser(username, password) == null)
            /** 写入数据库失败 */
            throw new CourseWarn(UserWarnEnum.REGISTER_FAILED);

        return new CommonOutParams(true);
    }

    /** 修改密码业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_PASSWORD)
    public CommonOutParams userPassword(PasswordInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String password = inParams.getPassword();
        if (password == null)
            throw new CourseWarn(UserWarnEnum.PASSWORD_LACK);
        User user = userProcessor.getUserByUsername(username);

        /** 修改密码 */
        if (userProcessor.updatePassword(username, password) == 0)
        /** 写入数据库失败 */
            throw new CourseWarn(UserWarnEnum.PASSWORD_FAILED);

        return new CommonOutParams(true);
    }

    /** 修改用户名业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_USERNAME)
    public CommonOutParams userUsername(UsernameInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String name = inParams.getName();
        if (userProcessor.getUserByUsername(name) != null)
            throw new CourseWarn(UserWarnEnum.REGISTER_DUPLICATION);

        /** 修改用户名 */
        if (userProcessor.updateUsername(username, name) == 0)
        /** 写入数据库失败 */
            throw new CourseWarn(UserWarnEnum.USERNAME_FAILED);

        return new CommonOutParams(true);
    }

    /** 修改昵称业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_NICKNAME)
    public CommonOutParams userNickname(NicknameInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String nickname = inParams.getNickname();

        /** 修改昵称 */
        if (userProcessor.updateNickname(username, nickname) == 0)
        /** 写入数据库失败 */
            throw new CourseWarn(UserWarnEnum.NICKNAME_FAILED);

        return new CommonOutParams(true);
    }

    /** 修改头像业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_AVATAR)
    public CommonOutParams userAvatar(AvatarInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String avatar = inParams.getAvatar();

        /** 修改头像 */
        if (userProcessor.updateAvatar(username, avatar) == 0)
        /** 写入数据库失败 */
            throw new CourseWarn(UserWarnEnum.AVATAR_FAILED);

        return new CommonOutParams(true);
    }

    /** 查找用户业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_SEARCH)
    public SearchOutParams userSearch(SearchInParams inParams) throws Exception {
        String search = inParams.getSearch();
        String username = inParams.getUsername();
        User userSearch = userProcessor.getUserByUsername(search);
        User user = userProcessor.getUserByUsername(username);

        if (userSearch == null)
        /** 用户不存在 */
            throw new CourseWarn(UserWarnEnum.USER_NOT_EXIST);

        /** 删除部分信息 */
        userSearch.setPassword(null);
        if (!username.equals(search)){
            userSearch.setContacts(null);
        }

        boolean isContact = user.getContacts().contains(userSearch.getId());

        return new SearchOutParams(true,userSearch,isContact);
    }

    /** 查找多个用户业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_SEARCH_USERS)
    public SearchUsersOutParams usersSearch(SearchUsersInParams inParams) throws Exception {
        List<String> users = inParams.getUsers();
        String username = inParams.getUsername();
        List<User> usersSearch = userProcessor.getUsersById(users);
        User user = userProcessor.getUserByUsername(username);

        /** 删除部分信息 */
        for (User userSearch :usersSearch){
            userSearch.setPassword(null);
            userSearch.setContacts(null);
        }

        return new SearchUsersOutParams(true,usersSearch);
    }

    /** 添加联系人业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_ADD)
    public CommonOutParams userAdd(AddInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String add = inParams.getAdd();

        if (userProcessor.addContact(username, add) == 0)
        /** 用户不存在 */
            throw new CourseWarn(UserWarnEnum.USER_NOT_EXIST);

        return new CommonOutParams(true);
    }

    /** 删除联系人业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_DELETE)
    public CommonOutParams userDelete(DeleteInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String delete = inParams.getDelete();

        if (userProcessor.deleteContact(username, delete) == 0)
        /** 用户不存在 */
            throw new CourseWarn(UserWarnEnum.USER_NOT_EXIST);

        return new CommonOutParams(true);
    }

    /** 浏览联系人业务 */
    @NeedLogin
    @BizType(BizTypeEnum.USER_VIEW)
    public ContactsOutParams userView(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();

        List<User> contacts = userProcessor.viewContacts(username);

        return new ContactsOutParams(true,contacts);
    }
}
