/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author kwesi
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JFrameGPA extends javax.swing.JFrame {   
    private JTextField Name;
    private JTextField ID;
    private JTextField CreditHours;
    private JTextField Grades;
    private JComboBox<String> courseComboBox;
    private JTextArea detailsArea;
    private JButton Calculate;
    private JButton View;
    private List<StudentClass> students = new ArrayList<>();

    /**
     * Creates new form JFrameGPA
     */
    public JFrameGPA() {
        setTitle("VALLEY VIEW GRADE POINT");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(14, 2));

        add(new JLabel("Name:"));
        Name = new JTextField();
        add(Name);

        add(new JLabel("ID:"));
        ID = new JTextField();
        add(ID);

        // Course combo box
        add(new JLabel("Course:"));
        String[] courses = {
            "Elements of Programming",
            "Object Oriented",
            "Digital Electronics",
            "Networking",
            "Language2"
        };
        courseComboBox = new JComboBox<>(courses);
        add(courseComboBox);

        add(new JLabel("Credit Hours:"));
        CreditHours = new JTextField();
        add(CreditHours);

        add(new JLabel("Grade:"));
        Grades = new JTextField();
        add(Grades);

        Calculate = new JButton("Calculate GPA");
        add(Calculate);

        View = new JButton("View Records");
        add(View);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        add(new JScrollPane(detailsArea));

        // Action listener for Calculate GPA button
        Calculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateGPA();
            }
        });

        // Action listener for View Records button
        View.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewRecords();
            }
        });
    }

    private void calculateGPA() {
        String name = Name.getText();
        String id = ID.getText();
        String course = (String) courseComboBox.getSelectedItem();
        String creditHoursText = CreditHours.getText();
        String grade = Grades.getText();

        if (name.isEmpty() || id.isEmpty() || course == null || creditHoursText.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double creditHours = Double.parseDouble(creditHoursText);
            double gradePoint = getGradePoint(grade);

            StudentClass student = new StudentClass(name, id, course, creditHours, gradePoint);
            double gpa = calculateGPA(student);

            saveToFile(student, gpa);

            detailsArea.setText("Name: " + name + "\nID: " + id + "\nCourse: " + course + "\nCredit Hours: " + creditHours + "\nGrade: " + grade + "\nGPA: " + gpa);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid credit hours. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculateGPA(StudentClass student) {
        double totalPoints = student.getGradePoint() * student.getCreditHours();
        double totalHours = student.getCreditHours();

        return totalHours > 0 ? totalPoints / totalHours : 0;
    }

    private double getGradePoint(String grade) {
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
            case "D":
                return 1.5;
            case "E":
                return 1.0;
            case "F":
                return 0.0;
        }
    }

    private void saveToFile(StudentClass student, double gpa) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt", true))) {
            writer.write("Name: " + student.getName() + ", ID: " + student.getId() + ", Course: " + student.getCourse() + ", Credit Hours: " + student.getCreditHours() + ", Grade: " + student.getGradePoint() + ", GPA: " + gpa);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void viewRecords() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("gpa_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        detailsArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JFrameGPA().setVisible(true);
            }
        });
    }
}
        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jTextId = new javax.swing.JTextField();
        jTextGrades = new javax.swing.JTextField();
        jCBCourses = new javax.swing.JComboBox<>();
        jTextName = new javax.swing.JTextField();
        jTextHours = new javax.swing.JTextField();
        BtnCal = new javax.swing.JButton();
        BtnClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.setBackground(new java.awt.Color(255, 51, 255));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jTextField1.setText("VALLEY VIEW GRADE POINT");

        jTextId.setBackground(new java.awt.Color(0, 204, 204));
        jTextId.setText("ID:");

        jTextGrades.setBackground(new java.awt.Color(51, 255, 51));
        jTextGrades.setText("Grades: ");
        jTextGrades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextGradesActionPerformed(evt);
            }
        });

        jCBCourses.setBackground(new java.awt.Color(102, 102, 255));
        jCBCourses.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "elements of programming", "digital electonics" }));
        jCBCourses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBCoursesActionPerformed(evt);
            }
        });

        jTextName.setBackground(new java.awt.Color(153, 0, 153));
        jTextName.setText("Name:");

        jTextHours.setBackground(new java.awt.Color(255, 153, 153));
        jTextHours.setText("Credit Hours:");

        BtnCal.setBackground(new java.awt.Color(255, 255, 0));
        BtnCal.setText("CALCULATE");

        BtnClear.setBackground(new java.awt.Color(102, 255, 255));
        BtnClear.setText("VIEW");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "File", "Database" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 724, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(94, 94, 94)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextId, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextName, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextGrades, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jCBCourses, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextHours, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(132, 132, 132)
                                    .addComponent(BtnCal)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(BtnClear)
                                    .addGap(98, 98, 98)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(190, 190, 190)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jTextName, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jTextId, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCBCourses, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jTextHours, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jTextGrades, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnClear)
                    .addComponent(BtnCal))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextGradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextGradesActionPerformed
        private void jTextGradesActionPerformed(java.awt.event.ActionEvent evt) {
    
//    String grade = Grades.getText();
//
//    
//    if (!isValidGrade(grade)) {
//        JOptionPane.showMessageDialog(this, "Invalid grade. Please enter a valid grade (A, B+, B, C+, C, D, E, F).", "Error", JOptionPane.ERROR_MESSAGE);
//        Grades.setText(""); 
//    }
}

private boolean isValidGrade(String grade) {
    grade = grade.toUpperCase();
    return grade.equals("A") || grade.equals("B+") || grade.equals("B") || grade.equals("C+") || grade.equals("C") || grade.equals("D") || grade.equals("E") || grade.equals("F");
}
    }//GEN-LAST:event_jTextGradesActionPerformed

    private void jCBCoursesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBCoursesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBCoursesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameGPA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameGPA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameGPA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameGPA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameGPA().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCal;
    private javax.swing.JButton BtnClear;
    private javax.swing.JComboBox<String> jCBCourses;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextGrades;
    private javax.swing.JTextField jTextHours;
    private javax.swing.JTextField jTextId;
    private javax.swing.JTextField jTextName;
    // End of variables declaration//GEN-END:variables
}
