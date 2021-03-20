package top.codecrab.chat.netty;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当通道中有新事件会自动调用
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //获取消息
        String text = textWebSocketFrame.text();
        System.out.println("接受到的消息为：" + text);

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
}
