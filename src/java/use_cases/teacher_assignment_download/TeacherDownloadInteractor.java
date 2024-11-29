package use_cases.teacher_assignment_download;

import com.dropbox.core.DbxException;
import entities.PDFFile;
import data_access.FileDataAccessInterface;

import java.io.File;

public class TeacherDownloadInteractor implements TeacherDownloadInputBoundary {
    private final FileDataAccessInterface fileDataAccessInterface;
    private final TeacherDownloadOutputBoundary outputBoundary;

    public TeacherDownloadInteractor(
            FileDataAccessInterface fileDataAccessInterface,
            TeacherDownloadOutputBoundary outputBoundary) {
        this.fileDataAccessInterface = fileDataAccessInterface;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void downloadWrittenAssignment(String courseName, String studentEmail) throws DbxException {
        String filePath = "/" + courseName + "/" + studentEmail;
        if (!fileDataAccessInterface.fileExistsByPath(filePath)) {
            outputBoundary.presentError("The suggested path is not found.");
        }

        PDFFile file = fileDataAccessInterface.getFile(filePath);

        if (file == null) {
            outputBoundary.presentError("The downloaded file is not found.");
        } else {
            outputBoundary.presentSuccess("You downloaded the assignment" + file.getFileName(), file);
        }

    }
}
