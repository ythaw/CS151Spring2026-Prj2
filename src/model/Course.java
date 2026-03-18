package model;
import java.util.List;

public class Course {
    private String courseCode;
    private String title;
    private int credits;
    private String description;
    private List<Course> prerequisites;

    public void addPrerequisite(Course course) {
        prerequisites.add(course);
    }
    public void removePrerequisite(Course course) {
        prerequisites.remove(course);
    }
    public boolean prerequisitesMetBy(List<Course> completedCourses) {
        return completedCourses.containsAll(prerequisites);
    }
    public boolean hasPrerequisites() {
        return !prerequisites.isEmpty();
    }
    public String getCatalogSummary() {
        return courseCode + ": " + title + " (" + credits + " credits)\n" + "prerequisites: " + 
        (hasPrerequisites()? prerequisites.stream().map(c -> c.courseCode).reduce((a,b) -> a + ", " + b).orElse("None") : "None"
        + "\n" + description);
    }
    public boolean isValidCreditValue(){
        return credits > 0 && credits <= 6;
    }

}
