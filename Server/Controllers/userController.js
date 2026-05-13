const userService = require("../Services/userService");

function getMe(req, res) {

    return res.status(200).json({
        message: "인증 성공",
        user: req.user
    });
}

async function getMyInfo(req, res) {
    try {

        const user =
            await userService.getMyInfo(
                req.user.id
            );

        return res.status(200).json(
            user
        );

    } catch (error) {

        return res.status(400).json({
            message: error.message
        });
    }
} 

module.exports = {
    getMe,
    getMyInfo
};