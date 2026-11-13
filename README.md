# bank-core-web

은행권 레거시 스타일 웹 프로젝트 샘플 (Spring MVC 5 XML 설정 + MyBatis + JSP/jQuery + Maven).

## 구조
```
src/main/java/com/bank
 ├─ account/{controller,service,service/impl,dao,vo}   Controller → Service → DAO
 └─ common/exception                                    BizException, @ControllerAdvice
src/main/resources
 ├─ config/{jdbc.properties,mybatis-config.xml}
 ├─ sqlmap/account/AccountMapper.xml                    쿼리는 XML, DAO 는 "namespace.id" 로 호출
 (DB 스크립트는 sql/oracle/ 참고 - 앱이 스키마를 만들지 않음)
src/main/webapp/WEB-INF
 ├─ web.xml / applicationContext.xml / dispatcher-servlet.xml
 └─ views/                                              JSP
sql/oracle/{00_drop_all,01_create_tables,02_seed_data,SP_TRANSFER}.sql   Oracle DDL / 샘플 데이터 / 프로시저 예시
```

## DB 연동 방식 비교 (모듈별로 하나씩)
| 모듈 | 방식 | 위치 |
|---|---|---|
| account (계좌/이체) | 1. MyBatis XML Mapper + SqlSessionTemplate (기본) | `AccountDao`, `sqlmap/account/AccountMapper.xml` |
| customer (고객) | 2. DAO 에서 `DriverManager` 로 직접 Connection 생성, 순수 JDBC | `CustomerDao` |
| loan (대출) | 3. SQL 을 만드는 전용 클래스(`LoanSql`)가 SQL 반환 → DAO 가 `JdbcTemplate` 으로 실행 | `LoanSql`, `LoanDao` |
| notice (공지) | 4. MyBatis 어노테이션 Mapper 인터페이스 (`@Select/@Insert`) | `NoticeMapper` |

모든 모듈은 `Controller → Service(인터페이스) → ServiceImpl → DAO/Mapper` 구조입니다.

## 프론트 → 백엔드 호출 방식 샘플 (`/sample`)
| 구분 | 방식 | 위치 |
|---|---|---|
| 1~3 | 고정 URL / URL+변수(쿼리스트링·PathVariable) / form 제출 | 기존 account·customer·loan·notice 화면 |
| 4 | jQuery Ajax: getJSON+PathVariable, get+쿼리, post+serialize, JSON 본문, HTML 조각 `.load()` | `sample/ajax.jsp` |
| 5 | fetch (순수 JS): GET/POST form/POST JSON/오류 처리 | `sample/fetch.jsp` |
| 6 | 공통 JS 래퍼(`common.js`) + URL 상수 파일(`urls.js`) | `webapp/resources/js`, `sample/common.jsp` |
| 7 | 서비스ID 단일 URL 방식 | 별도 레포에서 진행 예정 |

- JSON API 는 `ApiResult {success, code, message, data}` 포맷. Ajax/JSON 요청의 오류는 `GlobalExceptionHandler` 가 JSON 으로, 일반 화면 요청은 에러 JSP 로 응답.

## Oracle 준비 및 실행
1. Oracle 12c+ 에 앱 계정(예: `BANK_APP`) 생성 후 해당 계정으로 순서대로 실행
   - `sql/oracle/01_create_tables.sql` (재실행 시 `00_drop_all.sql` 먼저)
   - `sql/oracle/02_seed_data.sql` (선택. 직접 데이터를 넣을 거면 생략하고 컬럼 규칙만 참고)
2. `src/main/resources/config/jdbc.properties` 의 `jdbc.url / username / password` 수정
   (서비스명: `jdbc:oracle:thin:@//host:1521/SERVICE`, SID: `jdbc:oracle:thin:@host:1521:SID`)
3. 실행: `mvn jetty:run` → http://localhost:8080/bank/
   (기동 시 커넥션 풀이 DB 에 접속하므로 Oracle 에 연결 가능해야 함)

## 데이터 직접 입력 시 규칙
- 계좌 `STATUS_CD`: A 정상 / C 해지, 거래내역 `TRANS_TP`: W 출금 / D 입금
- 대출 `STATUS_CD`: R 신청 / A 승인 / X 거절
- 고객번호는 `C` + 6자리(`C000001`). 앱 등록분은 `SEQ_CUSTOMER`(4부터)로 채번되므로 직접 넣는 번호와 겹치지 않게 주의
- 입력 후 반드시 `COMMIT`

## WAS 배포
- `applicationContext.xml` 의 dataSource 를 JNDI(`jee:jndi-lookup`) 로 교체 가능
- `mvn package` → `target/bank.war` 를 WebLogic/JEUS/JBoss 에 배포
- 주의: customer 모듈(방식 2)은 JNDI 를 쓰지 않고 `jdbc.properties` 로 직접 접속함
