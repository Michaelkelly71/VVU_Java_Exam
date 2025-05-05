package models;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private List<Course> courses;
    private double gpa;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.courses = new ArrayList<>();
        this.gpa = 0.0;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public double calculateGPA() {
        if (courses.isEmpty()) {
            return 0.0;
        }

        double totalWeightedGradePoints = 0.0;
        int totalCreditHours = 0;

        for (Course course : courses) {
            totalWeightedGradePoints += course.getGradePoint() * course.getCreditHours();
            totalCreditHours += course.getCreditHours();
        }

        this.gpa = totalCreditHours > 0 ? totalWeightedGradePoints / totalCreditHours : 0.0;
        this.gpa = Math.round(this.gpa * 100.0) / 100.0;

        return this.gpa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
}