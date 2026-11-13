<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<c:if test="${not empty msg}"><div class="msg"><c:out value="${msg}"/></div></c:if>
<h2>계좌 상세</h2>
<p>
    계좌번호: <strong>${account.acctNo}</strong> /
    예금주: <c:out value="${account.custNm}"/> /
    잔액: <strong><fmt:formatNumber value="${account.balance}" pattern="#,##0"/> 원</strong>
</p>

<h3>거래내역</h3>
<table>
    <thead>
    <tr><th>일시</th><th>구분</th><th>상대계좌</th><th class="num">금액</th><th class="num">거래후잔액</th><th>메모</th></tr>
    </thead>
    <tbody>
    <c:forEach var="h" items="${histList}">
        <tr>
            <td><fmt:formatDate value="${h.transDt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${h.transTp == 'W' ? '출금' : '입금'}</td>
            <td>${h.counterAcctNo}</td>
            <td class="num"><fmt:formatNumber value="${h.amount}" pattern="#,##0"/></td>
            <td class="num"><fmt:formatNumber value="${h.balanceAfter}" pattern="#,##0"/></td>
            <td><c:out value="${h.memo}"/></td>
        </tr>
    </c:forEach>
    <c:if test="${empty histList}"><tr><td colspan="6">거래내역이 없습니다.</td></tr></c:if>
    </tbody>
</table>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
