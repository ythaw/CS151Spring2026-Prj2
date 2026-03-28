package model;

import exceptions.CourseFullException;
import exceptions.InactiveEntityException;
import exceptions.PrerequisiteNotMetException;
import exceptions.ScheduleConflictException;
import java.util.ArrayList;
import java.util.List;

public class StudentAccount extends Account {
    private String major;
    private int currentCredits;
    private List<Section> enrolledSections;
    private List<Course> completedCourses;

    public StudentAccount(String accountId, String name, String email, String password, String major) {
        super(accountId, name, email, password);
        
        if (major == null || major.trim().isEmpty()) {
        throw new IllegalArgumentException("Major cannot be empty.");
		}
    
        this.major = major.trim();
        this.currentCredits = 0;
        this.enrolledSections = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
    }

    // Returns the total credits from all currently enrolled sections
    public int calculateCurrentCreditLoad() {
        int total = 0;
        for (Section s : enrolledSections) {
            total += s.getCourse().getCredits();
        }
        setCurrentCredits(total);
        return total;
    }

    // Returns true if any enrolled section conflicts with the target section's schedule
    public boolean hasTimeConflict(Section target) {
        for (Section s : enrolledSections) {
            if (s.conflictsWith(target)) return true;
        }
        return false;
    }

    // Enrolls student in a section after validating all conditions
    public void addSection(Section section)
            throws InactiveEntityException, ScheduleConflictException,
                   PrerequisiteNotMetException, CourseFullException {
        if(canEnrollIn(section)){
            enrolledSections.add(section);
            section.enrollStudent(this);
            setCurrentCredits(currentCredits + section.getCourse().getCredits());
        }

    }

    // Drops a section and updates credit count
    public void dropSection(Section section) throws InactiveEntityException {
        if (!enrolledSections.contains(section)) {
            throw new InactiveEntityException("Student is not enrolled in section " + section.getSectionId() + ".");
        }
        enrolledSections.remove(section);
        section.dropStudent(this);
        setCurrentCredits(currentCredits - section.getCourse().getCredits());
    }

    // Adds a course to the student's completed courses if not already there
    public void addCompletedCourse(Course course) {
        if (course != null && !completedCourses.contains(course)) {
            completedCourses.add(course);
        }
    }

    // Returns all enrolled sections for a given term
    public List<Section> getSectionsByTerm(String term) {
        List<Section> result = new ArrayList<>();
        for (Section s : enrolledSections) {
            if (s.getTerm().equalsIgnoreCase(term)) {
                result.add(s);
            }
        }
        return result;
    }

    // Returns true if the student can enroll in the given section
    public boolean canEnrollIn(Section section) throws InactiveEntityException, ScheduleConflictException,
                                  PrerequisiteNotMetException, CourseFullException {
        if (!section.isActive()) {
            throw new InactiveEntityException("Section " + section.getSectionId() + " is not active.");
        }
        if (hasTimeConflict(section)) {
            throw new ScheduleConflictException("Schedule conflict with section " + section.getSectionId() + ".");
        }
        if (!section.getCourse().prerequisitesMetBy(completedCourses)) {
            throw new PrerequisiteNotMetException("Prerequisites not met for " + section.getCourse().getTitle() + ".");
        }
        if (!section.hasSeatAvailable()) {
            throw new CourseFullException("Section " + section.getSectionId() + " is full.");
        }
        return true;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String getContactCard() {
        return super.getContactCard() + "\nRole: " + getRole() + "\nMajor: " + major;
    }

    // Getters
    public String getMajor() { return major; }
    public int getCurrentCredits() { return currentCredits; }
    public List<Section> getEnrolledSections() { return enrolledSections; }
    public List<Course> getCompletedCourses() { return completedCourses; }

    // Setters
    public void setMajor(String major) {
        if (major == null || major.trim().isEmpty()) {
            throw new IllegalArgumentException("Major cannot be empty.");
        }
        this.major = major;
    }

    public void setCurrentCredits(int currentCredits) {
        if (currentCredits < 0) {
            throw new IllegalArgumentException("Credits cannot be negative.");
        }
        this.currentCredits = currentCredits;
    }

	@Override
	public String toString() {
		return "[Student] " + getName()
				+ " (ID: " + getAccountId() + ")"
				+ " | Email: " + getEmail()
				+ " | Major: " + major
				+ " | Credits: " + calculateCurrentCreditLoad()
				+ " | Status: " + getStatus();
	}
}
