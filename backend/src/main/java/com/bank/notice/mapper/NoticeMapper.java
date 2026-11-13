package com.bank.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.bank.notice.vo.NoticeVO;

/**
 * [방식 4] MyBatis 어노테이션 Mapper 인터페이스.
 * XML 없이 SQL 을 어노테이션으로 선언하고, 구현체는 MapperScannerConfigurer 가 프록시로 자동 생성한다.
 * (별도 DAO 클래스 없이 Service 가 이 인터페이스를 직접 주입받는다.)
 */
public interface NoticeMapper {

    @Select("SELECT NOTICE_SEQ, TITLE, CONTENT, WRITER, VIEW_CNT, REG_DT "
          + "FROM TB_NOTICE ORDER BY NOTICE_SEQ DESC")
    List<NoticeVO> selectNoticeList();

    @Select("SELECT NOTICE_SEQ, TITLE, CONTENT, WRITER, VIEW_CNT, REG_DT "
          + "FROM TB_NOTICE WHERE NOTICE_SEQ = #{noticeSeq}")
    NoticeVO selectNotice(@Param("noticeSeq") long noticeSeq);

    @Insert("INSERT INTO TB_NOTICE (NOTICE_SEQ, TITLE, CONTENT, WRITER, VIEW_CNT, REG_DT) "
          + "VALUES (SEQ_NOTICE.NEXTVAL, #{title}, #{content,jdbcType=VARCHAR}, #{writer}, 0, SYSDATE)")
    int insertNotice(NoticeVO vo);

    @Update("UPDATE TB_NOTICE SET VIEW_CNT = VIEW_CNT + 1 WHERE NOTICE_SEQ = #{noticeSeq}")
    int increaseViewCnt(@Param("noticeSeq") long noticeSeq);
}
