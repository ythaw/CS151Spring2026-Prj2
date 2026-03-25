package model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseCode;
    private String title;
    private int credits;
    private String description;
    private List<Course> prerequisites;

    public Course(String courseCode, String title, int credits, String description) {
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.description = description;
        this.prerequisites = new ArrayList<>();
    }

    public void addPrerequisite(Course course) {
        if (course != null && !prerequisites.contains(course)) {
            prerequisites.add(course);
        }
    }

    public void removePrerequisite(Course course) {
        prerequisites.remove(course);
    }

    public boolean prerequisitesMetBy(List<Course> completedCourses) {
        return completedCourses.containsAll(prerequisites);
    }

    public boolean hasPrerequisites() {
        return !prerequisites.isEmpty();
    }

    public String getCatalogSummary() {
        String prereqList = hasPrerequisites()
                ? prerequisites.stream().map(c -> c.courseCode).reduce((a, b) -> a + ", " + b).orElse("None")
                : "None";
        return courseCode + ": " + title + " (" + credits + " credits)\n"
                + "Prerequisites: " + prereqList + "\n"
                + description;
    }

    public boolean isValidCreditValue() {
        return credits > 0 && credits <= 6;
    }

    // Getters
    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getDescription() { return description; }
    public List<Course> getPrerequisites() { return prerequisites; }

    // Setters
    public void setCourseCode(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty.");
        }
        this.courseCode = courseCode;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.title = title;
    }

    public void setCredits(int credits) {
        if (credits <= 0 || credits > 6) {
            throw new IllegalArgumentException("Credits must be between 1 and 6.");
        }
        this.credits = credits;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return courseCode + ": " + title + " (" + credits + " credits)";
    }
}