package com.bank.loan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.account.service.AccountService;
import com.bank.loan.service.LoanService;

@Controller
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "status", required = false) String status, Model model) {
        model.addAttribute("loans", loanService.getLoanList(status));
        model.addAttribute("status", status);
        return "loan/list";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String applyForm(Model model) {
        model.addAttribute("accounts", accountService.getAccountList());
        return "loan/form";
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public String apply(@RequestParam("acctNo") String acctNo,
                        @RequestParam("principal") long principal,
                        @RequestParam("termMonths") int termMonths,
                        RedirectAttributes rttr) {
        loanService.applyLoan(acctNo, principal, termMonths);
        rttr.addFlashAttribute("msg", "대출 신청이 접수되었습니다.");
        return "redirect:/loan/list";
    }

    @RequestMapping(value = "/{loanNo}/approve", method = RequestMethod.POST)
    public String approve(@PathVariable("loanNo") long loanNo, RedirectAttributes rttr) {
        loanService.decideLoan(loanNo, true);
        rttr.addFlashAttribute("msg", "대출이 승인되었습니다.");
        return "redirect:/loan/list";
    }

    @RequestMapping(value = "/{loanNo}/reject", method = RequestMethod.POST)
    public String reject(@PathVariable("loanNo") long loanNo, RedirectAttributes rttr) {
        loanService.decideLoan(loanNo, false);
        rttr.addFlashAttribute("msg", "대출이 거절되었습니다.");
        return "redirect:/loan/list";
    }
}
