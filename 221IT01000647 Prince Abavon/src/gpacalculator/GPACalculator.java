package gpacalculator;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.*;

public class GPACalculator extends JFrame {

    private final JTextField nameField;
    private final JTextField idField;
    private final JTextField courseField;
    private final JTextField creditHoursField;
    private final JComboBox<String> gradeComboBox;
    private final JComboBox<String> saveOptionComboBox;
    private final JTextArea coursesTextArea;
    private final JLabel gpaResultLabel;
    
    private final ArrayList<CourseEntry> courseEntries = new ArrayList<>();
    
    // Grade point mapping
    private static final Map<String, Double> GRADE_POINTS = new HashMap<>();
    
    static {
        GRADE_POINTS.put("A", 4.0);
        GRADE_POINTS.put("B+", 3.5);
        GRADE_POINTS.put("B", 3.0);
        GRADE_POINTS.put("C+", 2.5);
        GRADE_POINTS.put("C", 2.0);
        GRADE_POINTS.put("D", 1.0);
        GRADE_POINTS.put("F", 0.0);
    }
    
    public GPACalculator() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        // Student info
        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        
        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        
        // Course input
        inputPanel.add(new JLabel("Course Name:"));
        courseField = new JTextField();
        inputPanel.add(courseField);
        
        inputPanel.add(new JLabel("Credit Hours:"));
        creditHoursField = new JTextField();
        inputPanel.add(creditHoursField);
        
        inputPanel.add(new JLabel("Grade:"));
        gradeComboBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "F"});
        inputPanel.add(gradeComboBox);
        
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton addButton = new JButton("Add Course");
        JButton calculateButton = new JButton("Calculate GPA");
        JButton saveButton = new JButton("Save Data");
        JButton clearButton = new JButton("Clear All");
        
        buttonPanel.add(addButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Courses display area
        JPanel coursesPanel = new JPanel(new BorderLayout());
        coursesPanel.setBorder(BorderFactory.createTitledBorder("Courses Added"));
        
        coursesTextArea = new JTextArea(10, 40);
        coursesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(coursesTextArea);
        coursesPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        
        // Save options
        JPanel saveOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        saveOptionsPanel.add(new JLabel("Save to:"));
        saveOptionComboBox = new JComboBox<>(new String[]{"File (gpa_records.txt)", "Database (gpa.db)"});
        saveOptionsPanel.add(saveOptionComboBox);
        
        resultsPanel.add(saveOptionsPanel, BorderLayout.NORTH);
        
        // GPA result
        JPanel gpaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gpaPanel.add(new JLabel("GPA: "));
        gpaResultLabel = new JLabel("0.00");
        gpaResultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gpaPanel.add(gpaResultLabel);
        
        resultsPanel.add(gpaPanel, BorderLayout.CENTER);
        
        // Add panels to main layout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(coursesPanel, BorderLayout.CENTER);
        bottomPanel.add(resultsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Event Listeners
        addButton.addActionListener(e -> addCourse());
        calculateButton.addActionListener(e -> calculateGPA());
        saveButton.addActionListener(e -> saveData());
        clearButton.addActionListener(e -> clearAll());
    }
    
    private void addCourse() {
        try {
            String courseName = courseField.getText().trim();
            int creditHours = Integer.parseInt(creditHoursField.getText().trim());
            String grade = (String) gradeComboBox.getSelectedItem();
            
            if (courseName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a course name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (creditHours <= 0) {
                JOptionPane.showMessageDialog(this, "Credit hours must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            CourseEntry entry = new CourseEntry(courseName, creditHours, grade);
            courseEntries.add(entry);
            
            updateCoursesDisplay();
            
            // Clear input fields for next entry
            courseField.setText("");
            creditHoursField.setText("");
            gradeComboBox.setSelectedIndex(0);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for credit hours.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCoursesDisplay() {
        StringBuilder sb = new StringBuilder();
        for (CourseEntry entry : courseEntries) {
            sb.append(String.format("%-20s %2d credit hours    Grade: %s\n", 
                      entry.getCourseName(), entry.getCreditHours(), entry.getGrade()));
        }
        coursesTextArea.setText(sb.toString());
    }
    
    private void calculateGPA() {
        if (courseEntries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one course.", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double totalPoints = 0;
        int totalCreditHours = 0;
        
        for (CourseEntry entry : courseEntries) {
            double gradePoint = GRADE_POINTS.get(entry.getGrade());
            int creditHours = entry.getCreditHours();
            
            totalPoints += gradePoint * creditHours;
            totalCreditHours += creditHours;
        }
        
        double gpa = totalPoints / totalCreditHours;
        gpaResultLabel.setText(String.format("%.2f", gpa));
    }
    
    private void saveData() {
        if (courseEntries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data to save.", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentName = nameField.getText().trim();
        String studentId = idField.getText().trim();
        
        if (studentName.isEmpty() || studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name and ID.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int saveOption = saveOptionComboBox.getSelectedIndex();
        
        if (saveOption == 0) {
            // Save to file
            saveToFile(studentName, studentId);
        } else {
            // Save to database - just show placeholder
            JOptionPane.showMessageDialog(this, "Database: Under Construction", "Database Save", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void saveToFile(String studentName, String studentId) {
        try {
            try (FileWriter writer = new FileWriter("gpa_records.txt")) {
                writer.write("Student: " + studentName + "\n");
                writer.write("ID: " + studentId + "\n\n");
                writer.write("Courses:\n");
                
                for (CourseEntry entry : courseEntries) {
                    writer.write(String.format("%-20s %2d credit hours    Grade: %s\n",
                            entry.getCourseName(), entry.getCreditHours(), entry.getGrade()));
                }
                
                writer.write("\nGPA: " + gpaResultLabel.getText() + "\n");
            }
            
            JOptionPane.showMessageDialog(this, "Data saved successfully to gpa_records.txt", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearAll() {
        nameField.setText("");
        idField.setText("");
        courseField.setText("");
        creditHoursField.setText("");
        gradeComboBox.setSelectedIndex(0);
        courseEntries.clear();
        coursesTextArea.setText("");
        gpaResultLabel.setText("0.00");
    }
 
    public static void main(String[] args) {
        // TODO code application logic here
        
         SwingUtilities.invokeLater(() -> {
            new GPACalculator().setVisible(true);
        });
    }
    
     // Inner class for course entries
    private static class CourseEntry {
        private final String courseName;
        private final int creditHours;
        private final String grade;
        
        public CourseEntry(String courseName, int creditHours, String grade) {
            this.courseName = courseName;
            this.creditHours = creditHours;
            this.grade = grade;
        }
        
        public String getCourseName() {
            return courseName;
        }
        
        public int getCreditHours() {
            return creditHours;
        }
        
        public String getGrade() {
            return grade;
        }
    }
}  