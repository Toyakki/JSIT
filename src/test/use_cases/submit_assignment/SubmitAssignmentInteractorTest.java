package use_cases.submit_assignment;

import data_access.InMemoryUserDataAccessObject;
import data_access.InMemoryFileDataAccessObject;
import entities.Account;
import entities.Assignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubmitAssignmentInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;
    private InMemoryFileDataAccessObject fileDsGateway;
    private SubmitAssignmentInteractor submitAssignmentInteractor;

    @BeforeEach
    void create() {
        userDsGateway = new InMemoryUserDataAccessObject();
        fileDsGateway = new InMemoryFileDataAccessObject();

        Assignment finalProject = new Assignment(
                "Final Project",
                "December 4th",
                "25", 
                "submitted",
                "false"
        );

        SubmitAssignmentOutputBoundary outputBoundary = new MockSubmitAssignmentPresenter();
        Account tohya_test_account = userDsGateway.getUserByEmail("henrik-ibsen707@gmail.com");

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
    public void testValidFormat() {
        // Implement the test logic
    }

    @Test
    public void testValidAssignment() {
        // Implement the test logic
    }

    @Test
    public void testValidSubmission() {
        // Implement the test logic
    }

    @Test
    public void testFailedSubmission() {
        // Implement the test logic
    }
}
