package com.soft1851.content.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soft1851.content.dao.MidUserShareMapper;
import com.soft1851.content.dao.ShareMapper;
import com.soft1851.content.domain.dto.*;
import com.soft1851.content.domain.entity.MidUserShare;
import com.soft1851.content.domain.entity.Share;
import com.soft1851.content.domain.entity.User;
import com.soft1851.content.domain.enums.AuditStatusEnum;
import com.soft1851.content.feignclient.UserCenterFeignClient;
import com.soft1851.content.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName ShareServiceImpl
 * @Description TODO
 * @Author yj
 * @Date 2020/9/26
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {
    private final ShareMapper shareMapper;
    private final UserCenterFeignClient userCenterFeignClient;
    private final MidUserShareMapper midUserShareMapper;
    private final RocketMQTemplate rocketMQTemplate;



    @Override
    public ShareDto findById(Integer id) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id", id);
        Share share = shareMapper.selectOneByExample(example);
        int userId = share.getUserId();
        ResponseDto user = this.userCenterFeignClient.findById(userId);

        User userDto = convert(user);
        ShareDto shareDto = new ShareDto();
        BeanUtils.copyProperties(share, shareDto);
        shareDto.setWxNickName(userDto.getWxNickname());
        return shareDto;
    }

    @Override
    public PageInfo<Share> query(String title, Integer pageNo, Integer pageSize, Integer userId) {
        //启动分页
        PageHelper.startPage(pageNo, pageSize);
        //构造查询实例
        Example example = new Example(Share.class);
        Example.Criteria criteria = example.createCriteria();
        //如标题关键字不空，则加上模糊查询条件，否则结果即所有数据
        if (StringUtil.isNotEmpty(title)) {
            criteria.andLike("title", "%" + title + "%");
        }
        //执行按条件查询
        List<Share> shares = this.shareMapper.selectByExample(example);
        //处理后的Share数据列表
        List<Share> shareDeal;
        // 1.如果用户未登录，那么downloadUrl全部设为null
        System.out.println(shares);
        if (userId == null) {
            shareDeal = shares.stream()
                    .peek(share -> {
                        share.setDownloadUrl(null);
                    })
                    .collect(Collectors.toList());
        }
        // 2.如果用户登录了，那么查询一下mid_user_share，如果没有数据，那么这条share的downloadUrl也设为null
        //只有自己分享的资源才能看到下载链接，否则显示兑换
        else {
            shareDeal = shares.stream()
                    .peek(share -> {
                        MidUserShare midUserShare = this.midUserShareMapper.selectOne(
                                MidUserShare.builder()
                                        .userId(userId)
                                        .shareId(share.getId())
                                        .build()
                        );
                        if (midUserShare == null) {
                            share.setDownloadUrl(null);
                        }
                    })
                    .collect(Collectors.toList());
        }
        return new PageInfo<>(shareDeal);
    }

    @Override
    public Share contribute(ShareDto1 shareDto1) {
        Share share = Share.builder()
                .title(shareDto1.getTitle())
                .price(shareDto1.getPrice())
                .summary(shareDto1.getSummary())
                .author(shareDto1.getAuthor())
                .isOriginal(shareDto1.getIsOriginal())
                .userId(shareDto1.getUserId())
                .downloadUrl(shareDto1.getDownloadUrl())
                .createTime(Date.valueOf(LocalDate.now()))
                .updateTime(Date.valueOf(LocalDate.now()))
                .cover("https://soft-1851-yj.oss-cn-beijing.aliyuncs.com/img/0b4cb59a-9ce2-4166-86c4-1e680fa07fb5.jpg")
                .buyCount(shareDto1.getPrice())
                .auditStatus("NOT_YET")
                .showFlag(1)
                .reason("未审核")
                .build();
        int n = this.shareMapper.insert(share);

        return share;
    }

    @Override
    public Share contributeById(Integer id, ShareRequestDTO shareRequestDTO) {
        Share share = Share.builder()
                .id(id)
                .author(shareRequestDTO.getAuthor())
                .downloadUrl(shareRequestDTO.getDownloadUrl())
                .isOriginal(shareRequestDTO.getIsOriginal())
                .price(shareRequestDTO.getPrice())
                .summary(shareRequestDTO.getSummary())
                .title(shareRequestDTO.getTitle())
                .build();
        shareMapper.updateByPrimaryKeySelective(share);
        return shareMapper.selectByPrimaryKey(id);
    }

    @Override
    public Share audit(Integer id, AuditDTO auditDTO) {
        Share share = shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("无此数据");
        }
        share.setAuditStatus(auditDTO.getAuditStatusEnum().toString());
        shareMapper.updateByPrimaryKeySelective(share);
        if (AuditStatusEnum.PASSED.equals(auditDTO.getAuditStatusEnum())) {
            this.rocketMQTemplate.convertAndSend(
                    "add-bonus",
                    UserAddBonusMsgDTO.builder()
                            .userId(share.getUserId())
                            .bonus(50)
                            .build());
        }


        return shareMapper.selectByPrimaryKey(id);
    }

    @Override
    public Share exchange(ExchangeDto exchangeDto) {
        int userId = exchangeDto.getUserId();
        int shareId = exchangeDto.getShareId();
        //根据id查询share，校验分享是否存在
        Share share = this.shareMapper.selectByPrimaryKey(shareId);
        System.out.println(share);
        if(share == null){
            throw new IllegalArgumentException("该分享不存在");
        }
        //获取到积分
        Integer price = share.getPrice();
        // 2. 如果当前用户已经兑换过该分享，则直接返回
        MidUserShare midUserShare = this.midUserShareMapper.selectOne(
                MidUserShare.builder()
                        .shareId(shareId)
                        .userId(userId)
                        .build()
        );
        System.out.println(midUserShare);
        if (midUserShare != null) {
            return share;
        }

        // 3. 根据当前登录的用户id，查询积分是否足够
        //这里一定要注意通过调用户中心接口得到的返回值，外面已经封装了一层了，要解析才能拿到真正的用户数据
        ResponseDto responseDto = this.userCenterFeignClient.findById(userId);


        User userDto = convert(responseDto);
        if (price>userDto.getBonus()){
            throw new IllegalArgumentException("用户积分不够");
        }
        System.out.println(userDto.getBonus());

        //4、扣积分
        this.userCenterFeignClient.reduceBonus(
                UserAddBonusMsgDTO.builder()
                        .userId(userId)
                        .bonus(price * -1)
                        .build()
        );
        //5. 向mid_user_share表里插入一条数据
        this.midUserShareMapper.insert(
                MidUserShare.builder()
                        .userId(userId)
                        .shareId(shareId)
                        .build()
        );
        return share;
    }

    @Override
    public List<Share> myContribute(Integer userId) {
        Example example = new Example(Share.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        List<Share> shareList = this.shareMapper.selectByExample(example);
        if (shareList.size() == 0) {
            throw new IllegalArgumentException("该用户没有投稿");
        }
        return shareList;
    }

    @Override
    public List<Share> queryMy(Integer userId) {
        //首先查找MidUserShare
        Example example = new Example(MidUserShare.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        List<MidUserShare> midUserShares =  midUserShareMapper.selectByExample(example);
        if (midUserShares.size() == 0){
            throw  new IllegalArgumentException("该用户没有这条分享");
        }
        //之后在根据shareId 去查找share
        List<Share> shareList = new ArrayList<>();
        midUserShares.forEach(midUserShare -> {
            Share share = this.shareMapper.selectByPrimaryKey(midUserShare.getShareId());
            shareList.add(share);
        });

        return shareList;
    }


    /**
     * 将统一的返回响应结果转换为UserDTO类型
     * @param responseDTO
     * @return
     */
    private User convert(ResponseDto responseDTO) {
        ObjectMapper mapper = new ObjectMapper();
        User userDTO = null;
        try {
            String json = mapper.writeValueAsString(responseDTO.getData());

            System.out.println(json);
            userDTO = mapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userDTO;
    }

}