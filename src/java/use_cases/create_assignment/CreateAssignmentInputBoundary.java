package use_cases.create_assignment;

import java.io.File;

public interface CreateAssignmentInputBoundary {
    void createAssignment(
            String email,
            String assignmentName,
            String courseName,
            String dueDate,
            String marks,
            File rawAssignmentFile
    );
}
