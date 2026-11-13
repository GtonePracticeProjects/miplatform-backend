<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<c:if test="${not empty msg}"><div class="msg"><c:out value="${msg}"/></div></c:if>
<h2>고객 관리</h2>
<form method="get" action="${pageContext.request.contextPath}/customer/list" style="display:flex;gap:8px">
    <input type="text" name="keyword" value="<c:out value='${keyword}'/>" placeholder="고객명 검색">
    <button type="submit" style="margin:0">검색</button>
    <a href="${pageContext.request.contextPath}/customer/register"><button type="button" style="margin:0">신규 등록</button></a>
</form>
<table>
    <thead><tr><th>고객번호</th><th>고객명</th><th>전화번호</th><th>이메일</th><th>등록일</th></tr></thead>
    <tbody>
    <c:forEach var="c" items="${customers}">
        <tr>
            <td>${c.custNo}</td>
            <td><c:out value="${c.custNm}"/></td>
            <td><c:out value="${c.phone}"/></td>
            <td><c:out value="${c.email}"/></td>
            <td><fmt:formatDate value="${c.regDt}" pattern="yyyy-MM-dd"/></td>
        </tr>
    </c:forEach>
    <c:if test="${empty customers}"><tr><td colspan="5">조회 결과가 없습니다.</td></tr></c:if>
    </tbody>
</table>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
