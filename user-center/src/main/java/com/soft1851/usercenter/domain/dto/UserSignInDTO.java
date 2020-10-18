package com.soft1851.usercenter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:
 *
 * @author：
 * @create 2020-10-15 11:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignInDTO {
    private Integer userId;
    private String event;
}
