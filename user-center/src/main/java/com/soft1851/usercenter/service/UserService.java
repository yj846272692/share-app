package com.soft1851.usercenter.service;


import com.soft1851.usercenter.domain.dto.LoginDTO;
import com.soft1851.usercenter.domain.dto.ResponseDto;
import com.soft1851.usercenter.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.usercenter.domain.dto.UserSignInDTO;
import com.soft1851.usercenter.domain.entity.BonusEventLog;
import com.soft1851.usercenter.domain.entity.User;

import java.util.List;


/**
 * @ClassName UserService
 * @Description TODO
 * @Author yj
 * @Date 2020/9/27
 **/
public interface UserService {
    /**
     * 根据id获得用户详情
     *
     * @param id
     * @return User
     */
    User findById(Integer id);

    /**
     * 增加积分
     *
     * @param userAddBonusMsgDTO
     */
    void addBonusById(UserAddBonusMsgDTO userAddBonusMsgDTO);

    /**
     * 登录接口
     */
    User login(LoginDTO loginDTO);

    /**
     * 用户签到
     * @param signInDTO
     * @return
     */

    ResponseDto signIn(UserSignInDTO signInDTO);

    /**
     * 判断用户是否签到
     */
    ResponseDto checkIsSign(UserSignInDTO signInDTO);

    /**
     * 根根据id查询明细
     * @param userId
     * @return
     */
    List<BonusEventLog> searchAllById(Integer userId);

    /**
     *
     */

    /**
     * 扣积分
     * @param userAddBonusMsgDTO
     * @return
     */
    User reduceBonus(UserAddBonusMsgDTO userAddBonusMsgDTO);

}
