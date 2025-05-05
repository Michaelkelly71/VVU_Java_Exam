import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LOGIN {
    public static void main(String[] args) {
       
        JFrame frame = new JFrame("Student Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null);

        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 100, 30);
        frame.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 50, 200, 30);
        frame.add(nameField);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(50, 100, 100, 30);
        frame.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(150, 100, 200, 30);
        frame.add(idField);

        
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(125, 180, 100, 30);
        frame.add(loginButton);

        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();

                if (name.isEmpty() || id.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both Name and Student ID", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("gpa_records.txt", true))) {
                        writer.write("Name: " + name + ", Student ID: " + id);
                        writer.newLine();
                        JOptionPane.showMessageDialog(frame, "Login Successful! Record saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error saving to file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Notify the user that the database is under construction
                    JOptionPane.showMessageDialog(frame, "Note: The database is currently under construction.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        
        frame.setVisible(true);
    }
}

