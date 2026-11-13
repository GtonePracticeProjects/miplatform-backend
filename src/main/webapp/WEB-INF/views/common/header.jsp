<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Bank Core Web</title>
    <script src="${pageContext.request.contextPath}/webjars/jquery/3.7.1/jquery.min.js"></script>
    <style>
        body { font-family: "Malgun Gothic", sans-serif; margin: 0; background: #f4f6f9; color: #222; }
        header { background: #0b3d91; color: #fff; padding: 14px 24px; }
        header a { color: #fff; margin-right: 18px; text-decoration: none; }
        main { max-width: 900px; margin: 24px auto; background: #fff; padding: 24px; border-radius: 6px; }
        table { width: 100%; border-collapse: collapse; margin-top: 12px; }
        th, td { border-bottom: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background: #eef2f8; }
        td.num, th.num { text-align: right; }
        .msg { background: #e8f5e9; padding: 10px; border-radius: 4px; margin-bottom: 12px; }
        .err { color: #c62828; }
        label { display: block; margin-top: 12px; font-weight: bold; }
        input, select { padding: 6px; width: 100%; box-sizing: border-box; }
        button { margin-top: 16px; padding: 8px 20px; background: #0b3d91; color: #fff; border: 0; border-radius: 4px; cursor: pointer; }
    </style>
</head>
<body>
<header>
    <strong>Bank Core Web</strong> &nbsp;
    <a href="${pageContext.request.contextPath}/account/list">계좌목록</a>
    <a href="${pageContext.request.contextPath}/account/transfer">계좌이체</a>
    <a href="${pageContext.request.contextPath}/customer/list">고객관리</a>
    <a href="${pageContext.request.contextPath}/loan/list">대출</a>
    <a href="${pageContext.request.contextPath}/notice/list">공지사항</a>
    <a href="${pageContext.request.contextPath}/sample">호출방식 샘플</a>
</header>
<main>
