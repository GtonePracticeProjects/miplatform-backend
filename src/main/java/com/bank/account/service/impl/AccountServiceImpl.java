package com.bank.account.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.account.dao.AccountDao;
import com.bank.account.service.AccountService;
import com.bank.account.vo.AccountVO;
import com.bank.account.vo.TransHistVO;
import com.bank.account.vo.TransferReqVO;
import com.bank.common.exception.BizException;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static final String STATUS_ACTIVE = "A";

    @Autowired
    private AccountDao accountDao;

    @Override
    @Transactional(readOnly = true)
    public List<AccountVO> getAccountList() {
        return accountDao.selectAccountList();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountVO getAccount(String acctNo) {
        AccountVO acct = accountDao.selectAccount(acctNo);
        if (acct == null) {
            throw new BizException("E101", "존재하지 않는 계좌입니다. (" + acctNo + ")");
        }
        return acct;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransHistVO> getTransHistList(String acctNo) {
        return accountDao.selectTransHistList(acctNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferReqVO req) {
        // 1. 입력값 검증
        if (req.getFromAcctNo() == null || req.getToAcctNo() == null
                || req.getFromAcctNo().trim().isEmpty() || req.getToAcctNo().trim().isEmpty()) {
            throw new BizException("E001", "출금/입금 계좌번호는 필수입니다.");
        }
        if (req.getFromAcctNo().equals(req.getToAcctNo())) {
            throw new BizException("E002", "출금계좌와 입금계좌가 동일합니다.");
        }
        if (req.getAmount() <= 0) {
            throw new BizException("E003", "이체금액은 0보다 커야 합니다.");
        }

        // 2. 데드락 방지: 항상 계좌번호 오름차순으로 행 잠금
        String first = req.getFromAcctNo().compareTo(req.getToAcctNo()) < 0 ? req.getFromAcctNo() : req.getToAcctNo();
        String second = first.equals(req.getFromAcctNo()) ? req.getToAcctNo() : req.getFromAcctNo();
        AccountVO a1 = accountDao.selectAccountForUpdate(first);
        AccountVO a2 = accountDao.selectAccountForUpdate(second);
        AccountVO from = first.equals(req.getFromAcctNo()) ? a1 : a2;
        AccountVO to = first.equals(req.getFromAcctNo()) ? a2 : a1;

        // 3. 업무 규칙 검증
        if (from == null) {
            throw new BizException("E101", "존재하지 않는 출금계좌입니다.");
        }
        if (to == null) {
            throw new BizException("E101", "존재하지 않는 입금계좌입니다.");
        }
        if (!STATUS_ACTIVE.equals(from.getStatusCd()) || !STATUS_ACTIVE.equals(to.getStatusCd())) {
            throw new BizException("E104", "해지되었거나 거래가 불가한 계좌입니다.");
        }
        if (from.getBalance() < req.getAmount()) {
            throw new BizException("E102", "잔액이 부족합니다. (잔액: " + from.getBalance() + ")");
        }

        // 4. 잔액 반영 + 거래내역
        accountDao.updateBalance(from.getAcctNo(), -req.getAmount());
        accountDao.updateBalance(to.getAcctNo(), req.getAmount());

        insertHist(from.getAcctNo(), TransHistVO.TP_WITHDRAW, req.getAmount(),
                from.getBalance() - req.getAmount(), to.getAcctNo(), req.getMemo());
        insertHist(to.getAcctNo(), TransHistVO.TP_DEPOSIT, req.getAmount(),
                to.getBalance() + req.getAmount(), from.getAcctNo(), req.getMemo());

        log.info("이체 완료 from={} to={} amount={}", from.getAcctNo(), to.getAcctNo(), req.getAmount());
    }

    private void insertHist(String acctNo, String tp, long amount, long balanceAfter, String counter, String memo) {
        TransHistVO h = new TransHistVO();
        h.setAcctNo(acctNo);
        h.setTransTp(tp);
        h.setAmount(amount);
        h.setBalanceAfter(balanceAfter);
        h.setCounterAcctNo(counter);
        h.setMemo(memo);
        accountDao.insertTransHist(h);
    }
}
