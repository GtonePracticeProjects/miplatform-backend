package com.bank.notice.service;

import java.util.List;

import com.bank.notice.vo.NoticeVO;

public interface NoticeService {

    List<NoticeVO> getNoticeList();

    /** 상세 조회 (조회수 +1) */
    NoticeVO readNotice(long noticeSeq);

    void writeNotice(NoticeVO vo);
}
