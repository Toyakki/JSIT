package use_cases.submit_assignment;

import entities.Account;
import data_access.FileDataAccessInterface;
import entities.Assignment;
import entities.PDFFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class SubmitAssignmentInteractor implements SubmitAssignmentInputBoundary {
    private final FileDataAccessInterface fileDataAccessInterface;
    private final SubmitAssignmentOutputBoundary outputBoundary;
    private final Assignment assignment_metadata;
    private final Account account;

    public SubmitAssignmentInteractor(FileDataAccessInterface fileDataAccessInterface,
                                      SubmitAssignmentOutputBoundary outputBoundary,
                                      Assignment assignment_metadata,
                                      Account account) {
        this.fileDataAccessInterface = fileDataAccessInterface;
        this.outputBoundary = outputBoundary;
        this.assignment_metadata = assignment_metadata;
        this.account = account;
    }

    @Override
    public void submitAssignment(File selectedFile, Assignment assignment_metadata) {
        try {
            if (!selectedFile.getName().endsWith(".pdf")) {
                throw new IllegalArgumentException("Only PDF files are allowed.");
            }
            if (!selectedFile.exists() || !selectedFile.canRead()) {
                outputBoundary.presentError("File does not exist or cannot be read: " + selectedFile.getAbsolutePath());
                return;
            }

            byte[] content = Files.readAllBytes(selectedFile.toPath());
            String studentEmail = account.getEmail();
            String courseName = assignment_metadata.getName();

            // Create a unique path for the file in Dropbox
            String dropboxPath = "/" + courseName + "/" + studentEmail;

            PDFFile new_assignment = new PDFFile(
                    selectedFile.getName(),
                    dropboxPath,
                    content);

            // Modify the assignment metadata.
            assignment_metadata.

            fileDataAccessInterface.saveFile(new_assignment);

            outputBoundary.presentSuccess("File uploaded successfully to: " + dropboxPath);

        } catch (Exception e) {
            outputBoundary.presentError("File could not be uploaded successfully.");
        }
    }
}