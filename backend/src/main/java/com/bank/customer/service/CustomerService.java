package com.bank.customer.service;

import java.util.List;

import com.bank.customer.vo.CustomerVO;

public interface CustomerService {

    List<CustomerVO> getCustomerList(String keyword);

    CustomerVO getCustomer(String custNo);

    /** 신규 고객 등록, 채번된 고객번호 반환 */
    String registerCustomer(CustomerVO vo);
}
