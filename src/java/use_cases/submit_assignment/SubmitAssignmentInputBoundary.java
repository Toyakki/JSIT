package use_cases.submit_assignment;

import entities.Assignment;

import java.io.File;

public interface SubmitAssignmentInputBoundary {
    void submitAssignment(File selectedFile, Assignment assignment, String courseName);
}
