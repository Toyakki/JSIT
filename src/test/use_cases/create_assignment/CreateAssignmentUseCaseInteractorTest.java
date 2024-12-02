package use_cases.create_assignment;

import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entities.*;
import interface_adapters.create_assignment.CreateAssignmentPresenter;

import java.io.File;

public class CreateAssignmentUseCaseInteractorTest {
    private InMemoryFileDataAccessObject fileDsGateway;
    private InMemoryUserDataAccessObject userDsGateway;

    void create() {
        fileDsGateway = new InMemoryFileDataAccessObject();
        userDsGateway = new InMemoryUserDataAccessObject();
        Account teacher = AccountFactory.createAccount("lindsey@mail.com", "Abc123456!", "teacher");
        Course course = CourseFactory.createClass("Lindsey", "Software Design");
        teacher.addCourse(course);
        userDsGateway.saveUser(teacher);
    }

    public void testValidFileUpload() {
        CreateAssignmentPresenter presenter = new CreateAssignmentPresenter(){
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
        assertEqual(fileDsGateway.getAllFiles().size(), 1);
        assertEqual(course.getAssignments().size(),1);
        assertEqual(savedAssignment.getName(), "Assignment 1");
        assertEqual(savedAssignment.getDueDate(), "12/25");
        assertEqual(savedAssignment.getMarks(), "100");
        assertEqual(savedAssignment.getStage(),"assigned");
        assertEqual(savedAssignment.getMarksReceivedStatus(), "false");
    }
}

