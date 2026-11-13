<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>계좌 목록</h2>
<table>
    <thead>
    <tr><th>계좌번호</th><th>예금주</th><th class="num">잔액</th><th>상태</th><th>잔액조회(Ajax)</th></tr>
    </thead>
    <tbody>
    <c:forEach var="a" items="${accounts}">
        <tr>
            <td><a href="${pageContext.request.contextPath}/account/${a.acctNo}">${a.acctNo}</a></td>
            <td><c:out value="${a.custNm}"/></td>
            <td class="num"><fmt:formatNumber value="${a.balance}" pattern="#,##0"/> 원</td>
            <td>${a.statusCd == 'A' ? '정상' : '해지'}</td>
            <td><button type="button" class="btn-bal" data-acct="${a.acctNo}" style="margin:0;padding:3px 10px">조회</button></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<script>
    $(function () {
        $('.btn-bal').on('click', function () {
            var acct = $(this).data('acct');
            $.getJSON('${pageContext.request.contextPath}/account/api/' + acct, function (r) {
                alert(r.custNm + ' 님 현재 잔액: ' + r.balance.toLocaleString() + ' 원');
            });
        });
    });
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
