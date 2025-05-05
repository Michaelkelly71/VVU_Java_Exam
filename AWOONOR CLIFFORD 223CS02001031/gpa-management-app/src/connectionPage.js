const fs = require('fs');
const path = require('path');
const filePath = path.join(__dirname, '../gpa_records.txt');

function saveGPARecord(gpa) {
    const record = `${gpa}\n`;
    fs.appendFile(filePath, record, (err) => {
        if (err) {
            console.error('Error saving GPA record:', err);
        } else {
            console.log('GPA record saved successfully.');
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('gpaForm');
    form.addEventListener('submit', (event) => {
        event.preventDefault();
        const gpaInput = document.getElementById('gpaInput').value;
        saveGPARecord(gpaInput);
        form.reset();
    });
});