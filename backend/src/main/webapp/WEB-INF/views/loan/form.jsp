<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>대출 신청</h2>
<form method="post" action="${pageContext.request.contextPath}/loan/apply">
    <label>대출 계좌</label>
    <select name="acctNo">
        <c:forEach var="a" items="${accounts}">
            <option value="${a.acctNo}">${a.acctNo} (<c:out value="${a.custNm}"/>)</option>
        </c:forEach>
    </select>
    <label>대출금액 (100만원 ~ 1억원)</label>
    <input type="number" name="principal" min="1000000" max="100000000" step="10000" required>
    <label>대출기간 (개월, 3~60)</label>
    <input type="number" name="termMonths" min="3" max="60" required>
    <button type="submit">신청</button>
</form>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
