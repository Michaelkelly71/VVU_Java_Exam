const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, '../../gpa_records.txt');

const saveToFile = (data) => {
    fs.appendFileSync(filePath, data + '\n', 'utf8');
};

const readFromFile = () => {
    if (fs.existsSync(filePath)) {
        return fs.readFileSync(filePath, 'utf8');
    }
    return '';
};

module.exports = {
    saveToFile,
    readFromFile
};