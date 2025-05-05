
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConnectionPage {

    private static final String FILE_NAME = "gpa_records.txt";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to the GPA Records System!");
            System.out.print("Enter Student Name: ");
            String studentName = scanner.nextLine();

            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine();

            System.out.print("Enter GPA: ");
            String gpa = scanner.nextLine();

            String record = "Name: " + studentName + ", ID: " + studentId + ", GPA: " + gpa;

            saveRecordToFile(record);

            System.out.println("Record saved successfully!");
        }
    }

    private static void saveRecordToFile(String record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the record: " + e.getMessage());
        }
    }
}