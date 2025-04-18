

# 🛡️ 보험몽 (InsurDream) 🛡️

> **광고 전화 없이, 나에게 딱 맞는 보험 가입 시뮬레이션 웹 서비스**\
> SeSAC MSA 기반 자바 풀스택 개발 전문가 양성과정 – Final Project

---
<br>

## ✈️ 프로젝트 소개

`보험몽`은 복잡하고 불투명했던 보험 가입 과정을 **맞춤형 추천·시뮬레이션·결제**까지 한 번에 해결해 주는 웹 서비스입니다.

- 건강정보 기반 맞춤형 보험 추천 📑
- 실시간 보험료·특약·혜택 시뮬레이션 💸
- Toss Payments API를 이용한 간편 결제·자동이체 ⚡
- Google Authenticator TOTP 2차 인증 🔐
- SSE + Redis Pub/Sub 알림(자동이체 D‑1, 북마크 추가/취소) 🔔
- 북마크·마이페이지 등 사용자 편의 기능 🚀
<br>

## ⏰ 개발 기간
2025‑02‑10 \~ 2025‑03‑12 (5주)

<br>

## 👩‍💻 멤버 구성
<table>
    <tr height="150px">
       <td align="center" width="150px">
        <a href="https://github.com/JonghyeokNam"><img height="130px" width="100px" src="https://github.com/user-attachments/assets/ade59e4d-c45e-4902-b7dd-1e47c635f476"/></a>
            <br />
            <a href="https://github.com/JonghyeokNam">남종혁(팀장)</a>
      </td> <td align="center" width="150px">
        <a href="https://github.com/jenny7732"><img height="130px" width="100px" src="https://github.com/user-attachments/assets/438ae1bd-8582-41af-93a3-e8d06bc2bc11"/></a>
            <br />
            <a href="https://github.com/jenny7732">이유진(팀원)</a>
      </td>
      <td align="center" width="150px">
        <a href="https://github.com/nmskfkai"><img height="130px" width="100px" src="https://github.com/user-attachments/assets/5ba73f08-a9c0-49a9-a9b4-84b43153bc54"/></a>
            <br />
            <a href="https://github.com/nmskfkai">이지민(팀원)</a>
      </td>
       <td align="center" width="150px">
        <a href="https://github.com/ekdms6"><img height="130px" width="100px" src="https://github.com/user-attachments/assets/6e7fddd3-872e-49db-96d2-c7d5dc7b0fd1"/></a>
            <br />
            <a href="https://github.com/ekdms6">장다은(팀원)</a>
      </td> 
     <tr/>
     <tr>
       <td align="center" width="150px">
         북마크 및 알림, CI/CD 및 인프라 구축
       </td>
       <td align="center" width="150px">
         카카오 소셜로그인, Google Auth 기반 2차 인증
       </td>
       <td align="center" width="150px">
         보험상품 필터링, 건강정보 기반 추천시스템
       </td>
       <td align="center" width="150px">
         결제 및 자동이체 (Toss payments API 연동)
       </td>
    </tr>
</table>
<br>


## 📌 기술 스택

### 🛠 Frontend
[![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://reactjs.org/)
![Zustand](https://img.shields.io/badge/Zustand-FFCC00?style=for-the-badge&logoColor=white)
[![React Query](https://img.shields.io/badge/React%20Query-FF4154?style=for-the-badge&logo=react-query&logoColor=white)](https://tanstack.com/query)
[![React Router](https://img.shields.io/badge/React%20Router-CA4245?style=for-the-badge&logo=react-router&logoColor=white)](https://reactrouter.com/)
[![Axios](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge)](https://axios-http.com/)
&#x20; &#x20;

### 🛠 Backend
[![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/) 
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot) 
![SSE](https://img.shields.io/badge/SSE-0A66C2?style=for-the-badge) 
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
&#x20;    &#x20;

### 🛠 Infra
![AWS](https://img.shields.io/badge/AWS-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![Redis Pub/Sub](https://img.shields.io/badge/Redis%20Pub%2FSub-DC382D?style=for-the-badge&logo=redis&logoColor=white)
&#x20; &#x20;

### 🛠 CI/CD & Container
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white)
![Github Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
&#x20; &#x20;

### 🛠 Communucation
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)
![Jira](https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=Jira&logoColor=white)
[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
&#x20; &#x20;
<br>
<br>

## 📌 주요 기능
**회원가입 및 로그인**
- Kakao OAuth2 + Google Authenticator(TOTP) 2차 인증
<br>

**마이 페이지**
- 정보 조회 및 수정
- 건강 정보 조회 및 수정
- 북마크 보험 모아보기
- 가입한 보험 확인
- 보험금 자동이체 설정 및 날짜 확인
<br>

**메인 페이지**
- 보험사 및 보험 카테고리 선택을 통한 검색
- 보험 상품 조회 및 가입
<br>

**보험 추천**
- 사용자의 위험 요소 확인
- 건강정보(나이·직업·질환·가족력 등) 기반 가중치 모델로 보험 상품 TOP‑N 추천
<br>

**보험 가입**
- 사전 항목 동의 및 특약 확인
- 보험료 및 보장 항목 확인 
- 보험 가입 및 Toss Payments를 활용한 보험금 결제
<br>

**알림**
- 북마크 추가/취소 
- 보험금 자동이체 전날(D‑1)
<br>

## 🌐 시스템 아키텍처
![boheommong_architecture_v1](https://github.com/user-attachments/assets/ac99f710-3e50-447b-a66c-97670b5df6a5)
<br>
<br>

## :octocat: Git Commit Convention
**:bulb: Commit Type**
- 타입은 태그와 제목으로 구성되고, 태그는 영어로 쓰되 첫 문자는 대문자로 한다.
- `태그: 제목` 형태이며 `:`뒤에만 space가 있음에 유의한다.
    - `feat`  :  새로운 기능 추가
    - `fix`  :  버그 수정
    - `docs`  : 문서 수정
    - `style`  :  코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
    - `refactor`  :  코드 리펙토링
    - `test`  :  테스트 코드, 리펙토링 테스트 코드 추가
    - `chore`  :  빌드 업무 수정, 패키지 매니저 수정

