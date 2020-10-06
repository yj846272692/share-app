package com.soft1851.user.service;


import com.soft1851.user.entity.User;


/**
 * @ClassName UserService
 * @Description TODO
 * @Author yj
 * @Date 2020/9/27
 **/
public interface UserService {
    /**
     * 根据id获得用户详情
     * @param id
     * @return User
     */
    User findById(Integer id);

}
