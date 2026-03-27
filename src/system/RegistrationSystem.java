package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Account;
import model.Course;
import model.ProfessorAccount;
import model.Schedule;
import model.Section;
import model.StudentAccount;

public class RegistrationSystem {
    private Map<String, StudentAccount> students;
    private Map<String, ProfessorAccount> professors;
    private Map<String, Course> courses;
    private Map<String, Section> sections;

    private static final int MAX_INSTANCES = 100;

    public RegistrationSystem() {
        students = new HashMap<>();
        professors = new HashMap<>();
        courses = new HashMap<>();
        sections = new HashMap<>();
    }

    public Account authenticate(String accountId, String password) {
        if (students.containsKey(accountId)) {
            StudentAccount s = students.get(accountId);
            if (s.login(password)) return s;
        }
        if (professors.containsKey(accountId)) {
            ProfessorAccount p = professors.get(accountId);
            if (p.login(password)) return p;
        }
        return null;
    }

    public void registerStudent(StudentAccount student) {
        if (students.size() >= MAX_INSTANCES) { System.out.println("Limit reached."); return; }
        students.put(student.getAccountId(), student);
    }

    public void registerProfessor(ProfessorAccount professor) {
        if (professors.size() >= MAX_INSTANCES) { System.out.println("Limit reached."); return; }
        professors.put(professor.getAccountId(), professor);
    }

    public void addCourse(Course course) {
        if (courses.size() >= MAX_INSTANCES) { System.out.println("Limit reached."); return; }
        courses.put(course.getCourseCode(), course);
    }

    public void removeCourse(String courseCode) {
        List<String> toRemove = new ArrayList<>();
        for (Section s : sections.values())
            if (s.getCourse().getCourseCode().equals(courseCode)) toRemove.add(s.getSectionId());
        for (String id : toRemove) removeSection(id);
        courses.remove(courseCode);
    }

    public void addSection(Section section) {
        if (sections.size() >= MAX_INSTANCES) { System.out.println("Limit reached."); return; }
        sections.put(section.getSectionId(), section);
    }

    public void removeSection(String sectionId) {
        Section section = sections.get(sectionId);
        if (section == null) return;
        List<StudentAccount> copy = new ArrayList<>(section.getEnrolledStudents());
        for (StudentAccount student : copy) {
            try { student.dropSection(section); } catch (Exception e) { System.out.println(e.getMessage()); }
        }
        ProfessorAccount prof = section.getInstructor();
        if (prof != null) prof.removeSection(section);
        sections.remove(sectionId);
    }

    public void enrollStudentInSection(String studentId, String sectionId) {
        StudentAccount student = students.get(studentId);
        Section section = sections.get(sectionId);
        if (student == null || section == null) return;
        try { student.addSection(section); } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    public void dropStudentFromSection(String studentId, String sectionId) {
        StudentAccount student = students.get(studentId);
        Section section = sections.get(sectionId);
        if (student == null || section == null) return;
        try { student.dropSection(section); } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    public List<Section> listSectionsByCourse(String courseCode) {
        List<Section> result = new ArrayList<>();
        for (Section s : sections.values())
            if (s.getCourse().getCourseCode().equals(courseCode)) result.add(s);
        return result;
    }

    public List<Section> listSectionsByTerm(String term) {
        List<Section> result = new ArrayList<>();
        for (Section s : sections.values())
            if (s.getTerm().equalsIgnoreCase(term)) result.add(s);
        return result;
    }

    // Lookup helpers
    public StudentAccount getStudent(String id) { return students.get(id); }
    public ProfessorAccount getProfessor(String id) { return professors.get(id); }
    public Course getCourse(String code) { return courses.get(code); }
    public Section getSection(String id) { return sections.get(id); }
    public Map<String, StudentAccount> getAllStudents() { return students; }
    public Map<String, ProfessorAccount> getAllProfessors() { return professors; }
    public Map<String, Course> getAllCourses() { return courses; }
    public Map<String, Section> getAllSections() { return sections; }

    public void loadSampleData() {
        StudentAccount s1 = new StudentAccount("s1", "Alice", "alice@email.com", "pass", "CS");
        StudentAccount s2 = new StudentAccount("s2", "Bob", "bob@email.com", "pass", "Math");
        students.put(s1.getAccountId(), s1);
        students.put(s2.getAccountId(), s2);

        ProfessorAccount p1 = new ProfessorAccount("p1", "Dr. Smith", "smith@email.com", "pass", "CS");
        professors.put(p1.getAccountId(), p1);

        Course c1 = new Course("CS101", "Intro to CS", 3, "Basics of programming");
        Course c2 = new Course("CS201", "Data Structures", 3, "Arrays, lists, trees");
        c2.addPrerequisite(c1);
        courses.put(c1.getCourseCode(), c1);
        courses.put(c2.getCourseCode(), c2);

        Schedule sched1 = new Schedule(Set.of("Monday", "Wednesday", "Friday"), 480, 570, "Room 101");
        Section sec1 = new Section("SEC1", c1, p1, sched1, "Fall", 30);
        p1.assignSection(sec1);
        sections.put(sec1.getSectionId(), sec1);
    }

    public String getSystemSummary() {
        long activeStudents = students.values().stream().filter(s -> s.isActive()).count();
        long activeProfessors = professors.values().stream().filter(p -> p.isActive()).count();
        long activeSections = sections.values().stream().filter(s -> s.isActive()).count();
        return "=== Registration System Summary ===\n"
            + "Total Students: " + students.size() + " (Active: " + activeStudents + ")\n"
            + "Total Professors: " + professors.size() + " (Active: " + activeProfessors + ")\n"
            + "Total Courses: " + courses.size() + "\n"
            + "Total Sections: " + sections.size() + " (Active: " + activeSections + ")\n";
    }
}