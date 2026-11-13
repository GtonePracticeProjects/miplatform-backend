<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- HTML 조각만 응답 (html/body 태그 없음). 화면에서 $('#tbody').load(url) 로 삽입 --%>
<c:forEach var="a" items="${accounts}">
<tr>
    <td>${a.acctNo}</td>
    <td><c:out value="${a.custNm}"/></td>
    <td class="num"><fmt:formatNumber value="${a.balance}" pattern="#,##0"/></td>
</tr>
</c:forEach>
