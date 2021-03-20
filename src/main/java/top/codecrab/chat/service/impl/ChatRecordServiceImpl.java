package top.codecrab.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.chat.entity.ChatRecord;
import top.codecrab.chat.mapper.ChatRecordMapper;
import top.codecrab.chat.service.ChatRecordService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author codecrab
 * @since 2021-03-19
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements ChatRecordService {

}
