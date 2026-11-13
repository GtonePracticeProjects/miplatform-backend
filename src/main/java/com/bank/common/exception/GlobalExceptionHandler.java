package com.bank.common.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.bank.common.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 일반 화면 요청 → 에러 JSP, Ajax/fetch(JSON) 요청 → ApiResult JSON 으로 응답한다.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @ExceptionHandler(BizException.class)
    public ModelAndView handleBiz(BizException e, HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.warn("업무 오류 [{}] {}", e.getErrorCode(), e.getMessage());
        return respond(req, res, 400, e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleEtc(Exception e, HttpServletRequest req, HttpServletResponse res) throws IOException {
        log.error("시스템 오류", e);
        return respond(req, res, 500, "E999", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    }

    private ModelAndView respond(HttpServletRequest req, HttpServletResponse res, int status,
                                 String code, String msg) throws IOException {
        if (isAjax(req)) {
            res.setStatus(status);
            res.setContentType("application/json;charset=UTF-8");
            MAPPER.writeValue(res.getWriter(), ApiResult.fail(code, msg));
            return null; // 응답을 직접 작성했으므로 View 렌더링 없음
        }
        ModelAndView mav = new ModelAndView("common/error");
        mav.addObject("errorCode", code);
        mav.addObject("errorMsg", msg);
        return mav;
    }

    private boolean isAjax(HttpServletRequest req) {
        String xhr = req.getHeader("X-Requested-With");
        String accept = req.getHeader("Accept");
        return "XMLHttpRequest".equals(xhr)
                || (accept != null && accept.contains("application/json"))
                || req.getRequestURI().contains("/api/");
    }
}
