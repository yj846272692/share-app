package com.soft1851.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName ContentController
 * @Description TODO
 * @Author hyj
 * @Date 2020/9/23
 **/
@RestController
@RequestMapping(value = "/test")
public class ContentController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String getHello() {
        return restTemplate.getForObject("http://localhost:8005/user/hello", String.class);
    }

}