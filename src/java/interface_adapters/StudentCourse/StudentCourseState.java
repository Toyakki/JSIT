package interface_adapters.StudentCourse;

import java.util.ArrayList;
import java.util.Map;

public class StudentCourseState {
    private String CourseName;
    private ArrayList<String> AssignmentsName;
    private ArrayList<String> AssignmentsStage;
    private ArrayList<String> AssignmentsDueDates;
    private ArrayList<String> AssignmentsMarks;
    private ArrayList<Float> AssignmentsMarksRecived;

    public String getCourseName() {return this.CourseName;}

    public ArrayList<String> getAssignmentsNames() {return this.AssignmentsName;}

    public ArrayList<String> getAssignmentsStages() {return this.AssignmentsStage;}

    public ArrayList<String> getAssignmentsDueDates() {return this.AssignmentsDueDates;}

    public ArrayList<String> getAssignmentsMarks() {return this.AssignmentsMarks;}

    public ArrayList<Float> getAssignmentsMarksRecived() {return this.AssignmentsMarksRecived;}
}
