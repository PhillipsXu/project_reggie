package com.pf.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pf.enums.Status;
import com.pf.pojo.User;
import com.pf.service.IUserService;
import com.pf.utils.GenerateUtil;
import com.pf.utils.R;
import com.pf.utils.SMSUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private HttpSession session;

    @ApiOperation("发送验证码短信")
    @GetMapping("/sendMsg")
    public R sendMsg(@RequestParam("phone") String phone) {
        String reg = "/1((34[0-8])|(8\\d{2})|(([35][0-35-9]|4[579]|66|7[35678]|9[1389])\\d{1}))\\d{7}/\n";
        if (!phone.matches(reg)) return R.fail("手机号错误！");

        try {
            SMSUtil.send(phone);
            return R.success("短信发送成功！", null);
        } catch (Exception e) {
            log.error("Exception      : {}", e.getLocalizedMessage());
            return R.fail("短信发送失败！");
        }
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R register(@RequestBody User user) {
        if (userService.save(user)) {
            return R.success("注册成功！", null);
        }
        return R.fail("注册失败！");
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> map) {
        String phoneNumber = null;
        String code = null;
        if (map.get("phoneNumber") != null) phoneNumber = map.get("phoneNumber");
        if (map.get("code") != null) code = map.get("code");
        if (SMSUtil.verifyCode(phoneNumber, code)) {
            /* 查询用户是否存在 */
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phoneNumber);
            User user = userService.getOne(wrapper);

            /* 登录转注册 */
            if (user == null) {
                User newUser = new User();
                newUser.setPhone(phoneNumber);
                newUser.setStatus(Status.Enable.getValue());
                newUser.setName(GenerateUtil.generateUsername());
                userService.save(newUser);
                session.setAttribute("user", phoneNumber);
                session.setAttribute("userId", newUser.getId());
                return R.success("登录成功！",newUser);
            }

            session.setAttribute("user", phoneNumber);
            session.setAttribute("userId", user.getId());
            return R.success("登录成功！",user);
        } else {
            return R.fail("验证码不正确！");
        }
    }
}
