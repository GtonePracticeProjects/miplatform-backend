<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>공지 등록</h2>
<form method="post" action="${pageContext.request.contextPath}/notice/write">
    <label>제목</label><input type="text" name="title" maxlength="200" required>
    <label>작성자</label><input type="text" name="writer" maxlength="50" required>
    <label>내용</label><textarea name="content" rows="8" style="width:100%;box-sizing:border-box" maxlength="4000"></textarea>
    <button type="submit">등록</button>
</form>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
