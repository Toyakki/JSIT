package interface_adapters.submit_assignment;

import use_cases.submit_assignment.SubmitAssignmentInputBoundary;
import data_access.InMemoryUserDataAccessObject;

import java.io.File;

public class SubmitAssignmentController {
    private final SubmitAssignmentInputBoundary inputBoundary;
    private final InMemoryUserDataAccessObject dataAccessObject;
    public SubmitAssignmentController(
            SubmitAssignmentInputBoundary inputBoundary,
            InMemoryUserDataAccessObject dataAccessObject) {
        this.inputBoundary = inputBoundary;
        this.dataAccessObject  = dataAccessObject;
    }

    public void handleSubmitAssignment(File selectedFile,
                                       String courseName,
                                       String email) {
        inputBoundary.submitAssignment(selectedFile, courseName, email);
    }
}
