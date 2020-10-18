package com.soft1851.content.service;

import com.github.pagehelper.PageInfo;
import com.soft1851.content.domain.dto.ShareRequestDTO;
import com.soft1851.content.domain.entity.Share;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShareServiceTest {
    @Autowired
    private ShareService shareService;

    @Test
    void query() {
        PageInfo<Share> query = shareService.query(null, 1, 2, 1);
        List<Share> list = query.getList();
        list.forEach(item -> System.out.println(item.getTitle() + "," + item.getDownloadUrl()));
    }

    @Test
    void contributeById() {
        Integer id = 11;
        ShareRequestDTO build = ShareRequestDTO.builder()
                .author("dsad")
                .downloadUrl("sadsad")
                .isOriginal(true)
                .price(123)
                .summary("dsad")
                .title("asdasd")
                .build();
        shareService.contributeById(id, build);
    }
}