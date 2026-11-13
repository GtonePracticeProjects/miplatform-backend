package com.bank.account.vo;

import java.io.Serializable;
import java.util.Date;

/** 계좌 (TB_ACCOUNT) */
public class AccountVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String acctNo;
    private String custNm;
    private long balance;
    /** A:정상, C:해지 */
    private String statusCd;
    private Date regDt;

    public String getAcctNo() { return acctNo; }
    public void setAcctNo(String acctNo) { this.acctNo = acctNo; }
    public String getCustNm() { return custNm; }
    public void setCustNm(String custNm) { this.custNm = custNm; }
    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }
    public String getStatusCd() { return statusCd; }
    public void setStatusCd(String statusCd) { this.statusCd = statusCd; }
    public Date getRegDt() { return regDt; }
    public void setRegDt(Date regDt) { this.regDt = regDt; }
}
