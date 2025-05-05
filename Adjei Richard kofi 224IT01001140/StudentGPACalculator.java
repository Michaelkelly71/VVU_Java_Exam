import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;

public class StudentGPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JTextField[] courseFields, creditFields, gradeFields;
    private JLabel gpaResult;
    private JButton calculateButton, closeButton, addCourseButton;
    private JComboBox<String> storageComboBox, genderComboBox;
    private final int MAX_COURSES = 10;
    private int visibleCourses = 5; // Start with 5 visible courses
    
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

    public StudentGPACalculator() {
        setTitle("Student GPA Calculator");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        Font modernFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        nameField = new JTextField(20);
        nameField.setFont(modernFont);
        
        idField = new JTextField(20);
        idField.setFont(modernFont);
        
        courseFields = new JTextField[MAX_COURSES];
        creditFields = new JTextField[MAX_COURSES];
        gradeFields = new JTextField[MAX_COURSES];
        
        for (int i = 0; i < MAX_COURSES; i++) {
            courseFields[i] = new JTextField(20);
            courseFields[i].setFont(modernFont);
            
            creditFields[i] = new JTextField(5);
            creditFields[i].setFont(modernFont);
            
            gradeFields[i] = new JTextField(3);
            gradeFields[i].setFont(modernFont);
        }
        
        gpaResult = new JLabel("Student's GPA: ");
        gpaResult.setFont(modernFont.deriveFont(Font.BOLD, 16));
        gpaResult.setForeground(new Color(0, 100, 0));
        
        calculateButton = new JButton("Calculate GPA");
        calculateButton.setFont(modernFont.deriveFont(Font.BOLD));
        calculateButton.setBackground(new Color(70, 130, 180));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        
        closeButton = new JButton("Close");
        closeButton.setFont(modernFont.deriveFont(Font.BOLD));
        closeButton.setBackground(new Color(220, 80, 60));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        
        addCourseButton = new JButton("Add Course");
        addCourseButton.setFont(modernFont.deriveFont(Font.BOLD));
        addCourseButton.setBackground(new Color(60, 179, 113));
        addCourseButton.setForeground(Color.WHITE);
        addCourseButton.setFocusPainted(false);
        addCourseButton.addActionListener(e -> {
            if (visibleCourses < MAX_COURSES) {
                visibleCourses++;
                layoutComponents();
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Maximum of 10 courses reached", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        String[] storageOptions = {"File", "Database"};
        storageComboBox = new JComboBox<>(storageOptions);
        storageComboBox.setFont(modernFont);
        storageComboBox.setSelectedIndex(0);
        
        String[] genderOptions = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genderOptions);
        genderComboBox.setFont(modernFont);
        
        calculateButton.addActionListener(e -> calculateGPA());
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Top panel - Student info
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new TitledBorder("Student Information"));
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0 - NAME
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("NAME:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        infoPanel.add(nameField, gbc);
        
        // Row 1 - ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        infoPanel.add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        infoPanel.add(idField, gbc);
        
        // Row 2 - Gender
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        infoPanel.add(new JLabel("Gender:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        infoPanel.add(genderComboBox, gbc);
        
        // Row 3 - Storage Option
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        infoPanel.add(new JLabel("Save to:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        infoPanel.add(storageComboBox, gbc);
        
        // Courses panel
        JPanel coursesPanel = new JPanel(new GridBagLayout());
        coursesPanel.setBorder(new TitledBorder("Course Information (Max 10 Courses)"));
        coursesPanel.setBackground(Color.WHITE);
        
        // Header row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        coursesPanel.add(new JLabel("Course Name"), gbc);
        
        gbc.gridx = 1;
        coursesPanel.add(new JLabel("Credit Hours"), gbc);
        
        gbc.gridx = 2;
        coursesPanel.add(new JLabel("Grade"), gbc);
        
        // Course rows
        for (int i = 0; i < visibleCourses; i++) {
            gbc.gridy = i + 1;
            
            gbc.gridx = 0;
            coursesPanel.add(courseFields[i], gbc);
            
            gbc.gridx = 1;
            coursesPanel.add(creditFields[i], gbc);
            
            gbc.gridx = 2;
            coursesPanel.add(gradeFields[i], gbc);
        }
        
        // Bottom panel - Result and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));
        
        JPanel resultPanel = new JPanel();
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        resultPanel.add(gpaResult);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(addCourseButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(closeButton);
        
        bottomPanel.add(resultPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add all panels to main panel
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(coursesPanel), BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        getContentPane().removeAll();
        add(mainPanel);
    }

    private void calculateGPA() {
        try {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String gender = (String) genderComboBox.getSelectedItem();
            String storageOption = (String) storageComboBox.getSelectedItem();
            
            if (name.isEmpty() || id.isEmpty()) {
                showError("Please enter NAME and ID");
                return;
            }
            
            double totalGradePoints = 0;
            double totalCreditHours = 0;
            int enteredCourses = 0;
            StringBuilder coursesDetails = new StringBuilder();
            
            for (int i = 0; i < visibleCourses; i++) {
                String course = courseFields[i].getText().trim();
                String creditStr = creditFields[i].getText().trim();
                String grade = gradeFields[i].getText().trim().toUpperCase();
                
                // Skip completely empty rows
                if (course.isEmpty() && creditStr.isEmpty() && grade.isEmpty()) {
                    continue;
                }
                
                // Validate all fields are filled
                if (course.isEmpty() || creditStr.isEmpty() || grade.isEmpty()) {
                    showError("Please fill all fields for course " + (i+1) + " or leave all empty");
                    return;
                }
                
                try {
                    double creditHours = Double.parseDouble(creditStr);
                    if (creditHours <= 0) {
                        showError("Credit hours must be positive for course " + (i+1));
                        return;
                    }
                    
                    Double gradePoint = GRADE_POINTS.get(grade);
                    if (gradePoint == null) {
                        showError("Invalid grade for course " + (i+1) + ": " + grade + 
                                 "\nValid grades: A, B+, B, C+, C, D, E, F");
                        return;
                    }
                    
                    totalGradePoints += gradePoint * creditHours;
                    totalCreditHours += creditHours;
                    enteredCourses++;
                    
                    coursesDetails.append(String.format("%-30s %5.1f credits %5s (%.1f points)\n", 
                            course, creditHours, grade, gradePoint));
                    
                } catch (NumberFormatException e) {
                    showError("Invalid credit hours format for course " + (i+1) + 
                             "\nPlease enter a valid number");
                    return;
                }
            }
            
            if (enteredCourses == 0) {
                showError("Please enter at least one course");
                return;
            }
            
            double gpa = totalGradePoints / totalCreditHours;
            gpaResult.setText(String.format("Student's GPA: %.2f (Based on %d courses)", gpa, enteredCourses));
            
            String data = String.format(
                "STUDENT RECORD\nNAME: %s\nID: %s\nGender: %s\n\nCOURSES (%d):\n%s\nFINAL GPA: %.2f",
                name, id, gender, enteredCourses, coursesDetails.toString(), gpa
            );
            
            if ("File".equals(storageOption)) {
                saveToFile("gpa_records.txt", data);
                JOptionPane.showMessageDialog(this, "GPA calculated and saved to file!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "File: Yet to be implemented.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            showError("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void saveToFile(String fileName, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("==================================================\n");
            writer.write(data);
            writer.newLine();
            writer.write("--------------------------------------------------\n");
            writer.write("Database: Under Construction.\n");
            writer.write("Saved at: " + new java.util.Date() + "\n");
        } catch (IOException e) {
            showError("Failed to save file: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGPACalculator calculator = new StudentGPACalculator();
            calculator.setVisible(true);
        });
    }
}