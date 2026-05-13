const fs = require("fs");

function readJson(path) {

    const data = fs.readFileSync(path, "utf-8");

    return JSON.parse(data);
}

function writeJson(path, data) {

    fs.writeFileSync(
        path,
        JSON.stringify(data, null, 4)
    );
}

module.exports = {
    readJson,
    writeJson
};