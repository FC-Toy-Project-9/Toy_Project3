# 토이 프로젝트 3 : 여행 여정을 기록과 관리하는 SNS 서비스 3단계

2023-11-10 ~ 2023-11-16

---

## 📌 목차

- [멤버](#멤버)
- [설정](#설정)
- [설계](#설계)
    - [DB 설계](#DB-설계)
    - [API 설계](#API-설계)
- [개발 내용](#개발-내용)
- [실행 결과 캡처](#실행-결과-캡처)
- [API 문서](#API-문서)

---

## 멤버

- 👩🏻‍💻 [정의정](https://github.com/JeongUijeong)
- 👩🏻‍💻 [양유림](https://github.com/YurimYang)
- 👩🏻‍💻 [강민정](https://github.com/ypd06021)
- 👩🏻‍💻 [이의인](https://github.com/dldmldlsy)

---

## 설정

- 자바 버전: 17
- 스프링 버전: 6.0.13
- 스프링 부트 버전: 3.1.5
- 의존성
    - Spring Boot Starter
        - `org.springframework.boot:spring-boot-starter-data-jpa`
        - `org.springframework.boot:spring-boot-starter-security`
        - `org.springframework.boot:spring-boot-starter-validation`
        - `org.springframework.boot:spring-boot-starter-web`
        - `org.springframework.boot:spring-boot-starter-test`
    - Lombok
        - `org.projectlombok:lombok`
        - `org.projectlombok:lombok`
        - `org.projectlombok:lombok`
        - `org.projectlombok:lombok:1.18.28`
    - DB
        - `org.mariadb.jdbc:mariadb-java-client`
        - `com.h2database:h2:2.2.220`
    - JSON
        - `org.json:json:20230227`
    - Spring REST Docs
        - `org.springframework.restdocs:spring-restdocs-asciidoctor`
        - `org.springframework.restdocs:spring-restdocs-mockmvc`
    - Security
        - `org.springframework.security:spring-security-test`
    - Jwt
        - `io.jsonwebtoken:jjwt:0.9.1`
        - `javax.xml.bind:jaxb-api:2.3.1`
        - `com.fasterxml.jackson.core:jackson-databind:2.13.4.1`
    - QueryDSL
        - `com.querydsl:querydsl-jpa:5.0.0:jakarta`
        - `com.querydsl:querydsl-apt:5.0.0:jakarta`
        - `jakarta.annotation:jakarta.annotation-api`
        - `jakarta.persistence:jakarta.persistence-api`
- `applicaion.yaml`, `application-secret.yaml` 파일은 LMS에서 확인하실 수 있습니다!

---

## 설계

### DB 설계 (ERD)

> ![](src/main/resources/image/erd.png)

### API 설계

[REST Docs](#API-문서)를 통해 확인하실 수 있습니다.

---

## 개발 내용

- 회원 기능
    - 회원 가입을 할 수 있습니다.
    - 회원 로그인을 할 수 있습니다.
- 여행 등록 기능
    - 여행 일정을 등록할 수 있습니다.
- 특정 여행의 여정 등록 기능
    - 하나의 여행에 여러 개의 여정 정보를 기록할 수 있습니다.
- 여행 조회 기능
    - 등록된 여행 전체 리스트를 조회할 수 있습니다.
    - 검색어(여행 이름, 회원 닉네임)로 검색할 수 있습니다.
    - 정렬(최신순, 좋아요순) 기능이 있습니다.
    - 회원이 좋아요 한 여행 리스트를 조회할 수 있습니다.
- 여행 정보 수정 기능
    - 여행 정보를 수정할 수 있습니다.
    - 여정 정보는 별도로 수정 기능을 제공합니다.
- 여정 위치 정보 키워드 검색 기능
    - Kakao API를 활용해 키워드로 위치 정보를 검색할 수 있습니다. 
- 여정 정보 수정 기능
    - 여정 정보를 수정할 수 있습니다.
- 좋아요 기능
    - 회원이 여행에 대해 좋아요를 등록할 수 있습니다.
    - 좋아요를 취소할 수 있습니다.
- 댓글 기능
    - 회원이 여행에 댓글을 등록할 수 있습니다.
    - 댓글을 수정할 수 있습니다.
    - 댓글을 삭제할 수 있습니다.
- 예외 처리
    - 각 요청에 대한 예외처리가 있습니다.

---

## 실행 결과 캡처

> `Console`
> ![](src/main/resources/image/console.png)
>
> `DB`
> ![](src/main/resources/image/db_trip.png)
> ![](src/main/resources/image/db_itinerary.png)
> ![](src/main/resources/image/db_member.png)
> ![](src/main/resources/image/db_likes.png)
> ![](src/main/resources/image/db_comment.png)
>
> ※ 각 API 요청 결과는 [REST Docs](#API-문서)에서 확인하실 수 있습니다.
>
> ※ Postman 테스트 결과는 LMS로 제출한 기획안에서 확인하실 수 있습니다.

---

## API 문서

※ Spring REST Docs로 문서화했습니다.

> `index`
> ![](src/main/resources/image/api_docs_index.png)
>
> `Trip API Docs`
> ![](src/main/resources/image/api_docs_trip.png)
>
> `Itinerary API Docs`
> ![](src/main/resources/image/api_docs_itinerary.png)
>
> `Member API Docs`
> ![](src/main/resources/image/api_docs_member.png)
>
> `Like API Docs`
> ![](src/main/resources/image/api_docs_like.png)
>
> `Comment API Docs`
> ![](src/main/resources/image/api_docs_comment.png)
> 