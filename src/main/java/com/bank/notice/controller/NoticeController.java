package com.bank.notice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.notice.service.NoticeService;
import com.bank.notice.vo.NoticeVO;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("notices", noticeService.getNoticeList());
        return "notice/list";
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String writeForm() {
        return "notice/form";
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String write(NoticeVO vo, RedirectAttributes rttr) {
        noticeService.writeNotice(vo);
        rttr.addFlashAttribute("msg", "공지사항이 등록되었습니다.");
        return "redirect:/notice/list";
    }

    @RequestMapping(value = "/{noticeSeq}", method = RequestMethod.GET)
    public String detail(@PathVariable("noticeSeq") long noticeSeq, Model model) {
        model.addAttribute("notice", noticeService.readNotice(noticeSeq));
        return "notice/detail";
    }
}
