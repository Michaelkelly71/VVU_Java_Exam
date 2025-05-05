import * as fs from 'fs';
import * as path from 'path';
import { writeGPARecord, readGPARecords } from './utils/fileHandler';

const gpaRecordsFilePath = path.join(__dirname, '../data/gpa_records.txt');

function handleUserInput(name: string, gpa: number) {
    if (isValidInput(name, gpa)) {
        writeGPARecord(gpaRecordsFilePath, name, gpa);
        console.log(`GPA record for ${name} with GPA ${gpa} has been saved.`);
    } else {
        console.log('Invalid input. Please provide a valid name and GPA.');
    }
}

function isValidInput(name: string, gpa: number): boolean {
    return typeof name === 'string' && name.trim() !== '' && !isNaN(gpa) && gpa >= 0 && gpa <= 4;
}

function displayGPARecords() {
    const records = readGPARecords(gpaRecordsFilePath);
    console.log('GPA Records:');
    records.forEach(record => {
        console.log(record);
    });
}

// Example usage
handleUserInput('John Doe', 3.5);
displayGPARecords();