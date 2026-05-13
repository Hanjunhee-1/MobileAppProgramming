const path = require("path");

const {
    readJson
} = require("../Utils/fileUtil");

const userPath = path.join(
    __dirname,
    "../Database/User.json"
);

const todoPath = path.join(
    __dirname,
    "../Database/Todo.json"
);

async function getMyInfo(userId) {

    const users =
        readJson(userPath);

    const todos =
        readJson(todoPath);

    const user = users.find(
        user => user.id === userId
    );

    if (!user) {
        throw new Error(
            "유저를 찾을 수 없습니다."
        );
    }

    const userTodos =
        todos.filter(
            todo =>
                todo.user_id === userId
        );

    const completedTodos =
        userTodos.filter(
            todo =>
                todo.is_completed
        );

    return {

        id: user.id,

        name: user.name,

        total_gold:
            user.total_gold,

        todo_count:
            userTodos.length,

        completed_todo_count:
            completedTodos.length
    };
}

module.exports = {
    getMyInfo
};