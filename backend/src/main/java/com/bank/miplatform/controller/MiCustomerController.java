package com.bank.miplatform.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bank.customer.service.CustomerService;
import com.bank.customer.vo.CustomerVO;
import com.bank.miplatform.adapter.MiDataset;
import com.bank.miplatform.adapter.MiDatasets;
import com.bank.miplatform.adapter.MiPlatformAdapter;
import com.bank.miplatform.adapter.MiResponse;

/** MiPlatform 고객 화면용 진입점. 기존 CustomerService 를 그대로 재사용한다. */
@Controller
@RequestMapping("/miplatform/customer")
public class MiCustomerController {

    @Autowired
    private MiPlatformAdapter adapter;
    @Autowired
    private CustomerService customerService;

    /** in: ds_search(keyword) / out: ds_customer */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public void search(HttpServletRequest req, HttpServletResponse res) throws IOException {
        MiDataset cond = adapter.read(req).getDataset("ds_search");
        String keyword = cond == null ? null : cond.getString(0, "keyword");
        MiResponse out = MiResponse.ok();
        out.dataset(MiDatasets.fromBeans("ds_customer", CustomerVO.class, customerService.getCustomerList(keyword)));
        adapter.write(res, out);
    }

    /** in: ds_customer(custNm,phone,email) / out: 변수 custNo */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(HttpServletRequest req, HttpServletResponse res) throws IOException {
        MiDataset ds = adapter.read(req).getDataset("ds_customer");
        CustomerVO vo = new CustomerVO();
        vo.setCustNm(ds.getString(0, "custNm"));
        vo.setPhone(ds.getString(0, "phone"));
        vo.setEmail(ds.getString(0, "email"));
        String custNo = customerService.registerCustomer(vo);
        adapter.write(res, MiResponse.ok().variable("custNo", custNo));
    }
}
