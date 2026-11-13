package com.bank.sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bank.account.service.AccountService;
import com.bank.account.vo.AccountVO;
import com.bank.account.vo.TransferReqVO;
import com.bank.common.ApiResult;
import com.bank.customer.service.CustomerService;
import com.bank.customer.vo.CustomerVO;

/**
 * 프론트 → 백엔드 호출 방식 비교용 샘플.
 *  - /sample/*            : 데모 화면(JSP)
 *  - /sample/api/*        : JSON 응답 API (ApiResult 포맷)
 *  - /sample/fragment/*   : HTML 조각 응답 (서버 렌더링 조각을 화면에 끼워 넣는 방식)
 * 기존 Service 를 재사용하며, 비즈니스 로직은 두지 않는다.
 */
@Controller
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;

    // ---------- 화면 ----------
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "sample/index";
    }

    @RequestMapping(value = "/ajax", method = RequestMethod.GET)
    public String ajax() {
        return "sample/ajax";
    }

    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public String fetch() {
        return "sample/fetch";
    }

    @RequestMapping(value = "/common", method = RequestMethod.GET)
    public String common() {
        return "sample/common";
    }

    /** 공통 JS 의 fn_goPage / fn_submit 이 이동해 오는 결과 확인용 화면 */
    @RequestMapping(value = "/result", method = {RequestMethod.GET, RequestMethod.POST})
    public String result(@RequestParam(value = "acctNo", required = false) String acctNo,
                         @RequestParam(value = "via", required = false) String via, Model model) {
        model.addAttribute("acctNo", acctNo);
        model.addAttribute("via", via);
        return "sample/result";
    }

    // ---------- JSON API ----------
    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult<List<AccountVO>> accounts() {
        return ApiResult.ok(accountService.getAccountList());
    }

    /** PathVariable 로 변수 전달 */
    @RequestMapping(value = "/api/accounts/{acctNo}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult<AccountVO> account(@PathVariable("acctNo") String acctNo) {
        return ApiResult.ok(accountService.getAccount(acctNo));
    }

    /** 쿼리스트링(요청 파라미터)으로 변수 전달 */
    @RequestMapping(value = "/api/customers", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult<List<CustomerVO>> customers(@RequestParam(value = "keyword", required = false) String keyword) {
        return ApiResult.ok(customerService.getCustomerList(keyword));
    }

    /** form 방식 본문 (application/x-www-form-urlencoded) → 모델 어트리뷰트 바인딩 */
    @RequestMapping(value = "/api/transfer", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<Void> transferForm(TransferReqVO req) {
        accountService.transfer(req);
        return ApiResult.ok(null);
    }

    /** JSON 본문 (application/json) → @RequestBody 바인딩 */
    @RequestMapping(value = "/api/transfer-json", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<Void> transferJson(@RequestBody TransferReqVO req) {
        accountService.transfer(req);
        return ApiResult.ok(null);
    }

    // ---------- HTML 조각 ----------
    @RequestMapping(value = "/fragment/accounts", method = RequestMethod.GET)
    public String accountRows(Model model) {
        model.addAttribute("accounts", accountService.getAccountList());
        return "sample/fragment/accountRows";
    }
}
