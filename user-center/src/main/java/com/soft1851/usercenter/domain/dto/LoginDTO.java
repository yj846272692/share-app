package com.soft1851.usercenter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    /**
     * openID
     */
    private String openId;

    /**
     * 微信昵称
     */
    private String wxNickname;

    /**
     * 头像地址
     */
    private String avatarUrl;
}
