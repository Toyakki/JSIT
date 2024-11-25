package interface_adapters.SubmitAssignment;

import use_cases.SubmitAssignment.SubmitAssignmentInputBoundary;

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
