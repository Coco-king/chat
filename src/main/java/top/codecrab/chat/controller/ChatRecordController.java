package top.codecrab.chat.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codecrab.chat.entity.ChatRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController extends BaseController {

    @PostMapping("/findByUserIdAndFriendId")
    public List<ChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        List<ChatRecord> records = chatRecordService.list(new QueryWrapper<ChatRecord>()
                .eq("userid", userid).eq("friendid", friendid)
                .eq("has_delete", 0));

        List<ChatRecord> toUser = chatRecordService.list(new QueryWrapper<ChatRecord>()
                .eq("userid", friendid).eq("friendid", userid)
                .eq("has_delete", 0));

        records.addAll(toUser);
        //把发送给自己的消息设置为已读
        toUser.forEach((record) -> record.setHasRead(1));
        chatRecordService.updateBatchById(toUser);
        return records;
    }

    @PostMapping("/findUnreadByUserid")
    public List<ChatRecord> findUnreadByUserid(String userid) {
        List<ChatRecord> records = chatRecordService.list(new QueryWrapper<ChatRecord>()
                .eq("friendid", userid).eq("has_read", 0));

        Map<String, ChatRecord> recordMap = records.stream()
                .collect(Collectors.toMap(ChatRecord::getUserid, (record) -> record,
                        (a, b) -> b.setCount(a.getCount() == null ? 2 : a.getCount() + 1)));
        return new ArrayList<>(recordMap.values());
    }

}
