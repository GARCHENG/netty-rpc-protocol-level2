package com.garcheng.rpc.example.protocol;

import com.garcheng.rpc.example.handle.RpcServerInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    private String address;
    private int port;

    public NettyServer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void startNettyServer() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RpcServerInitalizer());
        try {
            ChannelFuture future = bootstrap.bind(this.address, this.port).sync();
            log.info("Server started Success on Port,{}", this.port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


}
