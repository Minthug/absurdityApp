# AbsurdityApp - 형제자매를 위한 심부름 플랫폼 👨‍👧‍👦

## 프로젝트 소개
AbsurdityApp은 형제자매 간의 심부름을 재미있게 만드는 새로운 방식의 플랫폼입니다. 
배달 앱처럼 심부름을 등록하고, 적절한 보상과 함께 심부름을 수행하며 형제자매 간의 유대감을 강화하는 것이 목적입니다.

### 개발 동기
- 새로운 기술 스택 학습 및 실전 적용
- 개인 프로젝트를 통한 기술적 성장
- 형제자매 간 소통 방식의 새로운 제안

## 기술 스택

### Backend
- Java 17
- Spring Boot 3.3.4
- Spring Security
- Spring Data JPA
- QueryDSL 5.0.0
- WebSocket
- Spring Cloud GCP

### Database & Cache
- PostgreSQL
- Redis
- Apache Kafka

### Message Queue
- RabbitMQ
- Kafka

### Authentication & Authorization
- JWT (java-jwt 4.2.1)
- OAuth2.0 (Google, Naver, Kakao)

### Storage
- Google Cloud Storage

### Payment Integration
- Iamport Payment API

### Development Tools
- Lombok
- MapStruct
- Swagger
- Spring Actuator

### Testing
- JUnit 5
- Mockito
- AssertJ
- Spring Security Test
- Testcontainers
- Awaitility

### Infrastructure
- Docker
- Google Cloud Platform (GCP)

## 주요 기능
### 1. 심부름 관리
- 실시간 심부름 등록/수락 시스템
- WebSocket 기반 실시간 알림
- 카테고리별 심부름 분류
- QueryDSL을 활용한 동적 검색

### 2. 결제 시스템
- Iamport API 연동
- 포인트 적립/사용
- 결제 내역 관리
- 정산 시스템

### 3. 인증/보안
- JWT 기반 토큰 인증
- OAuth2.0 소셜 로그인
- Spring Security 기반 보안 설정
- Redis 기반 토큰 관리

### 4. 파일 관리
- GCP Storage 연동
- 이미지 업로드/다운로드
- 파일 캐싱

## 프로젝트 구조
```
src
 ├── main
 │   ├── java
 │   │   └── setting
 │   │       ├── config
 │   │       ├── controller
 │   │       ├── domain
 │   │       ├── repository
 │   │       ├── service
 │   │       └── util
 │   └── resources
 └── test
     └── java
         └── setting
```

## 개발 환경 설정
```bash
# 프로젝트 클론
git clone https://github.com/username/absurdity-app.git

# Gradle 빌드
./gradlew build

# Docker 컨테이너 실행
docker-compose up -d

# 애플리케이션 실행
./gradlew bootRun
```

## 테스트
```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "TestClassName"
```

## API 문서
- Swagger UI: `/swagger-ui.html`
- API 문서: [API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md)

## 주요 설정 파일
- `application.yml`: 애플리케이션 설정
- `docker-compose.yml`: Docker 컨테이너 설정
- `build.gradle`: 프로젝트 의존성 관리

## 개선 사항
- [ ] 웹사이트 구축 및 배포
- [ ] 테스트 커버리지 향상
- [ ] 성능 모니터링 시스템 구축
- [ ] CI/CD 파이프라인 구축
- [ ] 다국어 지원

## 개발자 정보
- GitHub: [minthug](https://github.com/minthug)
- Email: jkisimmortal4.gmail.com

## 라이선스
이 프로젝트는 MIT 라이선스를 따릅니다. [LICENSE](LICENSE) 파일에서 자세한 내용을 확인하세요.
