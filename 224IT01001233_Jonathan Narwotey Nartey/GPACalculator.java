import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPACalculator extends JFrame {

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

    
    private JTextField txtName, txtID;
    private JComboBox<String> cmbSaveOption;
    private JButton btnAddCourse, btnCalculate, btnSave;
    private JTextArea txtResult;
    private JPanel coursesPanel;
    private List<CourseEntry> courseEntries = new ArrayList<>();

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    
        JPanel studentPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        
        studentPanel.add(new JLabel("Student Name:"));
        txtName = new JTextField();
        studentPanel.add(txtName);
        
        studentPanel.add(new JLabel("Student ID:"));
        txtID = new JTextField();
        studentPanel.add(txtID);

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePanel.setBorder(BorderFactory.createTitledBorder("Save Option"));
        savePanel.add(new JLabel("Save to:"));
        
        String[] saveOptions = {"File", "Database"};
        cmbSaveOption = new JComboBox<>(saveOptions);
        savePanel.add(cmbSaveOption);

        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBorder(BorderFactory.createTitledBorder("Courses"));
        
    
        addCourseEntry();

    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAddCourse = new JButton("Add Course");
        btnAddCourse.addActionListener(e -> addCourseEntry());
        
        btnCalculate = new JButton("Calculate GPA");
        btnCalculate.addActionListener(e -> calculateGPA());
        
        btnSave = new JButton("Save Data");
        btnSave.addActionListener(e -> saveData());
        
        buttonPanel.add(btnAddCourse);
        buttonPanel.add(btnCalculate);
        buttonPanel.add(btnSave);

    
        txtResult = new JTextArea(10, 50);
        txtResult.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResult);
        

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(studentPanel, BorderLayout.NORTH);
        topPanel.add(savePanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(coursesPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.EAST);

        add(mainPanel);
    }

    private void addCourseEntry() {
        CourseEntry entry = new CourseEntry();
        courseEntries.add(entry);
        coursesPanel.add(entry);
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void calculateGPA() {
        try {
            double totalGradePoints = 0;
            double totalCredits = 0;
            
            for (CourseEntry entry : courseEntries) {
                String grade = entry.getGrade();
                double credits = entry.getCredits();
                
                if (grade.isEmpty() || credits <= 0) {
                    JOptionPane.showMessageDialog(this, "Please fill all course details correctly", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Double gradePoint = GRADE_POINTS.get(grade);
                if (gradePoint == null) {
                    JOptionPane.showMessageDialog(this, "Invalid grade selected", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                totalGradePoints += gradePoint * credits;
                totalCredits += credits;
            }
            
            if (totalCredits == 0) {
                JOptionPane.showMessageDialog(this, "No courses to calculate", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double gpa = totalGradePoints / totalCredits;
            DecimalFormat df = new DecimalFormat("#.##");
            
            txtResult.setText("Student Name: " + txtName.getText() + "\n" +
                             "Student ID: " + txtID.getText() + "\n\n" +
                             "Courses:\n");
            
            for (CourseEntry entry : courseEntries) {
                txtResult.append(entry.getCourseName() + " - " + 
                                 entry.getCredits() + " credits - " + 
                                 entry.getGrade() + "\n");
            }
            
            txtResult.append("\nTotal GPA: " + df.format(gpa));
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for credits", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveData() {
        String saveOption = (String) cmbSaveOption.getSelectedItem();
        
        if (saveOption.equals("File")) {
            saveToFile();
        } else {
            saveToDatabase();
        }
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save GPA Data");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Student Name: " + txtName.getText());
                writer.println("Student ID: " + txtID.getText());
                writer.println("\nCourses:");
                
                for (CourseEntry entry : courseEntries) {
                    writer.println(entry.getCourseName() + "," + 
                                  entry.getCredits() + "," + 
                                  entry.getGrade());
                }
                
                writer.println("\n" + txtResult.getText());
                JOptionPane.showMessageDialog(this, "Data saved successfully to file", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveToDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            
        
            Connection conn = DriverManager.getConnection("jdbc:sqlite:gpa_data.db");
            
            
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "name TEXT, student_id TEXT, gpa REAL)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "student_id TEXT, course_name TEXT, credits REAL, grade TEXT)");
            
            
            String studentId = txtID.getText();
            double gpa = extractGPAFromResult();
            
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students (name, student_id, gpa) VALUES (?, ?, ?)");
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, studentId);
            pstmt.setDouble(3, gpa);
            pstmt.executeUpdate();
            
            
            pstmt = conn.prepareStatement("INSERT INTO courses (student_id, course_name, credits, grade) VALUES (?, ?, ?, ?)");
            
            for (CourseEntry entry : courseEntries) {
                pstmt.setString(1, studentId);
                pstmt.setString(2, entry.getCourseName());
                pstmt.setDouble(3, entry.getCredits());
                pstmt.setString(4, entry.getGrade());
                pstmt.executeUpdate();
            }
            
            conn.close();
            JOptionPane.showMessageDialog(this, "Data saved successfully to database", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double extractGPAFromResult() {
        String resultText = txtResult.getText();
        String[] lines = resultText.split("\n");
        
        for (String line : lines) {
            if (line.startsWith("Total GPA: ")) {
                return Double.parseDouble(line.substring("Total GPA: ".length()));
            }
        }
        return 0.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GPACalculator calculator = new GPACalculator();
            calculator.setVisible(true);
        });
    }


    private class CourseEntry extends JPanel {
        private JTextField txtCourseName;
        private JTextField txtCredits;
        private JComboBox<String> cmbGrade;

        public CourseEntry() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            
            add(new JLabel("Course Name:"));
            txtCourseName = new JTextField(10);
            add(txtCourseName);
            
            add(new JLabel("Credits:"));
            txtCredits = new JTextField(5);
            add(txtCredits);
            
            add(new JLabel("Grade:"));
            cmbGrade = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
            add(cmbGrade);
            
            JButton btnRemove = new JButton("Remove");
            btnRemove.addActionListener(e -> {
                courseEntries.remove(this);
                coursesPanel.remove(this);
                coursesPanel.revalidate();
                coursesPanel.repaint();
            });
            add(btnRemove);
        }
        
        public String getCourseName() {
            return txtCourseName.getText();
        }
        
        public double getCredits() {
            try {
                return Double.parseDouble(txtCredits.getText());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        
        public String getGrade() {
            return (String) cmbGrade.getSelectedItem();
        }
    }
}