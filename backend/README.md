# bank-core-web (MiPlatform 샘플 백엔드)

은행권 레거시 스타일 웹 프로젝트 샘플 (Spring MVC 5 XML 설정 + MyBatis + Maven. 화면은 MiPlatform(`../frontend`) 이며 JSP/jQuery 화면은 제거됨. 상위 `../README.md` 의 검증 상태 참고).

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

## MiPlatform 진입점 (`com.bank.miplatform`)
| 구분 | 내용 |
|---|---|
| `adapter/` | `MiPlatformAdapter`(인터페이스), `XmlMiPlatformAdapter`(JDK XML 기본 구현), `MiDataset/MiRequest/MiResponse`(벤더 비의존 모델), `MiDatasets`(VO→Dataset) |
| `controller/` | `/miplatform/{account,customer,loan,notice}/**` (POST). 기존 Service 를 그대로 호출 |
| `MiExceptionHandler` | 오류를 HTTP 200 + `ErrorCode`/`ErrorMsg`/`BizCode` 변수로 응답 |

기존 `GlobalExceptionHandler` 와 `common/error.jsp` 는 남아 있으나 `/miplatform/**` 에는 적용되지 않는다.
**빌드/기동은 검증하지 못했다** (Maven/Oracle 미설치). 상위 README 참고.

## Oracle 준비 및 실행
1. Oracle 12c+ 에 앱 계정(`BANK_MI`) 생성 후 해당 계정으로 순서대로 실행
   - `sql/oracle/01_create_tables.sql` (재실행 시 `00_drop_all.sql` 먼저)
   - `sql/oracle/02_seed_data.sql` (선택. 직접 데이터를 넣을 거면 생략하고 컬럼 규칙만 참고)
2. `src/main/resources/config/jdbc.properties` 의 `jdbc.url / username / password` 수정
   (서비스명: `jdbc:oracle:thin:@//host:1521/SERVICE`, SID: `jdbc:oracle:thin:@host:1521:SID`)
3. 실행: `mvn jetty:run` (컨텍스트 경로 `/bank`) → 예: POST http://localhost:8080/bank/miplatform/account/list
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
