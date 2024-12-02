package use_cases.upload_feedback;

import data_access.UserDataAccessInterface;
import entities.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UploadFeedbackInteractor implements UploadFeedbackInputBoundary {
    private UserDataAccessInterface userDataAccessInterface;

    public UploadFeedbackInteractor(UserDataAccessInterface userDataAccessInterface) {
        this.userDataAccessInterface = userDataAccessInterface;
    }

    @Override
    public void uploadFeedback(File feedback, String nameCourse, String studentEmail, String nameAssignment) {
        Account user = userDataAccessInterface.getUserByEmail(studentEmail);
        Course course = user.getCourseByName(nameCourse);
        Assignment assignment = course.getAssignmentByName(nameAssignment);
        Submission submission = assignment.getSubmission(studentEmail);
        String fileName = nameCourse + "/" +  studentEmail + "/Feedback";
        try {
            PDFFile pdfFile = new PDFFile(nameAssignment, fileName, Files.readAllBytes(feedback.toPath()));
            submission.setFeedbackFile(pdfFile);
        } catch (IOException e) {
            throw new RuntimeException("File could not be read");
        }
    }
}
