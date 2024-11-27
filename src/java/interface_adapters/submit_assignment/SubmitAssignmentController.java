package interface_adapters.submit_assignment;


import use_cases.submit_assignment.SubmitAssignmentInputBoundary;

import java.io.File;

public class SubmitAssignmentController {
    private final SubmitAssignmentInputBoundary inputBoundary;

    public SubmitAssignmentController(SubmitAssignmentInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void handleSubmitAssignment(File selectedFile, String courseName) {
        inputBoundary.submitAssignment(selectedFile, courseName);
    }
}
