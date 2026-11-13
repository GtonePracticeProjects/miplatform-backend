<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>[방식 5] fetch (순수 JS, jQuery 미사용)</h2>
<p>이 화면의 호출 코드는 jQuery 를 쓰지 않습니다. (header 의 jQuery 는 로드되어 있지만 사용하지 않음)</p>

<button type="button" id="b1">5-1. GET 목록</button>
<button type="button" id="b2">5-2. GET + URLSearchParams</button>
<button type="button" id="b3">5-3. POST form 본문</button>
<button type="button" id="b4">5-4. POST JSON 본문</button>
<button type="button" id="b5">5-5. 잘못된 계좌 (오류 처리)</button>

<h3>로그</h3>
<pre id="log" style="background:#111;color:#0f0;padding:10px;min-height:80px;white-space:pre-wrap"></pre>

<script>
    var CTX = '${pageContext.request.contextPath}';
    var API = CTX + '/sample/api';

    function log(title, obj) {
        var el = document.getElementById('log');
        el.textContent = '[' + title + '] ' + (typeof obj === 'string' ? obj : JSON.stringify(obj)) + '\n' + el.textContent;
    }

    /** fetch 는 HTTP 400/500 에서도 reject 하지 않으므로 res.ok 를 직접 검사해야 한다 */
    async function call(url, options) {
        var res = await fetch(url, options);
        var body = await res.json();
        if (!res.ok || body.success === false) {
            throw body;
        }
        return body;
    }

    document.getElementById('b1').addEventListener('click', async function () {
        try {
            var r = await call(API + '/accounts', {headers: {'Accept': 'application/json'}});
            log('5-1', r.data.length + '건');
        } catch (e) { log('5-1 오류', e); }
    });

    document.getElementById('b2').addEventListener('click', async function () {
        var qs = new URLSearchParams({keyword: '이'});           // 인코딩 자동 처리
        try {
            var r = await call(API + '/customers?' + qs.toString());
            log('5-2', r.data);
        } catch (e) { log('5-2 오류', e); }
    });

    document.getElementById('b3').addEventListener('click', async function () {
        var body = new URLSearchParams({fromAcctNo: '110-1001-0001', toAcctNo: '110-1001-0002', amount: '1000', memo: 'fetch-form'});
        try {
            var r = await call(API + '/transfer', {method: 'POST', body: body});   // Content-Type 자동 지정
            log('5-3', r);
        } catch (e) { log('5-3 오류', e); }
    });

    document.getElementById('b4').addEventListener('click', async function () {
        try {
            var r = await call(API + '/transfer-json', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({fromAcctNo: '110-1001-0001', toAcctNo: '110-1001-0002', amount: 3000, memo: 'fetch-json'})
            });
            log('5-4', r);
        } catch (e) { log('5-4 오류', e); }
    });

    document.getElementById('b5').addEventListener('click', async function () {
        try {
            await call(API + '/accounts/' + encodeURIComponent('999-NOT-EXIST'));
        } catch (e) { log('5-5 오류(정상 동작)', e); }
    });
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
