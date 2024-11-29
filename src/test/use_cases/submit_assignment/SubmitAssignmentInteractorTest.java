package use_cases.submit_assignment;

import data_access.InMemoryUserDataAccessObject;
import data_access.InMemoryFileDataAccessObject;
import entities.Account;
import entities.PDFFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class SubmitAssignmentInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;
    private InMemoryFileDataAccessObject fileDsGateway;
    private SubmitAssignmentInteractor submitAssignmentInteractor;
    private MockSubmitAssignmentPresenter outputBoundary;


    @BeforeEach
    void create() {
        userDsGateway = new InMemoryUserDataAccessObject();
        fileDsGateway = new InMemoryFileDataAccessObject();
        outputBoundary = new MockSubmitAssignmentPresenter();
        submitAssignmentInteractor = new SubmitAssignmentInteractor(
                fileDsGateway,
                outputBoundary,
                userDsGateway
        );
    }

    private static class MockSubmitAssignmentPresenter implements SubmitAssignmentOutputBoundary {
        private String successMessage;
        private String errorMessage;

        @Override
        public void presentSuccess(String message) {
            this.successMessage = message;
        }

        @Override
        public void presentError(String message) {
            this.errorMessage = message;
        }

        public String getSuccesMessage() {
            return successMessage;
        }
        public String getErrorMessage() {
            return errorMessage;
        }
    }

    @Test
    public void testValidSubmission() throws Exception{
        Account tohya = userDsGateway.getUserByEmail("henrik-ibsen707@gmail.com");
        userDsGateway.saveUser(tohya);

        File testFile = Files.createTempFile("test", ".pdf").toFile();
        Files.write(testFile.toPath(), "Sample".getBytes());

        submitAssignmentInteractor.submitAssignment(
                testFile,
                "CSC207",
                "henrik-ibsen707@gmail.com",
                userDsGateway
        );

        PDFFile savedFile = fileDsGateway.getFile("/CSC207/henrik-ibsen707@gmail.com");

        String expectedfileName = "test.pdf";
        String expectedfilePath = "/CSC207/henrik-ibsen707@gmail.com"
        String actualfileName = savedFile.getFileName();
        String actualfilePath = savedFile.getFilePath();

        assertNotNull(savedFile);
        assertEquals(expectedfileName, actualfileName);
        assertEquals(expectedfilePath, actualfilePath);
        assertEquals("File uploaded successfully to: " + expectedfilePath,
                outputBoundary.getSuccesMessage());
    }

    @Test
    void testSubmitAssignmentWithNonPDFFile() {

    }

    @Test
    public void testValidFormat() {
        // Implement the test logic
    }

    @Test
    public void testValidAssignment() {
        // Implement the test logic
    }

    @Test
    public void testFailedSubmission() {
        // Implement the test logic
    }
}
