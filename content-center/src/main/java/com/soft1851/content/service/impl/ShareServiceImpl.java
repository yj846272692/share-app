package com.soft1851.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soft1851.content.dao.MidUserShareMapper;
import com.soft1851.content.dao.ShareMapper;
import com.soft1851.content.domain.dto.ShareDto;
import com.soft1851.content.domain.dto.UserDto;
import com.soft1851.content.domain.entity.MidUserShare;
import com.soft1851.content.domain.entity.Share;
import com.soft1851.content.domain.entity.User;
import com.soft1851.content.feignclient.UserCenterFeignClient;
import com.soft1851.content.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

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

    @Override
    public ShareDto findById(Integer id) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id",id);
        Share share = shareMapper.selectOneByExample(example);
        int userId = share.getUserId();
        UserDto user = this.userCenterFeignClient.findById(userId);
        ShareDto shareDto = new ShareDto();
        BeanUtils.copyProperties(share,shareDto);
        shareDto.setWxNickName(user.getWxNickname());
        return shareDto;
    }
    @Override
    public PageInfo<Share> query(String title, Integer pageNo, Integer pageSize, Integer userId) {
        //启动分页
        PageHelper.startPage(pageNo,pageSize);
        //构造查询实例
        Example example = new Example(Share.class);
        Example.Criteria criteria = example.createCriteria();
        //如标题关键字不空，则加上模糊查询条件，否则结果即所有数据
        if (StringUtil.isEmpty(title)){
            criteria.andLike("title","%"+title+"%");
        }
        //执行按条件查询
        List<Share> shares = this.shareMapper.selectByExample(example);
        //处理后的Share数据列表
        List<Share> shareDeal;
        // 1.如果用户未登录，那么downloadUrl全部设为null
        if (userId == null){
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
                        if (midUserShare == null){
                            share.setDownloadUrl(null);
                        }
                    })
                    .collect(Collectors.toList());
        }
        return new PageInfo<>(shareDeal);
    }
}