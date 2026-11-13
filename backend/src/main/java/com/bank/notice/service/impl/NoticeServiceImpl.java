package com.bank.notice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.common.exception.BizException;
import com.bank.notice.mapper.NoticeMapper;
import com.bank.notice.service.NoticeService;
import com.bank.notice.vo.NoticeVO;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NoticeVO> getNoticeList() {
        return noticeMapper.selectNoticeList();
    }

    @Override
    @Transactional
    public NoticeVO readNotice(long noticeSeq) {
        NoticeVO vo = noticeMapper.selectNotice(noticeSeq);
        if (vo == null) {
            throw new BizException("E401", "존재하지 않는 공지입니다.");
        }
        noticeMapper.increaseViewCnt(noticeSeq);
        vo.setViewCnt(vo.getViewCnt() + 1);
        return vo;
    }

    @Override
    @Transactional
    public void writeNotice(NoticeVO vo) {
        if (vo.getTitle() == null || vo.getTitle().trim().isEmpty()) {
            throw new BizException("E402", "제목은 필수입니다.");
        }
        if (vo.getWriter() == null || vo.getWriter().trim().isEmpty()) {
            throw new BizException("E403", "작성자는 필수입니다.");
        }
        noticeMapper.insertNotice(vo);
    }
}
