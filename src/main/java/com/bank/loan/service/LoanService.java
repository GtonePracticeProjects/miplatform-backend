package com.bank.loan.service;

import java.util.List;

import com.bank.loan.vo.LoanVO;

public interface LoanService {

    List<LoanVO> getLoanList(String statusCd);

    /** 대출 신청 (금리/월납입액 산출 후 신청 상태로 저장) */
    void applyLoan(String acctNo, long principal, int termMonths);

    /** 승인(A) 또는 거절(X) */
    void decideLoan(long loanNo, boolean approve);
}
