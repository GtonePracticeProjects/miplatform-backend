<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>고객 등록</h2>
<form method="post" action="${pageContext.request.contextPath}/customer/register">
    <label>고객명</label><input type="text" name="custNm" maxlength="50" required>
    <label>전화번호</label><input type="text" name="phone" placeholder="010-1234-5678">
    <label>이메일</label><input type="email" name="email">
    <button type="submit">등록</button>
</form>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
