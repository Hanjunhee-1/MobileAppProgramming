const path = require("path");

const {
    readJson,
    writeJson
} = require("../Utils/fileUtil");

const todoPath = path.join(
    __dirname,
    "../Database/Todo.json"
);

const historyPath = path.join(
    __dirname,
    "../Database/ValueHistory.json"
);

const { calculateValue } = require("../Utils/valueCalculator");

async function getTodos(userId) {

    const todos = readJson(todoPath);

    const userTodos = todos.filter(
        todo => todo.user_id === userId
    );

    userTodos.forEach(todo => {

        const newValue = calculateValue(todo);

        if (todo.current_value !== newValue) {
            todo.current_value = newValue;

            saveValueHistory(todo.id, newValue);
        }

        todo.updated_at =
            new Date().toISOString();
    });

    writeJson(todoPath, todos);

    return userTodos;
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

    const newValue =
        calculateValue(todo);

    if (todo.current_value !== newValue) {

        todo.current_value = newValue;

        saveValueHistory(
            todo.id,
            newValue
        );
    }

    todo.updated_at =
        new Date().toISOString();

    writeJson(todoPath, todos);

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

async function saveValueHistory(todoId, value) {

    const histories = readJson(historyPath);

    const newHistory = {

        id:
            histories.length > 0
                ? histories[histories.length - 1].id + 1
                : 1,

        todo_id: todoId,

        value,

        created_at:
            new Date().toISOString()
    };

    histories.push(newHistory);

    writeJson(historyPath, histories);
}

module.exports = {
    getTodos,
    getTodo,
    createTodo,
    updateTodo,
    deleteTodo
};