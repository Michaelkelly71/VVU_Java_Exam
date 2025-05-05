import React, { useEffect, useState } from 'react';
import { readFromFile } from './utils/fileHandler';

const ViewRecordsPage = () => {
    const [records, setRecords] = useState([]);

    useEffect(() => {
        const fetchRecords = async () => {
            const data = await readFromFile();
            setRecords(data.split('\n').filter(record => record)); // Filter out empty lines
        };

        fetchRecords();
    }, []);

    return (
        <div>
            <h1>GPA Records</h1>
            <ul>
                {records.map((record, index) => (
                    <li key={index}>{record}</li>
                ))}
            </ul>
        </div>
    );
};

export default ViewRecordsPage;