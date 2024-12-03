package use_cases.submit_assignment;

import data_access.DropBoxDataAccessObject;
import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.*;
import data_access.InMemoryUserDataAccessObject;
import interface_adapters.student_course.StudentCourseViewModel;
import interface_adapters.submit_assignment.SubmitAssignmentPresenter;
import use_cases.submit_assignment.SubmitAssignmentInteractor;
import use_cases.submit_assignment.SubmitAssignmentInputBoundary;
import use_cases.submit_assignment.SubmitAssignmentOutputBoundary;

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
    private StudentCourseViewModel viewModel;
    private SubmitAssignmentPresenter presenter;

    @BeforeEach
    void setUp() {
        // Initialize DAOs
        userDAO = new InMemoryUserDataAccessObject();
        // TODO: Replace this with inMemory.
        fileDAO = new DropBoxDataAccessObject();

        // Initialize ViewModel and Presenter
        viewModel = new StudentCourseViewModel();
        presenter = new SubmitAssignmentPresenter(viewModel);

        interactor = new SubmitAssignmentInteractor(fileDAO, presenter, userDAO);

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

        interactor = new SubmitAssignmentInteractor(fileDAO, presenter, userDAO);
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

        // Assert
        PDFFile savedFile = fileDAO.getFile("/MAT101/student1@example.com/" + validFile.getName());
        assertNotNull(savedFile, "The file should be saved in the DAO.");
        assertEquals(validFile.getName(), savedFile.getFileName(), "The file name should match.");
        validFile.deleteOnExit();
    }
}