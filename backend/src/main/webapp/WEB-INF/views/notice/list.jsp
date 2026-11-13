<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<c:if test="${not empty msg}"><div class="msg"><c:out value="${msg}"/></div></c:if>
<h2>공지사항</h2>
<a href="${pageContext.request.contextPath}/notice/write"><button type="button" style="margin:0">글쓰기</button></a>
<table>
    <thead><tr><th>번호</th><th>제목</th><th>작성자</th><th class="num">조회</th><th>등록일</th></tr></thead>
    <tbody>
    <c:forEach var="n" items="${notices}">
        <tr>
            <td>${n.noticeSeq}</td>
            <td><a href="${pageContext.request.contextPath}/notice/${n.noticeSeq}"><c:out value="${n.title}"/></a></td>
            <td><c:out value="${n.writer}"/></td>
            <td class="num">${n.viewCnt}</td>
            <td><fmt:formatDate value="${n.regDt}" pattern="yyyy-MM-dd"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
