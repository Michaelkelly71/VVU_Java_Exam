import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ViewRecords {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("View GPA Records");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        
        JTextArea recordsArea = new JTextArea();
        recordsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(recordsArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        
        try (BufferedReader reader = new BufferedReader(new FileReader("gpa_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                recordsArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        
        frame.setVisible(true);
    }
}