package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UserWarnEnum;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Controller.Params.UserParams.In.*;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.*;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        if (user == null || !user.getPassword().equals(inParams.getPassword()))
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
}
