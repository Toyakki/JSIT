package use_cases.submit_assignment;

import data_access.InMemoryUserDataAccessObject;
import data_access.InMemoryFileDataAccessObject;
import entities.Account;
import entities.Course;
import entities.CourseFactory;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;


import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
        Account tohya_test_account = userDsGateway.getUserByEmail("henrik-ibsen707@gmail.com");

        submitAssignmentInteractor = new SubmitAssignmentInteractor(fileDsGateway, outputBoundary, tohya_test_account);
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
    }

    @Test
    public void testValidAssignment() {

    }

    @Test
    public void testValidSubmission() {

    }

    @Test
    public void testFailedSubmission() {

    }

}
