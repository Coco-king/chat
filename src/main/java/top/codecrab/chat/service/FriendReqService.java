package top.codecrab.chat.service;

import top.codecrab.chat.common.Result;
import top.codecrab.chat.entity.FriendReq;
import com.baomidou.mybatisplus.extension.service.IService;
import top.codecrab.chat.entity.User;
import top.codecrab.chat.vo.AccountProfile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
public interface FriendReqService extends IService<FriendReq> {

    Result sendRequest(FriendReq friendReq);

    List<AccountProfile> findFriendReqByUserid(String userid);

    Result ignoreFriendReq(String reqid);

    Result acceptFriendReq(String reqid);

    List<User> findFriendByUserid(String userid);
}
