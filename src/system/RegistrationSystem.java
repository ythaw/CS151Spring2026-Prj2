package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import java.util.List;

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

    // constructor
    public RegistrationSystem() {
        students = new HashMap<>();
        professors = new HashMap<>();
        courses = new HashMap<>();
        sections = new HashMap<>();
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
        students.put(student.getAccountId(), student);
    }

    public void registerProfessor(ProfessorAccount professor) {
        professors.put(professor.getAccountId(), professor);
    }

    public void addCourse(Course course) {
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
        sections.put(section.getSectionId(), section);
    }

    public void removeSection(String sectionId) {
        Section section = sections.get(sectionId);

        if (section == null) {
            return;
        }

        // Remove section from all enrolled students
        List<StudentAccount> enrolledCopy = new ArrayList<>(section.getEnrolledStudents());
        try{
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

    public void enrollStudentInSection(String studentId, String sectionId) {
        StudentAccount student = students.get(studentId);
        Section section = sections.get(sectionId);

        if (student == null || section == null) {
            return;
        }
        try {
            student.addSection(section);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropStudentFromSection(String studentId, String sectionId) {
        StudentAccount student = students.get(studentId);
        Section section = sections.get(sectionId);

        if (student == null || section == null) {
            return;
        }

        try {
            student.dropSection(section);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    public List<Section> listSectionsByCourse(String courseCode) {
        List<Section> result = new ArrayList<>();

        for (Section section : sections.values()) {
            if (section.getCourse().getCourseCode().equals(courseCode)) {
                result.add(section);
            }
        }
        return result;
    }

    public List<Section> listSectionsByTerm(String term) {
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
        Schedule sched1 = new Schedule(Set.of("Monday", "Wednesday", "Friday"), 0, 45, "Room 101");

        // Create section
        Section sec1 = new Section("SEC1", c1, p1, sched1, "Fall", 30);
        sections.put(sec1.getSectionId(), sec1);
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
}
