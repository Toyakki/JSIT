package use_cases.submit_grade;

import entities.Assignment;

public interface SubmitGradeInputBoundary {
    void submitGrade(String grade, String course, String email, Assignment assignment);
}
