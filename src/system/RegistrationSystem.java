package system;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import model.StudentAccount;
import model.ProfessorAccount;
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
    public RegistrationSystem(){
        students = new HashMap<>();
        professors = new HashMap<>();
        courses = new HashMap<>();
        sections = new HashMap<>();
    }

    public Account authenticate(String accountId, String password){
        return null;
    }

    public void registerStudent(StudentAccount student){

    }

    public void registerProfessor(ProfessorAccount professor){

    }

    public void addCourse(Course course){

    }
    public void removeCourse(String courseCode){

    }

    public void addSection(Section section){

    }

    public void removeSection(String sectionId){

    }

    public void enrollStudentInSection(String studentId, String sectionId){

    }

    public void dropStudentInSection(String studentId, String sectionId){

    }

    public List<Section> listSectionsByCourse(String courseCode){
        return null;
    }

    public List<Section> listSectionsByTerm(String term){
        return null;
    }
}
