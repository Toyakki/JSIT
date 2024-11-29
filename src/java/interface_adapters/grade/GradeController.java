package interface_adapters.grade;

import entities.Assignment;
import entities.Course;
import use_cases.submit_grade.SubmitGradeInputBoundary;

import javax.security.auth.Subject;

public class GradeController {
    private SubmitGradeInputBoundary submitGradeInputBoundary;

    public GradeController(SubmitGradeInputBoundary submitGradeInputBoundary) {
        this.submitGradeInputBoundary = submitGradeInputBoundary;
    }

    public void setGrade(String grade, String course, String email, Assignment assignment) {
        submitGradeInputBoundary.submitGrade(grade, course, email, assignment);
    }
}
