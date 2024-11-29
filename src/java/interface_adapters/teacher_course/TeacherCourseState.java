package interface_adapters.teacher_course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherCourseState {
    private String courseName;
    private String email;

    private List<String> assignmentNames;
    private List<String> assignmentDueDates;
    private List<Map<String, String>> assignmentMarks;
    private List<Map<String, String>> assignmentStages;
    private List<Map<String, String>> assignmentMarksReceived;
    private List<String> assignmentBaseMarks;
    private List<String> studentEmails;

    public TeacherCourseState(String courseName,
                              String email,
                              List<String> assignmentNames,
                              List<String> assignmentDueDates,
                              List<Map<String, String>> assignmentMarks,
                              List<Map<String, String>> assignmentStages,
                              List<Map<String, String>> assignmentMarksReceived,
                              List<String> assignmentBaseMarks,
                              List<String> studentEmails
    ) {
        this.courseName = courseName;
        this.email = email;
        this.assignmentNames = List.copyOf(assignmentNames);
        this.assignmentDueDates = List.copyOf(assignmentDueDates);
        this.assignmentMarks = assignmentMarks;
        this.assignmentStages = assignmentStages;
        this.assignmentMarksReceived = assignmentMarksReceived;
        this.assignmentBaseMarks = List.copyOf(assignmentBaseMarks);
        this.studentEmails = List.copyOf(studentEmails);
    }

    public TeacherCourseState(){
        this.assignmentNames = new ArrayList<>();
        this.assignmentDueDates = new ArrayList<>();
        this.assignmentMarks = new ArrayList<>();
        this.assignmentStages = new ArrayList<>();
        this.assignmentMarksReceived = new ArrayList<>();
        this.studentEmails = new ArrayList<>();
        this.assignmentBaseMarks = new ArrayList<>();
    }

    public String getEmail() {return email;}

    public String getCourseName() {
        return courseName;
    }

    public List<String> getAssignmentsNames() {
        return List.copyOf(assignmentNames);
    }

    public List<String> getAssignmentsDueDates(){
        return List.copyOf(assignmentDueDates);
    }

    public List<Map<String, String>> getAssignmentsMarks(){ return assignmentMarks; }

    public List<Map<String, String>> getAssignmentsStages(){ return assignmentStages; }

    public List<String> getStudentEmails(){ return List.copyOf(studentEmails); }

    public List<String> getAssignmentBaseMarks(){ return assignmentBaseMarks; }
}
