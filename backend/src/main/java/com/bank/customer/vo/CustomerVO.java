package com.bank.customer.vo;

import java.io.Serializable;
import java.util.Date;

/** 고객 (TB_CUSTOMER) */
public class CustomerVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String custNo;
    private String custNm;
    private String phone;
    private String email;
    private Date regDt;

    public String getCustNo() { return custNo; }
    public void setCustNo(String custNo) { this.custNo = custNo; }
    public String getCustNm() { return custNm; }
    public void setCustNm(String custNm) { this.custNm = custNm; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getRegDt() { return regDt; }
    public void setRegDt(Date regDt) { this.regDt = regDt; }
}
