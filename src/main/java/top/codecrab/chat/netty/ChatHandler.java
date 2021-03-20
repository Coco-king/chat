package top.codecrab.chat.netty;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import top.codecrab.chat.entity.ChatRecord;
import top.codecrab.chat.service.ChatRecordService;
import top.codecrab.chat.utils.SpringUtil;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当通道中有新事件会自动调用
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //获取消息
        String text = textWebSocketFrame.text();
        log.info("接受到的消息为：{}", text);

        Message message = JSONUtil.toBean(text, Message.class);
        switch (message.getType()) {
            case 0:
                UserChannelMap.put(message.getChatRecord().getUserid(), context.channel());
                UserChannelMap.print();
                break;
            case 1:
                ChatRecordService recordService = SpringUtil.getBean(ChatRecordService.class);
                ChatRecord chatRecord = message.getChatRecord();
                chatRecord.setHasRead(0);
                chatRecord.setHasDelete(0);
                chatRecord.setCreatetime(LocalDateTime.now());
                recordService.save(chatRecord);

                //获取聊天对象的通道
                Channel channel = UserChannelMap.get(chatRecord.getFriendid());
                if (channel != null) {
                    //朋友在线
                    channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(message)));
                } else {
                    //朋友不在线
                    log.info("朋友不在线 ID：{}", chatRecord.getFriendid());
                }
                break;
        }

        //遍历客户端连接，把消息写出
        for (Channel client : clients) {
            client.writeAndFlush(new TextWebSocketFrame(
                    DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN) + "：" + text
            ));
        }
    }

    /**
     * 当有一个新的客户端连接进来后，会调用这个方法
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //把新加入的连接加入到连接中
        clients.add(ctx.channel());
    }

    /**
     * 出现异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //删除通道
        log.info("通道异常");
        Channel channel = ctx.channel();
        UserChannelMap.remove(channel);
        channel.close();
    }

    /**
     * 用户断开连接时
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //通道关闭
        log.info("通道关闭");
        UserChannelMap.remove(ctx.channel());
    }
}
