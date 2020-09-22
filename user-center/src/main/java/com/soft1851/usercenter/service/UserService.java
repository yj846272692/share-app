package com.soft1851.usercenter.service;

import com.soft1851.usercenter.entity.User;

import java.util.List;

/**
 * (TUser)表服务接口
 *
 * @author YangJinG
 * @since 2020-09-20
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(String id);

}