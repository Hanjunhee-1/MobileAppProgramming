// V = B * I * T + R
// 현재의 가치 = 기본 가치 x 중요도 계수 x 시간 가중치 + 랜덤 변동값
// V -> value
// B = base_value
// I = importance multiplier
// T = time weight
// R = random value

// T = 1 + 1/(D+1)
// D = endDate - nowDate

const importance = require("../Database/Importance");

function calculateValue(todo) {

    const baseValue = todo.base_value;

    const importanceMultiplier =
        importance[todo.importance].multiplier;

    const now = new Date();

    const endDate = new Date(todo.time.end);

    const diffMs = endDate - now;

    const remainingDays =
        Math.max(
            Math.ceil(
                diffMs / (1000 * 60 * 60 * 24)
            ),
            0
        );

    const timeWeight =
        1 + (1 / (remainingDays + 1));

    const randomValue =
        Math.floor(Math.random() * 11) - 5;

    const calculatedValue =
        (
            baseValue *
            importanceMultiplier *
            timeWeight
        ) + randomValue;

    return Math.max(
        Math.round(calculatedValue),
        0
    );
}

module.exports = {
    calculateValue
};