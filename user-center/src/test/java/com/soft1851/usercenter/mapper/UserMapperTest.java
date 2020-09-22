package com.soft1851.usercenter.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author zeng
 * @Date 2020/9/16 9:36
 * @Description
 * @Version 1.0
 */
@SpringBootTest
class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void queryById() {
        System.out.println(userMapper.queryById("1"));

    }
}