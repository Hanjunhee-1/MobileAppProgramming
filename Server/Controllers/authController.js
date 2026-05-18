const authService = require("../services/authService");

async function googleLogin(req, res) {

    try {
        const result = await authService.googleLogin(req.body.idToken);

        res.json(result);
    } catch (e) {
        res.status(401).json({

            message:
                e.message
        });
    }
}

module.exports = {
    googleLogin
};