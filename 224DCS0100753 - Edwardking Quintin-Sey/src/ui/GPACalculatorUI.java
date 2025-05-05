package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import models.Course;
import models.Student;

// Please run the run.bat file to run the program. In VS Code, click the run/play symbol or use this command. .\.run.bat Thanks - Edwardking. 

public class GPACalculatorUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField nameField;
    private JTextField idField;
    private JTextField courseField;
    private JTextField creditHoursField;
    private JComboBox<String> gradeComboBox;
    private JComboBox<String> storageComboBox;
    private JButton addCourseButton;
    private JButton calculateButton;
    private JTable coursesTable;
    private DefaultTableModel tableModel;
    private JLabel gpaResultLabel;

    private List<Course> courses;

    public GPACalculatorUI() {
        courses = new ArrayList<>();

        setTitle("GPA Calculator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color startColor = new Color(240, 248, 255);
                Color endColor = new Color(204, 229, 255);
                GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = createInputPanel();

        JPanel tablePanel = createTablePanel();

        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setForeground(new Color(0, 51, 102));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameField = new JTextField();

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setForeground(new Color(0, 51, 102));
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));
        idField = new JTextField();

        JLabel courseLabel = new JLabel("Course Name:");
        courseLabel.setForeground(new Color(0, 51, 102));
        courseLabel.setFont(new Font("Arial", Font.BOLD, 12));
        courseField = new JTextField();

        JLabel creditLabel = new JLabel("Credit Hours:");
        creditLabel.setForeground(new Color(0, 51, 102));
        creditLabel.setFont(new Font("Arial", Font.BOLD, 12));
        creditHoursField = new JTextField();

        JLabel gradeLabel = new JLabel("Grade:");
        gradeLabel.setForeground(new Color(0, 51, 102));
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 12));

        String[] grades = { "A", "B+", "B", "C+", "C", "D", "E", "F" };
        gradeComboBox = new JComboBox<>(grades);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(courseLabel);
        inputPanel.add(courseField);
        inputPanel.add(creditLabel);
        inputPanel.add(creditHoursField);
        inputPanel.add(gradeLabel);
        inputPanel.add(gradeComboBox);

        return inputPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setOpaque(false);

        String[] columns = { "Course", "Credit Hours", "Grade", "Grade Points" };
        tableModel = new DefaultTableModel(columns, 0);
        coursesTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Course List"));

        addCourseButton = new JButton("Add Course");
        addCourseButton.setBackground(new Color(70, 130, 180));
        addCourseButton.setForeground(Color.BLACK);

        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCourse();
            }
        });

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(addCourseButton, BorderLayout.SOUTH);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 10));
        buttonPanel.setOpaque(false);

        JPanel storagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        storagePanel.setOpaque(false);

        JLabel storageLabel = new JLabel("Storage Method:");
        storageLabel.setForeground(new Color(0, 51, 102));
        storageLabel.setFont(new Font("Arial", Font.BOLD, 12));

        String[] storageOptions = { "Database", "File" };
        storageComboBox = new JComboBox<>(storageOptions);

        storagePanel.add(storageLabel);
        storagePanel.add(storageComboBox);

        gpaResultLabel = new JLabel("GPA: 0.00");
        gpaResultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gpaResultLabel.setForeground(new Color(0, 102, 0));

        calculateButton = new JButton("Calculate GPA");
        calculateButton.setBackground(new Color(46, 139, 87));
        calculateButton.setForeground(Color.BLACK);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateGPA();
            }
        });

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        resultPanel.setOpaque(false);
        resultPanel.add(gpaResultLabel);
        resultPanel.add(calculateButton);

        buttonPanel.add(storagePanel, BorderLayout.WEST);
        buttonPanel.add(resultPanel, BorderLayout.EAST);

        return buttonPanel;
    }

    private void addCourse() {
        String courseName = courseField.getText().trim();
        String creditHoursText = creditHoursField.getText().trim();
        String grade = gradeComboBox.getSelectedItem().toString();

        if (courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a course name.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int creditHours;
        try {
            creditHours = Integer.parseInt(creditHoursText);
            if (creditHours <= 0) {
                JOptionPane.showMessageDialog(this, "Credit hours must be a positive number.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for credit hours.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Course course = new Course(courseName, creditHours, grade);
        courses.add(course);

        Object[] row = {
                courseName,
                creditHours,
                grade,
                course.getGradePoint()
        };

        tableModel.addRow(row);

        courseField.setText("");
        creditHoursField.setText("");
        gradeComboBox.setSelectedIndex(0);
    }

    private void calculateGPA() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student name.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one course.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(id, name);
        for (Course course : courses) {
            student.addCourse(course);
        }

        double gpa = student.calculateGPA();
        gpaResultLabel.setText("GPA: " + String.format("%.2f", gpa));

        String storageMethod = storageComboBox.getSelectedItem().toString();

        if ("Database".equals(storageMethod)) {
            saveToDatabase(student);
        } else {
            JOptionPane.showMessageDialog(this, "File: Yet to be implemented.", "Storage",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveToDatabase(Student student) {
        DatabaseConnection db = new DatabaseConnection();
        boolean success = db.saveStudent(student);
        db.close();

        if (success) {
            JOptionPane.showMessageDialog(this, "Student data saved successfully to database.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save student data to database.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GPACalculatorUI().setVisible(true);
            }
        });
    }
}