package use_cases.submit_assignment;

import data_access.InMemoryUserDataAccessObject;
import entities.Assignment;

import java.io.File;

public interface SubmitAssignmentInputBoundary {
    void submitAssignment(File selectedFile, String courseName, String assignmentName, String email);
}
