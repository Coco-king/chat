package top.codecrab.chat.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.codecrab.chat.common.Result;
import top.codecrab.chat.controller.BaseController;
import top.codecrab.chat.entity.FriendReq;
import top.codecrab.chat.entity.User;
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
@RequestMapping("/friend")
public class FriendController extends BaseController {

    @PostMapping("/sendRequest")
    public Result sendRequest(@RequestBody FriendReq friendReq) {
        return friendReqService.sendRequest(friendReq);
    }

    @PostMapping("/findFriendReqByUserid")
    public List<AccountProfile> findFriendReqByUserid(String userid) {
        return friendReqService.findFriendReqByUserid(userid);
    }

    @PostMapping("/ignoreFriendReq")
    public Result ignoreFriendReq(String reqid) {
        return friendReqService.ignoreFriendReq(reqid);
    }

    @PostMapping("/acceptFriendReq")
    public Result acceptFriendReq(String reqid) {
        return friendReqService.acceptFriendReq(reqid);
    }

    @PostMapping("/findFriendByUserid")
    public List<User> findFriendByUserid(String userid) {
        return friendReqService.findFriendByUserid(userid);
    }

}
