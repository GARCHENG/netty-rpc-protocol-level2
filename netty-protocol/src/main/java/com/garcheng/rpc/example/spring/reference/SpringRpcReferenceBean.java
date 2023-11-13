package com.garcheng.rpc.example.spring.reference;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class SpringRpcReferenceBean implements FactoryBean<Object> {

    private Object object;

    private Class<?> interfaceClass;

    private String serviceAddress;

    private int servicePort;


    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }


    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public void init(){
        this.object = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?> []{interfaceClass},new RpcInvokeProxy(serviceAddress,servicePort));
    }

}
