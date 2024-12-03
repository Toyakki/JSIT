package use_cases.create_assignment;

import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entities.*;
import interface_adapters.create_assignment.CreateAssignmentPresenter;
import interface_adapters.teacher_course.TeacherCourseViewModel;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CreateAssignmentUseCaseInteractorTest {
    private InMemoryFileDataAccessObject fileDsGateway;
    private InMemoryUserDataAccessObject userDsGateway;

    @Test
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
        TeacherCourseViewModel teacherCourseViewModel = new TeacherCourseViewModel();
        CreateAssignmentPresenter presenter = new CreateAssignmentPresenter(teacherCourseViewModel){
            public void prepareFailView(){
                fail ("Valid assignment upload, test case failure unexpected.");
            }
            public void refreshView(){
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
        assertEquals(savedAssignment.getName(), "Assignment 1");
        assertEquals(savedAssignment.getDueDate(), "12/25");
        assertEquals(savedAssignment.getMarks(), "100");
        assertEquals(savedAssignment.getStage(),"assigned");
        assertEquals(savedAssignment.getMarksReceivedStatus(), "false");
    }
}

