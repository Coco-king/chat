package top.codecrab.chat.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codecrab.chat.entity.ChatRecord;

import java.util.List;
import java.util.Map;

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
        return chatRecordService.list(new QueryWrapper<ChatRecord>()
                .eq("userid", userid).eq("friendid", friendid)
                .eq("has_delete", 0)
                .or()
                .eq("userid", friendid).eq("friendid", userid)
                .eq("has_delete", 0));
    }

    @PostMapping("/findUnreadByUserid")
    public Map<String, Object> findUnreadByUserid(String userid) {
        List<ChatRecord> records = chatRecordService.list(new QueryWrapper<ChatRecord>()
                .eq("friendid", userid).eq("has_read", 0));
        return MapUtil.builder("count", (Object) records.size()).put("data", records).build();
    }

}
