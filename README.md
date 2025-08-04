# Sionic AI - LLM Demo Application

백엔드 개발자 과제 - AI 챗봇 서비스 구축

## 개발 과정

### 1. 과제 분석 방법

Claude Desktop을 활용한 프롬프트 엔지니어링을 통해 과제를 분석했습니다.  
요구사항을 명확히 하고, 제한된 시간 내에서 구현 가능한 핵심 기능들을 식별하여 우선순위를 설정했습니다.

**분석 결과 도출된 우선순위:**
1. 사용자 인증 시스템 (회원가입/로그인)
2. AI 챗봇 대화 기능
3. 대화 스레드 관리 및 조회
4. API 문서화 및 예외 처리

### 2. AI 활용 개발 방식

IntelliJ MCP(Model Context Protocol)를 통해 Claude Desktop과 연결하여 개발을 진행했습니다.

**개발 프로세스:**
- 구현할 기능을 프롬프트로 상세 설명
- 엔티티, 리포지토리, 컨트롤러, 서비스 등 필요한 코드 생성
- 생성된 코드를 직접 검토 및 수정
- 실시간 피드백을 통한 코드 개선

### 3. 가장 구현하기 어려웠던 기능

**대화 스레드 기반 AI 채팅**

사용자와 AI의 대화 내역을 스레드 단위로 데이터베이스에 저장하고  
이를 기반으로 연속적인 대화가 가능한 시스템을 구현하는 것이 가장 어려움이 있었습니다.  
기존에는 인메모리 기반의 채팅 메모리 기능을 주로 구현해왔기 때문에 데이터베이스 기반의 영구 저장 방식으로 전환하는 과정에서 새로운 접근 방법이 필요했습니다.


---

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