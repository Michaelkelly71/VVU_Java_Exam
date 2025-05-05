
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JTextField[] courseFields, creditFields, gradeFields;
    private JLabel gpaResult;
    private JButton calculateButton, closeButton;
    private JComboBox<String> storageComboBox, genderComboBox;
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

    public GPACalculator() {
        setTitle("Student GPA Calculator");
        setSize(800, 700);
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
            courseFields[i] = new JTextField(15);
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
        
        String[] storageOptions = {"File", "Database"};
        storageComboBox = new JComboBox<>(storageOptions);
        storageComboBox.setFont(modernFont);
        storageComboBox.setSelectedIndex(0);
        
        String[] genderOptions = {"Male", "Female"};
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
        infoPanel.setBorder(new TitledBorder("Student Details"));
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
        infoPanel.add(nameField, gbc);
        
        // Row 1 - ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(new JLabel("ID:"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(idField, gbc);
        
        // Row 2 - Gender
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(new JLabel("Gender:"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(genderComboBox, gbc);
        
        // Row 3 - Storage Option
        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(new JLabel("Save to:"), gbc);
        
        gbc.gridx = 1;
        infoPanel.add(storageComboBox, gbc);
        
        // Courses panel - Reorganized layout
        JPanel coursesPanel = new JPanel(new GridBagLayout());
        coursesPanel.setBorder(new TitledBorder("Course Details (Max 5 Courses)"));
        coursesPanel.setBackground(Color.WHITE);
        
        // Header row - Reordered columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        coursesPanel.add(new JLabel("Grade"), gbc);
        
        gbc.gridx = 1;
        coursesPanel.add(new JLabel("Credit Hours"), gbc);
        
        gbc.gridx = 2;
        coursesPanel.add(new JLabel("Course"), gbc);
        
        // Course rows - Reordered to match headers
        for (int i = 0; i < MAX_COURSES; i++) {
            gbc.gridy = i + 1;
            
            gbc.gridx = 0;
            coursesPanel.add(gradeFields[i], gbc);
            
            gbc.gridx = 1;
            coursesPanel.add(creditFields[i], gbc);
            
            gbc.gridx = 2;
            coursesPanel.add(courseFields[i], gbc);
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
        buttonPanel.add(calculateButton);
        buttonPanel.add(closeButton);
        
        bottomPanel.add(resultPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add all panels to main panel
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(coursesPanel), BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
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
            StringBuilder coursesDetails = new StringBuilder();
            
            for (int i = 0; i < MAX_COURSES; i++) {
                String grade = gradeFields[i].getText().trim().toUpperCase();
                String creditStr = creditFields[i].getText().trim();
                String course = courseFields[i].getText().trim();
                
                if (grade.isEmpty() && creditStr.isEmpty() && course.isEmpty()) {
                    continue;
                }
                
                if (grade.isEmpty() || creditStr.isEmpty() || course.isEmpty()) {
                    showError("Please fill all course details or leave entire row empty");
                    return;
                }
                
                try {
                    double creditHours = Double.parseDouble(creditStr);
                    Double gradePoint = GRADE_POINTS.get(grade);
                    
                    if (gradePoint == null) {
                        showError("Invalid grade: " + grade + "\nValid grades are: A, B+, B, C+, C, D, E, F");
                        return;
                    }
                    
                    totalGradePoints += gradePoint * creditHours;
                    totalCreditHours += creditHours;
                    
                    coursesDetails.append(String.format("%s (Grade: %s, Credits: %.1f)\n", 
                            course, grade, creditHours));
                    
                } catch (NumberFormatException e) {
                    showError("Credit hours must be a number");
                    return;
                }
            }
            
            if (totalCreditHours == 0) {
                showError("Please enter at least one course");
                return;
            }
            
            double gpa = totalGradePoints / totalCreditHours;
            gpaResult.setText(String.format("Student's GPA: %.2f", gpa));
            
            String data = String.format(
                "STUDENT RECORD\nNAME: %s\nID: %s\nGender: %s\n\nCOURSES:\n%s\nFINAL GPA: %.2f",
                name, id, gender, coursesDetails.toString(), gpa
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
            showError("An error occurred: " + e.getMessage());
        }
    }
    
    private void saveToFile(String fileName, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(data);
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();
            writer.write("Database: Under Construction.");
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GPACalculator calculator = new GPACalculator();
            calculator.setVisible(true);
        });
    }
}