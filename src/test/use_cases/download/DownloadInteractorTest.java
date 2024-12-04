package use_cases.download;

import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Course;
import entities.CourseFactory;
import entities.PDFFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_cases.download.DownloadInteractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DownloadInteractorTest {
    private FileDataAccessInterface fileDsGateway;
    private UserDataAccessInterface userDsGateway;
    private DownloadInteractor interactor;

    @BeforeEach
    void setUp() {
        // Initialize DAOs
        fileDsGateway = new InMemoryFileDataAccessObject();
        userDsGateway = new InMemoryUserDataAccessObject();

        interactor = new DownloadInteractor(fileDsGateway, userDsGateway);

        // Setup test data
        byte[] sampleContent = "Sample PDF Content".getBytes();
        PDFFile submittedFile = new PDFFile("submittedAssignment.pdf", "/MAT101/Assignment1/student1@example.com", sampleContent);
        PDFFile feedbackFile = new PDFFile("feedback.pdf", "/MAT101/Assignment1/student1@example.com/feedback", sampleContent);
        PDFFile originalFile = new PDFFile("originalAssignment.pdf", "/MAT101/Assignment1", sampleContent);

        fileDsGateway.saveFile(submittedFile);
        fileDsGateway.saveFile(feedbackFile);
        fileDsGateway.saveFile(originalFile);

        // Create a course and user
        Course math101 = CourseFactory.createClass("Joe King", "MAT101");
        List<Course> test_course = new ArrayList<>();
        test_course.add(math101);

        Account student = new Account("student1@example.com", "password123", "student", test_course);
        userDsGateway.saveUser(student);
    }

    @Test
    void testDownloadWrittenAssignment_Success() {
        // Act
        interactor.downloadWrittenAssignment("MAT101", "Assignment1", "student1@example.com");

        // Assert
        File downloadedFile = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "submittedAssignment.pdf");
        assertTrue(downloadedFile.exists(), "The submitted assignment file should be downloaded.");
        assertEquals("submittedAssignment.pdf", downloadedFile.getName(), "The file name should match.");
        downloadedFile.delete(); // Clean up
    }


    @Test
    void testDownloadFeedback_Success() {
        // Act
        interactor.downloadFeedback("MAT101", "Assignment1", "student1@example.com");

        // Assert
        File downloadedFile = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "feedback.pdf");
        assertTrue(downloadedFile.exists(), "The feedback file should be downloaded.");
        assertEquals("feedback.pdf", downloadedFile.getName(), "The file name should match.");
        downloadedFile.delete(); // Clean up
    }

    @Test
    void testDownloadOriginal_Success() {
        // Act
        interactor.downloadOriginal("MAT101", "Assignment1");

        // Assert
        File downloadedFile = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "originalAssignment.pdf");
        assertTrue(downloadedFile.exists(), "The original assignment file should be downloaded.");
        assertEquals("originalAssignment.pdf", downloadedFile.getName(), "The file name should match.");
        downloadedFile.delete(); // Clean up
    }

    @Test
    void testDownloadWrittenAssignment_InvalidPath() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                interactor.downloadWrittenAssignment("InvalidCourse", "Assignment1", "student1@example.com"));
        assertEquals("could not download file", exception.getMessage(), "An invalid course path should result in an exception.");
    }

    @Test
    void testDownloadFeedback_MissingFile() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                interactor.downloadFeedback("MAT101", "Assignment1", "nonexistent@example.com"));
        assertEquals("could not download file", exception.getMessage(), "A missing feedback file should result in an exception.");
    }

    @Test
    void testDownloadOriginal_InvalidPath() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                interactor.downloadOriginal("InvalidCourse", "Assignment1"));
        assertEquals("could not download file", exception.getMessage(), "An invalid course path should result in an exception.");
    }

    @Test
    void testDownloadWrittenAssignment_FileIOException() throws IOException {
        // Simulate file system error
        String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";
        File restrictedFile = new File(downloadsFolderPath, "submittedAssignment.pdf");

        // Create a restricted file to simulate an IOException
        assertTrue(restrictedFile.createNewFile(), "Failed to create a restricted file for testing.");
        assertTrue(restrictedFile.setWritable(false), "Failed to set file to read-only.");

        // Save test data to the DAO
        byte[] sampleContent = "Sample PDF Content".getBytes();
        PDFFile testFile = new PDFFile("submittedAssignment.pdf", "/MAT101/Assignment1/student1@example.com", sampleContent);
        fileDsGateway.saveFile(testFile);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                interactor.downloadWrittenAssignment("MAT101", "Assignment1", "student1@example.com")
        );

        assertEquals("could not download file", exception.getMessage(), "An IOException should result in a runtime exception with the correct message.");

        // Clean up
        restrictedFile.setWritable(true); // Reset permissions to allow deletion
        restrictedFile.delete();
    }

    @Test
    void testDownloadFeedback_FileIOException() throws IOException {
        // Simulate file system error
        String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";
        File restrictedFile = new File(downloadsFolderPath, "feedback.pdf");

        // Create a restricted file to simulate an IOException
        assertTrue(restrictedFile.createNewFile(), "Failed to create a restricted file for testing.");
        assertTrue(restrictedFile.setWritable(false), "Failed to set file to read-only.");

        // Save test data to the DAO
        byte[] sampleContent = "Sample Feedback Content".getBytes();
        PDFFile testFile = new PDFFile("feedback.pdf", "/MAT101/Assignment1/student1@example.com/feedback", sampleContent);
        fileDsGateway.saveFile(testFile);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                interactor.downloadFeedback("MAT101", "Assignment1", "student1@example.com")
        );

        assertEquals("could not download file", exception.getMessage(), "An IOException should result in a runtime exception with the correct message.");

        // Clean up
        restrictedFile.setWritable(true); // Reset permissions to allow deletion
        restrictedFile.delete();
    }

    @Test
    void testDownloadOriginalRestrictedFile_IOException() throws IOException {
        // Simulate file system error
        String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";
        File restrictedFile = new File(downloadsFolderPath, "originalAssignment.pdf");

        // Create a restricted file to simulate an IOException
        assertTrue(restrictedFile.createNewFile(), "Failed to create a restricted file for testing.");
        assertTrue(restrictedFile.setWritable(false), "Failed to set file to read-only.");

        // Save test data to the DAO
        byte[] sampleContent = "Sample Original Content".getBytes();
        PDFFile testFile = new PDFFile("originalAssignment.pdf", "/MAT101/Assignment1", sampleContent);
        fileDsGateway.saveFile(testFile);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                interactor.downloadOriginal("MAT101", "Assignment1")
        );

        assertEquals("could not download file", exception.getMessage(), "An IOException should result in a runtime exception with the correct message.");

        // Clean up
        restrictedFile.setWritable(true); // Reset permissions to allow deletion
        restrictedFile.delete();
    }

}
