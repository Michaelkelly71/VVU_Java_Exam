import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class GPACalculator extends JFrame implements ActionListener {
    private JTextField nameField, idField;
    private JTable courseTable;
    private JComboBox<String> saveCombo;
    private JButton calculateBtn, saveBtn, addCourseBtn;
    private JLabel gpaLabel;
    private DefaultTableModel tableModel;
    private double gpa;
    private DatabaseHandler dbHandler = new DatabaseHandler();

    public GPACalculator() {
        setTitle("GPA Calculator");
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        String[] columns = { "Course", "Credit Hours", "Grade" };
        tableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);

        addCourseBtn = new JButton("Add Course");
        addCourseBtn.addActionListener(this);
        calculateBtn = new JButton("Calculate GPA");
        calculateBtn.addActionListener(this);
        saveBtn = new JButton("Save Data");
        saveBtn.addActionListener(this);

        saveCombo = new JComboBox<>(new String[] { "File", "Database" });
        gpaLabel = new JLabel("GPA: ");

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(addCourseBtn);
        controlPanel.add(calculateBtn);
        controlPanel.add(new JLabel("Save to:"));
        controlPanel.add(saveCombo);
        controlPanel.add(saveBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(gpaLabel, BorderLayout.NORTH);
        southPanel.add(controlPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addCourseBtn) {
            if (tableModel.getRowCount() < 7) {
                tableModel.addRow(new Object[] { "", "", "" });
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "You can only add up to 7 courses.",
                        "Course Limit Reached",
                        JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == calculateBtn) {
            try {
                gpa = calculateGPA();
                gpaLabel.setText(String.format("GPA: %.2f", gpa));
                JOptionPane.showMessageDialog(
                        this,
                        String.format("Your GPA has been calculated successfully: %.2f", gpa),
                        "GPA Calculated",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Calculation Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == saveBtn) {
            if (nameField.getText().trim().isEmpty() || idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Student Name and ID cannot be empty.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (saveCombo.getSelectedItem().equals("File")) {
                saveToFile();
            } else {
                saveToDatabase();
            }
        }
    }

    private double calculateGPA() throws Exception {
        double totalPoints = 0, totalHours = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String hoursStr = tableModel.getValueAt(i, 1).toString();
            String grade = tableModel.getValueAt(i, 2).toString();

            if (hoursStr.isEmpty() || grade.isEmpty()) {
                throw new Exception("All fields in the table must be filled.");
            }

            double creditHours;
            try {
                creditHours = Double.parseDouble(hoursStr);
            } catch (NumberFormatException e) {
                throw new Exception("Credit Hours must be a valid number.");
            }

            double gradePoint = getGradePoint(grade);
            totalPoints += gradePoint * creditHours;
            totalHours += creditHours;
        }

        if (totalHours == 0) {
            throw new Exception("Total Credit Hours cannot be zero.");
        }

        return totalPoints / totalHours;
    }

    private double getGradePoint(String grade) throws Exception {
        switch (grade.toUpperCase()) {
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
            case "D+":
                return 1.5;
            case "E":
                return 1.0;
            case "F":
                return 0.0;
            default:
                throw new Exception("Invalid grade: " + grade);
        }
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("gpa_records.txt", true))) {
            writer.println("Student Name: " + nameField.getText());
            writer.println("Student ID: " + idField.getText());
            writer.println("Courses:");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.printf("  %s, %s Credit Hours, Grade: %s%n",
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2));
            }
            writer.printf("GPA: %.2f%n", gpa);
            writer.println("====================================");
            JOptionPane.showMessageDialog(
                    this,
                    "Data saved to gpa_records.txt successfully.",
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error saving to file: " + e.getMessage(),
                    "File Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToDatabase() {
        StringBuilder courseDetails = new StringBuilder();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            courseDetails.append(String.format("%s, %s Credit Hours, Grade: %s%n",
                    tableModel.getValueAt(i, 0),
                    tableModel.getValueAt(i, 1),
                    tableModel.getValueAt(i, 2)));
        }

        try {
            dbHandler.saveToDatabase(nameField.getText(), idField.getText(), courseDetails.toString(), gpa);
            JOptionPane.showMessageDialog(
                    this,
                    "Data saved to database successfully.",
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error saving to database: " + e.getMessage(),
                    "Database Save Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected error: " + e.getMessage(),
                    "Unexpected Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GPACalculator().setVisible(true);
        });
    }
}
