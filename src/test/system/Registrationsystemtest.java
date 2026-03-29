package test.system;

import exceptions.InactiveEntityException;
import model.*;
import system.RegistrationSystem;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationSystemTest {

    private RegistrationSystem system;
    private StudentAccount student;
    private ProfessorAccount professor;
    private Course course;
    private Section section;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        system = new RegistrationSystem();

        student   = new StudentAccount("stu1", "Alice", "alice@email.com", "pass", "CS");
        professor = new ProfessorAccount("prof1", "Dr. Smith", "smith@email.com", "pass", "CS");
        course    = new Course("CS101", "Intro to CS", 3, "Basics");
        schedule  = new Schedule(Set.of("Monday", "Wednesday"), 540, 630, "Room 101");
        section   = new Section("SEC1", course, professor, schedule, "Fall", 30);

        system.registerStudent(student);
        system.registerProfessor(professor);
        system.addCourse(course);
        system.addSection(section);
    }

    // ─────────────────────────────────────────────────────────────
    // accountIdExists
    // ─────────────────────────────────────────────────────────────

    @Test
    void accountIdExists_returnsTrueForRegisteredStudent() {
        assertTrue(system.accountIdExists("stu1"));
    }

    @Test
    void accountIdExists_returnsTrueForRegisteredProfessor() {
        assertTrue(system.accountIdExists("prof1"));
    }

    @Test
    void accountIdExists_returnsFalseForUnknownId() {
        assertFalse(system.accountIdExists("unknown99"));
    }

    // ─────────────────────────────────────────────────────────────
    // authenticate
    // ─────────────────────────────────────────────────────────────

    @Test
    void authenticate_validStudentCredentials_returnsAccount() {
        Account result = system.authenticate("stu1", "pass");
        assertNotNull(result);
        assertInstanceOf(StudentAccount.class, result);
    }

    @Test
    void authenticate_validProfessorCredentials_returnsAccount() {
        Account result = system.authenticate("prof1", "pass");
        assertNotNull(result);
        assertInstanceOf(ProfessorAccount.class, result);
    }

    @Test
    void authenticate_wrongPassword_returnsNull() {
        assertNull(system.authenticate("stu1", "wrongpass"));
    }

    @Test
    void authenticate_unknownId_returnsNull() {
        assertNull(system.authenticate("nobody", "pass"));
    }

    // ─────────────────────────────────────────────────────────────
    // registerStudent / registerProfessor
    // ─────────────────────────────────────────────────────────────

    @Test
    void registerStudent_duplicateId_throwsIllegalArgument() {
        StudentAccount duplicate = new StudentAccount("stu1", "Bob", "bob@email.com", "pass", "Math");
        assertThrows(IllegalArgumentException.class, () -> system.registerStudent(duplicate));
    }

    @Test
    void registerProfessor_duplicateId_throwsIllegalArgument() {
        ProfessorAccount duplicate = new ProfessorAccount("prof1", "Dr. Jones", "jones@email.com", "pass", "Math");
        assertThrows(IllegalArgumentException.class, () -> system.registerProfessor(duplicate));
    }

    @Test
    void registerStudent_newStudent_appearsInGetAllStudents() {
        StudentAccount s2 = new StudentAccount("stu2", "Bob", "bob@email.com", "pass", "Math");
        system.registerStudent(s2);
        assertTrue(system.getAllStudents().stream()
                .anyMatch(s -> s.getAccountId().equals("stu2")));
    }

    // ─────────────────────────────────────────────────────────────
    // addCourse / removeCourse
    // ─────────────────────────────────────────────────────────────

    @Test
    void addCourse_courseLookupSucceeds() {
        assertNotNull(system.getCourseByCode("CS101"));
    }

    @Test
    void removeCourse_courseNoLongerExists() {
        // Need a course without sections to avoid cascade removal complexity
        Course c2 = new Course("MATH101", "Calculus", 4, "Math");
        system.addCourse(c2);
        system.removeCourse("MATH101");
        assertNull(system.getCourseByCode("MATH101"));
    }

    @Test
    void removeCourse_alsoCascadeRemovesItsSection() {
        // SEC1 belongs to CS101
        system.removeCourse("CS101");
        assertNull(system.getSectionById("SEC1"));
    }

    // ─────────────────────────────────────────────────────────────
    // addSection / removeSection / getSectionById
    // ─────────────────────────────────────────────────────────────

    @Test
    void getSectionById_existingSection_returnsSection() {
        assertNotNull(system.getSectionById("SEC1"));
    }

    @Test
    void getSectionById_unknownId_returnsNull() {
        assertNull(system.getSectionById("FAKE"));
    }

    @Test
    void removeSection_sectionNoLongerExists() {
        system.removeSection("SEC1");
        assertNull(system.getSectionById("SEC1"));
    }

    @Test
    void removeSection_unknownId_doesNotThrow() {
        assertDoesNotThrow(() -> system.removeSection("DOESNOTEXIST"));
    }

    // ─────────────────────────────────────────────────────────────
    // enrollStudentInSection
    // ─────────────────────────────────────────────────────────────

    @Test
    void enrollStudentInSection_validEnrollment_returnsTrue() {
        boolean result = system.enrollStudentInSection("stu1", "SEC1");
        assertTrue(result);
    }

    @Test
    void enrollStudentInSection_invalidStudentId_returnsFalse() {
        assertFalse(system.enrollStudentInSection("ghost", "SEC1"));
    }

    @Test
    void enrollStudentInSection_invalidSectionId_returnsFalse() {
        assertFalse(system.enrollStudentInSection("stu1", "GHOST"));
    }

    @Test
    void enrollStudentInSection_inactiveSection_returnsFalse() {
        system.deactivateSection("SEC1", "Cancelled");
        assertFalse(system.enrollStudentInSection("stu1", "SEC1"));
    }

    @Test
    void enrollStudentInSection_duplicateEnrollment_returnsFalse() {
        system.enrollStudentInSection("stu1", "SEC1");
        assertFalse(system.enrollStudentInSection("stu1", "SEC1"));
    }

    @Test
    void enrollStudentInSection_sectionFull_returnsFalse() {
        // Create a section with capacity 1 and fill it with a different student
        StudentAccount filler = new StudentAccount("filler", "Filler", "filler@email.com", "pass", "CS");
        system.registerStudent(filler);
        Section tinySection = new Section("TINY", course, professor, schedule, "Spring", 1);
        system.addSection(tinySection);

        system.enrollStudentInSection("filler", "TINY");         
        assertFalse(system.enrollStudentInSection("stu1", "TINY")); 
    }

    @Test
    void enrollStudentInSection_scheduleConflict_returnsFalse() {
        // Enroll in SEC1 first
        system.enrollStudentInSection("stu1", "SEC1");

        // Create a second section with an overlapping schedule
        Schedule conflicting = new Schedule(Set.of("Monday"), 560, 620, "Room 202");
        Section sec2 = new Section("SEC2", course, professor, conflicting, "Fall", 30);
        system.addSection(sec2);

        assertFalse(system.enrollStudentInSection("stu1", "SEC2"));
    }

    @Test
    void enrollStudentInSection_prerequisiteNotMet_returnsFalse() {
        Course advanced = new Course("CS201", "Data Structures", 3, "Advanced");
        advanced.addPrerequisite(course);   // CS101 is a prerequisite
        system.addCourse(advanced);

        Schedule sched2 = new Schedule(Set.of("Tuesday", "Thursday"), 480, 570, "Room 202");
        Section advSection = new Section("ADV1", advanced, professor, sched2, "Fall", 30);
        system.addSection(advSection);

        // student has NOT completed CS101 — prerequisite not met
        assertFalse(system.enrollStudentInSection("stu1", "ADV1"));
    }

    // ─────────────────────────────────────────────────────────────
    // dropStudentFromSection
    // ─────────────────────────────────────────────────────────────

    @Test
    void dropStudentFromSection_enrolledStudent_dropsSuccessfully() throws InactiveEntityException {
        system.enrollStudentInSection("stu1", "SEC1");
        system.dropStudentFromSection("stu1", "SEC1");
        assertFalse(system.getSectionById("SEC1").isStudentEnrolled(student));
    }

    @Test
    void dropStudentFromSection_notEnrolled_throwsInactiveEntityException() {
        assertThrows(InactiveEntityException.class,
                () -> system.dropStudentFromSection("stu1", "SEC1"));
        }

    // ─────────────────────────────────────────────────────────────
    // assignProfessorToSection
    // ─────────────────────────────────────────────────────────────

    @Test
    void assignProfessorToSection_valid_professorsTeachingSectionUpdated() {
        ProfessorAccount prof2 = new ProfessorAccount("prof2", "Dr. Lee", "lee@email.com", "pass", "Math");
        system.registerProfessor(prof2);

        Schedule sched2 = new Schedule(Set.of("Tuesday"), 480, 570, "Room 202");
        Section sec2 = new Section("SEC2", course, null, sched2, "Fall", 30);
        system.addSection(sec2);

        system.assignProfessorToSection("prof2", "SEC2");
        assertEquals(prof2, system.getSectionById("SEC2").getInstructor());
    }

    @Test
    void assignProfessorToSection_invalidIds_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> system.assignProfessorToSection("ghost", "SEC1"));
    }

    @Test
    void assignProfessorToSection_inactiveSection_throwsIllegalArgument() {
        system.deactivateSection("SEC1", "Cancelled");
        assertThrows(IllegalArgumentException.class,
                () -> system.assignProfessorToSection("prof1", "SEC1"));
    }

    // ─────────────────────────────────────────────────────────────
    // deactivateSection / activateSection
    // ─────────────────────────────────────────────────────────────

    @Test
    void deactivateSection_sectionBecomesInactive() {
        system.deactivateSection("SEC1", "Low enrollment");
        assertFalse(system.getSectionById("SEC1").isActive());
    }

    @Test
    void activateSection_reactivatesDeactivatedSection() {
        system.deactivateSection("SEC1", "Low enrollment");
        system.activateSection("SEC1");
        assertTrue(system.getSectionById("SEC1").isActive());
    }

    @Test
    void deactivateSection_unknownId_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> system.deactivateSection("GHOST", "reason"));
    }

    // ─────────────────────────────────────────────────────────────
    // listSectionsByCourse / listSectionsByTerm
    // ─────────────────────────────────────────────────────────────

    @Test
    void listSectionsByCourse_matchingCourseAndTerm_returnsSection() {
        List<Section> result = system.listSectionsByCourse("CS101", "Fall");
        assertEquals(1, result.size());
        assertEquals("SEC1", result.get(0).getSectionId());
    }

    @Test
    void listSectionsByCourse_wrongTerm_returnsEmpty() {
        List<Section> result = system.listSectionsByCourse("CS101", "Spring");
        assertTrue(result.isEmpty());
    }

    @Test
    void listSectionsByTerm_matchingTerm_returnsSection() {
        List<Section> result = system.listSectionsByTerm("Fall");
        assertFalse(result.isEmpty());
    }

    @Test
    void listSectionsByTerm_noMatch_returnsEmpty() {
        List<Section> result = system.listSectionsByTerm("Summer");
        assertTrue(result.isEmpty());
    }

    @Test
    void listSectionsByTerm_nullTerm_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> system.listSectionsByTerm(null));
    }

    @Test
    void listSectionsByTerm_emptyTerm_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> system.listSectionsByTerm("  "));
    }

    // ─────────────────────────────────────────────────────────────
    // addCompletedCourseForStudent
    // ─────────────────────────────────────────────────────────────

    @Test
    void addCompletedCourseForStudent_valid_courseAppearsInCompletedList() {
        system.addCompletedCourseForStudent("stu1", "CS101");
        assertTrue(student.getCompletedCourses().contains(course));
    }

    @Test
    void addCompletedCourseForStudent_unknownStudent_doesNotThrow() {
        assertDoesNotThrow(() -> system.addCompletedCourseForStudent("ghost", "CS101"));
    }

    @Test
    void addCompletedCourseForStudent_unknownCourse_doesNotThrow() {
        assertDoesNotThrow(() -> system.addCompletedCourseForStudent("stu1", "FAKE999"));
    }

    // ─────────────────────────────────────────────────────────────
    // removeStudent / removeProfessor
    // ─────────────────────────────────────────────────────────────

    @Test
    void removeStudent_existingStudent_isRemovedFromSystem() {
        system.removeStudent("stu1");
        assertFalse(system.accountIdExists("stu1"));
    }

    @Test
    void removeStudent_unknownId_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> system.removeStudent("ghost"));
    }

    @Test
    void removeProfessor_existingProfessor_isRemovedFromSystem() {
        system.removeProfessor("prof1");
        assertFalse(system.accountIdExists("prof1"));
    }

    @Test
    void removeProfessor_unknownId_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> system.removeProfessor("ghost"));
    }

    // ─────────────────────────────────────────────────────────────
    // getSystemSummary
    // ─────────────────────────────────────────────────────────────

    @Test
    void getSystemSummary_containsCountInfo() {
        String summary = system.getSystemSummary();
        assertTrue(summary.contains("Total Students"));
        assertTrue(summary.contains("Total Professors"));
        assertTrue(summary.contains("Total Courses"));
        assertTrue(summary.contains("Total Sections"));
    }

    // ─────────────────────────────────────────────────────────────
    // getAllStudents / getAllProfessors / getAllCourses / getAllSections
    // ─────────────────────────────────────────────────────────────

    @Test
    void getAllStudents_returnsRegisteredStudents() {
        assertEquals(1, system.getAllStudents().size());
    }

    @Test
    void getAllProfessors_returnsRegisteredProfessors() {
        assertEquals(1, system.getAllProfessors().size());
    }

    @Test
    void getAllCourses_returnsAddedCourses() {
        assertEquals(1, system.getAllCourses().size());
    }

    @Test
    void getAllSections_returnsAddedSections() {
        assertEquals(1, system.getAllSections().size());
    }

    // ─────────────────────────────────────────────────────────────
    // MAX_INSTANCES (100) — RegistrationSystem requirement
    // ─────────────────────────────────────────────────────────────

    private static final int MAX_INSTANCES = 100;

    @Test
    void registerStudent_atMaxInstances_rejects101st() {
        RegistrationSystem sys = new RegistrationSystem();
        for (int i = 0; i < MAX_INSTANCES; i++) {
            sys.registerStudent(new StudentAccount(
                    "s" + i, "Student" + i, "s" + i + "@test.edu", "pass", "CS"));
        }
        assertEquals(MAX_INSTANCES, sys.getAllStudents().size());

        sys.registerStudent(new StudentAccount(
                "s_extra", "Should Not Add", "extra@test.edu", "pass", "CS"));

        assertEquals(MAX_INSTANCES, sys.getAllStudents().size());
        assertFalse(sys.accountIdExists("s_extra"));
    }

    @Test
    void registerProfessor_atMaxInstances_rejects101st() {
        RegistrationSystem sys = new RegistrationSystem();
        for (int i = 0; i < MAX_INSTANCES; i++) {
            sys.registerProfessor(new ProfessorAccount(
                    "p" + i, "Prof" + i, "p" + i + "@test.edu", "pass", "CS"));
        }
        assertEquals(MAX_INSTANCES, sys.getAllProfessors().size());

        sys.registerProfessor(new ProfessorAccount(
                "p_extra", "Should Not Add", "extra@test.edu", "pass", "CS"));

        assertEquals(MAX_INSTANCES, sys.getAllProfessors().size());
        assertFalse(sys.accountIdExists("p_extra"));
    }

    @Test
    void addCourse_atMaxInstances_rejects101st() {
        RegistrationSystem sys = new RegistrationSystem();
        for (int i = 0; i < MAX_INSTANCES; i++) {
            String code = String.format("C%03d", i);
            sys.addCourse(new Course(code, "Title " + i, 3, "Description"));
        }
        assertEquals(MAX_INSTANCES, sys.getAllCourses().size());

        sys.addCourse(new Course("C_EXTRA", "Should Not Add", 3, "N/A"));

        assertEquals(MAX_INSTANCES, sys.getAllCourses().size());
        assertNull(sys.getCourseByCode("C_EXTRA"));
    }

    @Test
    void addSection_atMaxInstances_rejects101st() {
        RegistrationSystem sys = new RegistrationSystem();
        Course course = new Course("CS100", "Shared", 3, "For sections");
        ProfessorAccount prof = new ProfessorAccount("prof0", "T", "t@test.edu", "pass", "CS");
        Schedule schedule = new Schedule(Set.of("Tuesday", "Thursday"), 480, 570, "Room A");
        sys.addCourse(course);
        sys.registerProfessor(prof);

        for (int i = 0; i < MAX_INSTANCES; i++) {
            sys.addSection(new Section("SEC" + i, course, prof, schedule, "Fall", 30));
        }
        assertEquals(MAX_INSTANCES, sys.getAllSections().size());

        sys.addSection(new Section("SEC_EXTRA", course, prof, schedule, "Fall", 30));

        assertEquals(MAX_INSTANCES, sys.getAllSections().size());
        assertNull(sys.getSectionById("SEC_EXTRA"));
    }
}