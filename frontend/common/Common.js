// =====================================================================
// Common.js - 공통 스크립트 (각 화면에서 include "common/Common.js"; 로 사용)
// MiPlatform 3.x 가정. MiPlatform 에서 열어 검증하지 않은 텍스트 소스.
// =====================================================================

// 서버 기본 URL (배포 환경에 맞게 변경)
var gv_svcUrl = "http://localhost:8080/bank";

// 메뉴 정보: 메뉴ID -> 서비스 경로.
// 실제 프로젝트에서는 로그인 시 서버에서 내려받은 메뉴 Dataset(ds_menu)을 쓰는 경우가 많다.
// 여기서는 단순화를 위해 스크립트 상수 배열로 둔다.
var gv_menuUrl = new Object();
gv_menuUrl["ACCT_LIST"]   = "/miplatform/account/list";
gv_menuUrl["LOAN_DECIDE"] = "/miplatform/loan/decide";

// 메뉴ID 로 서비스 경로를 조회한다. (호출 URL 이 정적 문자열이 아니라 변수에서 결정됨)
function gfn_getMenuUrl(strMenuId)
{
    return gv_menuUrl[strMenuId];
}

/**
 * 공통 Transaction 래퍼.
 * @param strSvcId    서비스ID (콜백에서 어떤 호출인지 구분)
 * @param strPath     서버 경로 (예: "/miplatform/account/transfer")
 * @param strInDs     서버로 보낼 Dataset 매핑  ("서버명=클라이언트명")
 * @param strOutDs    서버에서 받을 Dataset 매핑 ("클라이언트명=서버명")
 * @param strArgs     추가 변수 ("name=value ...")
 * @param strCallback 완료 콜백 함수명  function xxx(svcId, errorCode, errorMsg)
 */
function gfn_transaction(strSvcId, strPath, strInDs, strOutDs, strArgs, strCallback)
{
    Transaction(strSvcId, gv_svcUrl + strPath, strInDs, strOutDs, strArgs, strCallback);
}

// 공통 오류 처리: 콜백 첫머리에서 호출해 오류면 true 를 돌려준다.
// 서버는 ErrorCode(숫자, 0=정상) / ErrorMsg 를 내려준다. 업무 코드(E###)는 BizCode 변수.
function gfn_isError(nErrorCode, strErrorMsg)
{
    if (nErrorCode != 0) {
        alert(strErrorMsg);
        return true;
    }
    return false;
}
