package use_cases.submit_assignment;

import entities.*;
import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class SubmitAssignmentInteractor implements SubmitAssignmentInputBoundary {
    private final FileDataAccessInterface fileDataAccessInterface;
    private final SubmitAssignmentOutputBoundary outputBoundary;
    private final UserDataAccessInterface userDataAccessObject;

    public SubmitAssignmentInteractor(FileDataAccessInterface fileDataAccessInterface,
                                      SubmitAssignmentOutputBoundary outputBoundary,
                                      UserDataAccessInterface userDataAccessObject) {
        this.fileDataAccessInterface = fileDataAccessInterface;
        this.outputBoundary = outputBoundary;
        this.userDataAccessObject = userDataAccessObject;
                                      }

    @Override
    public void submitAssignment(File selectedFile,
                                 String courseName,
                                 String assignmentName,
                                 String email){
        try{
            if (!selectedFile.getName().endsWith(".pdf")) {
                throw new IllegalArgumentException("File must be a PDF");
            }
            if (!selectedFile.exists()) {
                throw new FileNotFoundException("File does not exist");
            }
            if (!selectedFile.canRead()) {
                throw new IllegalArgumentException("File cannot be read");
            }

            byte[] content = Files.readAllBytes(selectedFile.toPath());

            Account account = userDataAccessObject.getUserByEmail(email);
            String studentEmail = account.getEmail();

            String dropboxPath = "/" + courseName + "/" + assignmentName
                    + "/" + studentEmail + "/" + selectedFile.getName();

            PDFFile newAssignment = new PDFFile(
                    selectedFile.getName(),
                    dropboxPath,
                    content);

            Course course = account.getCourseByName(courseName);

            Assignment assignment = course.getAssignmentByName(assignmentName);

            Submission submission = new Submission(
                    newAssignment
            );

            assignment.setSubmission(email, submission);

            fileDataAccessInterface.saveFile(newAssignment);

            // Call presenter
            outputBoundary.presentSuccess(courseName, email, assignment);


        } catch (IllegalArgumentException | IOException e){
            outputBoundary.presentError(e.getMessage());
        }
    }
}