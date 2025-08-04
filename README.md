# Sionic AI - LLM Demo Application

백엔드 개발자 과제 - AI 챗봇 서비스 구축

## 프로젝트 개요

이 프로젝트는 AI 챗봇 서비스를 위한 API를 제공합니다. 사용자는 AI 제공자를 통해 대화형 AI와 상호작용할 수 있습니다.  
이 서비스는 대화 스레드를 관리하고, 사용자 인증을 제공합니다.

### 주요 기능

- 사용자 인증 (회원가입/로그인)
- AI 챗봇과의 대화 생성 및 관리
- 대화 스레드 조회

## 기술 스택

- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.5.4
- **Database**: PostgreSQL 15
- **ORM**: Spring Data JPA
- **Authentication**: JWT (JSON Web Token)
- **AI Service**: Google Gemini
- **Build Tool**: Gradle (Kotlin DSL)
- **AI Integration**: LangChain4j

## 시작하기

### 환경 변수 설정

다음 환경 변수들을 설정하거나 기본값을 사용합니다:

```bash
# 데이터베이스 설정
export DB_URL=your-db-url
export DB_USERNAME=your-db-username
export DB_PASSWORD=your-db-password

# JWT 설정
export JWT_SECRET=your-secret
export JWT_ACCESS_TOKEN_EXPIRATION=3600000
export JWT_REFRESH_TOKEN_EXPIRATION=604800000

# LLM API Key 설정
export GEMINI_API_KEY=your-gemini-api-key
```

## API 문서

### 인증 API

#### 회원가입

```
POST /api/auth/register
```

**요청 본문**:
```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "홍길동"
}
```

**응답 예시**:
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600000,
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "홍길동",
    "role": "MEMBER"
  }
}
```

#### 로그인

```
POST /api/auth/login
```

**요청 본문**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**응답 예시**:
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600000,
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "홍길동",
    "role": "MEMBER"
  }
}
```

### 챗봇 API

#### 채팅 생성

```
POST /api/v1/chat
```

**요청 헤더**:
```
Authorization: Bearer <your-access-token>
```

**요청 본문**:
```json
{
  "prompt": "안녕하세요, 오늘 날씨가 어떤가요?",
  "provider": "GOOGLE",
  "modelName": "gemini-2.5-flash",
  "isStreaming": false,
  "threadId": null
}
```

- `prompt`: 사용자의 질문 또는 프롬프트 (필수)
- `provider`: AI 제공자 (GOOGLE 또는 OPENAI) (필수)
- `modelName`: 사용할 모델 이름 (필수)
- `isStreaming`: 스트리밍 응답 여부 (선택, 기본값: false)
- `threadId`: 기존 대화 스레드 ID (선택, 없으면 새 스레드 생성)

**응답 예시**:
```json
{
  "id": 1,
  "threadId": 1,
  "question": "안녕하세요, 오늘 날씨가 어떤가요?",
  "answer": "안녕하세요! 제가 실시간 날씨 정보에 접근할 수 없어 정확한 날씨를 알려드릴 수 없습니다. 하지만 날씨를 확인하시려면 기상청 웹사이트나 날씨 앱을 이용해보시는 것을 추천드립니다.",
  "modelName": "gemini-2.5-flash",
  "isStreaming": false,
  "createdAt": "2025-08-04T17:52:30"
}
```

#### 채팅 기록 조회

```
GET /api/v1/chat?page=0&size=10
```

**요청 헤더**:
```
Authorization: Bearer <your-access-token>
```

**쿼리 파라미터**:
- `page`: 페이지 번호 (기본값: 0)
- `size`: 페이지 크기 (기본값: 10)

**응답 예시**:
```json
[
  {
    "id": 2,
    "threadId": 1,
    "question": "인공지능에 대해 설명해주세요",
    "answer": "인공지능(AI)은 인간의 학습, 추론, 인식 등의 지능을 컴퓨터 시스템으로 구현한 기술입니다...",
    "modelName": "gemini-2.5-flash",
    "isStreaming": false,
    "createdAt": "2025-08-04T17:55:20"
  },
  {
    "id": 1,
    "threadId": 1,
    "question": "안녕하세요, 오늘 날씨가 어떤가요?",
    "answer": "안녕하세요! 제가 실시간 날씨 정보에 접근할 수 없어 정확한 날씨를 알려드릴 수 없습니다...",
    "modelName": "gemini-2.5-flash",
    "isStreaming": false,
    "createdAt": "2025-08-04T17:52:30"
  }
]
```

## 인증 헤더

모든 보호된 API 호출 시 다음 헤더를 포함해야 합니다:

```
Authorization: Bearer <your-access-token>
```