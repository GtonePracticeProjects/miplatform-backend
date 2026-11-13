/**
 * [방식 6-a] 공통 JS 호출 래퍼 (jQuery 필요, urls.js 선행 로딩).
 * 로딩 표시, 에러 메시지, 응답 포맷(ApiResult) 처리를 한 곳에서 담당한다.
 * 실제 프로젝트에서는 여기에 CSRF 토큰, 세션 만료 처리 등을 추가한다.
 */
var Bank = (function ($) {

    function showLoading(on) {
        $('#loading').toggle(!!on);
    }

    /**
     * 공통 Ajax
     * opts: { urlKey | url, pathVars, method, data, json(true면 JSON 본문), success(data), error(result), loading }
     */
    function ajax(opts) {
        var url = opts.url || fn_url(opts.urlKey, opts.pathVars);
        var method = (opts.method || 'GET').toUpperCase();
        var useLoading = opts.loading !== false;

        var settings = {
            url: url,
            type: method,
            dataType: 'json',
            headers: {'X-Requested-With': 'XMLHttpRequest'}
        };
        if (opts.json) {
            settings.contentType = 'application/json; charset=UTF-8';
            settings.data = JSON.stringify(opts.data || {});
        } else {
            settings.data = opts.data || {};
        }

        if (useLoading) { showLoading(true); }
        $.ajax(settings)
            .done(function (res) {
                if (res && res.success === false) {
                    handleError(res, opts);
                } else if (opts.success) {
                    opts.success(res ? res.data : null, res);
                }
            })
            .fail(function (xhr) {
                var res = xhr.responseJSON || {code: 'E000', message: '서버와 통신할 수 없습니다. (' + xhr.status + ')'};
                handleError(res, opts);
            })
            .always(function () {
                if (useLoading) { showLoading(false); }
            });
    }

    function handleError(res, opts) {
        if (opts.error) {
            opts.error(res);
        } else {
            alert('[' + res.code + '] ' + res.message);
        }
    }

    /** GET + 쿼리스트링 */
    function get(urlKey, params, success, pathVars) {
        ajax({urlKey: urlKey, pathVars: pathVars, method: 'GET', data: params, success: success});
    }

    /** POST (form 방식) */
    function post(urlKey, params, success) {
        ajax({urlKey: urlKey, method: 'POST', data: params, success: success});
    }

    /** POST (JSON 본문) */
    function postJson(urlKey, params, success) {
        ajax({urlKey: urlKey, method: 'POST', data: params, json: true, success: success});
    }

    /** 화면 이동 (GET). 값은 자동 인코딩 */
    function goPage(urlKey, params) {
        var qs = $.param(params || {});
        location.href = fn_url(urlKey) + (qs ? '?' + qs : '');
    }

    /** 숨은 form 을 만들어 POST 이동 (값이 URL 에 노출되지 않음) */
    function submitPage(urlKey, params) {
        var $f = $('<form>', {method: 'post', action: fn_url(urlKey)});
        $.each(params || {}, function (k, v) {
            $('<input>', {type: 'hidden', name: k, value: v}).appendTo($f);
        });
        $f.appendTo('body').submit();
    }

    return {ajax: ajax, get: get, post: post, postJson: postJson, goPage: goPage, submitPage: submitPage};
})(jQuery);
