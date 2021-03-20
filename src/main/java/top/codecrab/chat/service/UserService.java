package top.codecrab.chat.service;

import top.codecrab.chat.bo.LoginUser;
import top.codecrab.chat.common.Result;
import top.codecrab.chat.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.chat.vo.AccountProfile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
public interface UserService extends IService<User> {

    Result register(AccountProfile profile);

    Result login(LoginUser loginUser);
}
