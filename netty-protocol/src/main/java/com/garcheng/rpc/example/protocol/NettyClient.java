package com.garcheng.rpc.example.protocol;

import com.garcheng.rpc.example.core.RpcProtocol;
import com.garcheng.rpc.example.core.RpcRequest;
import com.garcheng.rpc.example.handle.RpcClientInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    private String address;
    private int port;

    private final Bootstrap bootstrap;
    private final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    public NettyClient(String address,int port){
        log.info("begin init Netty Client,{},{}",address,port);
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitalizer());

        this.address = address;
        this.port = port;
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(this.address, this.port).sync();
        future.addListener(listen -> {
            if(future.isSuccess()){
                log.info("connect rpc server {} success.",this.address);
            }else{
                log.error("connect rpc server {} failed. ",this.address);
                future.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        log.info("begin transfer data");
        future.channel().writeAndFlush(protocol);
    }


}
