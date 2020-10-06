package com.soft1851.content.service;

import com.soft1851.content.domain.entity.Notice;

public interface NoticeService {
    /**
     * 查询最新公告
     * @return
     */
    Notice getLatest();
}
