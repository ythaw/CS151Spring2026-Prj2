package model;

import java.util.ArrayList;
import java.util.List;

public class StudentAccount extends Account{
    private String major;
    private int currentCredits;
    private List<Section> enrolledSections;
    private List<Course> completeCourses;

    public StudentAccount(String major, int currentCredits){
      this.major = major;
      this.currentCredits = currentCredits;
      this.enrolledSections = new ArrayList<>();
      this.completeCourses = new ArrayList<>();
    }
    
    public int CalculateCurrentCreditLoad(){
      int total = 0;
      for (Section s : enrolledSections){
        total += s.getCredits();
      }
      return total;
    }

    public boolean hasTimeConflict(Section target){
      for (Section s : enrolledSections){
        if (s.conflictsWith(target)) return true;
      }
      return false;
    }

    public void addSection(Section section){
      
    }

    public void dropSection(Section section){

    }

    public void addCompletedCourse(Course course){

    }

    public List<Section> getSectionsByTerm(String term){
      return new ArrayList<>();
    }

    public boolean canEnrollIn(Section section){
      return false;
    }
}
