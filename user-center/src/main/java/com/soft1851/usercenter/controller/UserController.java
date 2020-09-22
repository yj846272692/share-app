package com.soft1851.usercenter.controller;

import com.soft1851.usercenter.entity.User;
import com.soft1851.usercenter.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (TUser)表控制层
 *
 * @author YangJinG
 * @since 2020-09-20
 */
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectById/{id}")
    public User selectOne(@PathVariable("id") String id) {
        return this.userService.queryById(id);
    }

}