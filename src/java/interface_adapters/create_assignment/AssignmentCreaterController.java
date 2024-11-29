package interface_adapters.create_assignment;

import use_cases.create_assignment.CreateAssignmentInteractor;

import java.io.File;

public class AssignmentCreaterController {
    private CreateAssignmentInteractor interactor;

    public AssignmentCreaterController(CreateAssignmentInteractor interactor) {
        this.interactor = interactor;
    }

    public void createAssignment(
            String email, String assignmentName, String courseName, String dueDate, String marks, File newAssignmentFile
    ) {
        interactor.createAssignment(email, assignmentName, courseName, dueDate, marks, newAssignmentFile);
    }
}
