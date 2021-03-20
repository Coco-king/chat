package top.codecrab.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.codecrab.chat.common.Result;
import top.codecrab.chat.entity.Friend;
import top.codecrab.chat.entity.FriendReq;
import top.codecrab.chat.entity.User;
import top.codecrab.chat.mapper.FriendReqMapper;
import top.codecrab.chat.service.FriendReqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.chat.service.FriendService;
import top.codecrab.chat.service.UserService;
import top.codecrab.chat.vo.AccountProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
@Service
public class FriendReqServiceImpl extends ServiceImpl<FriendReqMapper, FriendReq> implements FriendReqService {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Override
    @Transactional
    public Result sendRequest(FriendReq friendReq) {
        if (StrUtil.equals(friendReq.getFromUserid(), friendReq.getToUserid())) {
            return Result.fail("不能添加自己为好友");
        }

        User fromUser = userService.getById(friendReq.getFromUserid());
        if (fromUser == null) return Result.fail("登录身份已过期");
        User toUser = userService.getById(friendReq.getToUserid());
        if (toUser == null) return Result.fail("找不到该用户");

        Friend friend = friendService.getOne(new QueryWrapper<Friend>()
                .eq("userid", fromUser.getId())
                .eq("friends_id", toUser.getId())
        );
        if (friend != null) return Result.fail("你们已经是好友啦！");

        FriendReq one = this.getOne(new QueryWrapper<FriendReq>()
                .eq("from_userid", friendReq.getFromUserid())
                .eq("to_userid", friendReq.getToUserid())
        );
        if (one != null) {
            friendReq.setId(one.getId());
        } else {
            friendReq.setId(null);
        }

        friendReq.setMessage(friendReq.getMessage());
        friendReq.setCreatetime(LocalDateTime.now());
        friendReq.setStatus(0);
        this.saveOrUpdate(friendReq);
        return Result.success(friendReq, "添加好友消息已发送");
    }

    @Override
    public List<AccountProfile> findFriendReqByUserid(String userid) {
        User user = userService.getById(userid);
        if (user == null) return null;

        List<FriendReq> reqList = this.list(new QueryWrapper<FriendReq>()
                .eq("to_userid", userid).eq("status", 0));

        //转为map，v为请求的id，key为来源id,第三个参数为当K重复时，执行什么操作，这里是覆盖
        Map<String, Long> collect = reqList.stream()
                .collect(Collectors.toMap(FriendReq::getFromUserid, FriendReq::getId, (a, b) -> a = b));

        Set<String> idList = collect.keySet();
        if (CollectionUtil.isEmpty(idList)) return null;
        Collection<User> users = userService.listByIds(idList);

        return users.stream().map(u -> {
            AccountProfile profile = new AccountProfile();
            BeanUtil.copyProperties(u, profile, "id");
            //此处使用好友请求的id，方便后面添加好友操作
            profile.setId(collect.get(u.getId().toString()));
            return profile;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Result ignoreFriendReq(String reqid) {
        FriendReq req = this.getOne(new QueryWrapper<FriendReq>()
                .eq("id", reqid).eq("status", 0));
        if (req == null) return Result.fail("该请求已拒绝或不存在");
        req.setStatus(-1);
        this.updateById(req);
        return Result.success();
    }

    @Override
    @Transactional
    public Result acceptFriendReq(String reqid) {
        FriendReq req = this.getOne(new QueryWrapper<FriendReq>()
                .eq("id", reqid).eq("status", 0));
        if (req == null) return Result.fail("该请求已拒绝或不存在");
        req.setStatus(1);
        this.updateById(req);
        System.out.println(LocalDateTime.now());

        //添加Friend关系，双方都需要添加
        List<Friend> friends = Arrays.asList(
                new Friend(null, req.getToUserid(), req.getFromUserid(), "", LocalDateTime.now()),
                new Friend(null, req.getFromUserid(), req.getToUserid(), "", LocalDateTime.now())
        );
        try {
            friendService.saveBatch(friends);
        } catch (Exception exception) {
            return Result.fail("你们已经是好友啦");
        }
        return Result.success("添加成功");
    }

    @Override
    public List<User> findFriendByUserid(String userid) {
        List<Friend> friends = friendService.list(new QueryWrapper<Friend>()
                .eq("userid", userid));
        if (CollectionUtil.isEmpty(friends)) return new ArrayList<>();

        List<String> userIds = friends.stream().map(Friend::getFriendsId).collect(Collectors.toList());
        return (List<User>) userService.listByIds(userIds);
    }
}
