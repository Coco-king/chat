package top.codecrab.chat.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("读空闲触发");
            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("写空闲触发");
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                log.info("读写空闲触发，关闭用户与通道的连接");
                ctx.channel().close();
            }
        }
    }
}
