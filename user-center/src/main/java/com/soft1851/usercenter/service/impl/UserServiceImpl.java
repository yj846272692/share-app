package com.soft1851.usercenter.service.impl;

import com.soft1851.usercenter.dao.BonusEventLogMapper;
import com.soft1851.usercenter.domain.dto.LoginDTO;
import com.soft1851.usercenter.domain.dto.ResponseDto;
import com.soft1851.usercenter.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.usercenter.domain.dto.UserSignInDTO;
import com.soft1851.usercenter.domain.entity.BonusEventLog;
import com.soft1851.usercenter.domain.entity.User;
import com.soft1851.usercenter.dao.UserMapper;
import com.soft1851.usercenter.service.UserService;
import com.soft1851.usercenter.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author yj
 * @Date 2020/9/27
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;

    @Override
    public User findById(Integer id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addBonusById(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        System.out.println(userAddBonusMsgDTO);
        // 为用户加积分
        Integer userId = userAddBonusMsgDTO.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + userAddBonusMsgDTO.getBonus());
        userMapper.updateByPrimaryKeySelective(user);

        // 写积分日志
        bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(userAddBonusMsgDTO.getBonus())
                        .event("CONTRIBUTE")
                        .createTime(new Date())
                        .description("投稿加分")
                        .build()
        );
    }

    @Override
    public User login(LoginDTO loginDTO) {
        //先根据openId查找用户
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("wxId", loginDTO.getOpenId());
        List<User> users = this.userMapper.selectByExample(example);
        //没找到，是新用户，直接注册
        if (users.size() == 0) {
            User saveUser = User.builder()
                    .wxId(loginDTO.getOpenId())
                    .avatarUrl(loginDTO.getAvatarUrl())
                    .wxNickname(loginDTO.getWxNickname())
                    .roles("user")
                    .bonus(100)
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            this.userMapper.insertSelective(saveUser);
            return saveUser;
        }
        return users.get(0);
    }

    @Override
    public ResponseDto signIn(UserSignInDTO signInDTO) {
        User user = this.userMapper.selectByPrimaryKey(signInDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("该用户不存在!");
        }
        Example example = new Example(BonusEventLog.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id DESC");
        criteria.andEqualTo("userId", signInDTO.getUserId());
        criteria.andEqualTo("event", "SIGN_IN");
        List<BonusEventLog> bonusEventLog = this.bonusEventLogMapper.selectByExample(example);
        //判断日志表有没有记录，如果没有直接插入数据并提示签到成功
        if (bonusEventLog.size() == 0) {
            this.bonusEventLogMapper.insert(BonusEventLog.builder()
                    .userId(signInDTO.getUserId())
                    .event("SIGN_IN")
                    .value(20)
                    .description("签到加积分")
                    .createTime(new Date())
                    .build());
            user.setBonus(user.getBonus() + 20);
            this.userMapper.updateByPrimaryKeySelective(user);
            return new ResponseDto(true, "200", "签到成功", user, 1l);
        } else {
            BonusEventLog bonusEventLog1 = bonusEventLog.get(0);
            Date date = bonusEventLog1.getCreateTime();
            try {
                if (DateUtil.checkAllotSigin(date) == 0) {
                    this.bonusEventLogMapper.insert(BonusEventLog.builder()
                            .userId(signInDTO.getUserId())
                            .event("SIGN_IN")
                            .value(20)
                            .description("签到加积分")
                            .createTime(new Date())
                            .build());
                    user.setBonus(user.getBonus() + 20);
                    this.userMapper.updateByPrimaryKeySelective(user);
                    return new ResponseDto(true, "200", "签到成功", user, 1l);
                } else if (DateUtil.checkAllotSigin(date) == 1) {
                    return new ResponseDto(false, "201", "签到失败", user.getWxNickname() + "今天已经签到过了", 1l);
                } else if (DateUtil.checkAllotSigin(date) == 2) {
                    return new ResponseDto(false, "202", "签到失败", user.getWxNickname() + "用户，今天数据错乱了", 1l);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ResponseDto(true, "200", "签到成功", user.getWxNickname() + "签到成功", 1l);
        }
    }

    @Override
    public ResponseDto checkIsSign(UserSignInDTO signInDTO) {
        User user = this.userMapper.selectByPrimaryKey(signInDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("该用户不存在!");
        }
        Example example = new Example(BonusEventLog.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("id DESC");
        criteria.andEqualTo("userId", signInDTO.getUserId());
        criteria.andEqualTo("event", "SIGN_IN");
        List<BonusEventLog> bonusEventLog = this.bonusEventLogMapper.selectByExample(example);
        //判断条件

        BonusEventLog bonusEventLog1 = bonusEventLog.get(0);
//        System.out.println(bonusEventLog1);
        Date date = bonusEventLog1.getCreateTime();
        try {
            if (DateUtil.checkAllotSigin(date) == 0) {
                return new ResponseDto(true, "200", "该用户还没有签到", "可以签到", 1l);
            } else if (DateUtil.checkAllotSigin(date) == 1) {
                return new ResponseDto(false, "201", "已经签到了", "不可以签到", 1l);
            } else if (DateUtil.checkAllotSigin(date) == 2) {
                return new ResponseDto(false, "202", "数据出错了", "不可以签到", 1l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseDto(true, "200", "该用户还没有签到", "可以签到", 1l);
    }

    @Override
    public List<BonusEventLog> searchAllById(Integer userId) {
        Example example = new Example(BonusEventLog.class);
        example.setOrderByClause("create_time DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        List<BonusEventLog> bonusEventLogList = this.bonusEventLogMapper.selectByExample(example);
        return bonusEventLogList;
    }

    /**
     * 减少积分
     *
     * @param userAddBonusMsgDTO
     * @return
     */
    @Override
    public User reduceBonus(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        User user = this.userMapper.selectByPrimaryKey(userAddBonusMsgDTO.getUserId());
        user.setBonus(user.getBonus() + userAddBonusMsgDTO.getBonus());
        this.userMapper.updateByPrimaryKeySelective(user);
        //插入日志，扣除相应的积分
        this.bonusEventLogMapper.insert(BonusEventLog.builder()
                .userId(userAddBonusMsgDTO.getUserId())
                .value(userAddBonusMsgDTO.getBonus())
                .event("CONTRIBUTE")
                .createTime(new Date())
                .description("兑换扣积分")
                .build()
        );
        return user;
    }
}
