package use_cases.teacher_assignment_download;

import entities.PDFFile;
import data_access.FileDataAccessInterface;

public class TeacherDownloadInteractor implements TeacherDownloadInputBoundary {
    private final FileDataAccessInterface fileDataAccessInterface;
    private final TeacherDownloadOutputBoundary outputBoundary;

    public TeacherDownloadInteractor(FileDataAccessInterface fileDataAccessInterface, TeacherDownloadOutputBoundary outputBoundary) {
        this.fileDataAccessInterface = fileDataAccessInterface;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void downloadWrittenAssignment(String courseName, String studentEmail) {
        try{
            String filePath = "/" + courseName + "/" + studentEmail;
            PDFFile file = fileDataAccessInterface.getFile(filePath);
            outputBoundary.presentSuccess("You downloaded the assignment of the student" + file.getName());
        } catch(Exception e){
            outputBoundary.presentError("File could not be downloaded.");
        }

    }
}
