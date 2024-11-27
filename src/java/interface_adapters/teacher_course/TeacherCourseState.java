package interface_adapters.teacher_course;

import java.util.ArrayList;
import java.util.List;

public class TeacherCourseState {
    private String courseName;
    private String email;

    private List<String> assignmentNames;
    private List<String> assignmentDueDates;
    private List<String> assignmentMarks;
    private List<String> assignmentStages;
    private List<String> assignmentMarksReceived;
    private List<String> studentEmails;

    public TeacherCourseState(String courseName,
                              String email,
                              List<String> assignmentNames,
                              List<String> assignmentDueDates,
                              List<String> assignmentMarks,
                              List<String> assignmentStages,
                              List<String> assignmentMarksReceived,
                              List<String> studentEmails
    ) {
        this.courseName = courseName;
        this.email = email;
        this.assignmentNames = List.copyOf(assignmentNames);
        this.assignmentDueDates = List.copyOf(assignmentDueDates);
        this.assignmentMarks = List.copyOf(assignmentMarks);
        this.assignmentStages = List.copyOf(assignmentStages);
        this.assignmentMarksReceived = List.copyOf(assignmentMarksReceived);
        this.studentEmails = List.copyOf(studentEmails);
    }

    public TeacherCourseState(){
        this.assignmentNames = new ArrayList<>();
        this.assignmentDueDates = new ArrayList<>();
        this.assignmentMarks = new ArrayList<>();
        this.assignmentStages = new ArrayList<>();
        this.assignmentMarksReceived = new ArrayList<>();
        this.studentEmails = new ArrayList<>();
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

    public List<String> getAssignmentsMarks(){
        return List.copyOf(assignmentMarks);
    }

    public List<String> getAssignmentsStages(){
        return List.copyOf(assignmentStages);
    }

    public List<String> getStudentEmails(){
        return List.copyOf(studentEmails);
    }
}
