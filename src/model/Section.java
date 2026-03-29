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

    // Create section
    public Section(String sectionId, Course course, ProfessorAccount instructor,
                   Schedule schedule, String term, int capacity) {
        if (sectionId == null || sectionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Section ID cannot be empty.");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null.");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null.");
        }
        if (term == null || term.trim().isEmpty()) {
            throw new IllegalArgumentException("Term cannot be empty.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }

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

    // Check if section has available seats
    public boolean hasSeatAvailable() {
        return enrolledStudents.size() < capacity;
    }

    // Return number of remaining seats
    public int seatsRemaining() {
        return capacity - enrolledStudents.size();
    }

    // Enroll a student in the section
    public void enrollStudent(StudentAccount student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        if (!active) {
            throw new IllegalArgumentException("Cannot enroll in an inactive section.");
        }
        if (enrolledStudents.contains(student)) {
            throw new IllegalArgumentException("Student is already enrolled in this section.");
        }
        if (!hasSeatAvailable()) {
            throw new IllegalArgumentException("Section is full.");
        }

        enrolledStudents.add(student);
    }

    // Drop a student from the section
    public void dropStudent(StudentAccount student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        if (!enrolledStudents.contains(student)) {
            throw new IllegalArgumentException("Student is not enrolled in this section.");
        }

        enrolledStudents.remove(student);
    }

    // Check if a student is enrolled
    public boolean isStudentEnrolled(StudentAccount student) {
        return enrolledStudents.contains(student);
    }

    // Check if this section conflicts with another section
    public boolean conflictsWith(Section other) {
        return other != null
                && schedule != null
                && other.schedule != null
                && schedule.overlaps(other.schedule);
    }

    // Check if this section is taught by a given professor
    public boolean isTaughtBy(ProfessorAccount professor) {
        return instructor != null && instructor.equals(professor);
    }

    // Deactivate section with reason
    @Override
    public void deactivate(String reason) {
        active = false;
        deactivationReason = (reason == null) ? "" : reason;
    }

    // Activate section
    @Override
    public void activate() {
        active = true;
        deactivationReason = "";
    }

    // Check if section is active
    @Override
    public boolean isActive() {
        return active;
    }

    // Return section status
    @Override
    public String getStatus() {
        return active ? "Active" : "Inactive: " + deactivationReason;
    }

    // Get section ID
    public String getSectionId() {
        return sectionId;
    }

    // Get course
    public Course getCourse() {
        return course;
    }

    // Get instructor
    public ProfessorAccount getInstructor() {
        return instructor;
    }

    // Get schedule
    public Schedule getSchedule() {
        return schedule;
    }

    // Get term
    public String getTerm() {
        return term;
    }

    // Get capacity
    public int getCapacity() {
        return capacity;
    }

    // Get enrolled students
    public List<StudentAccount> getEnrolledStudents() {
        return enrolledStudents;
    }

    // Set instructor
    public void setInstructor(ProfessorAccount instructor) {
        this.instructor = instructor;
    }

    // Set schedule
    public void setSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null.");
        }
        if(!schedule.isValid()) {
            throw new IllegalArgumentException("Schedule is not valid.");
        }
        this.schedule = schedule;
    }

    // Set capacity
    public void setCapacity(int capacity) {
        if (capacity < enrolledStudents.size()) {
            throw new IllegalArgumentException("Capacity cannot be less than enrolled students.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
    }

	@Override
	public String toString() {
		String instructorName = (instructor == null) ? "TBA" : instructor.getName();
		String scheduleInfo = (schedule == null) ? "TBA" : schedule.toDisplayString();

		return "Section ID: " + sectionId
				+ "| Course: " + course
				+ "| Term: " + term + "\n"
				+ "Instructor: " + instructorName
				+ "| Schedule: " + scheduleInfo 
				+ "\nSeats Remaining: " + seatsRemaining()
				+ "| Status: " + getStatus();
	}
}
