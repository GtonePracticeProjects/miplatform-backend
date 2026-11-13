package com.bank.miplatform.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bank.account.service.AccountService;
import com.bank.account.vo.AccountVO;
import com.bank.account.vo.TransHistVO;
import com.bank.account.vo.TransferReqVO;
import com.bank.miplatform.adapter.MiDataset;
import com.bank.miplatform.adapter.MiDatasets;
import com.bank.miplatform.adapter.MiPlatformAdapter;
import com.bank.miplatform.adapter.MiRequest;
import com.bank.miplatform.adapter.MiResponse;

/** MiPlatform 계좌 화면용 진입점. 기존 AccountService 를 그대로 재사용한다. */
@Controller
@RequestMapping("/miplatform/account")
public class MiAccountController {

    @Autowired
    private MiPlatformAdapter adapter;
    @Autowired
    private AccountService accountService;

    /** in: 없음 / out: ds_account */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public void list(HttpServletRequest req, HttpServletResponse res) throws IOException {
        adapter.read(req);
        MiResponse out = MiResponse.ok();
        out.dataset(MiDatasets.fromBeans("ds_account", AccountVO.class, accountService.getAccountList()));
        adapter.write(res, out);
    }

    /** in: ds_cond(acctNo) / out: ds_account, ds_hist */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public void detail(HttpServletRequest req, HttpServletResponse res) throws IOException {
        MiRequest in = adapter.read(req);
        String acctNo = in.getDataset("ds_cond").getString(0, "acctNo");
        MiResponse out = MiResponse.ok();
        out.dataset(MiDatasets.fromBean("ds_account", AccountVO.class, accountService.getAccount(acctNo)));
        out.dataset(MiDatasets.fromBeans("ds_hist", TransHistVO.class, accountService.getTransHistList(acctNo)));
        adapter.write(res, out);
    }

    /** in: ds_transfer(fromAcctNo,toAcctNo,amount,memo) / out: 없음 */
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public void transfer(HttpServletRequest req, HttpServletResponse res) throws IOException {
        MiDataset ds = adapter.read(req).getDataset("ds_transfer");
        TransferReqVO vo = new TransferReqVO();
        vo.setFromAcctNo(ds.getString(0, "fromAcctNo"));
        vo.setToAcctNo(ds.getString(0, "toAcctNo"));
        vo.setAmount(ds.getLong(0, "amount"));
        vo.setMemo(ds.getString(0, "memo"));
        accountService.transfer(vo);
        adapter.write(res, MiResponse.ok());
    }
}
