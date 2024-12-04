package use_cases.create_assignment;

import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entities.*;
import interface_adapters.create_assignment.CreateAssignmentPresenter;
import interface_adapters.teacher_course.TeacherCourseState;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class CreateAssignmentUseCaseInteractorTest {
    private InMemoryFileDataAccessObject fileDsGateway;
    private InMemoryUserDataAccessObject userDsGateway;


    @BeforeEach
    void create() {
        fileDsGateway = new InMemoryFileDataAccessObject();
        userDsGateway = new InMemoryUserDataAccessObject();
        Account teacher = AccountFactory.createAccount("lindsey@mail.com", "Abc123456!", "teacher");
        Course course = CourseFactory.createClass("Lindsey", "Software Design");
        teacher.addCourse(course);
        userDsGateway.saveUser(teacher);
    }

    @Test
    public void testValidFileUpload() {
        CreateAssignmentOutputBoundary presenter = new CreateAssignmentOutputBoundary(){
            @Override
            public void prepareErrorView(String error) {
                fail ("Valid assignment upload, test case failure unexpected.");
            }

            @Override
            public void refreshView(TeacherCourseState state) {
                assert true;
            }
        };
        File assisgnmentFile = new File("desktop");
        CreateAssignmentInteractor interactor = new CreateAssignmentInteractor(presenter, userDsGateway, fileDsGateway);
        interactor.createAssignment("lindsey@mail.com", "Assignment 1",
                "Software Design", "12/25", "100", assisgnmentFile);
        Account teacher = userDsGateway.getUserByEmail("lindsey@mail.com");
        Course course = teacher.getCourseByName("Software Design");
        Assignment savedAssignment = course.getAssignments().getFirst();
        assertEquals(fileDsGateway.getAllFiles().size(), 1);
        assertEquals(course.getAssignments().size(),1);
        assertEquals("Assignment 1", savedAssignment.getName());
        assertEquals("12/25", savedAssignment.getDueDate());
        assertEquals("100", savedAssignment.getMarks());
        assertEquals("assigned", savedAssignment.getStage());
        assertEquals("false", savedAssignment.getMarksReceivedStatus());
    }
}

