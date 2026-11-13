package com.bank.customer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.bank.common.exception.DbException;
import com.bank.customer.vo.CustomerVO;

/**
 * [방식 2] DAO 에서 직접 Connection 을 생성해 SQL 을 실행하는 순수 JDBC 방식.
 *  - 커넥션 풀/Spring 트랜잭션과 무관하게 DriverManager 로 매번 연결 (오래된 레거시 코드에서 흔함)
 *  - 메서드마다 autoCommit 으로 즉시 반영됨. 운영 코드라면 풀(DataSource) 사용을 권장.
 */
@Repository
public class CustomerDao {

    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.driver}")
    private String driver;

    /** 레거시 WAS 환경에서는 드라이버 자동 등록이 안 되는 경우가 있어 명시적으로 로딩 */
    @PostConstruct
    public void loadDriver() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DbException("JDBC 드라이버를 찾을 수 없습니다: " + driver, e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public List<CustomerVO> selectCustomerList(String keyword) {
        String sql = "SELECT CUST_NO, CUST_NM, PHONE, EMAIL, REG_DT FROM TB_CUSTOMER "
                   + "WHERE CUST_NM LIKE ? ORDER BY CUST_NO";
        List<CustomerVO> list = new ArrayList<CustomerVO>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (keyword == null ? "" : keyword.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DbException("고객 목록 조회 실패", e);
        }
        return list;
    }

    public CustomerVO selectCustomer(String custNo) {
        String sql = "SELECT CUST_NO, CUST_NM, PHONE, EMAIL, REG_DT FROM TB_CUSTOMER WHERE CUST_NO = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, custNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DbException("고객 조회 실패", e);
        }
    }

    public String nextCustNo() {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT SEQ_CUSTOMER.NEXTVAL FROM DUAL");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return String.format("C%06d", rs.getLong(1));
        } catch (SQLException e) {
            throw new DbException("고객번호 채번 실패", e);
        }
    }

    public int insertCustomer(CustomerVO vo) {
        String sql = "INSERT INTO TB_CUSTOMER (CUST_NO, CUST_NM, PHONE, EMAIL, REG_DT) VALUES (?, ?, ?, ?, SYSDATE)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vo.getCustNo());
            ps.setString(2, vo.getCustNm());
            ps.setString(3, vo.getPhone());
            ps.setString(4, vo.getEmail());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("고객 등록 실패", e);
        }
    }

    private CustomerVO map(ResultSet rs) throws SQLException {
        CustomerVO vo = new CustomerVO();
        vo.setCustNo(rs.getString("CUST_NO"));
        vo.setCustNm(rs.getString("CUST_NM"));
        vo.setPhone(rs.getString("PHONE"));
        vo.setEmail(rs.getString("EMAIL"));
        Timestamp ts = rs.getTimestamp("REG_DT");
        vo.setRegDt(ts == null ? null : new java.util.Date(ts.getTime()));
        return vo;
    }
}
