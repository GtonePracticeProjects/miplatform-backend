package com.bank.account.vo;

import java.io.Serializable;

/** 이체 요청 파라미터 */
public class TransferReqVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fromAcctNo;
    private String toAcctNo;
    private long amount;
    private String memo;

    public String getFromAcctNo() { return fromAcctNo; }
    public void setFromAcctNo(String fromAcctNo) { this.fromAcctNo = fromAcctNo; }
    public String getToAcctNo() { return toAcctNo; }
    public void setToAcctNo(String toAcctNo) { this.toAcctNo = toAcctNo; }
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
