package com.soft1851.content.controller;

import com.soft1851.content.auth.CheckLogin;
import com.soft1851.content.domain.dto.*;
import com.soft1851.content.domain.entity.Share;
import com.soft1851.content.service.ShareService;
import com.soft1851.content.util.JwtOperator;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @ClassName ShareController
 * @Description TODO
 * @Author yj
 * @Date 2020/9/26
 **/
@Slf4j
@RestController
@RequestMapping(value = "/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {
    private final ShareService shareService;
    private final JwtOperator jwtOperator;
    @GetMapping(value = "/{id}")
    public ShareDto findById(@PathVariable Integer id) {
        return this.shareService.findById(id);
    }

    @GetMapping("/query")
    @ApiOperation(value = "分享列表", notes = "分享列表")
    public List<Share> query(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestHeader(value = "X-Token", required = false) String token) throws Exception {
        if (pageSize > 100) {
            pageSize = 100;
        }
        Integer userId = null;
        if (StringUtils.isNotBlank(token)) {
            Claims claims = this.jwtOperator.getClaimsFromToken(token);
            log.info(claims.toString());
            userId = (Integer) claims.get("id");
        } else {
            log.info("没有token");
        }
        return this.shareService.query(title, pageNo, pageSize, userId).getList();
    }

    @PostMapping("/contribute")
    @ApiOperation(value = "投稿", notes = "投稿情况")
    public Share contribute(@RequestBody ShareDto1 shareDto1) {
        return this.shareService.contribute(shareDto1);
    }

    @PutMapping("/contribute/{id}")
    @ApiOperation(value = "编辑投稿", notes = "投稿编辑")
    public Share contributeById(@PathVariable Integer id, @RequestBody ShareRequestDTO shareRequestDTO) {
        return this.shareService.contributeById(id, shareRequestDTO);
    }

    @PutMapping("/audit/{id}")
    @ApiModelProperty(value = "管理员审核投稿", notes = "管理员审核投稿")
    public Share audit(@PathVariable Integer id, @RequestBody AuditDTO auditDTO) {
        return this.shareService.audit(id, auditDTO);
    }

    @PostMapping("/exchange")
    @ApiOperation(value = "兑换分享资源",notes = "兑换分享资源")
    public Share exchange(@RequestBody ExchangeDto exchangeDto) {
        System.out.println(exchangeDto + ">>>>>>>>>>>>");
        return this.shareService.exchange(exchangeDto);
    }
    @PostMapping("/myContribute/{id}")
//    @CheckLogin
    @ApiOperation(value = "查询我的投稿",notes = "查询我的投稿")
    public List<Share> myContribute(@PathVariable Integer id){
        return this.shareService.myContribute(id);
    }

    @GetMapping("/myShare/{id}")
//    @CheckLogin
    @ApiOperation(value = "查询我的兑换",notes = "查询我的兑换")
    public List<Share> getMy(@PathVariable Integer id){
        return this.shareService.queryMy(id);
    }
}