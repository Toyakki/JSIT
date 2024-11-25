package interface_adapters.StudentCourse;

import java.util.ArrayList;
import java.util.Map;

public class StudentCourseState {
    private String email;
    private String courseName;
    private ArrayList<String> assignmentsName;
    private ArrayList<String> assignmentsStage;
    private ArrayList<String> assignmentsDueDates;
    private ArrayList<String> assignmentsMarks;
    private ArrayList<Float> assignmentsMarksRecived;

    public String getCourseName() {return this.courseName;}

    public ArrayList<String> getAssignmentsNames() {return this.assignmentsName;}

    public ArrayList<String> getAssignmentsStages() {return this.assignmentsStage;}

    public ArrayList<String> getAssignmentsDueDates() {return this.assignmentsDueDates;}

    public ArrayList<String> getAssignmentsMarks() {return this.assignmentsMarks;}

    public ArrayList<Float> getAssignmentsMarksRecived() {return this.assignmentsMarksRecived;}
}
