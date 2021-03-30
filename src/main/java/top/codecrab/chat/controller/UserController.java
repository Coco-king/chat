package top.codecrab.chat.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.codecrab.chat.bo.LoginUser;
import top.codecrab.chat.common.Result;
import top.codecrab.chat.entity.TestClass;
import top.codecrab.chat.entity.User;
import top.codecrab.chat.utils.ValidationUtil;
import top.codecrab.chat.vo.AccountProfile;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @RequestMapping("/test")
    public TestClass test(@RequestBody TestClass testClass) {
        return testClass;
    }

    @PostMapping("/register")
    public Result register(@RequestBody LoginUser loginUser) {
        //对前台传入的数据进行校检
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(loginUser);
        if (validResult.hasErrors()) {
            return Result.fail(validResult.getErrors());
        }
        return userService.register(loginUser);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginUser loginUser) {
        return userService.login(loginUser);
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file, String userid) {
        return userService.upload(file, userid);
    }

    @PostMapping("/updateNickname")
    public Result updateNickname(@RequestBody AccountProfile profile) {
        return userService.updateNickname(profile);
    }

    @PostMapping("/findById")
    public User findById(@RequestParam("userid") String userid) {
        return userService.getById(userid);
    }

    @PostMapping("/findByUsername")
    public Result findByUsername(
            @RequestParam("userid") String userid,
            @RequestParam("friendUsername") String friendUsername
    ) {
        User user = userService.getById(userid);
        if (user == null) return Result.fail("登录状态失效");

        User friend = userService.getOne(new QueryWrapper<User>()
                .eq("username", friendUsername).or()
                .eq("nickname", friendUsername)
        );
        if (friend == null) return Result.fail("找不到该用户");

        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(friend, profile);
        return Result.success(profile);
    }

}
