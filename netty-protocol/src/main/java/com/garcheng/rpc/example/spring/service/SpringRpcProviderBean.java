package com.garcheng.rpc.example.spring.service;

import com.garcheng.rpc.example.annotation.RpcRemoteService;
import com.garcheng.rpc.example.protocol.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;


@Slf4j
public class SpringRpcProviderBean implements InitializingBean , BeanPostProcessor {

    private final String serverAddress;
    private final int serverPort;

    public SpringRpcProviderBean(String serverAddress , int serverPort){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        new Thread(()->{
            log.info("begin deploy Netty Server to host {},on port {}",this.serverAddress,this.serverPort);
            new NettyServer(this.serverAddress,this.serverPort).startNettyServer();
        }).start();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcRemoteService.class)){
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String serverName = bean.getClass().getInterfaces()[0].getName();
                String key = serverName + "." + method.getName();
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                Mediator.beanMethodMap.put(key,beanMethod);
            }
        }
        return bean;
    }
}
