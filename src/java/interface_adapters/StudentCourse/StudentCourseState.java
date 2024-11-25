package interface_adapters.StudentCourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentCourseState {
    private String email;
    private String courseName;

    private List<String> assignmentNames;
    private List<String> assignmentDueDates;
    private List<String> assignmentMarks;
    private List<String> assignmentStages;
    private List<String> assignmentMarksReceived;

    public StudentCourseState(String email,
                              String courseName,
                              List<String> assignmentNames,
                              List<String> assignmentDueDates,
                              List<String> assignmentMarks,
                              List<String> assignmentStages,
                              List<String> assignmentMarksReceived
    ) {
        this.email = email;
        this.courseName = courseName;
        this.assignmentNames = List.copyOf(assignmentNames);
        this.assignmentDueDates = List.copyOf(assignmentDueDates);
        this.assignmentMarks = List.copyOf(assignmentMarks);
        this.assignmentStages = List.copyOf(assignmentStages);
        this.assignmentMarksReceived = List.copyOf(assignmentMarksReceived);
    }

    public StudentCourseState(){
        this.email = "";
        this.assignmentNames = new ArrayList<>();
        this.assignmentDueDates = new ArrayList<>();
        this.assignmentMarks = new ArrayList<>();
        this.assignmentStages = new ArrayList<>();
        this.assignmentMarksReceived = new ArrayList<>();
    }

    public String getCourseName(){
        return courseName;
    }

    public List<String> getAssignmentsNames(){
        return List.copyOf(assignmentNames);
    }

    public List<String> getAssignmentsDueDates(){
        return List.copyOf(assignmentDueDates);
    }

    public List<String> getAssignmentsMarks(){
        return List.copyOf(assignmentMarks);
    }

    public List<String> getAssignmentsStages(){
        return List.copyOf(assignmentStages);
    }

    public List<String> getAssignmentsMarksReceived(){
        return List.copyOf(assignmentMarksReceived);
    }
}
