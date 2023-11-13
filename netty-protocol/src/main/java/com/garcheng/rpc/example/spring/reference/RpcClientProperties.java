package com.garcheng.rpc.example.spring.reference;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
public class RpcClientProperties {

    private String serviceAddress;

    private int servicePort;
}
