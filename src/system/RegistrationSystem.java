package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.List;

import exceptions.CourseFullException;
import exceptions.InactiveEntityException;
import exceptions.PrerequisiteNotMetException;
import exceptions.ScheduleConflictException;

import model.StudentAccount;
import model.ProfessorAccount;
import model.Schedule;
import model.Course;
import model.Section;
import model.Account;

public class RegistrationSystem {
    // attributes
    private Map<String, StudentAccount> students;
    private Map<String, ProfessorAccount> professors;
    private Map<String, Course> courses;
    private Map<String, Section> sections;

    private static final int MAX_INSTANCES = 100;

    // constructor
    public RegistrationSystem() {
        students = new HashMap<>();
        professors = new HashMap<>();
        courses = new HashMap<>();
        sections = new HashMap<>();
    }
    
    //Helper method
	public boolean accountIdExists(String accountId) {
		return students.containsKey(accountId)
				|| professors.containsKey(accountId);
	}
	
    public Account authenticate(String accountId, String password) {
        if (students.containsKey(accountId)) {
            StudentAccount s = students.get(accountId);
            if (s.login(password)) {
                return s;
            }
        }

        if (professors.containsKey(accountId)) {
            ProfessorAccount p = professors.get(accountId);
            if (p.login(password)) {
                return p;
            }
        }
        return null;
    }

    public void registerStudent(StudentAccount student) {
        if (students.size() >= MAX_INSTANCES) {
            System.out.println("Cannot add more students. Limit reached.");
            return;
        }
        if (accountIdExists(student.getAccountId())) {
			throw new IllegalArgumentException("Account ID already exists.");
		}
		students.put(student.getAccountId(), student);
    }

    public void registerProfessor(ProfessorAccount professor) {
        if (professors.size() >= MAX_INSTANCES) {
            System.out.println("Cannot add more professors. Limit reached.");
            return;
        }
        if (accountIdExists(professor.getAccountId())) {
			throw new IllegalArgumentException("Account ID already exists.");
		}
		professors.put(professor.getAccountId(), professor);
    }

    public void addCourse(Course course) {
        if (courses.size() >= MAX_INSTANCES) {
            System.out.println("Cannot add more courses. Limit reached.");
            return;
        }
        courses.put(course.getCourseCode(), course);
    }

    public void removeCourse(String courseCode) {
        List<String> sectionIdsToRemove = new ArrayList<>();

        for (Section section : sections.values()) {
            if (section.getCourse().getCourseCode().equals(courseCode)) {
                sectionIdsToRemove.add(section.getSectionId());
            }
        }

        for (String sectionId : sectionIdsToRemove) {
            removeSection(sectionId);
        }

        courses.remove(courseCode);
    }

    public void addSection(Section section) {
        if (sections.size() >= MAX_INSTANCES) {
            System.out.println("Cannot add more sections. Limit reached.");
            return;
        }
        sections.put(section.getSectionId(), section);
    }

    public ProfessorAccount getProfessorById(String professorId) {
        return professors.get(professorId);
    }

    public StudentAccount getStudentById(String studentId) {
        return students.get(studentId);
    }

    public Course getCourseByCode(String courseCode) {
        return courses.get(courseCode);
    }

    /** Records that a student has completed a course  */
    public void addCompletedCourseForStudent(String studentId, String courseCode) {
        StudentAccount student = students.get(studentId);
        Course course = courses.get(courseCode);

        if (student == null) {
            System.out.println("Student not found: " + studentId);
            return;
        }
        if (course == null) {
            System.out.println("Course not found: " + courseCode);
            return;
        }

        student.addCompletedCourse(course);
        System.out.println("Recorded completed course " + courseCode + " for " + student.getName() + ".");
    }

    public Section getSectionById(String sectionId) {
        return sections.get(sectionId);
    }

