import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class GPAcalculator extends JFrame {
    private JTextField nameField, idField;
    private JTextField[] courseFields, creditFields, gradeFields;
    private JLabel gpaResult;
    private JButton calculateButton;
    private JComboBox<String> storageComboBox;
    private final int MAX_COURSES = 5;
    
    private static final Map<String, Double> GRADE_POINTS = new HashMap<>();
    static {
        GRADE_POINTS.put("A", 4.0);
        GRADE_POINTS.put("B+", 3.5);
        GRADE_POINTS.put("B", 3.0);
        GRADE_POINTS.put("C+", 2.5);
        GRADE_POINTS.put("C", 2.0);
        GRADE_POINTS.put("D", 1.5);
        GRADE_POINTS.put("E", 1.0);
        GRADE_POINTS.put("F", 0.0);
    }

    public GPAcalculator() {
        setTitle("GPA Calculator");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        nameField = new JTextField(20);
        idField = new JTextField(20);
        
        courseFields = new JTextField[MAX_COURSES];
        creditFields = new JTextField[MAX_COURSES];
        gradeFields = new JTextField[MAX_COURSES];
        
        for (int i = 0; i < MAX_COURSES; i++) {
            courseFields[i] = new JTextField(15);
            creditFields[i] = new JTextField(5);
            gradeFields[i] = new JTextField(3);
        }
        
        gpaResult = new JLabel("GPA: ");
        calculateButton = new JButton("Calculate GPA");
        
        // Storage options combo box
        String[] storageOptions = {"File", "Database"};
        storageComboBox = new JComboBox<>(storageOptions);
        storageComboBox.setSelectedIndex(0);
        
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateGPA();
            }
        });
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0 - Storage Option
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Save to:"), gbc);
        
        gbc.gridx = 1;
        add(storageComboBox, gbc);
        
        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Student Name:"), gbc);
        
        gbc.gridx = 1;
        add(nameField, gbc);
        
        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Student ID:"), gbc);
        
        gbc.gridx = 1;
        add(idField, gbc);
        
        // Row 3 - Header
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Courses"), gbc);
        
        gbc.gridx = 1;
        add(new JLabel("Credit Hours"), gbc);
        
        gbc.gridx = 2;
        add(new JLabel("Grade"), gbc);
        
        // Rows 4-8 - Course inputs
        for (int i = 0; i < MAX_COURSES; i++) {
            gbc.gridy = 4 + i;
            
            gbc.gridx = 0;
            add(courseFields[i], gbc);
            
            gbc.gridx = 1;
            add(creditFields[i], gbc);
            
            gbc.gridx = 2;
            add(gradeFields[i], gbc);
        }
        
        // Row 9 - GPA Result
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        add(gpaResult, gbc);
        
        // Row 10 - Calculate Button
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(calculateButton, gbc);
    }

    private void calculateGPA() {
        try {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String storageOption = (String) storageComboBox.getSelectedItem();
            
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter student name and ID", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double totalGradePoints = 0;
            double totalCreditHours = 0;
            StringBuilder coursesDetails = new StringBuilder();
            
            for (int i = 0; i < MAX_COURSES; i++) {
                String course = courseFields[i].getText().trim();
                String creditStr = creditFields[i].getText().trim();
                String grade = gradeFields[i].getText().trim().toUpperCase();
                
                if (course.isEmpty() && creditStr.isEmpty() && grade.isEmpty()) {
                    continue; // Skip empty rows
                }
                
                if (course.isEmpty() || creditStr.isEmpty() || grade.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please fill all course details or leave entire row empty", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    double creditHours = Double.parseDouble(creditStr);
                    Double gradePoint = GRADE_POINTS.get(grade);
                    
                    if (gradePoint == null) {
                        JOptionPane.showMessageDialog(this, 
                            "Invalid grade: " + grade + "\nValid grades are: A, B+, B, C+, C, D, E, F", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    totalGradePoints += gradePoint * creditHours;
                    totalCreditHours += creditHours;
                    
                    coursesDetails.append(String.format("Course: %s, Credit Hours: %.1f, Grade: %s, Grade Points: %.1f\n", 
                            course, creditHours, grade, gradePoint));
                    
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Credit hours must be a number", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (totalCreditHours == 0) {
                JOptionPane.showMessageDialog(this, "Please enter at least one course", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double gpa = totalGradePoints / totalCreditHours;
            gpaResult.setText(String.format("GPA: %.2f", gpa));
            
            // Prepare data for saving
            String data = String.format(
                "Student Name: %s\nStudent ID: %s\n\nCourses Details:\n%s\nGPA: %.2f",
                name, id, coursesDetails.toString(), gpa
            );
            
            // Handle storage based on selection
            if ("File".equals(storageOption)) {
                FileStorageManager.saveToFile("gpa_records.txt", data);
            } else { // Database
                JOptionPane.showMessageDialog(this, "Database: Under Construction.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}