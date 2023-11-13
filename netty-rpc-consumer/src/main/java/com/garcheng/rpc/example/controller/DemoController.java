package com.garcheng.rpc.example.controller;


import com.garcheng.rpc.example.IUserService;
import com.garcheng.rpc.example.annotation.RpcRemoteReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RpcRemoteReference
    IUserService userService;

    @GetMapping("test")
    public String test(){
        return userService.saveUser("garcheng");
    }
}
