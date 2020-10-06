package com.soft1851.user.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/content")
@Slf4j
public class UserHello {
    @GetMapping(value = "/hello")
    public String getHello(){
        log.info("我被调用了 。。");
        return "Hello user-center!";
    }




}
