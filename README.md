# 예약 구매 서비스
일반 상품 및 특정 시간이후 구매할 수 있는 상품(예약 상품)을 등록하고 구매할 수 있는 서비스입니다.

## Table of Contents
- [Skills](#skills)
- [Modules](#modules)
- [ERD](#erd)
- [문서](#문서)
  - [API 명세서](#api-명세서)
  - [포트폴리오](#포트폴리오)
- [도커 컴포즈 실행 명령어](#도커-컴포즈-실행-명령어)
- [References](#references)

## Skills
<div align=center> 
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
<img src="https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white">
<img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/h2-4479A1?style=for-the-badge">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

<br/>

<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/drawsql-%23000000.svg?style=for-the-badge&logo=diagrams.net&logoColor=white">
</div>

## Modules
| Service	      | Description	               | Authorization	 | Port |
|:--------------|:---------------------------|:---------------|:-----|
| `User`       | 사용자 관련 작업 관리               | jwt 토큰으로 권한 검사 | `8080` |
| `Product`       | 상품 관련 작업 관리                |                | `8084` |
| `Purchase`      | 주문 관련 작업 관리                |                | `8085` |
| `Payment`       | 결제 관련 작업 관리                |                | `8086` |
| `Stock`         | 재고 관련 작업 관리                |                | `8087` |
| `API Gateway`   | 게이트웨이 역할, 요청을 적절한 서비스로 라우팅 | jwt 토큰으로 권한 검사 | `8083` |
| `Eureka`        | 서비스 등록 및 검색                | | `8761` |

## ERD
![ERD](doc/ERD.png)
- blah blah

## 문서
>### [API 명세서](https://documenter.getpostman.com/view/27585524/2sA2rGtdyi)
>### [포트폴리오]()

## 도커 컴포즈 실행 명령어

> * 실행
>  ```
>  docker-compose up
>  ```
> * 빌드 후 실행
>   ```
>   docker-compose up --build
>   ```
> * 각 모듈 빌드 후 실행
>   ```
>   docker-compose up --build {서비스이름}
>   ```


## References

