<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<c:if test="${not empty msg}"><div class="msg"><c:out value="${msg}"/></div></c:if>
<h2>대출 현황</h2>
<form method="get" action="${pageContext.request.contextPath}/loan/list" style="display:flex;gap:8px">
    <select name="status">
        <option value="">전체</option>
        <option value="R" ${status == 'R' ? 'selected' : ''}>신청</option>
        <option value="A" ${status == 'A' ? 'selected' : ''}>승인</option>
        <option value="X" ${status == 'X' ? 'selected' : ''}>거절</option>
    </select>
    <button type="submit" style="margin:0">조회</button>
    <a href="${pageContext.request.contextPath}/loan/apply"><button type="button" style="margin:0">대출 신청</button></a>
</form>
<table>
    <thead><tr><th>번호</th><th>계좌</th><th class="num">대출금</th><th class="num">금리(%)</th><th class="num">기간(월)</th><th class="num">월 납입액</th><th>상태</th><th>신청일</th><th>처리</th></tr></thead>
    <tbody>
    <c:forEach var="l" items="${loans}">
        <tr>
            <td>${l.loanNo}</td>
            <td>${l.acctNo}</td>
            <td class="num"><fmt:formatNumber value="${l.principal}" pattern="#,##0"/></td>
            <td class="num">${l.rate}</td>
            <td class="num">${l.termMonths}</td>
            <td class="num"><fmt:formatNumber value="${l.monthlyPay}" pattern="#,##0"/></td>
            <td>${l.statusCd == 'R' ? '신청' : (l.statusCd == 'A' ? '승인' : '거절')}</td>
            <td><fmt:formatDate value="${l.reqDt}" pattern="yyyy-MM-dd"/></td>
            <td>
                <c:if test="${l.statusCd == 'R'}">
                    <form method="post" action="${pageContext.request.contextPath}/loan/${l.loanNo}/approve" style="display:inline">
                        <button type="submit" style="margin:0;padding:3px 8px">승인</button></form>
                    <form method="post" action="${pageContext.request.contextPath}/loan/${l.loanNo}/reject" style="display:inline">
                        <button type="submit" style="margin:0;padding:3px 8px;background:#c62828">거절</button></form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty loans}"><tr><td colspan="9">대출 내역이 없습니다.</td></tr></c:if>
    </tbody>
</table>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
