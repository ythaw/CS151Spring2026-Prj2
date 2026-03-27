package model;

import java.util.ArrayList;
import java.util.List;

public class ProfessorAccount extends Account {
    private String department;
    private List<Section> teachingSections;

    public ProfessorAccount(String accountId, String name, String email, String password, String department) {
        super(accountId, name, email, password);

        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }

        this.department = department;
        this.teachingSections = new ArrayList<>();
    }

	// Assign a section to professor
    public void assignSection(Section section) {
        if (section == null) {
            throw new IllegalArgumentException("Section cannot be null.");
        }
        if (!teachingSections.contains(section)) {
            teachingSections.add(section);
        }
    }

	// Remove a section from professor
    public void removeSection(Section section) {
        teachingSections.remove(section);
    }

	// Calculate total teaching credits

    public int calculateTeachingLoadCredits() {
        int totalCredits = 0;
        for (Section section : teachingSections) {
            if (section != null && section.getCourse() != null) {
                totalCredits += section.getCourse().getCredits();
            }
        }
        return totalCredits;
    }
    
	// View student roster of a section
	public List<StudentAccount> viewRoster(Section section) {
		if (section == null) {
			throw new IllegalArgumentException("Section cannot be null.");
		}
		if (!teachingSections.contains(section)) {
			throw new IllegalArgumentException("Professor is not assigned to this section.");
		}
		return section.getEnrolledStudents();
	}

	// Check if section schedule conflicts with current teaching schedule
	public boolean hasScheduleConflict(Section section) {
		if (section == null || section.getSchedule() == null) {
			return false;
		}
		
		for (Section currentSection : teachingSections) {
			if (currentSection != null && currentSection.getSchedule() != null) {
				if (currentSection.getSchedule().overlaps(section.getSchedule())) {
					return true;
				}
			}
		}
		return false;
	}

	// Get sections by term
	public List<Section> getSectionsByTerm(String term) {
		if (term == null || term.trim().isEmpty()) {
			throw new IllegalArgumentException("Term cannot be empty.");
		}

		List<Section> result = new ArrayList<>();

		for (Section section : teachingSections) {
			if (section != null && section.getTerm().equalsIgnoreCase(term)) {
				result.add(section);
			}
		}

		return result;
	}

	// Check if professor can teach this section
    public boolean canTeachSection(Section section) {
        if (section == null) {
            return false;
        }
        return !hasScheduleConflict(section);
    }

    public String getDepartment() {
        return department;
    }

    public List<Section> getTeachingSections() {
        return teachingSections;
    }

    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        this.department = department;
    }

	// Return account role
    @Override
    public String getRole() {
        return "Professor";
    }

    @Override
    public String getContactCard() {
        return super.getContactCard() + "\nRole: " + getRole() + "\nDepartment: " + department;
    }

    @Override
    public String toString() {
        return "Professor ID: " + getAccountId()
                + ", Name: " + getName()
                + ", Department: " + department
                + ", Teaching Load: " + calculateTeachingLoadCredits() + " credits";
    }
}
