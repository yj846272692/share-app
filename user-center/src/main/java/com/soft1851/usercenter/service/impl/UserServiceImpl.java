package com.soft1851.usercenter.service.impl;

import com.soft1851.usercenter.entity.User;
import com.soft1851.usercenter.mapper.UserMapper;
import com.soft1851.usercenter.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author YangJinG
 * @Date 2020/9/20
 * @Description
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User queryById(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
