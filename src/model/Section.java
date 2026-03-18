package model;

import interfaces.Deactivatable;
import java.util.ArrayList;
import java.util.List;

// Section represents one offering of a course in a specific term
public class Section implements Deactivatable {
    private String sectionId;
    private Course course;
    private ProfessorAccount instructor;
    private Schedule schedule;
    private String term;
    private int capacity;
    private boolean active;
    private String deactivationReason;
    private List<StudentAccount> enrolledStudents;

    // Constructor
    public Section(String sectionId, Course course, ProfessorAccount instructor,
                   Schedule schedule, String term, int capacity) {
        this.sectionId = sectionId;
        this.course = course;
        this.instructor = instructor;
        this.schedule = schedule;
        this.term = term;
        this.capacity = capacity;
        this.active = true;
        this.deactivationReason = "";
        this.enrolledStudents = new ArrayList<>();
    }

    // Check if the section still has empty seats
    public boolean hasSeatAvailable() {
        return enrolledStudents.size() < capacity;
    }

    // Return how many seats are left
    public int seatsRemaining() {
        return capacity - enrolledStudents.size();
    }

    // Enroll a student if valid, not already enrolled, and seat is available
    public void enrollStudent(StudentAccount student) {
        if (student != null && !enrolledStudents.contains(student) && hasSeatAvailable()) {
            enrolledStudents.add(student);
        }
    }

    // Remove a student from this section
    public void dropStudent(StudentAccount student) {
        enrolledStudents.remove(student);
    }

    // Check whether a student is already enrolled
    public boolean isStudentEnrolled(StudentAccount student) {
        return enrolledStudents.contains(student);
    }

    // Check whether this section conflicts with another section schedule
    public boolean conflictsWith(Section other) {
        return other != null && schedule != null && other.schedule != null
                && schedule.overlaps(other.schedule);
    }

    // Deactivate this section with a reason
    @Override
    public void deactivate(String reason) {
        active = false;
        deactivationReason = reason;
    }

    // Activate this section again
    @Override
    public void activate() {
        active = true;
        deactivationReason = "";
    }

    // Return whether the section is active
    @Override
    public boolean isActive() {
        return active;
    }

    // Return the status of the section
    @Override
    public String getStatus() {
        return active ? "Active" : "Inactive: " + deactivationReason;
    }

    public String getSectionId() {
        return sectionId;
    }

    public Course getCourse() {
        return course;
    }

    public ProfessorAccount getInstructor() {
        return instructor;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getTerm() {
        return term;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<StudentAccount> getEnrolledStudents() {
        return enrolledStudents;
    }
}
