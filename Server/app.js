// .env 파일 사용
require("dotenv").config();

// npm 패키지에서 express, cors 모듈 가져오기
const express = require("express");
const cors = require("cors");

// Routes 폴더에서 해당 js 파일 가져오기
const authRoutes = require("./Routes/authRoutes");
const userRoutes = require("./Routes/userRoutes");
const todoRoutes = require("./Routes/todoRoutes");

// app 시작점.
const app = express();

app.use(cors());
app.use(express.json());

// localhost:${PORT}/auth 로 접근하는 것은 전부 authRoutes 에서 처리
app.use("/auth", authRoutes);

// localhost:${PORT}/users 로 접근하는 것은 전부 userRoutes 에서 처리
app.use("/users", userRoutes);

// localhost:${PORT}/todos 로 접근하는 것은 전부 todoRoutes 에서 처리
app.use("/todos", todoRoutes);

// port 번호는 .env 파일을 따르되 3000 으로 고정
const PORT = process.env.PORT || 3000;

app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on localhost:${PORT}`);
})