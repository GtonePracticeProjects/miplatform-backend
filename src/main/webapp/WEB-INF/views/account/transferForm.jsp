<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>계좌 이체</h2>
<form id="frm" method="post" action="${pageContext.request.contextPath}/account/transfer">
    <label>출금계좌</label>
    <select name="fromAcctNo">
        <c:forEach var="a" items="${accounts}">
            <option value="${a.acctNo}">${a.acctNo} (<c:out value="${a.custNm}"/>)</option>
        </c:forEach>
    </select>
    <label>입금계좌</label>
    <select name="toAcctNo">
        <c:forEach var="a" items="${accounts}">
            <option value="${a.acctNo}">${a.acctNo} (<c:out value="${a.custNm}"/>)</option>
        </c:forEach>
    </select>
    <label>이체금액 (원)</label>
    <input type="number" name="amount" min="1" required>
    <label>메모</label>
    <input type="text" name="memo" maxlength="50">
    <button type="submit">이체 실행</button>
</form>
<script>
    $(function () {
        $('#frm').on('submit', function () {
            var amt = Number($('input[name=amount]').val());
            return amt > 0 && confirm(amt.toLocaleString() + '원을 이체하시겠습니까?');
        });
    });
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
