package com.garcheng.rpc.example.handle;

import com.garcheng.rpc.example.codec.RpcDecoder;
import com.garcheng.rpc.example.codec.RpcEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RpcServerInitalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(
                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                12,
                                4,
                                0,
                                0))
                .addLast(new RpcDecoder())
                .addLast(new RpcEncoder())
                .addLast(new RpcServerHandler());
    }
}
