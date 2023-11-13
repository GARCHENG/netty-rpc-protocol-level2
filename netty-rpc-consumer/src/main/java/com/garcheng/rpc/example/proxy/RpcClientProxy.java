package com.garcheng.rpc.example.proxy;

import java.lang.reflect.Proxy;

public class RpcClientProxy {

    public <T> T clientProxy(Class<T> interfaces ,String address,int port){

        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(),
                new Class<?> []{interfaces},new RpcInvokeProxy(address,port));
    }
}
