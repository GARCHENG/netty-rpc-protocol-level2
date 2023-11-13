package com.garcheng.rpc.example.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "gp.rpc")
public class RpcServerProperties {

    private int servicePort;

    private String registryAddress;  //注册中心的地址

}
