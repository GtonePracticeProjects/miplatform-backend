# miplatform-backend — MiPlatform 샘플 프로젝트

은행형 샘플 프로젝트(프론트 + 백엔드). 프론트엔드 화면에서 백엔드까지의 호출 지점
(화면 → URL → Controller → Service → DAO → SQL)을 분석하는 솔루션의 테스트용 샘플이다.
`base-v1.0` 태그(Spring MVC 5 XML 설정 + MyBatis + JSP, Oracle 전용)에서 파생했다.

## 먼저 읽을 것: 검증 상태 (솔직한 한계)

- **프론트엔드 소스는 MiPlatform 에서 열어보지 못했다.** MiPlatform 클라이언트/IDE/라이선스가 없는 PC 에서
  텍스트로 수기 작성한 파일이다. 폼 XML 의 요소·속성명과 스크립트 API 사용법은 기억에 기반한 것이라
  실제 IDE 에서 열면 오류가 나거나 보정이 필요할 수 있다.
- **백엔드는 Maven 빌드를 하지 못했다.** Maven/Oracle 이 설치되어 있지 않다. 확인한 것은
  `com.bank.miplatform.adapter` 패키지를 서블릿/스프링 최소 스텁과 함께 `javac` 로 컴파일하고,
  XML 읽기/쓰기를 작은 테스트 프로그램으로 돌려본 것뿐이다. Controller, Spring 설정, MyBatis, DB 연동은
  컴파일·기동·실행 모두 하지 않았다.
- 어댑터가 읽고 쓰는 XML 형식(`Root/Variables/Dataset/ColumnInfo/Rows/Row/Col`)은 **가정**이며 실제
  MiPlatform 버전의 통신 스키마와 다를 수 있다. 벤더 라이브러리 구현체로 교체하는 것을 전제로 한다.

## 가정한 MiPlatform 버전

- **TOBESOFT MiPlatform 3.x (정확한 마이너 버전 미확인, 가정)**. 통신 방식은
  `Transaction(svcId, url, inDs, outDs, args, callback)` + 콜백 `fn(svcId, nErrorCode, strErrorMsg)`.
- 서버 측 벤더 라이브러리는 `com.tobesoft.platform` 계열로 가정한다 (클래스 구성은 구입한 제품의 문서로 확인 필요).

## 디렉터리

```
backend/    Spring MVC 5 + MyBatis + Oracle (Maven, war)   → backend/README.md
frontend/   MiPlatform 화면 소스(폼 XML + 스크립트)
  common/Common.js            공통 함수: gfn_transaction 래퍼, 메뉴 URL 변수, 공통 오류 처리
  account/AccountMain.xml     계좌 목록/상세/이체
  customer/CustomerSearch.xml 고객 검색/등록
  loan/LoanMain.xml           대출 목록/신청/승인·거절
docs/call-points.csv          호출 지점 정답지(ground truth)
```

## 호출 방식 구성 (분석 솔루션의 쉬운/어려운 케이스)

| 방식 | 설명 | 건수 |
|---|---|---|
| direct | `Transaction()` 에 URL 문자열 리터럴 | 3 (계좌목록, 고객검색, 대출목록) |
| wrapper | 공통 `gfn_transaction(...)` 경유 (래퍼 안에서 `gv_svcUrl + path`) | 3 (이체, 고객등록, 대출신청) |
| concatenated | URL 을 변수 연결로 조립 / 메뉴 변수에서 읽음 | 2 (계좌상세: 지역변수 연결, 대출승인/거절: 메뉴 변수) |

정답지는 `docs/call-points.csv`. 공지(`/miplatform/notice/**`) Controller 는 구현했지만 프론트 화면은
만들지 않았으므로 CSV 에 없다 (백엔드 전용 엔드포인트로 남겨둔 것).

## 통신 규약 (서버)

- 모든 `/miplatform/**` 는 POST, 본문/응답은 MiPlatform XML(Dataset + Variables). JSON 아님.
- 오류: HTTP 200 + 변수 `ErrorCode`(숫자, 0=정상, -1=업무오류, -2=시스템오류), `ErrorMsg`.
  업무 오류 코드(E104 등)는 변수 `BizCode` 로 별도 전달.
- Dataset 컬럼명은 VO 프로퍼티명(`acctNo`, `custNm` …), 날짜는 `yyyyMMddHHmmss` 문자열.

## 벤더 jar 연동 (Maven 좌표는 의도적으로 기재하지 않음)

벤더 서버 라이브러리의 Maven 좌표는 알 수 없어 `pom.xml` 에 넣지 않았다. 필요하면:

1. 제품 설치본/벤더에서 받은 MiPlatform 서버용 jar(`com.tobesoft.platform` 패키지를 담은 jar — 파일명·버전은 제품마다 다름)와
   그 의존 jar 를 확보한다.
2. 사내 Maven 저장소 또는 `mvn install:install-file` 로 직접 등록한 뒤 `pom.xml` 에 추가한다 (좌표는 등록자가 정함).
3. `MiPlatformAdapter` 를 구현하는 클래스(`read`: 요청 → `MiRequest`, `write`: `MiResponse` → 응답)를 만들어
   빈으로 등록하고, 기본 구현 `XmlMiPlatformAdapter` 의 `@Component` 를 제거(또는 `@Primary`/프로파일로 교체)한다.
   Controller 는 어댑터 인터페이스만 쓰므로 수정할 필요가 없다.

현재 어댑터 코드는 벤더 jar 없이 JDK 표준 XML API 만 사용해 컴파일되도록 작성했다.

## 변경 이력 요약 (base-v1.0 대비)

- 기존 프로젝트를 `backend/` 로 이동, `sample` 모듈과 JSP 화면·JSP 반환 Controller 제거
  (Service/ServiceImpl/DAO/Mapper, 4가지 DB 접근 방식, Oracle 스크립트는 유지)
- `/miplatform/**` Controller 4종 + `com.bank.miplatform.adapter` 추가
- Oracle 계정 `BANK_MI` (`jdbc.properties`, 비밀번호 비움)
