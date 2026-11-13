package com.bank.loan.vo;

import java.io.Serializable;
import java.util.Date;

/** 대출 (TB_LOAN). statusCd: R=신청, A=승인, X=거절 */
public class LoanVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long loanNo;
    private String acctNo;
    private long principal;
    private double rate;
    private int termMonths;
    private long monthlyPay;
    private String statusCd;
    private Date reqDt;

    public long getLoanNo() { return loanNo; }
    public void setLoanNo(long loanNo) { this.loanNo = loanNo; }
    public String getAcctNo() { return acctNo; }
    public void setAcctNo(String acctNo) { this.acctNo = acctNo; }
    public long getPrincipal() { return principal; }
    public void setPrincipal(long principal) { this.principal = principal; }
    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }
    public long getMonthlyPay() { return monthlyPay; }
    public void setMonthlyPay(long monthlyPay) { this.monthlyPay = monthlyPay; }
    public String getStatusCd() { return statusCd; }
    public void setStatusCd(String statusCd) { this.statusCd = statusCd; }
    public Date getReqDt() { return reqDt; }
    public void setReqDt(Date reqDt) { this.reqDt = reqDt; }
}
