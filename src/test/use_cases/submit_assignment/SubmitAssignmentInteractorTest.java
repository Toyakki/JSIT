package use_cases.submit_assignment;

import data_access.FileDataAccessInterface;
import data_access.InMemoryUserDataAccessObject;
import data_access.InMemoryFileDataAccessObject;
import entities.Account;
import entities.PDFFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

public class SubmitAssignmentInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;
    private InMemoryFileDataAccessObject fileDsGateway;
    private SubmitAssignmentInteractor submitAssignmentInteractor;


    @BeforeEach
    void create() {
        userDsGateway = new InMemoryUserDataAccessObject();
        fileDsGateway = new InMemoryFileDataAccessObject();
        SubmitAssignmentOutputBoundary outputBoundary = new MockSubmitAssignmentPresenter();
        submitAssignmentInteractor = new SubmitAssignmentInteractor(fileDsGateway, outputBoundary, userDsGateway);
    }

    private static class MockSubmitAssignmentPresenter implements SubmitAssignmentOutputBoundary {
        @Override
        public void presentSuccess(String message) {
            System.out.println("SUCCESS: " + message);
        }

        @Override
        public void presentError(String message) {
            System.err.println("Error: " + message);
        }
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
    public void testValidSubmission() throws Exception{
        Account user = userDsGateway.getUserByEmail("");
        File testFile = Files.createTempFile("test", ".pdf").toFile();
        Files.write(testFile.toPath(), "Sample".getBytes());
        submitAssignmentInteractor.submitAssignment(
                testFile,
                "CSC207",
                "test@example.com",
                userDsGateway
        );
        PDFFile savedFile = fileDsGateway.getFile("/CSC207/test@example.com");
        assertNotNull(savedFile);
        assertEquals("test.pdf", savedFile.getFileName());
        assertEquals("/CSC207/test@example.com", savedFile.getFilePath());
    }

    @Test
    public void testFailedSubmission() {
        // Implement the test logic
    }
}
