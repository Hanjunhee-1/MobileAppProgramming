const express = require("express");

const router = express.Router();

const todoController = require("../Controllers/todoController");

const authMiddleware = require("../Middleware/authMiddleware");

// 로그인한 사용자의 모든 TODO 가져오기
router.get(
    "/",
    authMiddleware,
    todoController.getTodos
);

// 로그인한 사용자가 요청한 특정 TODO 가져오기
router.get(
    "/:id",
    authMiddleware,
    todoController.getTodo
);

// 로그인한 사용자가 요청한 특정 TODO 에 대한 history 가져오기
router.get(
    "/:id/history",
    authMiddleware,
    todoController.getHistories
);

// TODO 추가하기
router.post(
    "/",
    authMiddleware,
    todoController.createTodo
);

// TODO 업데이트하기
router.patch(
    "/:id",
    authMiddleware,
    todoController.updateTodo
);

// TODO 삭제하기
router.delete(
    "/:id",
    authMiddleware,
    todoController.deleteTodo
);

module.exports = router;