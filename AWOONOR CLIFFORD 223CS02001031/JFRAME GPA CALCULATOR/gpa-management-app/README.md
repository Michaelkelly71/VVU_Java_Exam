# GPA Management App

## Overview
The GPA Management App is a simple application that allows users to submit their GPA records and view them later. The application consists of a connection page for data entry and a view records page for displaying the saved records.

## Project Structure
```
gpa-management-app
├── src
│   ├── connectionPage.js      # Handles user input and saves GPA records
│   ├── viewRecordsPage.js      # Reads and displays saved GPA records
│   └── utils
│       └── fileHandler.js      # Utility functions for file operations
├── gpa_records.txt             # Stores submitted GPA records
├── package.json                 # Configuration file for npm
└── README.md                    # Documentation for the project
```

## Features
- **Submit GPA Records**: Users can input their GPA records through the connection page.
- **View GPA Records**: Users can view all submitted GPA records on a separate page.
- **Data Persistence**: GPA records are saved in a text file (`gpa_records.txt`), ensuring data is retained between sessions.

## Installation
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```
   cd gpa-management-app
   ```
3. Install the dependencies:
   ```
   npm install
   ```

## Usage
- To submit GPA records, navigate to the connection page and fill out the form.
- To view submitted records, go to the view records page.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any enhancements or bug fixes.

## License
This project is licensed under the MIT License.