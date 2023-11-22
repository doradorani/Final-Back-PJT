# AGIJAGI(육아 전문 플랫폼 서비스) BACK

> BTC 3기 개발자과정 - 1조(ONE-TEAM)
>
> ## 개발자
>
> **김란희 안지수 김장훈**
>
> ## 프로젝트 기간
>
> 2023.10.19 ~ 2022.11.20
>
> ## 담당 멘토님
>
> 강현욱 멘토님
>
> ## FRONT Git
>
> https://github.com/doradorani/agijagi_front.git
> <br/> <br/>

-   [실행](#1-실행)
-   [개요](#2-개요)
    -   [주제](#주제)
    -   [선정배경](#선정배경)
    -   [프로젝트 폴더 설명](#프로젝트-폴더-설명)
-   [아키텍처](#3-아키텍처)
    -   [ERD](#ERD)
    -   [Infra Architecture](#Infra-Architecture)
    -   [API 명세서](#API-명세서)
-   [JWT](#4-JWT)
    -   [JWT](#JWT)
-   [환경 및 버전](#5-환경-및-버전)
    -   [Environment](#environment)
    -   [Dependency](#Dependency)
    -   [Java](#Java)
-   [팀원 역할](#6.팀원-역할)
-   [시연 영상](#7.시연-영상)
-   [PPT](#8-ppt)
    <br/>
    <br/>

    

# 1. 실행

---

-   gradle build

```bash
build.gradle -> build module 'agijagi_back'
```

<br/>

- run project

```bash
Run AgijagiBackApplication 
```

<br/>

<br/>

# 2. 개요

---

## 주제

-   예쁜 우리 아이 자랑하고 싶으신가요? 육아를 하는데 정보를 얻고 싶으신가요?
-   **'AGIJAGI'** 에서 찾으실 수 있습니다.
-   예쁜 우리 아이의 모습을 매일 기록하실 수 있도록 육아 일기 서비스와 육아 수첩 서비스를 제공합니다.
-   우리 아이의 모습을 자랑하고 싶고 정보를 얻을 수 있도록 육아 커뮤니티를 제공합니다.
-   육아 관련 제품을 저렴하게 구매하실 수 있도록 펀딩 서비스를 제공합니다.
    <br/>
    <br/>

## 선정배경

-   부산에 있는 많은 취준생들 사이에서 부산은 **노인과 바다**라는 불명예스러운 별명을 가지고 있음.
-   이 별명은 높은 청년이탈율과 **저출산율**에 근거한 별명이라고 판단했고 그 중에서도 낮은 출산율에 대한 문제를 심각하게 인지할 필요가 있다고 생각함.
-   부산 합계 출산율
    ![image](https://github.com/doradorani/agijagi_front/assets/96163167/f21da0ab-332c-4dcb-880c-d628d2fa86aa)
    <br/>
-   전국 합계 출산율 통계 그래프
   ![image](https://github.com/doradorani/agijagi_front/assets/96163167/1f1eb2ae-dd06-4707-99fa-812d47616422)

-   많은 이유가 있겠지만 그 중에서도 결혼, 출산, 육아에 관한 **부정적인 이미지를 개선**하는 것이 가장 시급한 문제라고 판단하고 **좋은 이미지를 많이 노출**시켜 관련 주제의 이미지 개선을 할 수 있는 육아 플랫폼 서비스를 주제로 선정
    <br/>
    <br/>

## 프로젝트 폴더 설명

-   `src - main - java/com/office/agijagi_back` : 실행시킬 java 폴더
-   `src - main - java/com/office/agijagi_back - Config` : Security, Swagger, Web Config
-   `src - main - java/com/office/agijagi_back - Controller` : Controller Package
-   `src - main - java/com/office/agijagi_back - Dto` : Dto Package
-   `src - main - java/com/office/agijagi_back - Mapper` : Mapper Package
-   `src - main - java/com/office/agijagi_back - Service` : Service Package
-   `src - main - java/com/office/agijagi_back - Util` : JWT, KaKao(Oauth), Response, S3 공통 Util
-   `src - main - resources - mybatis` : mybatis mapper.xml Package
-   `src - main - resources - application.properties` : application을 구동시킬 때 필요한 properties
-   `src - main - resources - log4jdbc.properties` : mybatis logging을 위해 필요한 properties
-   `build.gradle` : build할 gradle
    <br/>
    <br/>

# 3. 아키텍처

---

## ERD

![ERD](https://github.com/doradorani/agijagi_front/assets/96163167/f2266768-f285-4127-83e2-6494c8c0ebc5)

## Infra Architecture

![Infra Architecture](https://github.com/doradorani/agijagi_front/assets/96163167/ca677417-b760-4b9c-8c86-6e371426a9c0)

## API 명세서

API 명세서(Swagger): https://drive.google.com/file/d/1UzdPXteyF15Cmijuztx27BxKCEROs80U/view?usp=sharing

# 4. JWT

---

## JWT

-   구조
      - 헤더(HS256) + 페이로드(Json - Key/Value) + 서명(BASE64) => 대칭키
      - ※ RSA를 활용하여 비대칭키로 보안 강화 가능
-   사용
      - 사용자 토큰 인증(401)과 권한(403)을 확인
-   구조
      - 카카오 소셜 로그인을 통한 OAuth2.0 기반의 보안 기능 강화 로그인 방식
      - AccessToken - 5분, RefreshToken - 1시간, Axios Interceptor timeout - 10초
      - Redux와 Redux-persist를 활용한 클라이언트 토큰 관리 방식 사용
      - 다중 로그인 방지를 위한 이메일과 토큰의 Key/Value 형식 검증(추후 IP 검증 기능을 추가하여 RefreshToken 탈취에 대한 보안 강화 예정)
      - Axios Interceptor를 활용한 프론트와 서버간의 통신 최소화 및 서버 부하 분산화


# 5. 환경 및 버전

---

### Environment

-   Spring boot 2.7.17

### Dependency

-   Spring dependency-management 1.0.15

### Java

-   Java 17

<br/>
<br/>

# 6. 팀원 역할

---

![팀원 역할](https://github.com/doradorani/agijagi_front/assets/96163167/fd8b20eb-7f5c-44c6-a879-16bbd7a9e868)

| 이름                                    | 담당 직무                                      |
| --------------------------------------- | ---------------------------------------------- |
| [안지수](https://github.com/jisooAhn)   | 팀장 / 육아 커뮤니티, 공지사항 기능 및 뷰 설계   |
| [김란희](https://github.com/doradorani) | 팀원 / 육아 일기, 수첩 기능 및  ERD, 인프라 구축 |
| [김장훈](https://github.com/jangdebug)  | 팀원 / JWT 토큰, 멤버링, 육아 펀딩 기능 구현     |

<br/>
<br/>

# 7. 시연 영상

---

시연 영상 : https://youtu.be/QvTuvidmGKc
<br/>
<br/>

# 8. PPT

---

발표 PPT : https://drive.google.com/file/d/1HQ-H5Ofdiwl3LZHiFV_91s3zyKthICUC/view?usp=sharing
