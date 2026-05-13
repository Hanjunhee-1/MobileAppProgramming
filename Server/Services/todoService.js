const path = require("path");

const {
    readJson,
    writeJson
} = require("../Utils/fileUtil");

const todoPath = path.join(
    __dirname,
    "../Database/Todo.json"
);

async function getTodos(userId) {

    const todos = readJson(todoPath);

    return todos.filter(
        todo => todo.user_id === userId
    );
}

async function getTodo(userId, todoId) {

    const todos = readJson(todoPath);

    const todo = todos.find(
        todo =>
            todo.id === Number(todoId) &&
            todo.user_id === userId
    );

    if (!todo) {
        throw new Error("Todo를 찾을 수 없습니다.");
    }

    return todo;
}

async function createTodo(userId, body) {

    const todos = readJson(todoPath);

    const newTodo = {

        id:
            todos.length > 0
                ? todos[todos.length - 1].id + 1
                : 1,

        name: body.name,

        description: body.description || "",

        time: body.time,

        importance: body.importance,

        is_completed: false,

        base_value: body.base_value,

        current_value: body.base_value,

        earned_gold: 0,

        completed_at: null,

        user_id: userId,

        created_at: new Date().toISOString(),

        updated_at: new Date().toISOString()
    };

    todos.push(newTodo);

    writeJson(todoPath, todos);

    return newTodo;
}

async function updateTodo(userId, todoId, body) {

    const todos = readJson(todoPath);

    const index = todos.findIndex(
        todo =>
            todo.id === Number(todoId) &&
            todo.user_id === userId
    );

    if (index === -1) {
        throw new Error("Todo를 찾을 수 없습니다.");
    }

    todos[index] = {
        ...todos[index],
        ...body,
        updated_at: new Date().toISOString()
    };

    writeJson(todoPath, todos);

    return todos[index];
}

async function deleteTodo(userId, todoId) {

    const todos = readJson(todoPath);

    const filteredTodos = todos.filter(
        todo =>
            !(
                todo.id === Number(todoId) &&
                todo.user_id === userId
            )
    );

    writeJson(todoPath, filteredTodos);
}

module.exports = {
    getTodos,
    getTodo,
    createTodo,
    updateTodo,
    deleteTodo
};