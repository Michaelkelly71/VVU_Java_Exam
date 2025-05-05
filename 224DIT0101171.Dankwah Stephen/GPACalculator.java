import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;

public class GPACalculator extends JFrame {

    JTextField nameField, idField;
    JTextArea courseArea, creditHourArea, gradeArea;
    JComboBox<String> saveOption;
    JButton calculateBtn;
    JLabel resultLabel;

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1));

        nameField = new JTextField();
        idField = new JTextField();
        courseArea = new JTextArea(3, 20);
        creditHourArea = new JTextArea(3, 20);
        gradeArea = new JTextArea(3, 20);

        saveOption = new JComboBox<>(new String[]{"File", "Database"});
        calculateBtn = new JButton("Calculate GPA");
        resultLabel = new JLabel("Result: ", SwingConstants.CENTER);

        add(new JLabel("Student Name:"));
        add(nameField);
        add(new JLabel("Student ID:"));
        add(idField);
        add(new JLabel("Courses (comma separated):"));
        add(new JScrollPane(courseArea));
        add(new JLabel("Credit Hours (comma separated):"));
        add(new JScrollPane(creditHourArea));
        add(new JLabel("Grades (comma separated e.g. A, B, C):"));
        add(new JScrollPane(gradeArea));
        add(new JLabel("Save to:"));
        add(saveOption);
        add(calculateBtn);
        add(resultLabel);

        calculateBtn.addActionListener(e -> calculateAndSave());

        setVisible(true);
    }

    private void calculateAndSave() {
        try {
            String name = nameField.getText();
            String id = idField.getText();
            String[] courses = courseArea.getText().split(",");
            String[] creditsStr = creditHourArea.getText().split(",");
            String[] grades = gradeArea.getText().split(",");

            if (courses.length != creditsStr.length || courses.length != grades.length) {
                JOptionPane.showMessageDialog(this, "Each course must have a credit hour and grade.");
                return;
            }

            double totalPoints = 0;
            double totalCredits = 0;
            ArrayList<Integer> creditHours = new ArrayList<>();

            for (int i = 0; i < courses.length; i++) {
                int credit = Integer.parseInt(creditsStr[i].trim());
                double gradePoint = convertGrade(grades[i].trim());

                totalPoints += gradePoint * credit;
                totalCredits += credit;
                creditHours.add(credit);
            }

            double gpa = totalPoints / totalCredits;
            resultLabel.setText("Result: GPA = " + String.format("%.2f", gpa));

            if (saveOption.getSelectedItem().equals("File")) {
                saveToFile(name, id, courses, creditHours, grades, gpa);
                JOptionPane.showMessageDialog(this, "Saved to file.");
                System.out.println("Database: Under Construction.");
            } else {
                saveToDatabase(name, id, courses, creditHours, grades, gpa);
                JOptionPane.showMessageDialog(this, "Saved to database.");
                System.out.println("File: Yet to be Implemented.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private double convertGrade(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: throw new IllegalArgumentException("Invalid grade: " + grade);
        }
    }

    private void saveToFile(String name, String id, String[] courses, ArrayList<Integer> credits, String[] grades, double gpa) throws Exception {
        FileWriter fw = new FileWriter("gpa_records.txt", true);
        fw.write("Name: " + name + "\nID: " + id + "\n");
        for (int i = 0; i < courses.length; i++) {
            fw.write("Course: " + courses[i].trim() + ", Credit: " + credits.get(i) + ", Grade: " + grades[i].trim() + "\n");
        }
        fw.write("GPA: " + gpa + "\n---\n");
        fw.close();
    }

    private void saveToDatabase(String name, String id, String[] courses, ArrayList<Integer> credits, String[] grades, double gpa) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:gpa.db");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS gpa_records (name TEXT, id TEXT, course TEXT, credit INTEGER, grade TEXT, gpa REAL)");

        for (int i = 0; i < courses.length; i++) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO gpa_records VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, id);
            ps.setString(3, courses[i].trim());
            ps.setInt(4, credits.get(i));
            ps.setString(5, grades[i].trim());
            ps.setDouble(6, gpa);
            ps.executeUpdate();
        }

        conn.close();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GPACalculator::new);
    }
}
