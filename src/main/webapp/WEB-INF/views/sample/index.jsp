<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>프론트 → 백엔드 호출 방식 샘플</h2>
<table>
    <thead><tr><th>구분</th><th>방식</th><th>어디서 확인</th></tr></thead>
    <tbody>
    <tr><td>1~3</td><td>고정 URL / URL+변수 / form 제출 (페이지 이동형)</td>
        <td>기존 화면: 메뉴, 계좌 상세(PathVariable), 고객 검색(쿼리스트링), 이체·등록 form</td></tr>
    <tr><td>4</td><td>jQuery Ajax 변형 5가지</td><td><a href="${pageContext.request.contextPath}/sample/ajax">/sample/ajax</a></td></tr>
    <tr><td>5</td><td>fetch (순수 JS)</td><td><a href="${pageContext.request.contextPath}/sample/fetch">/sample/fetch</a></td></tr>
    <tr><td>6</td><td>공통 JS 래퍼 + URL 상수 파일</td><td><a href="${pageContext.request.contextPath}/sample/common">/sample/common</a></td></tr>
    </tbody>
</table>
<p>※ 이체 버튼은 실제 DB 의 잔액을 변경합니다. (110-1001-0001 → 110-1001-0002)</p>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
