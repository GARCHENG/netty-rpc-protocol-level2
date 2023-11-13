package com.garcheng.rpc.example.proxy;

import com.garcheng.rpc.example.constant.ReqType;
import com.garcheng.rpc.example.constant.RpcConstant;
import com.garcheng.rpc.example.constant.SerialType;
import com.garcheng.rpc.example.core.*;
import com.garcheng.rpc.example.protocol.NettyClient;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcInvokeProxy implements InvocationHandler {

    private String address;
    private int port;

    public RpcInvokeProxy(String address ,int port){
        this.address = address;
        this.port = port;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
        long requestId = RequestHolder.REQUESE_ID.get();
        Header header = new Header(RpcConstant.MAGIC, SerialType.JSON_SERIAL.code(), ReqType.REQUEST.code(),requestId ,0);
        requestRpcProtocol.setHeader(header);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParams(args);
        RpcFuture<RpcResponse> rpcFuture = new RpcFuture<>(new DefaultPromise<RpcResponse>(new DefaultEventLoop()));
        RequestHolder.REQUEST_MAP.put(requestId,rpcFuture);

        requestRpcProtocol.setContent(rpcRequest);
        new NettyClient(address,port).sendRequest(requestRpcProtocol);
        return rpcFuture.getPromise().get().getData();
    }
}
