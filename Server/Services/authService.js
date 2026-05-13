const jwt = require("jsonwebtoken");

const { SECRET_KEY } = require("../config/jwt");

const { readJson } = require("../utils/fileUtil");

async function login(body) {

    const { name, password } = body;

    const users = readJson("./Database/User.json");

    const user = users.find(
        user =>
            user.name === name &&
            user.password === password
    );

    if (!user) {
        throw new Error("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    const token = jwt.sign(
        {
            id: user.id,
            name: user.name
        },
        SECRET_KEY,
        {
            expiresIn: "1h"
        }
    );

    return {
        message: "로그인 성공",
        token
    };
}

module.exports = {
    login
};