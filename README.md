# Spring Boot P6spy Demo

이 프로젝트는 Spring Boot에서 P6spy를 사용하여 SQL 쿼리를 로깅하는 방법을 보여주는 데모 프로젝트입니다.

## P6spy란?

P6spy는 데이터베이스 작업을 모니터링하고 로깅하기 위한 프레임워크입니다. 주요 특징은 다음과 같습니다:

1. SQL 쿼리 모니터링
   - 실행되는 모든 SQL 쿼리 로깅
   - 쿼리 실행 시간 측정
   - 쿼리 파라미터 값 확인

2. 작동 방식
   - JDBC 드라이버를 프록시하여 동작
   - 실제 JDBC 드라이버를 감싸서 모든 데이터베이스 작업을 가로채기
   - 애플리케이션 코드 수정 없이 적용 가능

3. 장점
   - 개발 및 디버깅 시 유용
   - SQL 쿼리 성능 분석 용이
   - N+1 문제 등 성능 이슈 발견에 도움

4. 단점
   - 프로덕션 환경에서 성능 오버헤드 발생
   - 메모리 사용량 증가
   - 로그 파일 크기 증가

## 기술 스택

- Java 17 (Amazon Corretto)
- Spring Boot 3.3.11
- Spring Data JPA
- MySQL 8.0
- P6spy
- Docker & Docker Compose
- Thymeleaf

## 프로젝트 구조

```
src/main/java/com/smsoft/springbootp6spydemo
├── config
│   ├── JpaConfig.java
│   ├── P6spyConfig.java
│   └── P6spyPrettySqlFormatter.java
├── controller
│   └── UserController.java
├── entity
│   └── User.java
└── repository
    └── UserRepository.java
```

## 시작하기

### 1. MySQL 실행

```bash
docker-compose up -d
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 3. 웹 브라우저 접속

```
http://localhost:8080
```

## P6spy 설정

### 1. 의존성 추가

```gradle
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'
```

### 2. P6spy 설정 클래스

```java
@Configuration
public class P6spyConfig {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spyPrettySqlFormatter.class.getName());
    }
}
```

### 3. SQL 포맷터 구현

```java
public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        Date currentDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
        return format1.format(currentDate) + " | "+ "OperationTime : "+ elapsed + "ms" + sql;
    }
    
    private String formatSql(String category, String sql) {
        // SQL 포맷팅 로직
    }
}
```

### 4. application.yml 설정

```yaml
logging:
  level:
    p6spy: debug
```

## 예제 실행 결과

### 1. 사용자 목록 조회 시

```
23.03.14 15:30:45 | OperationTime : 15ms
HeFormatSql(P6Spy sql,Hibernate format):
    select 
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.name 
    from 
        user u1_0
```

### 2. 사용자 추가 시

```
23.03.14 15:31:20 | OperationTime : 25ms
HeFormatSql(P6Spy sql,Hibernate format):
    insert 
    into
        user
        (created_at, email, name) 
    values
        (?, ?, ?)
```

## 주요 기능

1. SQL 쿼리 로깅
   - 실행된 SQL 쿼리 확인
   - 쿼리 실행 시간 측정
   - 쿼리 포맷팅

2. 사용자 관리
   - 사용자 목록 조회
   - 새로운 사용자 추가
   - 생성 시간 자동 기록

## 주의사항

1. P6spy 사용 시 주의점
   - 개발 환경에서만 사용 권장
   - 프로덕션 환경에서는 성능에 영향을 줄 수 있음
   - 실제 운영 환경에서는 사용을 지양

2. 데이터베이스 설정
   - MySQL 8.0 이상 사용 시 `allowPublicKeyRetrieval=true` 설정 필요
   - UTF-8 문자셋 설정 필요

## 스크린샷
![데모 메인화면의 일부](https://github.com/user-attachments/assets/8def5425-e91a-497b-a061-3c7506b8f38f)
![p6spy 콘솔의 로그](https://github.com/user-attachments/assets/e7de16dc-ff1b-434b-bc04-5ac7db59ac03)


## 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다. 
