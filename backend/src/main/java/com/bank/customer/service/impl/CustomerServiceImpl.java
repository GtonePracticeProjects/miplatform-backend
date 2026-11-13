package com.bank.customer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.common.exception.BizException;
import com.bank.customer.dao.CustomerDao;
import com.bank.customer.service.CustomerService;
import com.bank.customer.vo.CustomerVO;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<CustomerVO> getCustomerList(String keyword) {
        return customerDao.selectCustomerList(keyword);
    }

    @Override
    public CustomerVO getCustomer(String custNo) {
        CustomerVO vo = customerDao.selectCustomer(custNo);
        if (vo == null) {
            throw new BizException("E201", "존재하지 않는 고객입니다. (" + custNo + ")");
        }
        return vo;
    }

    @Override
    public String registerCustomer(CustomerVO vo) {
        if (vo.getCustNm() == null || vo.getCustNm().trim().isEmpty()) {
            throw new BizException("E202", "고객명은 필수입니다.");
        }
        if (vo.getPhone() != null && !vo.getPhone().isEmpty()
                && !vo.getPhone().matches("\\d{2,3}-\\d{3,4}-\\d{4}")) {
            throw new BizException("E203", "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)");
        }
        vo.setCustNo(customerDao.nextCustNo());
        customerDao.insertCustomer(vo);
        return vo.getCustNo();
    }
}
