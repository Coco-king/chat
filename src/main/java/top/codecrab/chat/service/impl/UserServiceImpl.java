package top.codecrab.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.chat.bo.LoginUser;
import top.codecrab.chat.common.Result;
import top.codecrab.chat.entity.User;
import top.codecrab.chat.mapper.UserMapper;
import top.codecrab.chat.service.UserService;
import top.codecrab.chat.utils.CommonUtils;
import top.codecrab.chat.vo.AccountProfile;

import java.time.LocalDate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Transactional
    public Result register(AccountProfile profile) {
        User one = this.getOne(new QueryWrapper<User>()
                .eq("username", profile.getUsername()));
        if (one != null) return Result.fail("用户名已存在");
        one = this.getOne(new QueryWrapper<User>()
                .eq("nickname", profile.getNickname()));
        if (one != null) return Result.fail("昵称已存在");

        User user = new User();
        BeanUtil.copyProperties(profile, user);
        user.setPassword(SecureUtil.md5(profile.getPassword()));
        user.setPicSmall(null);
        user.setPicNormal(null);
        user.setQrcode("二维码");
        user.setSign(CommonUtils.getSign());
        user.setCreatetime(LocalDate.now());
        user.setId(null);
        user.setPhone(null);
        this.save(user);
        return Result.success(profile, "注册成功");
    }

    @Override
    public Result login(LoginUser loginUser) {
        User user = this.getOne(new QueryWrapper<User>()
                .eq("username", loginUser.getUsername()));
        if (user == null) return Result.fail("找不到用户");
        String Md5Pass = SecureUtil.md5(loginUser.getPassword());
        if (StrUtil.equals(Md5Pass, user.getPassword())) {
            return Result.success(user, "登陆成功");
        }
        return Result.fail("密码或用户名错误");
    }
}
