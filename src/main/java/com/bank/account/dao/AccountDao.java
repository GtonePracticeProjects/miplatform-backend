package com.bank.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bank.account.vo.AccountVO;
import com.bank.account.vo.TransHistVO;

/** SQL 은 sqlmap/account/AccountMapper.xml 에 정의, 여기서는 쿼리 ID 로만 호출한다. */
@Repository
public class AccountDao {
    private static final String NS = "account.";

    @Autowired
    private SqlSessionTemplate sqlSession;

    public List<AccountVO> selectAccountList() {
        return sqlSession.selectList(NS + "selectAccountList");
    }

    public AccountVO selectAccount(String acctNo) {
        return sqlSession.selectOne(NS + "selectAccount", acctNo);
    }

    public AccountVO selectAccountForUpdate(String acctNo) {
        return sqlSession.selectOne(NS + "selectAccountForUpdate", acctNo);
    }

    public int updateBalance(String acctNo, long delta) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("acctNo", acctNo);
        param.put("delta", delta);
        return sqlSession.update(NS + "updateBalance", param);
    }

    public int insertTransHist(TransHistVO vo) {
        return sqlSession.insert(NS + "insertTransHist", vo);
    }

    public List<TransHistVO> selectTransHistList(String acctNo) {
        return sqlSession.selectList(NS + "selectTransHistList", acctNo);
    }
}
