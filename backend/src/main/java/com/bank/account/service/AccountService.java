package com.bank.account.service;

import java.util.List;

import com.bank.account.vo.AccountVO;
import com.bank.account.vo.TransHistVO;
import com.bank.account.vo.TransferReqVO;

public interface AccountService {

    List<AccountVO> getAccountList();

    AccountVO getAccount(String acctNo);

    List<TransHistVO> getTransHistList(String acctNo);

    /** 계좌 이체 (출금 + 입금 + 거래내역 2건을 하나의 트랜잭션으로 처리) */
    void transfer(TransferReqVO req);
}
