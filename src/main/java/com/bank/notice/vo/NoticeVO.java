package com.bank.notice.vo;

import java.io.Serializable;
import java.util.Date;

/** 공지사항 (TB_NOTICE) */
public class NoticeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long noticeSeq;
    private String title;
    private String content;
    private String writer;
    private int viewCnt;
    private Date regDt;

    public long getNoticeSeq() { return noticeSeq; }
    public void setNoticeSeq(long noticeSeq) { this.noticeSeq = noticeSeq; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getWriter() { return writer; }
    public void setWriter(String writer) { this.writer = writer; }
    public int getViewCnt() { return viewCnt; }
    public void setViewCnt(int viewCnt) { this.viewCnt = viewCnt; }
    public Date getRegDt() { return regDt; }
    public void setRegDt(Date regDt) { this.regDt = regDt; }
}
