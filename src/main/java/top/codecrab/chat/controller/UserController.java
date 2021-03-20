package top.codecrab.chat.controller;


import org.springframework.web.bind.annotation.*;
import top.codecrab.chat.bo.LoginUser;
import top.codecrab.chat.common.Result;
import top.codecrab.chat.utils.ValidationUtil;
import top.codecrab.chat.vo.AccountProfile;

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

    @PostMapping("/register")
    public Result register(@RequestBody AccountProfile profile) {
        //对前台传入的数据进行校检
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(profile);
        if (validResult.hasErrors()) {
            return Result.fail(validResult.getErrors());
        }
        return userService.register(profile);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginUser loginUser) {
        return userService.login(loginUser);
    }

}
