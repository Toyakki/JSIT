package use_cases.submit_grade;

import entities.Assignment;

public interface SubmitGradeInputBoundary {
    void submitGrade(String grade, String nameCourse, String email, String nameAssignment);
}
