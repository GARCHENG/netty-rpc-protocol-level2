package com.garcheng.rpc.example.spring.reference;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SpringRpcReferenceAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @Bean
    public SpringRpcReferencePostProcess springRpcReferencePostProcess(){
        RpcClientProperties rc=new RpcClientProperties();
        rc.setServiceAddress("192.168.3.237");
        rc.setServicePort(8888);
        return new SpringRpcReferencePostProcess(rc);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
