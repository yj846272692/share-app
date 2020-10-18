package com.soft1851.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;
import com.purgeteam.dispose.starter.annotation.EnableGlobalDispose;



@EnableGlobalDispose
@SpringBootApplication
@MapperScan("com.soft1851.usercenter.dao")

public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
