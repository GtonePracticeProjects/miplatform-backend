<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2><c:out value="${notice.title}"/></h2>
<p>작성자: <c:out value="${notice.writer}"/> / 조회 ${notice.viewCnt} /
    <fmt:formatDate value="${notice.regDt}" pattern="yyyy-MM-dd HH:mm"/></p>
<hr>
<div style="white-space:pre-wrap"><c:out value="${notice.content}"/></div>
<p><a href="${pageContext.request.contextPath}/notice/list">목록</a></p>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
