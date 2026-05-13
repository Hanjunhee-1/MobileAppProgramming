function getMyInfo(req, res) {

    return res.status(200).json({
        message: "인증 성공",
        user: req.user
    });
}

module.exports = {
    getMyInfo
};