    public List<StudentAccount> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public List<ProfessorAccount> getAllProfessors() {
        return new ArrayList<>(professors.values());
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public List<Section> getAllSections() {
        return new ArrayList<>(sections.values());
    }

    public void removeSection(String sectionId) {
        Section section = sections.get(sectionId);

        if (section == null) {
            return;
        }

        // Remove section from all enrolled students
        List<StudentAccount> enrolledCopy = new ArrayList<>(section.getEnrolledStudents());
        try {
            for (StudentAccount student : enrolledCopy) {
                student.dropSection(section);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Remove section from professor
        ProfessorAccount professor = section.getInstructor();
        if (professor != null) {
            professor.removeSection(section);
        }

        // Finally remove from system map
        sections.remove(sectionId);
    }

    public void assignProfessorToSection(String professorId, String sectionId) {
        ProfessorAccount professor = professors.get(professorId);
        Section section = sections.get(sectionId);

        if (professor == null || section == null) {
            throw new IllegalArgumentException("Invalid professor or section ID.");
        }

        if (!section.isActive()) {
            throw new IllegalArgumentException("Cannot assign professor to an inactive section.");
        }

        if (!professor.canTeachSection(section)) {
            throw new IllegalArgumentException("Professor schedule conflicts with this section.");
        }

        ProfessorAccount previousInstructor = section.getInstructor();
        if (previousInstructor != null) {
            previousInstructor.removeSection(section);
        }

        section.setInstructor(professor);
        professor.assignSection(section);
    }

    public void deactivateSection(String sectionId, String reason) {
        Section section = sections.get(sectionId);
        if (section == null) {
            throw new IllegalArgumentException("Section not found.");
        }
        section.deactivate(reason);
    }

    public void activateSection(String sectionId) {
        Section section = sections.get(sectionId);
        if (section == null) {
            throw new IllegalArgumentException("Section not found.");
        }
        section.activate();
    }

    public boolean enrollStudentInSection(String studentId, String sectionId) {
        StudentAccount student = students.get(studentId);
        Section section = sections.get(sectionId);

        try {
            if (student == null || section == null) {
                System.out.println("Invalid student or section ID.");
                return false;
            }
            student.addSection(section);
            return true;
        } catch (InactiveEntityException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (ScheduleConflictException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (PrerequisiteNotMetException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (CourseFullException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void dropStudentFromSection(String studentId, String sectionId) {
        StudentAccount student = students.get(studentId);
        Section section = sections.get(sectionId);

        try {
            if (student == null || section == null) {
                throw new IllegalArgumentException("Invalid student or section ID.");
            }
            student.dropSection(section);
        } catch (InactiveEntityException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Section> listSectionsByCourse(String courseCode, String term) {
        List<Section> result = new ArrayList<>();

        for (Section section : sections.values()) {
            if (section.getCourse().getCourseCode().equals(courseCode) && section.getTerm().equalsIgnoreCase(term)) {
                result.add(section);
            }
        }
        return result;
    }

    public List<Section> listSectionsByTerm(String term) {
        if (term == null || term.trim().isEmpty()) {
            throw new IllegalArgumentException("Term cannot be empty.");
        }

        List<Section> result = new ArrayList<>();

        for (Section section : sections.values()) {
            if (section.getTerm().equalsIgnoreCase(term)) {
                result.add(section);
            }
        }
        return result;
    }

    public void loadSampleData() {
        // Create students
        StudentAccount s1 = new StudentAccount("s1", "Alice", "alice@email.com", "pass", "CS");
        StudentAccount s2 = new StudentAccount("s2", "Bob", "bob@email.com", "pass", "Math");

        students.put(s1.getAccountId(), s1);
        students.put(s2.getAccountId(), s2);

        // Create professor
        ProfessorAccount p1 = new ProfessorAccount("p1", "Dr. Smith", "smith@email.com", "pass", "CS");
        professors.put(p1.getAccountId(), p1);

        // Create course
        Course c1 = new Course("CS101", "Intro to CS", 3, "Basics of programming");
        courses.put(c1.getCourseCode(), c1);

        // Create schedule
        Schedule sched1 = new Schedule(Set.of("Monday", "Wednesday", "Friday"), 570, 645, "Room 101");

        // Create section
        Section sec1 = new Section("SEC1", c1, p1, sched1, "Fall", 30);
        sections.put(sec1.getSectionId(), sec1);
        p1.assignSection(sec1);
    }

    public String getSystemSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Registration System Summary ===\n");
        sb.append("Total Students: ").append(students.size()).append("\n");
        sb.append("Total Professors: ").append(professors.size()).append("\n");
        sb.append("Total Courses: ").append(courses.size()).append("\n");
        sb.append("Total Sections: ").append(sections.size()).append("\n");
        return sb.toString();
    }
    
	public void removeStudent(String id) {
		if (!students.containsKey(id)) {
			throw new IllegalArgumentException("Student not found.");
		}
		students.remove(id);
	}

	public void removeProfessor(String id) {
		if (!professors.containsKey(id)) {
			throw new IllegalArgumentException("Professor not found.");
		}
		professors.remove(id);
	}
}
