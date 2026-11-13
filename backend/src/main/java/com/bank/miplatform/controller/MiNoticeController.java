package com.bank.miplatform.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bank.miplatform.adapter.MiDataset;
import com.bank.miplatform.adapter.MiDatasets;
import com.bank.miplatform.adapter.MiPlatformAdapter;
import com.bank.miplatform.adapter.MiResponse;
import com.bank.notice.service.NoticeService;
import com.bank.notice.vo.NoticeVO;

/** MiPlatform 공지 화면용 진입점. 기존 NoticeService 를 그대로 재사용한다. (이번 샘플의 프론트 화면은 없음) */
@Controller
@RequestMapping("/miplatform/notice")
public class MiNoticeController {

    @Autowired
    private MiPlatformAdapter adapter;
    @Autowired
    private NoticeService noticeService;

    /** in: 없음 / out: ds_notice */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public void list(HttpServletRequest req, HttpServletResponse res) throws IOException {
        adapter.read(req);
        MiResponse out = MiResponse.ok();
        out.dataset(MiDatasets.fromBeans("ds_notice", NoticeVO.class, noticeService.getNoticeList()));
        adapter.write(res, out);
    }

    /** in: ds_cond(noticeSeq) / out: ds_notice */
    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public void read(HttpServletRequest req, HttpServletResponse res) throws IOException {
        long seq = adapter.read(req).getDataset("ds_cond").getLong(0, "noticeSeq");
        MiResponse out = MiResponse.ok();
        out.dataset(MiDatasets.fromBean("ds_notice", NoticeVO.class, noticeService.readNotice(seq)));
        adapter.write(res, out);
    }

    /** in: ds_notice(title,content,writer) / out: 없음 */
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public void write(HttpServletRequest req, HttpServletResponse res) throws IOException {
        MiDataset ds = adapter.read(req).getDataset("ds_notice");
        NoticeVO vo = new NoticeVO();
        vo.setTitle(ds.getString(0, "title"));
        vo.setContent(ds.getString(0, "content"));
        vo.setWriter(ds.getString(0, "writer"));
        noticeService.writeNotice(vo);
        adapter.write(res, MiResponse.ok());
    }
}
