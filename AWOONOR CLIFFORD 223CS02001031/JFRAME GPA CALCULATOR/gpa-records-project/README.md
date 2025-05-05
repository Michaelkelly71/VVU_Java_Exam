# GPA Records Project

This project is designed to manage GPA records through a simple connection page. Users can input their GPA data, which will be stored in a text file for easy retrieval and management.

## Project Structure

```
gpa-records-project
├── src
│   ├── connectionPage.ts       # Main connection page for user input and interaction
│   └── utils
│       └── fileHandler.ts      # Utility functions for file handling
├── data
│   └── gpa_records.txt         # Text file for storing GPA records
├── package.json                # npm configuration file
├── tsconfig.json               # TypeScript configuration file
└── README.md                   # Project documentation
```

## Setup Instructions

1. **Clone the repository:**
   ```
   git clone <repository-url>
   cd gpa-records-project
   ```

2. **Install dependencies:**
   ```
   npm install
   ```

3. **Compile TypeScript files:**
   ```
   npx tsc
   ```

4. **Run the application:**
   ```
   node dist/connectionPage.js
   ```

## Usage Guidelines

- Upon running the application, users will be prompted to enter their GPA records.
- The application will store the entered data in `data/gpa_records.txt`.
- Users can retrieve their GPA records as needed.

## Contributing

Feel free to submit issues or pull requests for improvements or bug fixes.