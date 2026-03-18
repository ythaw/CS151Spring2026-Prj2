package model;

import java.util.ArrayList;
import java.util.List;

public class StudentAccount {
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
}
