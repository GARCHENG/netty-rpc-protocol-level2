package com.garcheng.rpc.example;

import com.garcheng.rpc.example.proxy.RpcClientProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.garcheng.rpc.example.spring.reference","com.garcheng.rpc.example.controller","com.garcheng.rpc.example.annotation"})
@SpringBootApplication
public class NettyRpcConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyRpcConsumerApplication.class, args);
//        RpcClientProxy clientProxy = new RpcClientProxy();
//        IUserService iUserService = clientProxy.clientProxy(IUserService.class, "127.0.0.1", 8888);
//        System.out.println(iUserService.saveUser("garcheng"));

    }

}
