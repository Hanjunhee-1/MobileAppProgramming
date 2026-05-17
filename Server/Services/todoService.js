const path = require("path");

const {
    readJson,
    writeJson
} = require("../Utils/fileUtil");

const todoPath = path.join(
    __dirname,
    "../Database/Todo.json"
);

const userPath = path.join(
    __dirname,
    "../Database/User.json"
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

        if (todo.is_completed === false && todo.current_value !== newValue) {
            todo.current_value = calculateValue(todo);

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

    if (todo.is_completed === false && todo.current_value !== newValue) {

        todo.current_value = calculateValue(todo);

        saveValueHistory(
            todo.id,
            newValue
        );

        todo.updated_at =
            new Date().toISOString();

        writeJson(todoPath, todos);
    }

    return todo;
}

async function getHistories(userId, todoId) {
    const todos =
        readJson(todoPath);

    const histories =
        readJson(historyPath);

    // Todo 존재 + 권한 확인
    const todo = todos.find(
        todo =>
            todo.id === Number(todoId) &&
            todo.user_id === userId
    );

    if (!todo) {
        throw new Error(
            "Todo를 찾을 수 없습니다."
        );
    }

    const todoHistories =
        histories.filter(
            history =>
                history.todo_id ===
                Number(todoId)
        );

    // 날짜 오름차순 정렬
    todoHistories.sort(
        (a, b) =>
            new Date(a.created_at) -
            new Date(b.created_at)
    );

    return todoHistories;
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

    const users = readJson(userPath);

    const index = todos.findIndex(
        todo =>
            todo.id === Number(todoId) &&
            todo.user_id === userId
    );

    if (index === -1) {
        throw new Error("Todo를 찾을 수 없습니다.");
    }

    const wasCompleted =
        todos[index].is_completed;

    // 완료 취소 방지
    if (
        wasCompleted &&
        body.is_completed === false
    ) {
        throw new Error(
            "완료 상태는 취소할 수 없습니다."
        );
    }

    todos[index] = {
        ...todos[index],
        ...body,
        updated_at: new Date().toISOString()
    };

    const willBeCompleted =
        todos[index].is_completed;

    if (
        wasCompleted &&
        willBeCompleted
    ) {
        throw new Error(
            "이미 완료된 Todo입니다."
        );
    }

    /**
     * false -> true
     * 완료 처리
     */
    if (
        !wasCompleted &&
        willBeCompleted
    ) {

        const currentValue =
            calculateValue(
                todos[index]
            );

        let rewardGold =
            currentValue;

        const now = new Date();

        const endDate =
            new Date(
                todos[index].time.end
            );

        const remainingMs =
            endDate - now;

        const remainingDays =
            remainingMs /
            (1000 * 60 * 60 * 24);

        // 마감 1일 이내 보너스
        if (remainingDays <= 1) {

            rewardGold =
                Math.round(
                    rewardGold * 1.2
                );
        }

        todos[index].current_value =
            calculateValue(todos[index]);

        todos[index].reward_gold =
            rewardGold;

        todos[index].completed_at =
            new Date().toISOString();

        // 유저 Gold 지급
        const userIndex =
            users.findIndex(
                user =>
                    user.id === userId
            );

        users[userIndex].total_gold +=
            rewardGold;

        writeJson(userPath, users);
    }

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
    getHistories,
    createTodo,
    updateTodo,
    deleteTodo
};