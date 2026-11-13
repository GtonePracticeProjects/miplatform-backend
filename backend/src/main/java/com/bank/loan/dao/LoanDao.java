package com.bank.loan.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bank.loan.sql.LoanSql;
import com.bank.loan.vo.LoanVO;

/** SQL 은 LoanSql 에서 받아오고, 실행은 Spring JdbcTemplate 이 담당 (Spring 트랜잭션에 참여). */
@Repository
public class LoanDao {

    private final BeanPropertyRowMapper<LoanVO> rowMapper = new BeanPropertyRowMapper<LoanVO>(LoanVO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LoanVO> selectLoanList(String statusCd, String acctNo) {
        boolean hasStatus = statusCd != null && !statusCd.isEmpty();
        boolean hasAcct = acctNo != null && !acctNo.isEmpty();

        String sql = LoanSql.selectLoanList(hasStatus, hasAcct);
        List<Object> params = new ArrayList<Object>();
        if (hasStatus) {
            params.add(statusCd);
        }
        if (hasAcct) {
            params.add(acctNo);
        }
        return jdbcTemplate.query(sql, params.toArray(), rowMapper);
    }

    public LoanVO selectLoan(long loanNo) {
        try {
            return jdbcTemplate.queryForObject(LoanSql.selectLoan(), rowMapper, loanNo);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int insertLoan(LoanVO vo) {
        return jdbcTemplate.update(LoanSql.insertLoan(),
                vo.getAcctNo(), vo.getPrincipal(), vo.getRate(), vo.getTermMonths(), vo.getMonthlyPay());
    }

    public int updateStatus(long loanNo, String newStatus) {
        return jdbcTemplate.update(LoanSql.updateStatus(), newStatus, loanNo);
    }
}
