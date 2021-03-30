package top.codecrab.chat.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WebsocketInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // ------------------
        // 用于支持Http协议
        // ------------------

        // websocket基于http协议，需要有http的编解码器
        pipeline.addLast(new HttpServerCodec());
        // 对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 添加对HTTP请求和响应的聚合器:只要使用Netty进行Http编程都需要使用
        // 对HttpMessage进行聚合，聚合成FullHttpRequest或者FullHttpResponse
        // 在netty编程中都会使用到Handler
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        // ---------支持Web Socket -----------------

        // websocket服务器处理的协议，用于指定给客户端连接访问的路由: /ws
        // 本handler会帮你处理一些握手动作: handshaking(close, ping, pong) ping + pong = 心跳
        // 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //添加Netty空闲机制检查
        //第1个参数：读空闲超时时间 2：写空闲 3：读写空闲
        pipeline.addLast(new IdleStateHandler(4, 8, 12));

        //添加空闲后的操作
        pipeline.addLast(new HeartBeatHandler());

        // 添加自定义的handler
        pipeline.addLast(new ChatHandler());

    }
}
