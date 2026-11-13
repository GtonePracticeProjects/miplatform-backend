<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2 class="err">처리 중 오류가 발생했습니다</h2>
<p>오류코드: <c:out value="${errorCode}" default="-"/></p>
<p><c:out value="${errorMsg}" default="요청하신 페이지를 처리할 수 없습니다."/></p>
<p><a href="javascript:history.back()">뒤로가기</a></p>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
