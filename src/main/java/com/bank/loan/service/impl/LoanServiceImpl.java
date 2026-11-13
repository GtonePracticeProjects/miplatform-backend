package com.bank.loan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.account.service.AccountService;
import com.bank.account.vo.AccountVO;
import com.bank.common.exception.BizException;
import com.bank.loan.dao.LoanDao;
import com.bank.loan.service.LoanService;
import com.bank.loan.vo.LoanVO;

@Service
public class LoanServiceImpl implements LoanService {

    private static final long MIN_PRINCIPAL = 1000000L;
    private static final long MAX_PRINCIPAL = 100000000L;

    @Autowired
    private LoanDao loanDao;
    @Autowired
    private AccountService accountService;

    @Override
    @Transactional(readOnly = true)
    public List<LoanVO> getLoanList(String statusCd) {
        return loanDao.selectLoanList(statusCd, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyLoan(String acctNo, long principal, int termMonths) {
        AccountVO acct = accountService.getAccount(acctNo); // 없으면 BizException
        if (!"A".equals(acct.getStatusCd())) {
            throw new BizException("E104", "해지된 계좌로는 대출을 신청할 수 없습니다.");
        }
        if (principal < MIN_PRINCIPAL || principal > MAX_PRINCIPAL) {
            throw new BizException("E301", "대출금액은 100만원 이상 1억원 이하여야 합니다.");
        }
        if (termMonths < 3 || termMonths > 60) {
            throw new BizException("E302", "대출기간은 3~60개월이어야 합니다.");
        }

        LoanVO vo = new LoanVO();
        vo.setAcctNo(acctNo);
        vo.setPrincipal(principal);
        vo.setTermMonths(termMonths);
        vo.setRate(rateOf(termMonths));
        vo.setMonthlyPay(monthlyPay(principal, vo.getRate(), termMonths));
        loanDao.insertLoan(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decideLoan(long loanNo, boolean approve) {
        LoanVO loan = loanDao.selectLoan(loanNo);
        if (loan == null) {
            throw new BizException("E303", "존재하지 않는 대출건입니다.");
        }
        int updated = loanDao.updateStatus(loanNo, approve ? "A" : "X");
        if (updated == 0) {
            throw new BizException("E304", "이미 처리된 대출건입니다.");
        }
    }

    /** 기간별 금리(%) */
    private double rateOf(int termMonths) {
        if (termMonths <= 12) {
            return 4.5;
        }
        return termMonths <= 36 ? 5.2 : 5.9;
    }

    /** 원리금균등상환 월 납입액 */
    private long monthlyPay(long principal, double annualRate, int months) {
        double r = annualRate / 100 / 12;
        double pay = principal * r / (1 - Math.pow(1 + r, -months));
        return Math.round(pay);
    }
}
