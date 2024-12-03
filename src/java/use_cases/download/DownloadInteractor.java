package use_cases.download;


import data_access.UserDataAccessInterface;
import entities.PDFFile;
import data_access.FileDataAccessInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadInteractor implements DownloadInputBoundary {
    private final FileDataAccessInterface fileDataAccessInterface;
    private final UserDataAccessInterface userDataAccessInterface;

    public DownloadInteractor(
            FileDataAccessInterface fileDataAccessInterface,
            UserDataAccessInterface userDataAccessInterface) {
        this.fileDataAccessInterface = fileDataAccessInterface;
        this.userDataAccessInterface = userDataAccessInterface;
    }

    @Override
    public void downloadWrittenAssignment(String courseName,
                                          String assignmentName,
                                          String studentEmail){
        try {
            String filePath = "/" + courseName + "/" + assignmentName + "/" + studentEmail;

            PDFFile file = fileDataAccessInterface.getFile(filePath);
            // Determine the local Downloads folder

            if (file == null) {
                throw new IOException("could not download file");
            }

            String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";
            File localFile = new File(downloadsFolderPath, file.getFileName());
            // Save the file to the Downloads folder
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                fos.write(file.getFileContent());
            }

        } catch (IOException e) {
            throw new RuntimeException("could not download file");
        }
    }

    public void downloadFeedback(String courseName,
                                 String assignmentName,
                                 String studentEmail){
        try {
            String filePath = "/" + courseName  + "/" + assignmentName + "/" + studentEmail + "/feedback";
            System.out.println(filePath);
            PDFFile file = fileDataAccessInterface.getFile(filePath);

            if (file == null) {
                throw new IOException("could not download file");
            }
            // Determine the local Downloads folder
            String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";
            File localFile = new File(downloadsFolderPath, file.getFileName());
            // Save the file to the Downloads folder
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                fos.write(file.getFileContent());
            }
        } catch (IOException e) {
            throw new RuntimeException("could not download file");
        }
    }

    public void downloadOriginal(String courseName, String assignmentName){
        try {
            String filePath = "/" + courseName + "/" + assignmentName;

            PDFFile file = fileDataAccessInterface.getFile(filePath);
            // Determine the local Downloads folder

            if (file == null) {
                throw new IOException("could not download file");
            }

            String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";
            File localFile = new File(downloadsFolderPath, file.getFileName());
            // Save the file to the Downloads folder
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                fos.write(file.getFileContent());
            }
        } catch (IOException e) {
            throw new RuntimeException("could not download file");
        }
    }


}