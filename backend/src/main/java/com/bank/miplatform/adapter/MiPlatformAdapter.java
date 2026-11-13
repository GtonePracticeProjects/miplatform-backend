package com.bank.miplatform.adapter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MiPlatform 통신(Dataset/VariableList XML) 변환을 한 곳에 모은 어댑터.
 * Controller 는 이 인터페이스만 사용한다. 벤더 서버 라이브러리(com.tobesoft.platform 계열)를
 * 쓰려면 이 인터페이스의 구현체를 새로 만들어 Spring 빈으로 등록하면 된다 (README 참고).
 */
public interface MiPlatformAdapter {

    /** HTTP 요청 본문(MiPlatform XML)을 중립 모델로 변환 */
    MiRequest read(HttpServletRequest request) throws IOException;

    /** 중립 모델을 MiPlatform XML 로 HTTP 응답에 기록 */
    void write(HttpServletResponse response, MiResponse data) throws IOException;
}
