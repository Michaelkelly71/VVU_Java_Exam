import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GPAcalculator calculator = new GPAcalculator();
            calculator.setVisible(true);
        });
    }
}