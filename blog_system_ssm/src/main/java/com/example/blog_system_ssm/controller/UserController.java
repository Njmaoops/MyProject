package com.example.blog_system_ssm.controller;

import com.example.blog_system_ssm.common.*;
import com.example.blog_system_ssm.entity.UserInfo;
import com.example.blog_system_ssm.entity.vo.UserInfoVO;
import com.example.blog_system_ssm.service.ArticleService;
import com.example.blog_system_ssm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @Date 2023/5/9 12:32
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    // 图片验证码值
    String captchaCode = null;

    //实现注册功能
    @RequestMapping("/reg")
    public AjaxResult reg(UserInfo userInfo) {
        // 还是需要对数据进行非空校验  因为发送请求并不是只有通过前端可以发送
        if (userInfo == null || !StringUtils.hasLength(userInfo.getUsername())
                || !StringUtils.hasLength(userInfo.getPassword())) {
            return AjaxResult.fail(-1, "非法参数!");
        }
        // 这个位置注册需要进行加盐
        userInfo.setPassword(PassWordUtils.encrypt(userInfo.getPassword()));
        return AjaxResult.success(userService.reg(userInfo));
    }

    // 实现登录功能
    @RequestMapping("/login")
    public AjaxResult login(HttpServletRequest request, String username, String password, String captchaCode) {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)
                // 这里判断的是前端是否传入了一个验证码
                || !StringUtils.hasLength(captchaCode)) {
            return AjaxResult.fail(-1, "钓鱼执法");
        }
        // 这里直接判断验证码是否相同
        if (!captchaCode.equals(this.captchaCode)) {
            return AjaxResult.fail(-5, "验证码不正确,请重新输入!");
        }
        // 通过UserName 获取到了一个对象, 在进行判断 userinfo 的 password 是否相同
        UserInfo userInfo = userService.getUserByName(username);
        // 校验:
        if (userInfo != null && userInfo.getId() > 0 &&
                //对用户的状态进行校验
                //以后可能还要对用户做其他处理, 因此这里写的是状态而不是密码错误次数
                userInfo.getState() == 1) {
            // .. 有效的用户
            // 对输入进来的密码, 和数据库内的最终密码进行校验
            // 小于5次能够进行校验密码操作
            if (PassWordUtils.check(password, userInfo.getPassword())) {
                // 说明密码相同 登陆成功
                // 当登录成功之后重置错误次数
                userService.clearError(userInfo.getId());
                // 隐藏密码给前端 -->
                userInfo.setPassword("");
                // 将用户存储到session 中
                HttpSession session = request.getSession(true);
                session.setAttribute(AppVariable.USER_SESSION_KEY, userInfo);
                return AjaxResult.success(userInfo);
            } else {
                userService.upError(userInfo.getId());
                // 这里获取的userinfo 应该是新获取到的user对象, 也就是errorcount + 1了的
                int errorCount = userInfo.getErrorcount() + 1;
                // 说明这次错误之后,密码仍没有到达5次
                if (errorCount > 0 && errorCount < 5) {
                    String msg =
                            "这是第" + errorCount + "次输入错误,再错误" + (5 - errorCount) + "次将冻结账户!";
                    return AjaxResult.success(0, msg, null);
                } else if (errorCount >= 5) {
                    // 冻结用户
                    userService.freezeUserInfo(userInfo.getId());
                    return AjaxResult.success(0, "用户已被冻结,请往安全中心解冻!", null);
                }
            }
        }
        return AjaxResult.success(-1, null);

    }

    // 实现获取用户的文章数量
    @RequestMapping("/showinfo")
    public AjaxResult showInfo(HttpServletRequest request) {
        UserInfoVO userInfoVO = new UserInfoVO();
        // 1.得到当前用户 ( session 中获取 )
        UserInfo userInfo = UserSessionUtils.getSessionUser(request);
        if (userInfo == null) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // Spring提供了一个深拷贝的赋值 将 userinfo 的属性值 赋值给 userinfoVO
        BeanUtils.copyProperties(userInfo, userInfoVO);
        // 2.得到用户发表文章的总数
        userInfoVO.setArticleCount(articleService.getArtCountByUid(userInfoVO.getId()));
        return AjaxResult.success(userInfoVO);
    }

    // 注销
    @RequestMapping("/logout")
    public AjaxResult logout(HttpSession session) {
        // 将用户的信息删除掉
        session.removeAttribute(AppVariable.USER_SESSION_KEY);
        return AjaxResult.success(1, "success");
    }

    // 获取userId
    @RequestMapping("/getuserbyid")
    public AjaxResult getUserById(Integer id) {
        UserInfoVO userInfoVO = new UserInfoVO();
        if (id == null || id <= 0) {
            return AjaxResult.fail(-1, "参数错误");
        }
        UserInfo userById = userService.getUserById(id);
        if (userById == null) {
            return AjaxResult.fail(-1, "没有此用户");
        }
        // 设置敏感数据
        userById.setPassword("");

        BeanUtils.copyProperties(userById, userInfoVO);
        userInfoVO.setArticleCount(articleService.getArtCountByUid(id));
        System.out.println(userInfoVO);
        return AjaxResult.success(userInfoVO);
    }

    // 获取图形验证码
    @RequestMapping("/getcaptcha")
    public AjaxResult getCaptcha() {
        HashMap<String, String> captchaOfBase64 = CommonUtils.getCaptcha();
        //   只返回64的编码, 值交给login进行判断
        captchaCode = captchaOfBase64.get("captchaCode");
        return AjaxResult.success(captchaOfBase64.get("base64ImageCode"));
    }

    // 获取session中的全部信息
    @RequestMapping("/information")
    public AjaxResult getUserInformation(HttpServletRequest request) {
        // 从session中获取
        UserInfo userInfo = UserSessionUtils.getSessionUser(request);
        if (userInfo == null) {
            return AjaxResult.fail(0, "无法获取到user信息");
        }
        // 取消user密码的盐值?
        // 因为session会话中的密码设置为 "" 了, 因此需要从数据库内获取password信息

        return AjaxResult.success(userInfo);
    }

    // 修改密码
    @RequestMapping("/chpassword")
    public AjaxResult changePassword(Integer id, String password, String newPassword,
                                     HttpSession session) {
        if (!StringUtils.hasLength(password) || !StringUtils.hasLength(newPassword) || id == null) {
            AjaxResult.fail(-1, "非法参数");
        }
        // 获取密码,判断是否能够进行修改密码
        UserInfo userinfo = userService.getUserById(id);
        if (!PassWordUtils.check(password, userinfo.getPassword())) {
            return AjaxResult.fail(-2, "密码不一致,无法修改");
        }
        //对密码进行加盐操作
        String newPasswordSalt = PassWordUtils.encrypt(newPassword);
        // 进行修改操作
        int row = userService.changePassword(id, newPasswordSalt);
        // 将用户的信息进行注销
        logout(session);

        return AjaxResult.success(row);
    }

    //修改昵称
    @RequestMapping("/chnickname")
    public AjaxResult changeNickName(String nickname, HttpServletRequest request, HttpSession session) {
        if (!StringUtils.hasLength(nickname)) {
            return AjaxResult.fail(-1, "非法参数");
        }
        UserInfo userInfo = UserSessionUtils.getSessionUser(request);
        if (userInfo == null) {
            return AjaxResult.fail(-2, "获取不到用户的信息");
        }
        int rows = userService.changeNickName(userInfo.getId(), nickname);
        // 说明修改成功, 然后需要将 redis中session中的userinfo对象的缓存值修改一下
        if (rows == 1) {
            // 修改对象的值
            userInfo.setNickname(nickname);
            // 再将对象重新设置进入 session 中
            session.setAttribute(AppVariable.USER_SESSION_KEY, userInfo);
        }
        return AjaxResult.success(rows);
    }

    //修改安全问题以及答案
    @RequestMapping("/setsecurity")
    public AjaxResult setSecurityInfo(@RequestParam("securityquestion") String securityQuestion,
                                      @RequestParam("securityanswer") String securityAnswer,
                                      HttpServletRequest request,
                                      HttpSession session) {
        if (!StringUtils.hasLength(securityAnswer) || !StringUtils.hasLength(securityQuestion)) {
            return AjaxResult.fail(-1, "非法参数");
        }
        UserInfo userInfo = UserSessionUtils.getSessionUser(request);
        if (userInfo == null) {
            return AjaxResult.fail(-2, "获取不到用户的信息");
        }
        int row = userService.setSecurityInfo(securityQuestion, securityAnswer, userInfo.getId());
        // 缓存层更新session信息
        if (row == 1) {
            // 修改对象的值
            userInfo.setSecurityquestion(securityQuestion);
            userInfo.setSecurityanswer(securityAnswer);
            // 再将对象重新设置进入 session 中
            session.setAttribute(AppVariable.USER_SESSION_KEY, userInfo);
        }
        return AjaxResult.success(row);
    }

    @RequestMapping("/setall")
    public AjaxResult setAllInfo(UserInfo userInfo, String newPassword, HttpSession session) {
        if (userInfo == null) {
            return AjaxResult.fail(-1, "非法参数");
        }
        UserInfo userinfoDb = userService.getUserById(userInfo.getId());
        // 传入的对象和数据库内的对象进行比较
        if (!PassWordUtils.check(userInfo.getPassword(), userinfoDb.getPassword())) {
            return AjaxResult.fail(-2, "密码不一致,无法修改");
        }
        // 对newPassword进行加盐
        String encrypt = PassWordUtils.encrypt(newPassword);
        userInfo.setPassword(encrypt);
        int row = userService.setAllInfo(userInfo);

        // 更新缓存对象 ->
        if (row == 1) {
            UserInfo newUserInfo = userService.getUserById(userInfo.getId());
            session.setAttribute(AppVariable.USER_SESSION_KEY, newUserInfo);
        }
        return AjaxResult.success(row);
    }

    @RequestMapping("/findques")
    public AjaxResult findPassword(@RequestParam("securityquestion") String userInfo) {
        //1. 需要判断这个参数是不是用户名,如果是用户名,需要通过用户名获取到安全问题
        if (userInfo == null) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // 后续设置的user值可以指来使用
        UserInfo user = new UserInfo();
        // 这里不确定是username或者 sectyQ
        user.setSecurityquestion(userInfo);
        user.setUsername(userInfo);
        UserInfo userByName =
                userService.getUserByName(user.getUsername());
        UserInfo userBySecurityQuestion =
                userService.getUserBySecurityQuestion(user.getSecurityquestion());
        // 判断如果存在不存在密码的话, 就说明只是进行查找是否存在该用户 , 如果存在该密码, 那么就进行判断answer是否与刚查找出来的信息相同

        if (userByName == null && userBySecurityQuestion == null) {
            // 两者都为空就说明 既不是没有改安全问题,或者该用户
            return AjaxResult.fail(-1, "用户不存在");
        }
        //2. 通过数据库内的安全问题
        if (userByName != null) {
            // 说明是通过用户名查找到的用户
            // 敏感信息
            userByName.setPassword("");
            userByName.setSecurityanswer("");
            return AjaxResult.success(200, userByName);
        }
        if (userBySecurityQuestion != null) {
            userBySecurityQuestion.setPassword("");
            // 安全信息的答案也需要隐藏
            userBySecurityQuestion.setSecurityanswer("");
            return AjaxResult.success(200, userBySecurityQuestion);
        }

        //3. 将该安全问题返回给前端
        return AjaxResult.success(0);
    }

    @RequestMapping("/confirmans")
    public AjaxResult confirmAnswer(@RequestParam("securityquestion") String question,
                                    @RequestParam(value = "securityanswer", required = false) String answer) {
        // 合法性判断
        if (question == null || answer == null) {
            return AjaxResult.fail(-1, "非法参数");
        }
        UserInfo userInfo = userService.getUserBySecurityQuestion(question);
        if (userInfo == null) {
            // 说明不存在该用户
            return AjaxResult.fail(-2, "用户不存在");
        }
        // 存在,就需要进行判断 传入的answer和数据库内的ans是否相同
        if (!answer.equals(userInfo.getSecurityanswer())) {
            return AjaxResult.fail(-3, "安全问题不对!");
        }
        return AjaxResult.success(200, "进入修改密码页面");
    }

    @RequestMapping("/resetpwd")
    public AjaxResult resetPassword(UserInfo userInfo) {
        // 前端接收的user存在id 和 password 两个信息
        if (userInfo == null) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // 对密码进行加密
        String password = userInfo.getPassword();
        String encryptPassword = PassWordUtils.encrypt(password);
        userInfo.setPassword(encryptPassword);
        int rows = userService.setPasswordById(userInfo);
        if (rows == 1) {
            return AjaxResult.success(200, rows);
        }
        return AjaxResult.success(0);
    }
}
