import fs from 'fs';
import path from 'path';

const filePath = path.join(__dirname, '../../data/gpa_records.txt');

export const writeGPARecord = (record: string): void => {
    fs.appendFileSync(filePath, record + '\n', 'utf8');
};

export const readGPARecords = (): string[] => {
    if (!fs.existsSync(filePath)) {
        return [];
    }
    const data = fs.readFileSync(filePath, 'utf8');
    return data.split('\n').filter(line => line.trim() !== '');
};