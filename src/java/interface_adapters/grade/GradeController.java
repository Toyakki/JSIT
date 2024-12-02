package interface_adapters.grade;

import use_cases.submit_grade.SubmitGradeInputBoundary;

import javax.security.auth.Subject;

public class GradeController {
    private SubmitGradeInputBoundary submitGradeInputBoundary;

    public GradeController(SubmitGradeInputBoundary submitGradeInputBoundary) {
        this.submitGradeInputBoundary = submitGradeInputBoundary;
    }

    public void setGrade(String grade, String nameCourse, String email, String nameAssignment) {
        submitGradeInputBoundary.submitGrade(grade, nameCourse, email, nameAssignment);
    }
}
