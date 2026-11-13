package com.bank.miplatform;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bank.common.exception.BizException;
import com.bank.miplatform.adapter.MiPlatformAdapter;
import com.bank.miplatform.adapter.MiResponse;

/**
 * /miplatform/** Controller 의 예외를 HTTP 200 + ErrorCode/ErrorMsg 변수로 응답한다.
 * (MiPlatform 클라이언트는 콜백의 nErrorCode 로 오류를 판단하므로 JSON/에러 JSP 를 쓰지 않는다.)
 */
@ControllerAdvice(basePackages = "com.bank.miplatform.controller")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MiExceptionHandler.class);

    @Autowired
    private MiPlatformAdapter adapter;

    @ExceptionHandler(BizException.class)
    public void handleBiz(BizException e, HttpServletResponse res) throws IOException {
        log.warn("업무 오류 [{}] {}", e.getErrorCode(), e.getMessage());
        // MiPlatform 의 ErrorCode 는 숫자여야 하므로 -1 로 내리고 업무 코드(E###)는 BizCode 변수로 전달
        adapter.write(res, MiResponse.fail("-1", e.getMessage()).variable("BizCode", e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public void handleEtc(Exception e, HttpServletResponse res) throws IOException {
        log.error("시스템 오류", e);
        adapter.write(res, MiResponse.fail("-2", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.").variable("BizCode", "E999"));
    }
}
