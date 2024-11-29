package use_cases.submit_assignment;

import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import data_access.FileDataAccessInterface;
import entities.PDFFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class SubmitAssignmentInteractor implements SubmitAssignmentInputBoundary {
    private final FileDataAccessInterface fileDataAccessInterface;
    private final SubmitAssignmentOutputBoundary outputBoundary;
    private final InMemoryUserDataAccessObject userDataAccessObject;

    public SubmitAssignmentInteractor(FileDataAccessInterface fileDataAccessInterface,
                                      SubmitAssignmentOutputBoundary outputBoundary,
                                      InMemoryUserDataAccessObject userDataAccessObject) {
        this.fileDataAccessInterface = fileDataAccessInterface;
        this.outputBoundary = outputBoundary;
        this.userDataAccessObject = userDataAccessObject;
    }

    @Override
    public void submitAssignment(File selectedFile,
                                 String courseName,
                                 String email,
                                 InMemoryUserDataAccessObject userDataAccessObject)
    {
        try {
            if (!selectedFile.getName().endsWith(".pdf")) {
                throw new IllegalArgumentException("Only PDF files are allowed.");
            }
            if (!selectedFile.exists() || !selectedFile.canRead()) {
                outputBoundary.presentError("File does not exist or cannot be read: " + selectedFile.getAbsolutePath());
                return;
            }

            byte[] content = Files.readAllBytes(selectedFile.toPath());
            Account account = userDataAccessObject.getUserByEmail(email);
            String studentEmail = account.getEmail();

            // Create a unique path for the file in Dropbox
            String dropboxPath = "/" + courseName + "/" + studentEmail;

            PDFFile new_assignment = new PDFFile(
                    selectedFile.getName(),
                    dropboxPath,
                    content);

            fileDataAccessInterface.saveFile(new_assignment);

            outputBoundary.presentSuccess("File uploaded successfully to: " + dropboxPath);

        } catch (Exception e) {
            outputBoundary.presentError("File could not be uploaded successfully.");
        }
    }
}