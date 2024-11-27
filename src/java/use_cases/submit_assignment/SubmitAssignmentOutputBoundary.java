package use_cases.submit_assignment;

public interface SubmitAssignmentOutputBoundary {
    void presentSuccess(String message);
    void presentError(String message);
}
