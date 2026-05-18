const { OAuth2Client } = require("google-auth-library");

const jwt = require("jsonwebtoken");

const { SECRET_KEY } = require("../config/jwt");

const client = new OAuth2Client(
    process.env.GOOGLE_CLIENT_ID
);

const { readJson, writeJson } = require("../utils/fileUtil");

// 항상 package.json 의 "main" 을 기준으로 하기 때문에 경로를 잘 설정해주어야 함. 
const userPath = "./Database/User.json"

async function googleLogin(idToken) {
    // Google Token 검증
    const ticket =
        await client.verifyIdToken({

            idToken,

            audience:
                process.env.GOOGLE_CLIENT_ID
        });

    const payload = ticket.getPayload();

    const users = readJson(userPath);

    // email 기준으로 사용자 탐색
    let user = users.find(
        user => user.email === payload.email
    );

    // 최초 로그인 시 자동 회원가입
    if (!user) {
        user = {
            id : users.length > 0
                ? users[users.length - 1].id + 1
                : 1,
            
            name: payload.name,

            email: payload.email,

            total_gold: 0
        };

        users.push(user);

        writeJson(
            userPath,
            users
        );
    }

    // JWT 생성
    const token = 
        jwt.sign( 

            {
                id: user.id,
                name: user.name,
                email: user.email
            },

            SECRET_KEY,

            {
                expiresIn: "1h"
            }
        );

    return {
        message: "Google 로그인 성공",
        token,
        user
    };
}

module.exports = {
    googleLogin
};