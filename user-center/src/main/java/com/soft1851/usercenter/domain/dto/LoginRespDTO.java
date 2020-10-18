package com.soft1851.usercenter.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRespDTO {
    /**
     * 用户信息
     */
    private UserRespDTO user;

    /**
     * token数据
     */
    private  JwtTokenRespDTO token;

    /**
     * 用户当天是否签到
     */
    private  Integer isUserSignin;
}
