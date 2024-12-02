package use_cases.submit_assignment;

import entities.Assignment;

public interface SubmitAssignmentOutputBoundary {
    void presentSuccess(String courseName, String email, Assignment assignment);
    void presentError(String message);
}
