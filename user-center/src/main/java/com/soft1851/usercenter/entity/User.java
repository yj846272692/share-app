package com.soft1851.usercenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (TUser)实体类
 *
 * @author YangJinG
 * @since 2020-09-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_user")
public class User implements Serializable {
    private static final long serialVersionUID = -21117904334438215L;

    @Id
    @GeneratedValue(generator = "JDBC")
    private String id;

    private String name;

    private String avatar;

    private String phone;

}