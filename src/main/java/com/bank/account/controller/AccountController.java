package com.bank.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.account.service.AccountService;
import com.bank.account.vo.AccountVO;
import com.bank.account.vo.TransferReqVO;

/** 파라미터 파싱 후 Service 로 위임만 한다. 비즈니스 로직 금지. */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("accounts", accountService.getAccountList());
        return "account/list";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public String transferForm(Model model) {
        model.addAttribute("accounts", accountService.getAccountList());
        return "account/transferForm";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String transfer(TransferReqVO req, RedirectAttributes rttr) {
        accountService.transfer(req);
        rttr.addFlashAttribute("msg", "이체가 완료되었습니다.");
        return "redirect:/account/" + req.getFromAcctNo();
    }

    @RequestMapping(value = "/{acctNo}", method = RequestMethod.GET)
    public String detail(@PathVariable("acctNo") String acctNo, Model model) {
        model.addAttribute("account", accountService.getAccount(acctNo));
        model.addAttribute("histList", accountService.getTransHistList(acctNo));
        return "account/detail";
    }

    /** jQuery Ajax 잔액 조회 */
    @RequestMapping(value = "/api/{acctNo}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public AccountVO apiAccount(@PathVariable("acctNo") String acctNo) {
        return accountService.getAccount(acctNo);
    }
}
