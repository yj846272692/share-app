package com.soft1851.usercenter.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.soft1851.usercenter.domain.dto.*;
import com.soft1851.usercenter.domain.entity.BonusEventLog;
import com.soft1851.usercenter.domain.entity.User;
import com.soft1851.usercenter.service.UserService;
import com.soft1851.usercenter.util.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName UserController
 * @Description TODO
 * @Author yj
 * @Date 2020/9/23
 **/
@RequestMapping("/users")
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;
    private final JwtOperator jwtOperator;
    private final WxMaService wxMaService;

    @GetMapping(value = "/{id}")
    public User findUserById(@PathVariable Integer id) {
        log.info("我被请求了...");
        return this.userService.findById(id);
    }

//    @PostMapping("/bonus")
//    public void addBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDto) {
//        userService.addBonusById(userAddBonusMsgDto);
//    }

//    @PostMapping(value = "/login")
//    public LoginRespDTO login(@RequestBody LoginDTO loginDTO) throws WxErrorException {
//        String openId;
//        //微信小程序登录，需要根据code请求openId
//        if (loginDTO.getLoginCode() != null) {
//            //微信服务端校验是否已经登录的结果
//            WxMaJscode2SessionResult result = this.wxMaService.getUserService()
//                    .getSessionInfo(loginDTO.getLoginCode());
//            log.info(result.toString());
//            //微信的openId，用户在微信这边的唯一标识
//            openId = result.getOpenid();
//        }else {
//            openId = loginDTO.getOpenId();
//        }
//        //看用户是否注册，如果没有注册就（插入），如果已经注册就返回其信息
//        User user = userService.login(loginDTO,openId);
//        //颁发token
//        Map<String,Object> userInfo = new HashMap<>(3);
//        userInfo.put("id",user.getId());
//        userInfo.put("wxNickname" ,user.getWxNickname());
//        userInfo.put("role",user.getRoles());
//        String token = jwtOperator.generateToken(userInfo);
//        log.info(
//                "(登录成功，生成的token = {}",
////                user.getWxNickname(),
//                token,
//
//                jwtOperator.getExpirationTime()
//        );
//        //构造返回结果
//        return LoginRespDTO.builder()
//                .user(UserRespDTO.builder()
//                        .id(user.getId())
//                        .wxNickname(user.getWxNickname())
//                        .avatarUrl(user.getAvatarUrl())
//                        .bonus(user.getBonus())
//                        .build())
//                .token(JwtTokenRespDTO
//                        .builder()
//                        .token(token)
//                        .expirationTime(jwtOperator.getExpirationTime().getTime())
//                        .build())
//                .build();
//    }

    @PostMapping(value = "/login")
    public LoginRespDTO getUser(@RequestBody LoginDTO loginDto){
        User user = this.userService.login(loginDto);
        //颁发token
        Map<String,Object> userInfo = new HashMap<>(3);
        userInfo.put("id",user.getId());
        userInfo.put("wxNickName",user.getWxNickname());
        userInfo.put("role",user.getRoles());
        String token = jwtOperator.generateToken(userInfo);

        log.info(
                "{}登录成功，生成的token = {},有效期到:{}",
                user.getWxNickname(),
                token,
                jwtOperator.getExpirationTime()
        );
//        ResponseDto responseDto = this.userService.checkIsSign(UserSignInDTO.builder().userId(user.getId()).build());
//        int isUserSignin = 0;
//        if (responseDto.getCode()=="200"){
//            isUserSignin = 0;
//        }else{
//            isUserSignin = 1;
//        }
        return LoginRespDTO.builder()
                .user(UserRespDTO.builder()
                        .id(user.getId())
                        .avatarUrl(user.getAvatarUrl())
                        .wxNickname(user.getWxNickname())
                        .bonus(user.getBonus())
                        .build())
                .token(JwtTokenRespDTO
                        .builder()
                        .token(token)
                        .expirationTime(jwtOperator.getExpirationTime().getTime())
                        .build())
//                        .isUserSignin(isUserSignin)
                .build();
    }

    @PostMapping(value = "/signin")
    public ResponseDto signIn(@RequestBody UserSignInDTO userSignInDTO){
        return userService.signIn(userSignInDTO);
    }

    @PostMapping(value = "/bonus/{id}")
    public List<BonusEventLog> searchAll(@PathVariable Integer id) {
        log.info("积分明细");
        return this.userService.searchAllById(id);
    }
    @PostMapping(value = "/reduceBonus")
    public User reduceBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO){
        return userService.reduceBonus(userAddBonusMsgDTO);
    }

}