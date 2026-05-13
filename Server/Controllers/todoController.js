const todoService = require("../services/todoService");

// 해당 사용자의 TODO 모두 가져오기
async function getTodos(req, res) {

    try {

        const todos = await todoService.getTodos(
            req.user.id
        );

        return res.status(200).json(todos);

    } catch (error) {

        return res.status(500).json({
            message: error.message
        });
    }
}

// 해당 사용자의 특정 TODO 가져오기
async function getTodo(req, res) {

    try {

        const todo = await todoService.getTodo(
            req.user.id,
            req.params.id
        );

        return res.status(200).json(todo);

    } catch (error) {

        return res.status(404).json({
            message: error.message
        });
    }
}

// 해당 사용자의 특정 TODO 에 대한 ValueHistory 가져오기
async function getHistories(req, res) {
    try {

        const histories =
            await todoService.getHistories(
                req.user.id,
                req.params.id
            );

        return res.status(200).json(
            histories
        );

    } catch (error) {

        return res.status(400).json({
            message: error.message
        });
    }
}

// TODO 생성하기
async function createTodo(req, res) {

    try {

        const todo = await todoService.createTodo(
            req.user.id,
            req.body
        );

        return res.status(201).json(todo);

    } catch (error) {

        return res.status(400).json({
            message: error.message
        });
    }
}

// TODO 수정하기
async function updateTodo(req, res) {

    try {

        const todo = await todoService.updateTodo(
            req.user.id,
            req.params.id,
            req.body
        );

        return res.status(200).json(todo);

    } catch (error) {

        return res.status(400).json({
            message: error.message
        });
    }
}

// TODO 삭제하기
async function deleteTodo(req, res) {

    try {

        await todoService.deleteTodo(
            req.user.id,
            req.params.id
        );

        return res.status(200).json({
            message: "삭제 완료"
        });

    } catch (error) {

        return res.status(400).json({
            message: error.message
        });
    }
}

// 각종 함수들 모듈화하여 내보내기
module.exports = {
    getTodos,
    getTodo,
    getHistories,
    createTodo,
    updateTodo,
    deleteTodo
};