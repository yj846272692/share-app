package com.soft1851.content.service;

import com.github.pagehelper.PageInfo;
import com.soft1851.content.domain.dto.*;
import com.soft1851.content.domain.entity.Share;

import java.util.List;

/**
 * @ClassName ShareService
 * @Description TODO
 * @Author yj
 * @Date 2020/9/26
 **/
public interface ShareService {
    /**
     * 获得分享详情
     *
     * @return ShareDTO
     */
    ShareDto findById(Integer id);

    /**
     * 根据标题模糊查询某个用户的分享列表数据，title为空则为所有数据，查询结果分页
     *
     * @param title
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return PageInfo<Share>
     */
    PageInfo<Share> query(String title, Integer pageNo, Integer pageSize, Integer userId);

    /**
     * 投稿接口
     *
     * @param shareDto1
     * @return
     */
    Share contribute(ShareDto1 shareDto1);

    /**
     * 编辑投稿接口
     */
    Share contributeById(Integer id, ShareRequestDTO shareRequestDTO);


    /**
     * 管理员审核投稿
     */
    Share audit(Integer id, AuditDTO auditDTO);

    /**
     * 积分兑换资源
     *
     * @param exchangeDto
     * @return Share
     */
    Share exchange(ExchangeDto exchangeDto);

    /**
     * 根据用户id查询该用户投稿的资源
     * @param userId
     * @return
     */
    List<Share> myContribute(Integer userId);

    /**
     * 根据用户id查询该用户兑换的资源
     * @param userId
     * @return
     */
    List<Share> queryMy(Integer userId);
}
