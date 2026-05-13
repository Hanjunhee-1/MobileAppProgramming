const importance = {
    LOWEST: {
        text: "최하",
        score: 1,
        multiplier: 0.7
    },

    LOW: {
        text: "하",
        score: 2,
        multiplier: 0.8
    },

    MID_LOW: {
        text: "중하",
        score: 3,
        multiplier: 0.9
    },

    MID: {
        text: "중",
        score: 4,
        multiplier: 1.0
    },

    MID_HIGH: {
        text: "중상",
        score: 5,
        multiplier: 1.1
    },

    HIGH: {
        text: "상",
        score: 6,
        multiplier: 1.2
    },

    HIGHEST: {
        text: "최상",
        score: 7,
        multiplier: 1.3
    }
}

module.exports = importance;