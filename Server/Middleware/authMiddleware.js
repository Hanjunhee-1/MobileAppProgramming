const jwt = require("jsonwebtoken");

const { SECRET_KEY } = require("../config/jwt");

function authMiddleware(req, res, next) {

    const authHeader = req.headers.authorization;

    if (!authHeader) {

        return res.status(401).json({
            message: "토큰이 없습니다."
        });
    }

    // Authorization: Bearer eyJhbGci... 에서 JWT 부분만 추출 
    const token = authHeader.split(" ")[1];

    try {

        const decoded = jwt.verify(
            token,
            SECRET_KEY
        );

        req.user = decoded;

        next();

    } catch (error) {

        return res.status(401).json({
            message: "유효하지 않은 토큰입니다."
        });
    }
}

module.exports = authMiddleware;