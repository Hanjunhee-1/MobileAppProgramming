# 모앱실 개인과제 (Server)
## environment
- Node.js 설치 필요!! [https://nodejs.org]
- Node.js v18 이상 권장

## 로컬서버 실행방법
1. `Server` 디렉토리로 이동합니다.
    ```bash
        cd ./Server
    ```

2. `Server` 디렉토리에 `.env` 파일을 생성하고 아래와 같이 입력합니다.
    ```
        PORT=3000
        JWT_SECRET=REPLACE_SECRET_THAT_YOU_WANT
        GOOGLE_CLIENT_ID=이건 공개못함
    ```
3. 아래의 스크립트를 입력하여 백엔드 서버를 실행합니다.
    ```bash
        npm install
        npm run start
    ```

## 구조 설명
1. Config
    - `jwt.js`: jwt secret key 설정
2. Controllers
    - `authController.js`: 인증 및 인가 관련
    - `todoController.js`: todo 관련 CRUD
    - `userController.js`: user 관련 (myInfo)
3. Databse
    - `Importance.js`: 중요도
    - `Todo.json`: todo 데이터 구조
    - `User.json`: user 데이터 구조
    - `ValueHistory.json`: value history 데이터 구조
4. Middleware
    - `authMiddleware.js`: jwt 토큰 다루는 곳
5. Routes
    - `authRoutes.js`: auth 관련 라우팅
    - `todoRoutes.js`: todo 관련 라우팅
    - `userRoutes.js`: user 관련 라우팅
6. Services
    - `authService.js`: auth 관련 비즈니스 로직
    - `todoService.js`: todo 관련 비즈니스 로직
    - `userService.js`: user 관련 비즈니스 로직
7. Utils
    - `fileUtil.js`: json 데이터베이스 읽기/쓰기
    - `valueCalculator.js`: value 값 계산