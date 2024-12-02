//package use_cases.submit_assignment;
//
//import data_access.InMemoryUserDataAccessObject;
//import data_access.InMemoryFileDataAccessObject;
//import entities.Account;
//import entities.PDFFile;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.nio.file.Files;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class SubmitAssignmentInteractorTest {
//    private InMemoryUserDataAccessObject userDsGateway;
//    private InMemoryFileDataAccessObject fileDsGateway;
//    private SubmitAssignmentInteractor submitAssignmentInteractor;
//    private MockSubmitAssignmentPresenter outputBoundary;
//
//
//    @BeforeEach
//    void create() {
//        userDsGateway = new InMemoryUserDataAccessObject();
//        fileDsGateway = new InMemoryFileDataAccessObject();
//        outputBoundary = new MockSubmitAssignmentPresenter();
//        submitAssignmentInteractor = new SubmitAssignmentInteractor(
//                fileDsGateway,
//                outputBoundary,
//                userDsGateway
//        );
//    }
//
//    private static class MockSubmitAssignmentPresenter implements SubmitAssignmentOutputBoundary {
//        private String successMessage;
//        private String errorMessage;
//
//        @Override
//        public void presentSuccess(String message) {
//            this.successMessage = message;
//        }
//
//        @Override
//        public void presentError(String message) {
//            this.errorMessage = message;
//        }
//
//        public String getSuccessMessage() {
//            return successMessage;
//        }
//        public String getErrorMessage() {
//            return errorMessage;
//        }
//    }
//
//    @Test
//    public void testValidSubmission() throws Exception{
//
//        File testFile = Files.createTempFile("test", ".pdf").toFile();
//        Files.write(testFile.toPath(), "Sample".getBytes());
//
//        submitAssignmentInteractor.submitAssignment(
//                testFile,
//                "CSC207",
//                "henrik-ibsen707@gmail.com",
//                userDsGateway
//        );
//
//        PDFFile savedFile = fileDsGateway.getFile("/CSC207/henrik-ibsen707@gmail.com");
//
//        String expectedFileName = "test.pdf";
//        String expectedFilePath = "/CSC207/henrik-ibsen707@gmail.com";
//        String actualFileName = savedFile.getFileName();
//        String actualFilePath = savedFile.getFilePath();
//
//        assertNotNull(savedFile);
//        assertEquals(expectedFileName, actualFileName);
//        assertEquals(expectedFilePath, actualFilePath);
//        assertEquals("File uploaded successfully to: " + expectedFilePath,
//                outputBoundary.getSuccessMessage());
//    }
//
//    @Test
//    public void testNonPDFSubmission() {
//        // Arrange
//
//        File testFile = new File("test.txt");
//
//        // Act
//        submitAssignmentInteractor.submitAssignment(testFile, "CSC207", "test@example.com", userDsGateway);
//
//        // Assert
//        assertNull(outputBoundary.getSuccessMessage());
//        assertEquals("File must be a PDF", outputBoundary.getErrorMessage());
//    }
//    @Test
//    public void testInvalidFormat() {
//        // Implement the test logic
//    }
//
//    @Test
//    public void testInvalidAssignment() {
//        // Implement the test logic
//    }
//
//    @Test
//    public void testFailedSubmission() {
//        // Implement the test logic
//    }
//}
