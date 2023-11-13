package com.garcheng.rpc.example.spring.service;

import com.garcheng.rpc.example.core.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Mediator {

    public static Map<String,BeanMethod> beanMethodMap=new ConcurrentHashMap<>();

    private Mediator(){}

    private volatile static Mediator instance = null;

    public static Mediator getInstance(){
        if (instance == null){
            synchronized (Mediator.class){
                if (instance == null){
                    instance = new Mediator();
                }
                return instance;
            }
        }
        return instance;
    }

    public Object process(RpcRequest request){
        String key = request.getClassName() + "." + request.getMethodName();
        BeanMethod beanMethod = beanMethodMap.get(key);
        if (beanMethod != null) {
            try {
                return beanMethod.getMethod().invoke(beanMethod.getBean(),request.getParams());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

}
