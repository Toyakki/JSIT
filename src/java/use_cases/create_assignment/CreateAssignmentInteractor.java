package use_cases.create_assignment;

import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.Assignment;
import entities.Course;
import entities.PDFFile;
import interface_adapters.create_assignment.CreateAssignmentPresenter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CreateAssignmentInteractor {
    private final UserDataAccessInterface userDataAccessInterface;
    private final FileDataAccessInterface fileDataAccessInterface;
    private final CreateAssignmentPresenter presenter;

    public CreateAssignmentInteractor(
            CreateAssignmentPresenter presenter,
            UserDataAccessInterface userDataAccessInterface,
            FileDataAccessInterface fileDataAccessInterface
    ) {
        this.presenter = presenter;
        this.userDataAccessInterface = userDataAccessInterface;
        this.fileDataAccessInterface = fileDataAccessInterface;
    }

    public void createAssignment(
            String email,
            String assignmentName,
            String courseName,
            String dueDate,
            String marks,
            File rawAssignmentFile
    ) {
        try {

            byte[] assignmentBytes = Files.readAllBytes(rawAssignmentFile.toPath());
            // temporary naming scheme
            PDFFile encodedAssignmentFile = new PDFFile(
                    assignmentName,
                    courseName + "/assignments/" + assignmentName,
                    assignmentBytes
            );

            Assignment assignment = new Assignment(
                    assignmentName,
                    dueDate,
                    marks,
                    "assigned",
                    "false"
            );

            Course course = userDataAccessInterface.getUserByEmail(email).getCourseByName(courseName);
            course.addAssignment(assignment);

            // upload assignmentBytes
            fileDataAccessInterface.saveFile(encodedAssignmentFile);

            this.presenter.refreshView();

        } catch (IOException e) {
            throw new RuntimeException("File could not be read"); // replace with actual error handling
        }
    }
}
