package interface_adapters.TeacherCourse;

import java.util.List;
import java.util.Map;

public class TeacherCourseState {
    private String courseName;
    private List<String> assignmentNames;
    private List<String> assignmentDueDates;
    private List<String> assignmentMarks;
    private List<String> assignmentStages;
    private List<String> studentEmails;

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
