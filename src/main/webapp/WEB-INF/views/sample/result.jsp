<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>화면 이동 결과</h2>
<p>호출 방식: <strong><c:out value="${via}"/></strong></p>
<p>전달받은 acctNo: <strong><c:out value="${acctNo}"/></strong></p>
<p>브라우저 주소창의 URL 을 확인해 보세요. (GET 은 값이 노출, POST 는 노출되지 않음)</p>
<p><a href="${pageContext.request.contextPath}/sample">샘플 목록으로</a></p>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
