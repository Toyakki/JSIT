package use_cases.teacher_assignment_download;


import data_access.UserDataAccessInterface;
import entities.Assignment;
import entities.Course;
import entities.PDFFile;
import entities.Account;
import data_access.FileDataAccessInterface;
import interface_adapters.teacher_course.TeacherCourseState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDownloadInteractor implements TeacherDownloadInputBoundary {
    private final FileDataAccessInterface fileDataAccessInterface;
    private final UserDataAccessInterface userDataAccessInterface;
    private final TeacherDownloadOutputBoundary outputBoundary;

    public TeacherDownloadInteractor(
            FileDataAccessInterface fileDataAccessInterface,
            TeacherDownloadOutputBoundary outputBoundary,
            UserDataAccessInterface userDataAccessInterface) {
        this.fileDataAccessInterface = fileDataAccessInterface;
        this.outputBoundary = outputBoundary;
        this.userDataAccessInterface = userDataAccessInterface;
    }

//    Can I demand the teacher's email?
    @Override
    public void downloadWrittenAssignment(String courseName,
                                          String assignmentName,
                                          String teacherEmail,
                                          String studentEmail){
        try {
            String filePath = "/" + courseName + "/" + studentEmail;

            PDFFile file = fileDataAccessInterface.getFile(filePath);
            // Determine the local Downloads folder
            String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";
            File localFile = new File(downloadsFolderPath, file.getName());
            // Save the file to the Downloads folder
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                fos.write(file.getContent());
            }

            Account teacher_account = userDataAccessInterface.getUserByEmail(teacherEmail);


            Course course = teacher_account.getCourseByName(courseName);

            Assignment assignment = course.getAssignmentByName(assignmentName);

            TeacherCourseState teacherCourseState = new TeacherCourseState();
            outputBoundary.presentSuccess(
                    courseName,
                    teacherEmail,
                    assignment.getName(),
                    studentEmail,
                    assignment.getDueDate(),
                    assignment.getMarks(),
                    assignment.getStage(),
                    assignment.getMarksReceivedStatus()
            );
        } catch (IOException e) {
            outputBoundary.presentError(e.getMessage());
        }
    }
}
