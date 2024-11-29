package use_cases.submit_assignment;

import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import data_access.FileDataAccessInterface;
import entities.PDFFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

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
                                 String email){
        try{
            if (!selectedFile.getName().endsWith(".pdf")) {
                throw new IllegalArgumentException("File must be a PDF");
            }
            if (!selectedFile.canRead()) {
                throw new IllegalArgumentException("File cannot be read");
            }
            if (!selectedFile.exists()) {
                throw new FileNotFoundException("File does not exist");
            }
    
            byte[] content = Files.readAllBytes(selectedFile.toPath());
            Account account = userDataAccessObject.getUserByEmail(email);
            String studentEmail = account.getEmail();
            
            String dropboxPath = "/" + courseName + "/" + studentEmail;
    
            PDFFile newAssignment = new PDFFile(
                    selectedFile.getName(),
                    dropboxPath,
                    content);
    
            fileDataAccessInterface.saveFile(newAssignment);
    
            outputBoundary.presentSuccess();
            
        } catch (IllegalArgumentException | FileNotFoundException e){
            outputBoundary.presentError();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}