<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>[방식 6] 공통 JS 래퍼 + URL 상수 파일</h2>
<p>이 화면에는 URL 문자열이 한 줄도 없습니다. <code>resources/js/urls.js</code> 의 키와
    <code>resources/js/common.js</code> 의 <code>Bank.*</code> 함수만 사용합니다.</p>
<div id="loading" style="display:none;color:#0b3d91">처리 중...</div>

<button type="button" id="c1">6-1. Bank.get (목록)</button>
<button type="button" id="c2">6-2. Bank.get + pathVars</button>
<button type="button" id="c3">6-3. Bank.post (form)</button>
<button type="button" id="c4">6-4. Bank.postJson</button>
<button type="button" id="c5">6-5. 오류 공통 처리</button>
<button type="button" id="c6">6-6. 화면이동 GET (goPage)</button>
<button type="button" id="c7">6-7. 화면이동 POST (submitPage)</button>

<h3>로그</h3>
<pre id="log" style="background:#111;color:#0f0;padding:10px;min-height:80px;white-space:pre-wrap"></pre>

<script>var CTX = '${pageContext.request.contextPath}';</script>
<script src="${pageContext.request.contextPath}/resources/js/urls.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/common.js"></script>
<script>
    function log(title, obj) {
        $('#log').prepend('[' + title + '] ' + (typeof obj === 'string' ? obj : JSON.stringify(obj)) + '\n');
    }
    var transferParams = {fromAcctNo: '110-1001-0001', toAcctNo: '110-1001-0002', amount: 1000, memo: 'common-js'};

    $(function () {
        $('#c1').on('click', function () {
            Bank.get('ACCOUNT_LIST', null, function (data) { log('6-1', data.length + '건'); });
        });
        $('#c2').on('click', function () {
            Bank.get('ACCOUNT_ONE', null, function (data) { log('6-2', data); }, {acctNo: '110-1001-0001'});
        });
        $('#c3').on('click', function () {
            Bank.post('TRANSFER_FORM', transferParams, function () { log('6-3', '이체 완료'); });
        });
        $('#c4').on('click', function () {
            Bank.postJson('TRANSFER_JSON', $.extend({}, transferParams, {amount: 2000}), function () { log('6-4', '이체 완료(JSON)'); });
        });
        $('#c5').on('click', function () {
            // error 콜백을 안 주면 공통 alert 로 처리됨
            Bank.get('ACCOUNT_ONE', null, null, {acctNo: '999-NOT-EXIST'});
        });
        $('#c6').on('click', function () {
            Bank.goPage('RESULT_PAGE', {acctNo: '110-1001-0001', via: 'goPage(GET)'});
        });
        $('#c7').on('click', function () {
            Bank.submitPage('RESULT_PAGE', {acctNo: '110-1001-0001', via: 'submitPage(POST)'});
        });
    });
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
