package use_cases.submit_assignment;

import java.io.File;

public interface SubmitAssignmentInputBoundary {
    void submitAssignment(File selectedFile, String courseName);
}
