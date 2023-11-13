package com.garcheng.rpc.example.service;

import com.garcheng.rpc.example.IUserService;
import com.garcheng.rpc.example.annotation.RpcRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RpcRemoteService
public class UserServiceImpl implements IUserService {

    @Override
    public String saveUser(String name) {
        log.info("begin save user:{}",name);
        return "save User success: "+name;
    }
}