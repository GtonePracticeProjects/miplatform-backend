package com.bank.loan.sql;

/**
 * [방식 3] SQL 문자열을 전담 생성하는 별도 Java 클래스.
 * DAO 는 여기서 SQL 을 리턴받아 실행만 한다. (조건에 따라 동적으로 SQL 조립)
 * 주의: 값은 반드시 '?' 바인딩으로 처리하고, 문자열 연결로 넣지 않는다 (SQL Injection 방지).
 */
public final class LoanSql {

    private static final String COLUMNS =
            "LOAN_NO, ACCT_NO, PRINCIPAL, RATE, TERM_MONTHS, MONTHLY_PAY, STATUS_CD, REQ_DT";

    private LoanSql() {
    }

    /** 상태/계좌 조건이 있으면 WHERE 절을 동적으로 추가. 바인딩 순서: status → acctNo */
    public static String selectLoanList(boolean hasStatus, boolean hasAcctNo) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(COLUMNS).append(" FROM TB_LOAN WHERE 1 = 1");
        if (hasStatus) {
            sb.append(" AND STATUS_CD = ?");
        }
        if (hasAcctNo) {
            sb.append(" AND ACCT_NO = ?");
        }
        sb.append(" ORDER BY LOAN_NO DESC");
        return sb.toString();
    }

    public static String selectLoan() {
        return "SELECT " + COLUMNS + " FROM TB_LOAN WHERE LOAN_NO = ?";
    }

    /** 바인딩 순서: acctNo, principal, rate, termMonths, monthlyPay */
    public static String insertLoan() {
        return "INSERT INTO TB_LOAN (LOAN_NO, ACCT_NO, PRINCIPAL, RATE, TERM_MONTHS, MONTHLY_PAY, STATUS_CD, REQ_DT) "
             + "VALUES (SEQ_LOAN.NEXTVAL, ?, ?, ?, ?, ?, 'R', SYSDATE)";
    }

    /** 신청(R) 상태일 때만 변경. 바인딩 순서: newStatus, loanNo */
    public static String updateStatus() {
        return "UPDATE TB_LOAN SET STATUS_CD = ? WHERE LOAN_NO = ? AND STATUS_CD = 'R'";
    }
}
