# 예약 구매 서비스

## Table of Contents
- [Skills](#skills)
- [ERD](#erd)
- [도커 컴포즈 실행 명령어](#도커-컴포즈-실행-명령어)
- [API Reference](#api-reference)
- [구현과정(설계 및 의도)](#구현과정(설계-및-의도))
- [TIL 및 회고](#til-및-회고)
- [References](#references)

## Skills
<div align=center> 
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
<img src="https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white">
<img src="https://img.shields.io/badge/h2-4479A1?style=for-the-badge">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

<br/>

<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/drawsql-%23000000.svg?style=for-the-badge&logo=diagrams.net&logoColor=white">
</div>

## ERD
![ERD](doc/ERD.png)

## 도커 컴포즈 실행 명령어
``docker-compose up -d``

## API Reference

### 회원 API
<details>
<summary>회원 가입 - click</summary>

#### Request
`POST /user-service/signup`

```json
--boundary
Content-Disposition: form-data; name="data"
Content-Type: application/json

{
  "email": "test1@example.com",
  "password": "abc123^^",
  "name": "홍길동",
  "greeting": "안녕하세요.",
  "code": "706511",
  "userRole": "USER"
}
--boundary
Content-Disposition: form-data; name="profileImage"; filename="image.jpg"
Content-Type: image/jpeg

[이미지 데이터]
--boundary--
```
| Field          | Type     | Description          |
|:---------------|:---------|:---------------------|
| `email`      | `string` | (Required) 이메일       |
| `password`     | `string` | (Required) 비밀번호      |
| `name`     | `string` | (Required) 이름        |
| `greeting` | `string` | (Required) 인사말       |
| `code` | `int`    | (Required) 이메일 인증 코드 |
| `userRole` | `string` | (Required) 사용자 역할    |

#### Response
```text
201 CREATED
```
```text
400 Bad Request
{
    "error_code": "ALREADY_EXISTS_EMAIL",
    "message": "이미 가입된 이메일입니다."
}
```
```text
400 Bad Request
{
    "error_code": "INVALID_REQUEST",
    "message": "올바른 이메일 형식으로 입력해주세요."
}
```
```text
400 Bad Request
{
    "error_code": "INVALID_REQUEST",
    "message": "비밀번호는 숫자, 문자, 특수 문자를 각 1개 이상 포함하고, 최소 8자 이상이어야 합니다."
}
```
```text
401 Unauthorized
{
    "error_code": "EMAIL_AUTH_CODE_INCORRECT",
    "message": "이메일 인증 코드가 일치하지 않습니다."
}
```
</details>

## 구현과정(설계 및 의도)
<details>
<summary>JWT 로그인, 로그아웃</summary>

**고민1.** Refresh Token 저장 방식 
</br>

- 쿠키
- 세션
- Redis

**고민2.** 로그아웃한 Access Token이 탈취되었다면?
- Access Token 만료시간을 짧게 하여 피해 최소화
- 탈취된 토큰을 아예 사용할 수 없게하려면?
  - Access Token의 남은 만료시간만큼 Redis에 저장하여 해당 토큰이 Redis에 있다면 로그아웃한 토큰으로 판단하고 로그인 및 요청 불가
</details>

<details>
<summary>MSA 구조에서 사용자 인증 방식</summary>

- JWT를 적용하여 인증 서비스와 의존성 없이 각 서비스가 스스로 사용자 인증을 수행
- API Gateway에서 공통 인증 절차를 수행하여 각 서비스와 인증 절차를 추상화
- 추가 구현하면 좋을 것 같은 사항
  - 인증 캐시를 사용하여 반복된 인증 절차 줄이기
</details>

<details>
<summary>실시간 재고 다루기</summary>

**고민1.** 많은 사람이 한번에 재고 정보에 접근할 때 동시성 문제
</br>

- Redisson Lock, DB Lock 이용
- 원자적인 연산을 할 수 있도록 구현

**고민2.** 결제 프로세스에 진입하고 이탈할 때마다 재고를 업데이트한다. 이때, 
</br>
1. 
</details>

<details>
<summary>Controller와 Service의 DTO 분리</summary>

</details>



## TIL 및 회고
<details>
<summary>static 메서드 테스트하기</summary>
</details>


<details>
<summary>N + 1 문제</summary>
</details>


<details>
<summary>10000개 테스트 요청 시 HttpConnection 에러 발생</summary>
</details>

## References

