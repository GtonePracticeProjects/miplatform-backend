package com.bank.account.vo;

import java.io.Serializable;
import java.util.Date;

/** 거래내역 (TB_TRANS_HIST) */
public class TransHistVO implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String TP_WITHDRAW = "W";
    public static final String TP_DEPOSIT = "D";

    private long transSeq;
    private String acctNo;
    /** W:출금, D:입금 */
    private String transTp;
    private long amount;
    private long balanceAfter;
    private String counterAcctNo;
    private String memo;
    private Date transDt;

    public long getTransSeq() { return transSeq; }
    public void setTransSeq(long transSeq) { this.transSeq = transSeq; }
    public String getAcctNo() { return acctNo; }
    public void setAcctNo(String acctNo) { this.acctNo = acctNo; }
    public String getTransTp() { return transTp; }
    public void setTransTp(String transTp) { this.transTp = transTp; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public long getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(long balanceAfter) { this.balanceAfter = balanceAfter; }
    public String getCounterAcctNo() { return counterAcctNo; }
    public void setCounterAcctNo(String counterAcctNo) { this.counterAcctNo = counterAcctNo; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
    public Date getTransDt() { return transDt; }
    public void setTransDt(Date transDt) { this.transDt = transDt; }
}
