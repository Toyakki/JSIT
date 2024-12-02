package interface_adapters.grade;

import use_cases.grade.GradeInputBoundary;
import use_cases.submit_grade.SubmitGradeInputBoundary;

import javax.security.auth.Subject;
import java.io.File;

public class GradeController {
    private GradeInputBoundary gradeInputBoundary;

    public GradeController(GradeInputBoundary gradeInputBoundary) {
        this.gradeInputBoundary = gradeInputBoundary;
    }

    public void gradeAssignment(String grade, File file, String studentEmail, String teacherEmail,
                         String courseName, String assignmentName) {
        gradeInputBoundary.gradeAssignment(grade, file, studentEmail, teacherEmail, courseName, assignmentName);
    }
}
