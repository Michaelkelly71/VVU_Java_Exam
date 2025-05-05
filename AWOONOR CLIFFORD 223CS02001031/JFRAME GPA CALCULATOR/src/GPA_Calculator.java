import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GPA_Calculator {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("GPA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(null);

        
        JLabel courseLabel = new JLabel("Course Name:");
        courseLabel.setBounds(50, 50, 200, 30);
        frame.add(courseLabel);

        JTextField courseField = new JTextField();
        courseField.setBounds(250, 50, 200, 30);
        frame.add(courseField);

        JLabel gradeLabel = new JLabel("Grade (A, B+, B, C+, C, D, E, F):");
        gradeLabel.setBounds(50, 100, 200, 30);
        frame.add(gradeLabel);

        JTextField gradeField = new JTextField();
        gradeField.setBounds(250, 100, 200, 30);
        frame.add(gradeField);

        JLabel creditLabel = new JLabel("Credit Hours:");
        creditLabel.setBounds(50, 150, 200, 30);
        frame.add(creditLabel);

        JTextField creditField = new JTextField();
        creditField.setBounds(250, 150, 200, 30);
        frame.add(creditField);

        JButton addButton = new JButton("Add Course");
        addButton.setBounds(50, 200, 150, 30);
        frame.add(addButton);

        JButton calculateButton = new JButton("Calculate GPA");
        calculateButton.setBounds(250, 200, 150, 30);
        frame.add(calculateButton);

        JTextArea resultArea = new JTextArea();
        resultArea.setBounds(50, 250, 500, 200);
        resultArea.setEditable(false);
        frame.add(resultArea);

        
        final double[] totalGradePoints = {0};
        final int[] totalCreditHours = {0};

        // Add action listener for the "Add Course" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseName = courseField.getText();
                String grade = gradeField.getText().toUpperCase();
                String creditText = creditField.getText();

                if (courseName.isEmpty() || grade.isEmpty() || creditText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter Course Name, Grade, and Credit Hours", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int creditHours;
                try {
                    creditHours = Integer.parseInt(creditText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Credit Hours must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double gradePoints = getGradePoints(grade);
                if (gradePoints == -1) {
                    JOptionPane.showMessageDialog(frame, "Invalid grade entered. Please enter a valid grade (A, B+, B, C+, C, D, E, F)", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                totalGradePoints[0] += gradePoints * creditHours;
                totalCreditHours[0] += creditHours;

                // Save the course details to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt", true))) {
                    writer.write("Course: " + courseName + ", Grade: " + grade + ", Credit Hours: " + creditHours);
                    writer.newLine();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving to file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                resultArea.append("Added: Course " + courseName + ", Grade " + grade + ", Credit Hours " + creditHours + "\n");
                courseField.setText("");
                gradeField.setText("");
                creditField.setText("");
            }
        });

        // 
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (totalCreditHours[0] == 0) {
                    JOptionPane.showMessageDialog(frame, "No courses added. Please add courses first.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double gpa = totalGradePoints[0] / totalCreditHours[0];
                resultArea.append("\nTotal GPA: " + String.format("%.2f", gpa) + "\n");
            }
        });

        
        frame.setVisible(true);
    }

    
    private static double getGradePoints(String grade) {
        switch (grade) {
            case "A":
                return 4.0;
            case "B+":
                return 3.5;
            case "B":
                return 3.0;
            case "C+":
                return 2.5;
            case "C":
                return 2.0;
            case "D":
                return 1.5;
            case "E":
                return 1.0;
            case "F":
                return 0.0;
            default:
                return -1; 
        }
    }
}