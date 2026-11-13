/**
 * [방식 6-b] URL 상수 파일.
 * 화면마다 URL 문자열을 흩어 쓰지 않고 한 곳에서 관리한다.
 * 사용 전에 화면에서 var CTX = '${pageContext.request.contextPath}'; 를 먼저 선언해야 한다.
 * (브라우저 내장 URL 클래스와 겹치지 않도록 이름을 BANK_URL 로 함)
 */
var BANK_URL = {
    ACCOUNT_LIST:      '/sample/api/accounts',
    ACCOUNT_ONE:       '/sample/api/accounts/{acctNo}',      // {변수} 는 fn_url 이 치환
    CUSTOMER_LIST:     '/sample/api/customers',
    TRANSFER_FORM:     '/sample/api/transfer',
    TRANSFER_JSON:     '/sample/api/transfer-json',
    FRAGMENT_ACCOUNTS: '/sample/fragment/accounts',
    RESULT_PAGE:       '/sample/result'
};

/**
 * 키 + PathVariable 로 최종 URL 생성
 *   fn_url('ACCOUNT_ONE', {acctNo: '110-1001-0001'}) → /bank/sample/api/accounts/110-1001-0001
 */
function fn_url(key, pathVars) {
    var url = BANK_URL[key];
    if (!url) {
        throw new Error('정의되지 않은 URL 키: ' + key);
    }
    if (pathVars) {
        for (var name in pathVars) {
            if (Object.prototype.hasOwnProperty.call(pathVars, name)) {
                url = url.replace('{' + name + '}', encodeURIComponent(pathVars[name]));
            }
        }
    }
    return CTX + url;
}
