DROP DATABASE IF EXISTS survey_db;

CREATE DATABASE IF NOT EXISTS survey_db;

USE survey_db;

CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    gpa DECIMAL(3,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    credit_hours INT NOT NULL,
    grade VARCHAR(2) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE INDEX idx_student_id ON students(student_id);
CREATE INDEX idx_student_courses ON courses(student_id);

SELECT * FROM students;
SELECT * FROM courses;