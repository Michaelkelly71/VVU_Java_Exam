/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.testandscore;

import java.util.Scanner;

/**
 *
 * @author kwesi
 */
public class TestandScore {

    public static void main(String[] args) {
        try ( // Create a Scanner object to read input
                Scanner scanner = new Scanner(System.in)) {
            double testScore1;
            double testScore2;
            double testScore3;
            double averageScore;
            // Ask the user to input three test scores
            System.out.print("Enter the first test score: ");
            testScore1 = scanner.nextDouble();
            System.out.print("Enter the second test score: ");
            testScore2 = scanner.nextDouble();
            System.out.print("Enter the third test score: ");
            testScore3 = scanner.nextDouble();
            // Calculating the average of the three test scores
            averageScore = (testScore1 + testScore2 + testScore3) / 3;
            // Determine the letter grade
            char letterGrade;
            if (averageScore >= 90) {
                letterGrade = 'A';
            } else if (averageScore >= 80) {
                letterGrade = 'B';
            } else if (averageScore >= 70) {
                letterGrade = 'C';
            } else if (averageScore >= 60) {
                letterGrade = 'D';
            } else {
                letterGrade = 'F';
            }   // Display the results
            System.out.printf("\nThe average test score is: %.2f\n", averageScore);
            System.out.println("The letter grade is: " + letterGrade);
            // Close the scanner
        }
    }
}
