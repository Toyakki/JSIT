package use_cases.submit_assignment;

import data_access.*;
import entities.*;
import interface_adapters.student_course.StudentCourseState;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class SubmitAssignmentInteractorTest {

    private UserDataAccessInterface userDAO;
    private FileDataAccessInterface fileDAO;
    private SubmitAssignmentInteractor interactor;
    private MockSubmitAssignmentPresenter testPresenter;

    public class MockSubmitAssignmentPresenter implements SubmitAssignmentOutputBoundary {
        private boolean successCalled = false;
        private boolean errorCalled = false;
        private String lastErrorMessage = null;
        private StudentCourseState lastState = null;

        @Override
        public void presentSuccess(String courseName, String email, Assignment assignment) {
            successCalled = true;

            // Simulate creating the state as the real presenter would
            lastState = new StudentCourseState(
                    email,
                    courseName,
                    List.of(assignment.getName()),
                    List.of(assignment.getDueDate()),
                    List.of(assignment.getMarks()),
                    List.of(assignment.getStage()),
                    List.of(assignment.getMarksReceivedStatus())
            );
        }

        @Override
        public void presentError(String message) {
            errorCalled = true;
            lastErrorMessage = message;
        }

        public boolean isSuccessCalled() {
            return successCalled;
        }

        public boolean isErrorCalled() {
            return errorCalled;
        }

        public String getLastErrorMessage() {
            return lastErrorMessage;
        }

        public StudentCourseState getLastState() {
            return lastState;
        }
    }

    @BeforeEach
    void setUp() {
        // Initialize DAOs
        userDAO = new InMemoryUserDataAccessObject();
        fileDAO = new InMemoryFileDataAccessObject();

        testPresenter = new MockSubmitAssignmentPresenter();

        interactor = new SubmitAssignmentInteractor(fileDAO, testPresenter, userDAO);

        // Create test data
        Course math101 = CourseFactory.createClass("John Doe", "MAT101");
        Course csc207 = CourseFactory.createClass("Johnathon Calver", "CSC207");


        List<Course> test_course = new ArrayList<>();
        test_course.add(math101);
        test_course.add(csc207);

        List<Course> test_course_instructor = new ArrayList<>();
        test_course_instructor.add(math101);

        Account student = new Account("student1@example.com", "Password123!",
                "student", test_course);
        Account teacher = new Account("teacher1@example.com", "SecurePass!",
                "teacher", test_course_instructor);

        userDAO.saveUser(student);
        userDAO.saveUser(teacher);

        Assignment assignment1 = new Assignment(math101, "Assignment1", "December 1st",
                "100", "assigned", "false");

        math101.addAssignment(assignment1);

        interactor = new SubmitAssignmentInteractor(fileDAO, testPresenter, userDAO);
    }

    @Test
    void testSubmitAssignment_Success() throws IOException {
        // Arrange: Create a valid PDF file
        File validFile = Files.createTempFile("test_assignment", ".pdf").toFile();
        Files.writeString(validFile.toPath(), "Sample content");

        String courseName = "MAT101";
        String email = "student1@example.com";
        String assignmentName = userDAO.getUserByEmail("student1@example.com")
                .getCourseByName(courseName)
                .getAssignments()
                .getFirst()
                .getName();

        interactor.submitAssignment(validFile, courseName, assignmentName, email);


        assertTrue(testPresenter.isSuccessCalled(), "presentSuccess should be called.");
        StudentCourseState lastState = testPresenter.getLastState();
        assertNotNull(lastState, "State should not be null.");
        assertEquals(courseName, lastState.getCourseName(), "Course name should match.");
        assertEquals(email, lastState.getEmail(), "Email should match.");
        assertEquals(1, lastState.getAssignmentsNames().size(), "There should be one assignment.");
        assertEquals(assignmentName, lastState.getAssignmentsNames().get(0), "Assignment name should match.");
        validFile.deleteOnExit();
    }

    @Test
    void testSubmitAssignment_InvalidFileInput() {
        // Arrange: Use a nonexistent file
        File nonexistentFile = new File("nonexistent_assignment.pdf");
        String courseName = "MAT101";
        String email = "student1@example.com";
        String assignmentName = "Assignment1";

        // Act
        interactor.submitAssignment(nonexistentFile, courseName, assignmentName, email);

        // Assert
        assertTrue(testPresenter.isErrorCalled(), "presentError should be called.");
        assertEquals("File does not exist", testPresenter.getLastErrorMessage(), "Error message should match.");
    }

    @Test
    void testSubmitAssignment_InvalidFileType() {
        // Arrange
        File invalidFile = new File("test_assignment.txt");
        String courseName = "MAT101";
        String email = "student1@example.com";
        String assignmentName = "Assignment1";

        // Act
        interactor.submitAssignment(invalidFile, courseName, assignmentName, email);

        // Assert
        assertTrue(testPresenter.isErrorCalled(), "presentError should be called.");
        assertEquals("File must be a PDF", testPresenter.getLastErrorMessage(), "Error message should match.");
    }

    @Test
    void testSubmitAssignment_FileNotReadable() throws IOException {
        // Arrange
        File unreadableFile = Files.createTempFile("test_assignment", ".pdf").toFile();
        unreadableFile.setReadable(false);
        String courseName = "MAT101";
        String email = "student1@example.com";
        String assignmentName = "Assignment1";

        // Act
        interactor.submitAssignment(unreadableFile, courseName, assignmentName, email);

        // Assert
        assertTrue(testPresenter.isErrorCalled(), "presentError should be called.");
        assertEquals("File cannot be read", testPresenter.getLastErrorMessage(), "Error message should match.");
        unreadableFile.deleteOnExit();
    }

    @Test
    void testSubmitAssignment_NonexistentUser() {
        // Arrange
        File validFile = new File("test_assignment.pdf");
        String courseName = "MAT101";
        String invalidEmail = "nonexistent_user@example.com";
        String assignmentName = "Assignment1";

        // Act
        interactor.submitAssignment(validFile, courseName, assignmentName, invalidEmail);

        // Assert
        assertTrue(testPresenter.isErrorCalled(), "presentError should be called.");
        assertEquals("File does not exist", testPresenter.getLastErrorMessage(), "Error message should match.");
    }

    @Test
    void testSubmitAssignment_NonexistentCourse() {
        // Arrange
        File validFile = new File("test_assignment.pdf");
        String invalidCourseName = "UnknownCourse";
        String email = "student1@example.com";
        String assignmentName = "Assignment1";

        // Act
        interactor.submitAssignment(validFile, invalidCourseName, assignmentName, email);

        // Assert
        assertTrue(testPresenter.isErrorCalled(), "presentError should be called.");
        assertEquals("File does not exist", testPresenter.getLastErrorMessage(), "Error message should match.");
    }

    @Test
    void testSubmitAssignment_NonexistentAssignment() {
        // Arrange
        File validFile = new File("test_assignment.pdf");
        String courseName = "MAT101";
        String email = "student1@example.com";
        String invalidAssignmentName = "NonexistentAssignment";

        // Act
        interactor.submitAssignment(validFile, courseName, invalidAssignmentName, email);

        // Assert
        assertTrue(testPresenter.isErrorCalled(), "presentError should be called.");
        assertEquals("File does not exist", testPresenter.getLastErrorMessage(), "Error message should match.");
    }

}