package com.garcheng.rpc.example.handle;

import com.garcheng.rpc.example.constant.ReqType;
import com.garcheng.rpc.example.core.Header;
import com.garcheng.rpc.example.core.RpcProtocol;
import com.garcheng.rpc.example.core.RpcRequest;
import com.garcheng.rpc.example.core.RpcResponse;
import com.garcheng.rpc.example.spring.SpringBeanManager;
import com.garcheng.rpc.example.spring.service.Mediator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();

        Header header = msg.getHeader();
        header.setReqType(ReqType.RESPONSE.code());
        protocol.setHeader(header);
//        Object result = invoke(msg.getContent());
        Object result = Mediator.getInstance().process(msg.getContent());
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setMsg("success");
        rpcResponse.setData(result);
        protocol.setContent(rpcResponse);
        ctx.writeAndFlush(protocol);
    }

    @Deprecated
    private Object invoke(RpcRequest content) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(content.getClassName());
            Object bean = SpringBeanManager.getBean(clazz);
            Method method = clazz.getDeclaredMethod(content.getMethodName(), content.getParameterTypes());
            return method.invoke(bean,content.getParams());
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }
}
