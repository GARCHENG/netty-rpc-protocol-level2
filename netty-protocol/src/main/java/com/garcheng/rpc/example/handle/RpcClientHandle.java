package com.garcheng.rpc.example.handle;

import com.garcheng.rpc.example.core.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcClientHandle extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) throws Exception {
        log.info("receive Rpc Server Result");
        Header header = msg.getHeader();
        long requestId = header.getRequestId();
        RpcFuture rpcFuture = RequestHolder.REQUEST_MAP.remove(requestId);
        rpcFuture.getPromise().setSuccess(msg.getContent());
    }
}
