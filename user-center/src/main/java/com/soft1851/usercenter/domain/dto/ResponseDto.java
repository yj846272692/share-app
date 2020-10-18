package com.soft1851.usercenter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {
    private Boolean succ;
    private String code;
    private String msg;
    private Object data;
    private Long ts;
}
