package use_cases.submit_assignment;

import entities.Account;
import data_access.FileDataAccessInterface;
import entities.PDFFile;

import java.io.File;
import java.nio.file.Files;

public class SubmitAssignmentInteractor implements SubmitAssignmentInputBoundary {
    private final FileDataAccessInterface fileDataAccessInterface;
    private final SubmitAssignmentOutputBoundary outputBoundary;
    private final Account account;

    public SubmitAssignmentInteractor(FileDataAccessInterface fileDataAccessInterface,
                                      SubmitAssignmentOutputBoundary outputBoundary, Account account) {
        this.fileDataAccessInterface = fileDataAccessInterface;
        this.outputBoundary = outputBoundary;
        this.account = account;
    }

    @Override
    public void submitAssignment(File selectedFile, String courseName) {
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

            // Create a unique path for the file in Dropbox
            String dropboxPath = "/" + courseName + "/" + studentEmail + "_" + selectedFile.getName();

            PDFFile assignment = new PDFFile(
                    selectedFile.getName(),
                    dropboxPath,
                    content);

            fileDataAccessInterface.saveFile(assignment);

            outputBoundary.presentSuccess("File uploaded successfully to: " + dropboxPath);

        } catch (Exception e) {
            outputBoundary.presentError("File could not be uploaded successfully.");
        }
    }
}