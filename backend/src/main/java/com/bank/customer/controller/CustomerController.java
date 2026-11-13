package com.bank.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bank.customer.service.CustomerService;
import com.bank.customer.vo.CustomerVO;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("customers", customerService.getCustomerList(keyword));
        model.addAttribute("keyword", keyword);
        return "customer/list";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm() {
        return "customer/form";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(CustomerVO vo, RedirectAttributes rttr) {
        String custNo = customerService.registerCustomer(vo);
        rttr.addFlashAttribute("msg", "고객이 등록되었습니다. 고객번호: " + custNo);
        return "redirect:/customer/list";
    }
}
