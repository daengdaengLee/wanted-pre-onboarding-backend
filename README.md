# wanted-pre-onboarding-backend

- developed by 이건호
- [과제 설명 저장소](https://github.com/lordmyshepherd-edu/wanted-pre-onboardung-backend-selection-assignment)

## 애플리케이션 실행

도커 환경 준비 후 아래 명령어를 차례로 실행하세요.

```shell
$ ./gradlew bootBuildImage # 도커 이미지를 빌드합니다.
$ docker compose up
```

콘솔에 `Started WantedPreOnboardingBackendApplication in xxx seconds...` 메세지가 출력되면 애플리케이션이 준비된 상태입니다.

## API

API 실행을 위해 cURL 이 필요합니다.

### 회원가입

- Method : `POST`
- URI : `/users/sign-up`
- Request :
  ```typescript
  type Request = {
    email: string;
    password: string;
  };
  ```
    - email 에는 @ 가 있어야 합니다.
    - password 는 8자리 이상이어야 합니다.
- Response :
  ```typescript
  // 200 OK
  // 회원가입 성공
  type Response = {
    user: {
      id: string;
      email: string;
    };
  };
  ```
  ```typescript
  // 400 Bad Request
  // email 이나 password 가 조건에 맞지 않을 때
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 409 Conflict
  // 이미 사용중인 email 일 때
  type Response = {
    message: string;
  };
  ```
- 실행 :
  ```shell
  curl --location 'http://localhost:8080/users/sign-up' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "email": "{{사용할 이메일}}",
    "password": "{{사용할 비밀번호}}"
  }'
  ```

### 로그인

- Method : `POST`
- URI : `/users/sign-in`
- Request :
  ```typescript
  type Request = {
    email: string;
    password: string;
  };
  ```
    - email 에는 @ 가 있어야 합니다.
    - password 는 8자리 이상이어야 합니다.
- Response :
  ```typescript
  // 200 OK
  // 로그인 성공
  // 발급받은 token 은 이후 Authorization 헤더에 Bearer {{token}} 형태로 사용
  type Response = {
    user: {
      id: string;
      email: string;
    };
    token: string;
  };
  ```
  ```typescript
  // 400 Bad Request
  // email 이나 password 가 조건에 맞지 않을 때
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 401 Unauthorized
  // 비밀번호가 틀렸을 때
  type Response = {
    message: string;
  };
  ```
- 실행 :
  ```shell
  curl --location 'http://localhost:8080/users/sign-in' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "email": "{{로그인할 이메일}}",
    "password": "{{로그인할 비밀번호}}"
  }'  
  ```

### 게시글 생성

- Method : `POST`
- URI : `/posts`
- Request :
  ```typescript
  type Header = {
    Authorization: string; 
  };
  type Request = {
    title: string;
    content: string;
    author: {
      id: string;
    };
  };
  ```
    - Authorization 헤더에 `Bearer {{token}}` 을 추가해야 합니다. token 에는 로그인할 때 발급받은 토큰을 사용합니다.
    - author.id 는 로그인한 사용자의 아이디여야 합니다.
- Response :
  ```typescript
  // 200 OK
  // 게시글 작성 성공
  type Response = {
    post: {
      id: string;
      title: string;
      content: string;
      author: {
        id: string;
      };
    };
  };
  ```
  ```typescript
  // 400 Bad Request
  // 요청 데이터에 누락이 있거나 잘못되었을 때
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 401 Unauthorized
  // Authorization 헤더의 토큰이 잘못되었을 때
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 403 Forbidden 
  // 로그인한 사용자와 게시글의 작성자가 다를 때
  type Response = {
    message: string;
  };
  ```
- 실행 :
  ```shell
  curl --location 'http://localhost:8080/posts' \
  --header 'Authorization: Bearer {{token}}' \
  --header 'Content-Type: application/json' \
  --data '{
    "title": "{{게시글 제목}}",
    "content": "{{게시글 내용}}",
    "author": {
      "id": "{{작성자 id}}"
    }
  }'
  ```

### 게시글 조회

- Method : `GET`
- URI : `/posts/{{postId}}`
- Request :
    - postId 는 조회할 게시글의 id 입니다.
- Response :
  ```typescript
  // 200 OK
  // 게시글 조회 성공
  type Response = {
    post: {
      id: string;
      title: string;
      content: string;
      author: {
        id: string;
      };
    };
  };
  ```
  ```typescript
  // 404 Bad Request
  // 요청한 게시글이 없을 때
  type Response = {
    message: string;
  };
  ```
- 실행 :
  ```shell
  curl --location 'http://localhost:8080/posts/{{postId}}' 
  ```

### 게시글 수정

- Method : `PUT`
- URI : `/posts/{{postId}}`
- Request :
  ```typescript
  type Header = {
    Authorization: string; 
  };
  type Request = {
    title: string;
    content: string;
  };
  ```
    - postId 는 수정할 게시글의 id 입니다.
    - Authorization 헤더에 `Bearer {{token}}` 을 추가해야 합니다. token 에는 로그인할 때 발급받은 토큰을 사용합니다.
    - 자신이 쓴 게시글만 수정할 수 있습니다.
- Response :
  ```typescript
  // 200 OK
  // 게시글 수정 성공
  type Response = {
    post: {
      id: string;
      title: string;
      content: string;
      author: {
        id: string;
      };
    };
  };
  ```
  ```typescript
  // 400 Bad Request
  // 요청 데이터에 누락이 있거나 잘못되었을 때
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 401 Unauthorized
  // Authorization 헤더의 토큰이 잘못되었을 때
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 403 Forbidden 
  // 로그인한 사용자와 게시글의 작성자가 다를 때
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 404 Not Found
  // 요청한 게시글이 없을 때
  type Response = {
    message: string;
  };
  ```
- 실행 :
  ```shell
  curl --location --request PUT 'http://localhost:8080/posts/{{postId}}' \
  --header 'Authorization: Bearer {{token}}' \
  --header 'Content-Type: application/json' \
  --data '{
    "title": "{{수정할 게시글 제목}}",
    "content": "{{수정할 게시글 내용}}"
  }'
  ```

### 게시글 삭제

- Method : `DELETE`
- URI : `/posts/{{postId}}`
- Request :
    - postId 는 삭제할 게시글의 id 입니다.
    - Authorization 헤더에 `Bearer {{token}}` 을 추가해야 합니다. token 에는 로그인할 때 발급받은 토큰을 사용합니다.
    - 자신이 쓴 게시글만 삭제할 수 있습니다.
    - 멱등성을 만족합니다. 존재하지 않는 게시글에 대한 삭제 요청은 성공으로 처리합니다.
- Response :
  ```typescript
  // 200 OK
  // 게시글 삭제 성공
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 401 Unauthorized
  // Authorization 헤더의 토큰이 잘못되었을 때
  type Response = {
    message: string;
  };
  ```
  ```typescript
  // 403 Forbidden 
  // 로그인한 사용자와 게시글의 작성자가 다를 때
  type Response = {
    message: string;
  };
  ```
- 실행 :
  ```shell
  curl --location --request DELETE 'http://localhost:8080/posts/{{postId}}' \
  --header 'Authorization: Bearer {{token}}'
  ```

### 게시글 목록 조회

- Method : `GET`
- URI : `/posts?cursor={{postId}}&count={{postNum}}`
- Request :
    - 게시글 id 기준 내림차순으로 조회합니다.
    - cursor 로 전달한 postId 보다 작은 글 목록을 조회합니다.
    - cursor 가 없으면 가장 최신 게시글부터 조회합니다.
    - count 는 조회할 게시글 숫자입니다.
    - count 가 없으면 10개를 기본으로 조회합니다.
- Response :
  ```typescript
  // 200 OK
  // 게시글 목록 조회 성공
  // 조회할 게시글이 남아있는 경우 hasNext 가 true
  type Response = {
    posts: Array<{
      id: string;
      title: string;
      content: string;
      author: {
        id: string;
      };
    }>;
    hasNext: boolean;
  };
  ```
- 실행 :
  ```shell
  curl --location 'http://localhost:8080/posts'
  ```

## 데이터베이스 테이블 구조

```mermaid
erDiagram
    user {
        bigint id "PK"
        varchar(255) email
        varchar(255) password
    }
    post {
        bigint id "PK"
        varchar(255) title
        varchar(255) content
        bigint author_id "FK -> user.id"
    }
    user ||--o| post : contains
```

## 코드 구현

### 헥사고날 아키텍처

- 헥사고날 아키텍처(포트 & 어댑터 패턴)를 간소화하여 적용했습니다.
  - 엄격하게 지킬 경우 구현해야 할 요구사항 대비 지나치게 복잡하고 많은 코드가 생긴다고 판단했습니다.
- 패키지간 책임을 명확하게 구분했습니다.
- 패키지에서 제공할 기능을 인터페이스로 명시해서 명확하게 동작을 알 수 있도록 했습니다.
  - 성공 및 실패 경우를 모두 메소드 시그니처에 표현했습니다.
  - 세부 구현 코드를 보지 않아도 인터페이스의 동작을 모두 알 수 있도록 했습니다.
- 도메인 로직과 프레임워크의 기능을 구분하여 프레임워크 세부 내용이 도메인 로직에 끼치는 영향을 최소화했습니다.

### 인증 및 인가

- 스프링 시큐리티로 인증 로직을 구현할 공간(JWT 인증을 위한 커스텀 필터)를 확보했습니다.
- jjwt 로 JWT 생성, 검증, 파싱 동작을 구현했습니다. 해당 라이브러리를 통해 안전한 비밀키를 생성했습니다.
- 게시글 작성자 본인을 확인하는 인가 로직은 post 패키지에 책임이기 때문에 스프링 시큐리티 필터가 아닌 서비스 로직에서 담당하도록 작성했습니다.

### 게시글 페이지네이션

- 페이스북과 같은 무한 스크롤 형태를 가정하고 커서 기반 페이지네이션을 적용했습니다.
  - 게시글의 id 가 PK 이면서 생성한 순서대로 증가하는 값이기 때문에 id 기준 내림차순으로 페이지네이션을 구성했습니다.
- JPA 의 정렬 및 필터 조건을 메소드 컨벤션을 통해 사용했습니다.

## 데모 영상

- [구글 드라이브 링크](https://drive.google.com/file/d/1yUlC_eBAhfyc5e-OGR198IDfvcj1Lxfj/view?usp=sharing)

