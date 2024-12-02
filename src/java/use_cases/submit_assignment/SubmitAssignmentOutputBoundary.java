package use_cases.submit_assignment;

import interface_adapters.student_course.StudentCourseState;
import interface_adapters.submit_assignment.SubmitAssignmentState;

public interface SubmitAssignmentOutputBoundary {
    void presentSuccess(String courseName, String email);
    void presentError(String message);
}
