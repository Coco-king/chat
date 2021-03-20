package top.codecrab.chat.netty;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserChannelMap {

    private static final Map<String, Channel> USER_CHANNEL;

    static {
        USER_CHANNEL = new HashMap<>();
    }

    public static void put(String userId, Channel channel) {
        USER_CHANNEL.put(userId, channel);
    }

    public static Channel get(String userId) {
        return USER_CHANNEL.get(userId);
    }

    public static void remove(String userId) {
        USER_CHANNEL.remove(userId);
    }

    public static void remove(Channel channel) {
        if (channel == null || StrUtil.isEmpty(channel.id().asLongText())) {
            return;
        }

        for (String k : USER_CHANNEL.keySet()) {
            Channel v = USER_CHANNEL.get(k);
            if (channel.id().asLongText().equals(v.id().asLongText())) {
                USER_CHANNEL.remove(k);
                log.info("通道 {} 与 {} 断开连接", v.id().asLongText(), k);
                break;
            }
        }
    }

    public static void print() {
        USER_CHANNEL.forEach((k, v) -> {
            System.out.println("用户ID：" + k + " 通道ID：" + v.id().asLongText());
        });
    }

}
