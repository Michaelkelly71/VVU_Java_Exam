package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import models.Course;
import models.Student;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/survey_db";
    private static final String USER = "root";
    private static final String PASSWORD = "MySql1234";

    private Connection connection;

    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    public boolean saveStudent(Student student) {
        try {
            connection.setAutoCommit(false);

            String studentSQL = "INSERT INTO students (student_id, name, gpa) VALUES (?, ?, ?)";
            PreparedStatement studentStmt = connection.prepareStatement(studentSQL, Statement.RETURN_GENERATED_KEYS);
            studentStmt.setString(1, student.getId());
            studentStmt.setString(2, student.getName());
            studentStmt.setDouble(3, student.getGpa());

            int affectedRows = studentStmt.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                return false;
            }

            ResultSet generatedKeys = studentStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                connection.rollback();
                return false;
            }

            int dbStudentId = generatedKeys.getInt(1);

            List<Course> courses = student.getCourses();
            String courseSQL = "INSERT INTO courses (student_id, course_name, credit_hours, grade) VALUES (?, ?, ?, ?)";
            PreparedStatement courseStmt = connection.prepareStatement(courseSQL);

            for (Course course : courses) {
                courseStmt.setInt(1, dbStudentId);
                courseStmt.setString(2, course.getName());
                courseStmt.setInt(3, course.getCreditHours());
                courseStmt.setString(4, course.getGrade());
                courseStmt.addBatch();
            }

            courseStmt.executeBatch();

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }

            System.err.println("Error saving student data: " + e.getMessage());
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}