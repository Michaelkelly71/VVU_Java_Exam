/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kwesi
 */
public class StudentClass {
    private String name;
    private String id;
    private String course;
    private double creditHours;
    private double gradePoint;

    // Constructor
    public StudentClass(String name, String id, String course, double creditHours, double gradePoint) {
        this.name = name;
        this.id = id;
        this.course = course;
        this.creditHours = creditHours;
        this.gradePoint = gradePoint;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public double getCreditHours() {
        return creditHours;
    }

    public double getGradePoint() {
        return gradePoint;
    }
}

