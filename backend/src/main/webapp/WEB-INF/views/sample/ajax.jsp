<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<h2>[방식 4] jQuery Ajax 변형</h2>
<p>각 버튼은 같은 백엔드를 다른 방식으로 호출합니다. 결과는 아래 로그 영역에 출력됩니다.</p>

<h3>4-1. $.getJSON + PathVariable</h3>
<input type="text" id="acctNo" value="110-1001-0001" style="width:240px">
<button type="button" id="btn1">조회</button>

<h3>4-2. $.get + 데이터 객체(쿼리스트링) → JS 로 DOM 생성</h3>
<input type="text" id="keyword" value="김" style="width:240px">
<button type="button" id="btn2">고객 검색</button>
<table><thead><tr><th>고객번호</th><th>고객명</th><th>전화</th></tr></thead><tbody id="custBody"></tbody></table>

<h3>4-3. $.post + form.serialize() (x-www-form-urlencoded)</h3>
<form id="frmTransfer">
    출금 <input type="text" name="fromAcctNo" value="110-1001-0001" style="width:160px">
    입금 <input type="text" name="toAcctNo" value="110-1001-0002" style="width:160px">
    금액 <input type="number" name="amount" value="1000" style="width:100px">
    <input type="hidden" name="memo" value="ajax-form">
    <button type="button" id="btn3">이체(form)</button>
</form>

<h3>4-4. $.ajax + JSON 본문 (contentType: application/json)</h3>
<button type="button" id="btn4">이체(JSON, 2,000원)</button>

<h3>4-5. HTML 조각 응답 → .load() 로 삽입</h3>
<button type="button" id="btn5">계좌 목록 불러오기</button>
<table><thead><tr><th>계좌번호</th><th>예금주</th><th class="num">잔액</th></tr></thead><tbody id="acctBody"></tbody></table>

<h3>로그</h3>
<pre id="log" style="background:#111;color:#0f0;padding:10px;min-height:80px;white-space:pre-wrap"></pre>

<script>
    var CTX = '${pageContext.request.contextPath}';
    var API = CTX + '/sample';

    function log(title, obj) {
        $('#log').prepend('[' + title + '] ' + (typeof obj === 'string' ? obj : JSON.stringify(obj)) + '\n');
    }
    function onFail(title) {
        return function (xhr) {
            log(title + ' 실패 HTTP ' + xhr.status, xhr.responseJSON || xhr.responseText);
        };
    }

    $(function () {
        // 4-1
        $('#btn1').on('click', function () {
            var acct = encodeURIComponent($('#acctNo').val());
            $.getJSON(API + '/api/accounts/' + acct, function (res) {
                log('4-1 getJSON', res);
            }).fail(onFail('4-1'));
        });

        // 4-2
        $('#btn2').on('click', function () {
            $.get(API + '/api/customers', {keyword: $('#keyword').val()}, function (res) {
                var html = '';
                $.each(res.data, function (i, c) {
                    html += '<tr><td>' + c.custNo + '</td><td>' + $('<div>').text(c.custNm).html() +
                            '</td><td>' + (c.phone || '') + '</td></tr>';
                });
                $('#custBody').html(html);
                log('4-2 get', res.data.length + '건');
            }, 'json').fail(onFail('4-2'));
        });

        // 4-3
        $('#btn3').on('click', function () {
            $.post(API + '/api/transfer', $('#frmTransfer').serialize(), function (res) {
                log('4-3 post(form)', res);
            }, 'json').fail(onFail('4-3'));
        });

        // 4-4
        $('#btn4').on('click', function () {
            $.ajax({
                url: API + '/api/transfer-json',
                type: 'POST',
                contentType: 'application/json; charset=UTF-8',
                dataType: 'json',
                data: JSON.stringify({fromAcctNo: '110-1001-0001', toAcctNo: '110-1001-0002', amount: 2000, memo: 'ajax-json'})
            }).done(function (res) {
                log('4-4 ajax(JSON)', res);
            }).fail(onFail('4-4'));
        });

        // 4-5
        $('#btn5').on('click', function () {
            $('#acctBody').load(API + '/fragment/accounts', function (resp, status, xhr) {
                log('4-5 load', status + ' (' + xhr.status + ')');
            });
        });
    });
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
