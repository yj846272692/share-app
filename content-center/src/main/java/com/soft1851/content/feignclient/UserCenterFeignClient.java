package com.soft1851.content.feignclient;

import com.soft1851.content.domain.dto.ResponseDto;
import com.soft1851.content.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.content.domain.dto.UserDto;
import com.soft1851.content.domain.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "user-center")
public interface UserCenterFeignClient {
    /**
     * http://user-center/users/{id}
     *
     * @param id
     * @return UserDTO
     */
    @GetMapping("/users/{id}")
    ResponseDto findById(@PathVariable Integer id);

    /**
     * hello测试
     *
     * @return String
     */
    @GetMapping("/user/hello")
    String getHello();

    /**
     * 积分明细
     * @param
     * @return
     */
    @PostMapping(value = "/users/bonus")
    UserAddBonusMsgDTO addBonusById(@RequestParam Integer id);

    /**
     * 兑换扣积分
     * @param userAddBonusMsgDTO
     * @return
     */
    @PostMapping("/users/reduceBonus")
    User reduceBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO);
}
