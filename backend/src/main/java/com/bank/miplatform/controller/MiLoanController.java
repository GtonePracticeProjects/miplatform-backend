package com.bank.miplatform.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bank.loan.service.LoanService;
import com.bank.loan.vo.LoanVO;
import com.bank.miplatform.adapter.MiDataset;
import com.bank.miplatform.adapter.MiDatasets;
import com.bank.miplatform.adapter.MiPlatformAdapter;
import com.bank.miplatform.adapter.MiResponse;

/** MiPlatform 대출 화면용 진입점. 기존 LoanService 를 그대로 재사용한다. */
@Controller
@RequestMapping("/miplatform/loan")
public class MiLoanController {

    @Autowired
    private MiPlatformAdapter adapter;
    @Autowired
    private LoanService loanService;

    /** in: ds_cond(status: R/A/X, 빈값=전체) / out: ds_loan */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public void list(HttpServletRequest req, HttpServletResponse res) throws IOException {
        MiDataset cond = adapter.read(req).getDataset("ds_cond");
        String status = cond == null ? null : cond.getString(0, "status");
        if (status != null && status.trim().isEmpty()) {
            status = null;
        }
        MiResponse out = MiResponse.ok();
        out.dataset(MiDatasets.fromBeans("ds_loan", LoanVO.class, loanService.getLoanList(status)));
        adapter.write(res, out);
    }

    /** in: ds_apply(acctNo,principal,termMonths) / out: 없음 */
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public void apply(HttpServletRequest req, HttpServletResponse res) throws IOException {
        MiDataset ds = adapter.read(req).getDataset("ds_apply");
        loanService.applyLoan(ds.getString(0, "acctNo"), ds.getLong(0, "principal"), ds.getInt(0, "termMonths"));
        adapter.write(res, MiResponse.ok());
    }

    /** in: ds_decide(loanNo, approveYn: Y/N) / out: 없음 */
    @RequestMapping(value = "/decide", method = RequestMethod.POST)
    public void decide(HttpServletRequest req, HttpServletResponse res) throws IOException {
        MiDataset ds = adapter.read(req).getDataset("ds_decide");
        loanService.decideLoan(ds.getLong(0, "loanNo"), "Y".equals(ds.getString(0, "approveYn")));
        adapter.write(res, MiResponse.ok());
    }
}
